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
package thw_matp.ctrl;

import thw_matp.datatypes.Pruefer;
import thw_matp.datatypes.Pruefung;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CtrlPruefungen {

    public CtrlPruefungen(Database db) {
        this.db = db;
    }

    public DefaultTableModel get_data() {
        DefaultTableModel mdl = new DefaultTableModel();
        mdl.setColumnIdentifiers(new String[]{"Kennzeichen", "Datum", "Prüfer", "Ort", "Bestanden", "Ausgesondert", "Bemerkungen", "ID"});
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
                mdl.addRow(new Object[]{pruefung.kennzeichen, pruefung.datum, pruefer_name, pruefung.ov, pruefung.bestanden, pruefung.ausgesondert, pruefung.bemerkungen, pruefung.id});
            }
        }
        else {
            System.err.println("No entry in table pruefungen!");
        }
        return mdl;
    }

    public DefaultTableModel get_data(String kennzeichen) {
        kennzeichen = kennzeichen.replace('/', '-');
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

    public Pruefung add_pruefung(String kennzeichen, UUID pruefer, boolean bestanden, String bemerkungen, boolean ausgesondert, String ov) {
        kennzeichen = kennzeichen.replace('/', '-');
        try {
            return this.db.puefung_add_event(kennzeichen, pruefer, bestanden, bemerkungen, ausgesondert, ov);
        } catch (SQLException throwables) {
            System.err.println("Failed to enter new entry in table pruefungen!");
            throwables.printStackTrace();
            return null;
        }
    }

    public boolean edit_pruefung(UUID id, String kennzeichen, LocalDate datum, UUID pruefer, boolean bestanden, String bemerkungen, boolean ausgesondert) {
        kennzeichen = kennzeichen.replace('/', '-');
        try {
            this.db.puefung_update_event(id, kennzeichen, datum, pruefer, bestanden, bemerkungen, ausgesondert);
        } catch (SQLException throwables) {
            System.err.println("Failed to update entry " + id.toString() + " in table pruefungen!");
            throwables.printStackTrace();
            return false;
        }
        return true;
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
        kennzeichen = kennzeichen.replace('/', '-');
        try {
            this.db.pruefungen_remove(kennzeichen);
        } catch (SQLException throwables) {
            System.err.println("Failed to remove entry " + kennzeichen + " in table pruefungen!");
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public Pruefung find(UUID id) {
        try {
            return this.db.pruefungen_find(id);
        } catch (SQLException throwables) {
            System.err.println("Failed to find pruefung " + id.toString() + " in table pruefungen!");
            throwables.printStackTrace();
            return null;
        }
    }






    private final Database db;
}
