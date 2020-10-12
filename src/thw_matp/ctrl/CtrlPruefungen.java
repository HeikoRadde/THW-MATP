package thw_matp.ctrl;

import thw_matp.datatypes.Pruefer;
import thw_matp.datatypes.Pruefung;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class CtrlPruefungen {

    public CtrlPruefungen(Database db) {
        this.db = db;
    }

    public DefaultTableModel get_data() {
        DefaultTableModel mdl = new DefaultTableModel();
        mdl.setColumnIdentifiers(new String[]{"Kennzeichen", "Datum", "Prüfer", "Bestanden", "Ausgesondert", "Bemerkungen", "ID"});
        List<Pruefung> pruefungen = null;
        try {
            pruefungen = this.db.pruefungen_get_all();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(pruefungen != null) {
            for (Pruefung pruefung : pruefungen) {
                String pruefer_name;
                try {
                    Pruefer pruefer = db.pruefer_get(pruefung.pruefer);
                    pruefer_name = pruefer.vorname + " " + pruefer.name;
                } catch (SQLException | IOException throwables) {
                    pruefer_name = "";
                }
                mdl.addRow(new Object[]{pruefung.kennzeichen, pruefung.datum, pruefer_name, pruefung.bestanden, pruefung.ausgesondert, pruefung.bemerkungen, pruefung.id});
            }
        }
        else {
            System.err.println("No entry in table pruefungen!");
        }
        return mdl;
    }

    public DefaultTableModel get_data(String kennzeichen) {
        DefaultTableModel mdl = new DefaultTableModel();
        mdl.setColumnIdentifiers(new String[]{"Kennzeichen", "Datum", "Prüfer", "Bestanden", "ID", "Ausgesondert", "Bemerkungen"});
        List<Pruefung> pruefungen = null;
        try {
            pruefungen = this.db.pruefungen_get(kennzeichen);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(pruefungen != null) {
            for (Pruefung pruefung : pruefungen) {
                String pruefer_name;
                try {
                    Pruefer pruefer = db.pruefer_get(pruefung.pruefer);
                    pruefer_name = pruefer.vorname + " " + pruefer.name;
                } catch (SQLException | IOException throwables) {
                    pruefer_name = "";
                }
                mdl.addRow(new Object[]{pruefung.kennzeichen, pruefung.datum, pruefer_name, pruefung.bestanden, pruefung.id, pruefung.ausgesondert, pruefung.bemerkungen});
            }
        }
        else {
            System.err.println("No entry in table pruefungen!");
        }
        return mdl;
    }

    public Pruefung add_pruefung(String kennzeichen, UUID pruefer, boolean bestanden, String bemerkungen, boolean ausgesondert) {
        try {
            return this.db.puefung_add_event(kennzeichen, pruefer, bestanden, bemerkungen, ausgesondert);
        } catch (SQLException throwables) {
            System.err.println("Failed to enter new entry in table pruefungen!");
            throwables.printStackTrace();
            return null;
        }
    }

    public boolean remove_pruefung(UUID id) {
        try {
            this.db.pruefungen_remove(id);
        } catch (SQLException throwables) {
            System.err.println("Failed to remove entry " + id.toString() + " in table pruefungen!");
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean remove_pruefungen(String kennzeichen) {
        try {
            this.db.pruefungen_remove(kennzeichen);
        } catch (SQLException throwables) {
            System.err.println("Failed to remove entry " + kennzeichen + " in table pruefungen!");
            throwables.printStackTrace();
            return false;
        }
        return true;
    }




    private final Database db;
}
