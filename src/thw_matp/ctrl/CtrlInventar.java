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

import thw_matp.datatypes.Item;
import thw_matp.datatypes.Pruefung;
import thw_matp.util.PrinterProtocolAddingItemCSV;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CtrlInventar {

    public CtrlInventar(Database db) {
        this.db = db;
    }

    public boolean load_data(String filepath) {
        CSVImporter importer = new CSVImporter(filepath);
        try {
            importer.read(this.db);
            m_new_vorschriften = importer.get_added_vorschriften();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public DefaultTableModel get_data() {
        DefaultTableModel mdl = new DefaultTableModel();
        mdl.setColumnIdentifiers(new String[]{"Kennzeichen", "Sachnummer", "Bezeichnung", "Hersteller", "Baujahr", "Einheit", "OV", "Letzte Prüfung", "Bestanden", "Nächste Prüfung"});
        List<Item> inventar = null;
        try {
            inventar = this.db.inventar_get_all();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(inventar != null) {
            for (Item item : inventar) {
                try {
                    Pruefung last_pruefung = db.pruefungen_get_last(item.kennzeichen);
                    mdl.addRow(new Object[]{item.kennzeichen, item.sachnr, item.bezeichnung, item.hersteller, item.baujahr, item.einheit, item.ov, last_pruefung.datum, last_pruefung.bestanden, last_pruefung.datum.plusYears(1)});
                } catch (SQLException throwables) {
                    mdl.addRow(new Object[]{item.kennzeichen, item.sachnr, item.bezeichnung, item.hersteller, item.baujahr, item.einheit, item.ov});
                }
            }
        }
        else {
            System.err.println("No entry in table inventar!");
        }
        return mdl;
    }

    public Item get_item(String kennzeichen) {
        kennzeichen = kennzeichen.replace('/', '-');
        try {
            return this.db.inventar_get(kennzeichen);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.err.println("No entry " + kennzeichen + " in table inventar!");
            return null;
        }
    }

    public void add_item(String kennzeichen, String ov, String einheit, int baujahr, String hersteller, String bezeichnung, String sachnr) throws IllegalArgumentException {
        kennzeichen = kennzeichen.replace('/', '-');
        try {
            this.db.inventar_add(kennzeichen, ov, einheit, baujahr, hersteller, bezeichnung, sachnr);
            Item i = new Item(kennzeichen, ov, einheit, baujahr, hersteller, bezeichnung, sachnr);
            try {
                PrinterProtocolAddingItemCSV.add_new_item_event(Paths.get("").toAbsolutePath(), i);
            } catch (IOException e) {
                System.err.println("Couldn't log new Item!");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == Database.ERROR_CODE_REFERENTIAL_INTEGRITY_VIOLATED_PARENT_MISSING_1) {
                System.err.println("Sachnr " + sachnr +  " not known!");
                throw new IllegalArgumentException("Sachnr not known!");
            }
            else if (e.getErrorCode() == Database.DUPLICATE_KEY_1) {
                System.err.println("Kennzeichen " + kennzeichen +  " already existing!");
                throw new IllegalArgumentException("Kennzeichen existing!");
            }
            else {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException)e).getSQLState());
                System.err.println("Error Code: " + ((SQLException)e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
            }
        }
    }

    public ArrayList<String> get_added_vorschriften() {
        return this.m_new_vorschriften;
    }

    public void update(String kennzeichen, String ov, String einheit, int baujahr, String hersteller, String bezeichnung, String sachnr) throws IllegalArgumentException {
        kennzeichen = kennzeichen.replace('/', '-');
        try {
            this.db.inventar_update(kennzeichen, ov, einheit, baujahr, hersteller, bezeichnung, sachnr);
        } catch (SQLException e) {
            if (e.getErrorCode() == Database.ERROR_CODE_REFERENTIAL_INTEGRITY_VIOLATED_PARENT_MISSING_1) {
                System.err.println("Sachnr " + sachnr +  " not known!");
                throw new IllegalArgumentException("Sachnr not known!");
            }
            else {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException)e).getSQLState());
                System.err.println("Error Code: " + ((SQLException)e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
            }
        }
    }

    public boolean remove_item(String kennzeichen, CtrlPruefungen ctrl_pruefungen) {
        if (!ctrl_pruefungen.remove_pruefungen(kennzeichen)) return false;
        try {
            this.db.inventar_remove(kennzeichen);
        } catch (SQLException throwables) {
            System.err.println("Failed to remove entry " + kennzeichen + " in table inventar!");
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean remove_items(String sachnummer, CtrlPruefungen ctrl_pruefungen) {
        try {
            List<Item> items = this.db.inventar_get_by_sachnr(sachnummer);
            for (Item item : items)
            {
                if (!remove_item(item.kennzeichen, ctrl_pruefungen)) return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }


    private final Database db;
    private ArrayList<String> m_new_vorschriften;
}
