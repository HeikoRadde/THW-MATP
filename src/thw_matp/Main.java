package thw_matp;

import thw_matp.ctrl.*;
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

//        UIDefaults defs = UIManager.getDefaults();
//        defs.put("Panel.background", new ColorUIResource(COLOR_THW));

        WindowMain win = new WindowMain(ctrl_items, ctrl_pruefer, ctrl_pruefungen, ctrl_vorschriften);
        JPanel root = win.get_root_panel();
        JFrame frame = new JFrame();
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

//        {
//            InputStream is = Main.class.getClassLoader().getResourceAsStream("logo_thw_blau.png");
//            Image signature = null;
//            try {
//                signature = ImageIO.read(is);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Item item = new Item("0055-006957", "OV Berlin-Neukölln", "1. TZ, 1. B1", 2008, "Stihl", "Zurrgurt mit Ratsche (2500 daN)", "2940T00044");
//            String bemerkungen = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. ";
//            Pruefung pruefung = new Pruefung(UUID.fromString("4fb5db6b-bfc5-4862-b7af-47581d0bef10"), "0055-006957", LocalDate.now(), true, UUID.randomUUID(), bemerkungen, false);
//            Pruefer pruefer = new Pruefer(UUID.randomUUID(), "Radde", "Heiko", signature);
//            Vorschrift vorschrift = new Vorschrift("2940T00044", "GUV-R 500", "GUV-R500-2.8");
//            try {
//                PrinterProtocol.print_pruefung(Paths.get("/media/veracrypt1/Daten/Projekte"), pruefung, pruefer, item, vorschrift);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        System.exit(1);
    }


    private static final Color COLOR_THW = new Color(18,10,143);
}
