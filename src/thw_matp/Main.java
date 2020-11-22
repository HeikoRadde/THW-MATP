/*
    Copyright (c) 2020 Heiko Radde
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

import javax.swing.*;
import java.sql.SQLException;


public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::create_gui);
    }

    public static void create_gui() {
        Database db = null;
        try {
            db = new Database("thw_matp_datenbank");
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
        JFrame frame = new SaveJFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(root);
        frame.setTitle("THW-MATP");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        win.populate_table_inventar();
        win.populate_table_pruefer();
        win.populate_table_pruefungen();
        win.populate_table_vorschriften();
    }


}
