/*
    Copyright (c) 2021 Heiko Radde
    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
    documentation files (the "Software"), to deal in the Software without restriction, including without limitation
    the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
    to permit persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of
    the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
    THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
    TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package thw_matp;

import thw_matp.ctrl.*;
import thw_matp.ui.SaveJFrame;
import thw_matp.ui.WindowMain;
import thw_matp.ui.WindowStartup;
import thw_matp.util.PrinterProtocolTestingOverviewPDF;

import javax.swing.*;
import java.sql.SQLException;


/**
 * Main-function of the Program.
 * Starts the startu-window {@link thw_matp.ui.WindowStartup} and provides the init for the main-window {@link thw_matp.ui.WindowMain} after that window was closed
 */
public class Main {

    public static final String THW_MATP_DATENBANK = "thw_matp_datenbank";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::create_startup);
    }

    /**
     * Start and initialise the startup-window  {@link thw_matp.ui.WindowStartup}
     */
    public static void create_startup() {
        WindowStartup startup = new WindowStartup();
        startup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startup.pack();
        startup.setLocationRelativeTo(null);
        startup.setVisible(true);
        startup.setResizable(false);
    }

    /**
     * Start and initialise the main-window {@link thw_matp.ui.WindowMain}.
     */
    public static void create_gui() {
        Database db = null;
        try {
            if(Settings.getInstance().db_is_local()) {
                db = new DatabaseServer(THW_MATP_DATENBANK);
            }
            else {
                db = new Database(Settings.getInstance().get_ip(), Settings.getInstance().get_port(), THW_MATP_DATENBANK);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.exit(1);
        }

        CtrlInventar ctrl_items = new CtrlInventar(db);
        CtrlPruefer ctrl_pruefer = new CtrlPruefer(db);
        CtrlPruefungen ctrl_pruefungen = new CtrlPruefungen(db);
        CtrlVorschrift ctrl_vorschriften = new CtrlVorschrift(db);

        ctrl_vorschriften.init_db();

        WindowMain win = new WindowMain(ctrl_items, ctrl_pruefer, ctrl_pruefungen, ctrl_vorschriften);
        JPanel root = win.get_root_panel();
        SaveJFrame frame = new SaveJFrame();
        frame.setDefaultCloseOperation(SaveJFrame.EXIT_ON_CLOSE);
        frame.setContentPane(root);
        frame.setTitle("THW-MATP");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        win.populate_table_inventar();
        win.populate_table_pruefer();
        win.populate_table_pruefungen();
        win.populate_table_vorschriften();

        PrinterProtocolTestingOverviewPDF.set_path(Settings.getInstance().get_path_protocols());
    }


}
