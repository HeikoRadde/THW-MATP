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
package thw_matp.ctrl;

import thw_matp.datatypes.Pruefer;
import thw_matp.datatypes.Pruefung;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Controller for actions related to Prüfungen
 */
public class CtrlPruefungen {

    /**
     * @param db Database to be used
     */
    public CtrlPruefungen(Database db) {
        this.db = db;
    }

    /**
     * @return Get data related to all Prüfugen in a ready-to-use format for tables
     */
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

    /**
     *                      Get data of all Prüfungen of a single item
     * @param kennzeichen   Unique Kennzeichen of item
     * @return              Ready-to-use format for tables with all Prüfungen for a specific item
     */
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

    /**
     *                      Add a Prüfung to the database
     * @param kennzeichen   Unique ID of the checked item
     * @param pruefer       Unique ID of the Prüfer doing the test
     * @param bestanden     Item tested OK / not OK
     * @param bemerkungen   Comment for this test
     * @param ausgesondert  True if item was discarded from inventory after the test
     * @param ov            Ortsverband where the test was performed
     * @return              {@link thw_matp.datatypes.Pruefung} object if adding was successfull, null if not
     */
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

    /**
     *                      Edit a past Prüfung
     * @param id            Unique ID of the Prüfung to edit
     * @param kennzeichen   Potentially modified unique ID of the tested item
     * @param datum         Potentially modified data of the test
     * @param pruefer       Potentially modified unique ID of the Prüfer who did the test
     * @param bestanden     Potentially modified value if the item tested OK / not OK
     * @param bemerkungen   Potentially modified comment for this test
     * @param ausgesondert  Potentially modified value if the item was discarded from inventory after this test
     * @return              True if modification was successfull, false if it failed
     */
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

    /**
     *              Remove a Prüfung from the database
     * @param id    Unique ID of the Prüfung to remove
     * @return      True if removal was successfull, false if it failed
     */
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

    /**
     *                      Remove all Prüfungen related to an item of the inventory from the database
     * @param kennzeichen   Unique ID of the item
     * @return              True if removal was successfull, false if it failed
     */
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

    /**
     *              Retrieve the data of a certain Prüfung
     * @param id    Unique ID of the Prüfung
     * @return      {@link thw_matp.datatypes.Pruefung} object if found, null if not found
     */
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
