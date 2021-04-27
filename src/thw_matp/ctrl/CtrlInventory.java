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

import thw_matp.datatypes.Item;
import thw_matp.datatypes.Inspection;
import thw_matp.util.PrinterProtocolAddingItemCSV;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CtrlInventory {

    /**
     * @param db Database where the inventory is saved
     */
    public CtrlInventory(Database db) {
        this.db = db;
    }

    /**
     *                  Import inventory from CSV file
     * @param filepath  Path to the CSV file with the data to import
     * @return          true if data was imported, false if it failed
     */
    public boolean load_data(String filepath) {
        CSVImporter importer = new CSVImporter(filepath);
        try {
            importer.read(this.db);
            m_new_vorschriften = importer.get_added_inspections();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *          Retrieve all inventory as a {@link javax.swing.table.DefaultTableModel} ready to be displayed
     * @return  {@link javax.swing.table.DefaultTableModel} ready to be displayed
     */
    public DefaultTableModel get_data() {
        DefaultTableModel mdl = new DefaultTableModel();
        mdl.setColumnIdentifiers(new String[]{"Kennzeichen", "Sachnummer", "Bezeichnung", "Hersteller", "Baujahr", "Einheit", "OV", "Letzte Prüfung", "Bestanden", "Nächste Prüfung"});
        List<Item> inventory = null;
        try {
            inventory = this.db.inventory_get_all();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(inventory != null) {
            for (Item item : inventory) {
                try {
                    Inspection last_inspection = db.inspections_get_last(item.kennzeichen);
                    mdl.addRow(new Object[]{item.kennzeichen, item.sachnr, item.bezeichnung, item.hersteller, item.baujahr, item.einheit, item.ov, last_inspection.datum, last_inspection.bestanden, last_inspection.datum.plusYears(1)});
                } catch (SQLException throwables) {
                    mdl.addRow(new Object[]{item.kennzeichen, item.sachnr, item.bezeichnung, item.hersteller, item.baujahr, item.einheit, item.ov});
                }
            }
        }
        else {
            System.err.println("No entry in table inventory!");
        }
        return mdl;
    }

    /**
     *                      Get an Item identified by its `Kennzeichen`
     * @param kennzeichen   Kennzeichen identifier
     * @return              {@link thw_matp.datatypes.Item} if it was found, null if not
     */
    public Item get_item(String kennzeichen) {
        kennzeichen = kennzeichen.replace('/', '-');
        try {
            return this.db.inventory_get(kennzeichen);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.err.println("No entry " + kennzeichen + " in table inventory!");
            return null;
        }
    }

    /**
     *                      Add a item to the database
     * @param kennzeichen   Kennzeichen of the Item. Has to be unique
     * @param ov            OV where the Item is located
     * @param einheit       Einheit where the Item is dislocated
     * @param baujahr       Build year of the Item
     * @param hersteller    Producer of the Item
     * @param bezeichnung   Descriptive string / Name of the Item
     * @param sachnr        Sachnummer of the Vorschrift used for testing the Item
     * @throws IllegalArgumentException If the Kennzeichen is already in use or if the Sachnummer is not known
     */
    public void add_item(String kennzeichen, String ov, String einheit, int baujahr, String hersteller, String bezeichnung, String sachnr) throws IllegalArgumentException {
        kennzeichen = kennzeichen.replace('/', '-');
        try {
            this.db.inventory_add(kennzeichen, ov, einheit, baujahr, hersteller, bezeichnung, sachnr);
            Item i = new Item(kennzeichen, ov, einheit, baujahr, hersteller, bezeichnung, sachnr);
            try {
                PrinterProtocolAddingItemCSV.add_new_item_event(Settings.getInstance().get_path_protocols(), i);
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
                System.err.println("SQLState: " + e.getSQLState());
                System.err.println("Error Code: " + e.getErrorCode());
                System.err.println("Message: " + e.getMessage());
            }
        }
    }

    /**
     * @return All Sachnummern of the Vorschriften added during the runtime of the program
     */
    public ArrayList<String> get_added_vorschriften() {
        return this.m_new_vorschriften;
    }

    /**
     *                      Update the data of an Item
     * @param kennzeichen   Kennzeichen of the Item to update. Has to be unique and exist
     * @param ov            OV where the Item is located
     * @param einheit       Einheit where the Item is dislocated
     * @param baujahr       Build year of the Item
     * @param hersteller    Producer of the Item
     * @param bezeichnung   Descriptive string / Name of the Item
     * @param sachnr        Sachnummer of the Specification used for testing the Item
     * @throws IllegalArgumentException If the Sachnummer is not known
     */
    public void update(String kennzeichen, String ov, String einheit, int baujahr, String hersteller, String bezeichnung, String sachnr) throws IllegalArgumentException {
        kennzeichen = kennzeichen.replace('/', '-');
        try {
            this.db.inventory_update(kennzeichen, ov, einheit, baujahr, hersteller, bezeichnung, sachnr);
        } catch (SQLException e) {
            if (e.getErrorCode() == Database.ERROR_CODE_REFERENTIAL_INTEGRITY_VIOLATED_PARENT_MISSING_1) {
                System.err.println("Sachnr " + sachnr +  " not known!");
                throw new IllegalArgumentException("Sachnr not known!");
            }
            else {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + e.getSQLState());
                System.err.println("Error Code: " + e.getErrorCode());
                System.err.println("Message: " + e.getMessage());
            }
        }
    }

    /**
     *                          Remove a specified Item from the database and all the inspections attached to it
     * @param kennzeichen       Unique `Kennzeichen` of the Item
     * @param ctrl_pruefungen   Controler for the inspections
     * @return                  True on success, false on failure
     */
    public boolean remove_item(String kennzeichen, CtrlInspections ctrl_pruefungen) {
        kennzeichen = kennzeichen.replace('/', '-');
        if (!ctrl_pruefungen.remove_inspection(kennzeichen)) return false;
        try {
            this.db.inventory_remove(kennzeichen);
        } catch (SQLException throwables) {
            System.err.println("Failed to remove entry " + kennzeichen + " in table inventory!");
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *                          Remove all items connected to a given `Sachnummer` and all the inspections attached to those Items
     * @param sachnummer        Sachnummer used by the items to be deleted
     * @param ctrl_pruefungen   Controler for the inspections
     * @return                  True on success, false on failure
     */
    public boolean remove_items(String sachnummer, CtrlInspections ctrl_pruefungen) {
        try {
            List<Item> items = this.db.inventory_get_by_sachnr(sachnummer);
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
