package thw_matp;

import thw_matp.ctrl.*;
import thw_matp.ui.SaveJFrame;
import thw_matp.ui.WindowMain;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;


public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                create_gui();
            }
        });
    }

    public static void create_gui() {
        Database db = null;
        try {
            db = new Database();
            db.pruefer_add("Mustermann", "Max");
            db.pruefer_print_all();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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


    private static final Color COLOR_THW = new Color(18,10,143);
}
