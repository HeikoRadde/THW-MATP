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
package thw_matp.ui;

import thw_matp.ctrl.*;
import thw_matp.datatypes.Item;
import thw_matp.datatypes.Inspector;
import thw_matp.datatypes.Inspection;
import thw_matp.datatypes.Specification;
import thw_matp.util.PrinterProtocolInspectionPDF;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Main program window
 */
public class WindowMain {
    private JButton btn_import;
    private JPanel root_panel;
    private JTable tbl_pruefer;
    private JTable tbl_inventar;
    private JButton btn_pruefung;
    private JTable tbl_pruefungen;
    private JTextField inp_pruefung_kennzeichen;
    private JTable tbl_vorschriften;
    private JButton btn_refresh;
    private JTabbedPane tabs;
    private JButton btn_pruefer_add;
    private JButton btn_pruefer_remove;
    private JButton btn_pruefung_remove;
    private JButton btn_vorschrift_add;
    private JButton btn_vorschrift_remove;
    private JButton btn_inventar_add;
    private JButton btn_inventar_remove;
    private JButton btn_pruefer_edit;
    private JButton btn_inventar_edit;
    private JButton btn_vorschrift_edit;
    private JButton btn_pruefung_edit;
    private JButton btn_license;
    private JButton btn_pruefung_print;
    private JLabel lbl_ip;
    private JLabel lbl_port;
    private JTextField txt_db_mode;
    private JTextField txt_ip;
    private JTextField txt_port;
    private JButton btn_inet_addr_infos;
    private JPanel pnl_local;
    private JPanel pnl_remote;
    private JPanel pnl_inet_info;
    private JPanel pnl_mode;
    private JLabel txt_version_author;

    public WindowMain(CtrlInventory ctrl_inventory, CtrlInspectors ctrl_inspectors, CtrlInspections ctrl_inspections, CtrlSpecifications ctrl_specifications) {
        this.ctrl_inventory = ctrl_inventory;
        this.ctrl_inspectors = ctrl_inspectors;
        this.ctrl_inspections = ctrl_inspections;
        this.ctrl_specifications = ctrl_specifications;
        this.btn_import.addActionListener(this::btn_import_action_performed);
        this.btn_pruefung.addActionListener(this::btn_pruefung_action_performed);
        this.btn_refresh.addActionListener(this::btn_refresh_action_performed);
        this.btn_inventar_add.addActionListener(this::btn_add_action_performed);
        this.btn_inventar_remove.addActionListener(this::btn_remove_action_performed);
        this.btn_inventar_edit.addActionListener(this::btn_edit_action_performed);
        this.btn_pruefer_add.addActionListener(this::btn_add_action_performed);
        this.btn_pruefer_remove.addActionListener(this::btn_remove_action_performed);
        this.btn_pruefer_edit.addActionListener(this::btn_edit_action_performed);
        this.btn_pruefung_edit.addActionListener(this::btn_edit_action_performed);
        this.btn_pruefung_remove.addActionListener(this::btn_remove_action_performed);
        this.btn_pruefung_print.addActionListener(this::btn_pruefung_print_action_performed);
        this.btn_vorschrift_add.addActionListener(this::btn_add_action_performed);
        this.btn_vorschrift_edit.addActionListener(this::btn_edit_action_performed);
        this.btn_vorschrift_remove.addActionListener(this::btn_remove_action_performed);
        this.inp_pruefung_kennzeichen.addActionListener(this::inp_pruefung_kennzeichen_action_performed);
        this.btn_license.addActionListener(this::btn_licence_action_performed);
        this.btn_inet_addr_infos.addActionListener(this::btn_inet_addr_info_action_performed);
        this.tbl_inventar.setAutoCreateRowSorter(true);
        this.tbl_pruefer.setAutoCreateRowSorter(true);
        this.tbl_pruefungen.setAutoCreateRowSorter(true);
        this.tbl_vorschriften.setAutoCreateRowSorter(true);

        if(Settings.getInstance().db_is_local()) {
            this.txt_db_mode.setText("Lokal");
            this.pnl_inet_info.remove(this.pnl_remote);
        }
        else {
            this.txt_db_mode.setText("Netzwerk");
            this.txt_ip.setText(Settings.getInstance().get_ip());
            this.txt_port.setText(Settings.getInstance().get_port());
            this.pnl_inet_info.remove(this.pnl_local);
            this.pnl_inet_info.remove(this.btn_inet_addr_infos);
        }

        this.txt_version_author.setText("THW-MATP V" + Settings.getInstance().get_version() + " von Heiko Radde");
    }

    public JPanel get_root_panel() {
        return root_panel;
    }

    /**
     *          Event-handler for clicks on the 'Import' button.
     * @param e Event
     */
    public void btn_import_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_import) {
            final JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
            fc.setDialogTitle("CSV Datei ausw??hlen");
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            if (fc.showOpenDialog(get_root_panel()) == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getPath();
                if(!this.ctrl_inventory.load_data(path))
                {
                    JOptionPane.showMessageDialog(get_root_panel(), "Daten konnten nicht geladen werden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
                populate_table_inventar();
                ArrayList<String> new_vorschriften = this.ctrl_inventory.get_added_vorschriften();
                if (new_vorschriften != null) {
                    StringBuilder string_list = new StringBuilder();
                    for (String vorschrift : new_vorschriften) {
                        string_list.append(vorschrift);
                        string_list.append("\n");
                    }
                    JOptionPane.showMessageDialog(get_root_panel(),
                            "Beim Importieren der Datei wurden folgende, bisher unbekannte, Vorschriften gefunden:\n"
                                    + string_list
                                    + "Die unbekannten Vorschriften wurden mit leeren Daten erstellt und zur Datenbank hiunzugef??gt.",
                            "Vorschriften automatisch erstellt",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    /**
     *          Event-handler for clicking on the 'Pr??fung' button to start a inspection
     * @param e Event
     */
    public void btn_pruefung_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_pruefung) {
            WindowInspection win = new WindowInspection("Pr??fung", this.ctrl_inventory, this.ctrl_inspectors, this.ctrl_specifications, this.ctrl_inspections);
            win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            win.pack();
            win.setLocationRelativeTo(get_root_panel());
            win.setVisible(true);
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    /**
     *          Event-handle for clicking on the refresh button. Refreshes the currently visible table.
     * @param e Event
     */
    public void btn_refresh_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_refresh) {
            switch (tabs.getSelectedIndex()) {
                case 0 -> populate_table_inventar();
                case 1 -> populate_table_pruefer();
                case 2 -> populate_table_pruefungen();
                case 3 -> populate_table_vorschriften();
            }
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    /**
     *          Event-handler for clicking on the add buttons. Starts the dialog for adding a new entry to
     *          the database, depending on the currently selected table
     * @param e Event
     */
    public void btn_add_action_performed(ActionEvent e) {
        Object source = e.getSource();
        if (this.btn_inventar_add.equals(source)) {
            WindowAddItem win = new WindowAddItem("Neues Inventar", this.ctrl_inventory, this.ctrl_specifications);
            win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            win.pack();
            win.setLocationRelativeTo(get_root_panel());
            win.setVisible(true);
        }
        else if (this.btn_pruefer_add.equals(source)) {
            WindowAddInspector win = new WindowAddInspector("Neuer Pr??fer", this.ctrl_inspectors);
            win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            win.pack();
            win.setLocationRelativeTo(get_root_panel());
            win.setVisible(true);
        }
        else if (this.btn_vorschrift_add.equals(source)) {
            WindowAddSpecification win = new WindowAddSpecification(this.ctrl_specifications);
            win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            win.pack();
            win.setLocationRelativeTo(get_root_panel());
            win.setVisible(true);
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    /**
     *          Event-handler for the delete buttons. Starts the dialog for removing an entry from the database,
     *          depending on the currently selected table and entry.
     * @param e Event
     */
    public void btn_remove_action_performed(ActionEvent e) {
        Object source = e.getSource();
        Object[] options = {"Ja, unwiederruflich l??schen!",
                "Nein"};
        if (this.btn_inventar_remove.equals(source)) {
            int selected_row = this.tbl_inventar.getSelectedRow();
            if (selected_row != -1) {
                String kennzeichen = this.tbl_inventar.getModel().getValueAt(this.tbl_inventar.convertRowIndexToModel(selected_row), 0).toString();
                int reply = JOptionPane.showOptionDialog(get_root_panel(),
                        "Sollen das Ger??t mit dem Kennzeichen " + kennzeichen
                                + " und alle dazugeh??rigen Pr??fungen wirklich unwiederruflich gel??scht werden?",
                        "Achtung!",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,     //do not use a custom Icon
                        options,  //the titles of buttons
                        options[0]); //default button title
                if (reply == JOptionPane.YES_OPTION) {
                    System.out.println("Inventar remove " + kennzeichen);
                    if (!this.ctrl_inventory.remove_item(kennzeichen, this.ctrl_inspections)) {
                        JOptionPane.showMessageDialog(get_root_panel(),
                                "Fehler beim Entfernen der Eintr??ge zum Ger??t " + kennzeichen,
                                "Fehler!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        populate_table_pruefungen();
                        populate_table_inventar();
                    }
                }
            }
            else {
                _error_remove_selection();
                return;
            }
        }
        else if (this.btn_pruefer_remove.equals(source)) {
            int selected_row = this.tbl_pruefer.getSelectedRow();
            if (selected_row != -1) {
                String pruefer_id = this.tbl_pruefer.getModel().getValueAt(this.tbl_pruefer.convertRowIndexToModel(selected_row), 2).toString();
                String pruefer_name   = this.tbl_pruefer.getModel().getValueAt(this.tbl_pruefer.convertRowIndexToModel(selected_row), 1).toString()
                                + " " + this.tbl_pruefer.getModel().getValueAt(this.tbl_pruefer.convertRowIndexToModel(selected_row), 0).toString();
                int reply = JOptionPane.showOptionDialog(get_root_panel(),
                        "Soll der Pr??fer " + pruefer_name + " (ID: " + pruefer_id + ")\n"
                                + " wirklich unwiederruflich gel??scht werden?",
                        "Achtung!",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,     //do not use a custom Icon
                        options,  //the titles of buttons
                        options[0]); //default button title
                if (reply == JOptionPane.YES_OPTION) {
                    System.out.println("Pruefer remove" + pruefer_id);
                    if (!this.ctrl_inspectors.remove_inspector(UUID.fromString(pruefer_id))) {
                        JOptionPane.showMessageDialog(get_root_panel(),
                                "Fehler beim Entfernen des Pr??fers " + pruefer_name + " (ID: " + pruefer_id + ")!",
                                "Fehler!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        populate_table_pruefer();
                        populate_table_pruefungen();
                    }
                }
            }
            else {
                _error_remove_selection();
                return;
            }
        }
        else if (this.btn_pruefung_remove.equals(source)) {
            int selected_row = this.tbl_pruefungen.getSelectedRow();
            if (selected_row != -1) {
                String pruefung_id = this.tbl_pruefungen.getModel().getValueAt(this.tbl_pruefungen.convertRowIndexToModel(selected_row), 4).toString();
                int reply = JOptionPane.showOptionDialog(get_root_panel(),
                        "Soll die Pr??fung mit der ID" + pruefung_id
                                + " wirklich unwiederruflich gel??scht werden?",
                        "Achtung!",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,     //do not use a custom Icon
                        options,  //the titles of buttons
                        options[0]); //default button title
                if (reply == JOptionPane.YES_OPTION) {
                    System.out.println("Pruefung remove" + pruefung_id);
                    if (!this.ctrl_inspections.remnove_inspection(UUID.fromString(pruefung_id))) {
                        JOptionPane.showMessageDialog(get_root_panel(),
                                "Fehler beim Entfernen der Pr??fung mit der ID" + pruefung_id,
                                "Fehler!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        populate_table_pruefungen();
                    }
                }
            }
            else {
                _error_remove_selection();
                return;
            }
        }
        else if (this.btn_vorschrift_remove.equals(source)) {
            int selected_row = this.tbl_vorschriften.getSelectedRow();
            if (selected_row != -1) {
                String vorschrift_sachnr = this.tbl_vorschriften.getModel().getValueAt(this.tbl_vorschriften.convertRowIndexToModel(selected_row), 0).toString();
                int reply = JOptionPane.showOptionDialog(get_root_panel(),
                        "Sollen die Vorschrift zur Sachnummer " + vorschrift_sachnr
                                + " und alle dazugeh??rigen Pr??fungen wirklich unwiederruflich gel??scht werden?",
                        "Achtung!",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,     //do not use a custom Icon
                        options,  //the titles of buttons
                        options[0]); //default button title
                if (reply == JOptionPane.YES_OPTION) {
                    System.out.println("Vorschrift remove" + vorschrift_sachnr);
                    if (!this.ctrl_specifications.remove_specification(vorschrift_sachnr, ctrl_inventory, ctrl_inspections)) {
                        JOptionPane.showMessageDialog(get_root_panel(),
                                "Fehler beim Eintr??ge zur Sachnummer " + vorschrift_sachnr,
                                "Fehler!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        populate_table_inventar();
                        populate_table_pruefungen();
                        populate_table_vorschriften();
                    }
                }
            }
            else {
                _error_remove_selection();
                return;
            }
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    /**
     *          Event-handler for the edit buttons. Starts the dialog for editing an entry in the database,
     *          depending on the currently selected table and entry.
     * @param e Event
     */
    public void btn_edit_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_inventar_edit) {
            Item i;
            int selected_row = this.tbl_inventar.getSelectedRow();
            if(selected_row < 0) {
                _error_edit_selection();
                return;
            }
            String kennzeichen = this.tbl_inventar.getModel().getValueAt(this.tbl_inventar.convertRowIndexToModel(selected_row), 0).toString();
            i = this.ctrl_inventory.get_item(kennzeichen);
            if (i != null) {
                WindowEditItem win = new WindowEditItem("Editiere Inventar", this.ctrl_inventory, i, this.ctrl_specifications);
                win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                win.pack();
                win.setLocationRelativeTo(get_root_panel());
                win.setVisible(true);
            }
        }
        else if (e.getSource() == this.btn_pruefer_edit) {
            Inspector p;
            int selected_row = this.tbl_pruefer.getSelectedRow();
            if(selected_row < 0) {
                _error_edit_selection();
                return;
            }
            UUID id = UUID.fromString(this.tbl_pruefer.getModel().getValueAt(this.tbl_pruefer.convertRowIndexToModel(selected_row), 2).toString());
            p = this.ctrl_inspectors.find(id);
            if(p != null) {
                WindowEditInspector win = new WindowEditInspector("Pr??fer Editieren", this.ctrl_inspectors, p);
                win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                win.pack();
                win.setLocationRelativeTo(get_root_panel());
                win.setVisible(true);
            }
        }
        else if (e.getSource() == this.btn_pruefung_edit) {
            Inspection p;
            int selected_row = this.tbl_pruefungen.getSelectedRow();
            if(selected_row < 0) {
                _error_edit_selection();
                return;
            }
            String id = this.tbl_pruefungen.getModel().getValueAt(this.tbl_pruefungen.convertRowIndexToModel(selected_row), 6).toString();
            p = this.ctrl_inspections.find(UUID.fromString(id));
            if (p != null) {
                WindowEditInspection win = new WindowEditInspection(p, ctrl_inventory, ctrl_inspectors, ctrl_specifications, ctrl_inspections);
                win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                win.pack();
                win.setLocationRelativeTo(get_root_panel());
                win.setVisible(true);
            }
        }
        else if (e.getSource() == this.btn_vorschrift_edit) {
            Specification v;
            int selected_row = this.tbl_vorschriften.getSelectedRow();
            if(selected_row < 0) {
                _error_edit_selection();
                return;
            }
            String sachnummer = this.tbl_vorschriften.getModel().getValueAt(this.tbl_vorschriften.convertRowIndexToModel(selected_row), 0).toString();
            v = this.ctrl_specifications.get_specification(sachnummer);
            if (v != null) {
                WindowEditSpecification win = new WindowEditSpecification(this.ctrl_specifications, v);
                win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                win.pack();
                win.setLocationRelativeTo(get_root_panel());
                win.setVisible(true);
            }
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    /**
     * Error-message for missing selection of entry to be edited.
     */
    private void _error_edit_selection() {
        JOptionPane.showMessageDialog(get_root_panel(),
                "Bitte den Eintrag ausw??hlen, welcher editiert werden soll!",
                "Fehler!",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Error-message for missing selection of entry to be deleted.
     */
    private void _error_remove_selection() {
        JOptionPane.showMessageDialog(get_root_panel(),
                "Bitte den Eintrag ausw??hlen, welcher entfernt werden soll!",
                "Fehler!",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     *          Event-handler for the search field of inspections
     * @param e Event
     */
    public void inp_pruefung_kennzeichen_action_performed(ActionEvent e) {
        if (e.getSource() == this.inp_pruefung_kennzeichen) {
            if (this.inp_pruefung_kennzeichen.getText().isEmpty())
            {
                populate_table_pruefungen();
            }
            else {
                this.tbl_pruefungen.setModel(this.ctrl_inspections.get_data(this.inp_pruefung_kennzeichen.getText()));
            }
            resize_table_column_width(this.tbl_pruefungen);
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    /**
     *          Event-handler for the print button. Generates the protocol PDF of a selected inspections
     * @param e Event
     */
    public void btn_pruefung_print_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_pruefung_print) {
            int[] selections = this.tbl_pruefungen.getSelectedRows();
            JFileChooser f = new JFileChooser();
            f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            f.setCurrentDirectory(new java.io.File("."));
            f.setDialogTitle("Ordner ausw??hlen");
            if (f.showSaveDialog(this.root_panel) == JFileChooser.APPROVE_OPTION) {
                Path path = java.nio.file.Paths.get(f.getSelectedFile().getAbsolutePath());
                if (Files.notExists(path)) {
                    JOptionPane.showMessageDialog(get_root_panel(),
                            "Der Ordner '" + path.toAbsolutePath().toString() + "' existiert nicht!",
                            "Fehler!",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                for (int selection : selections) {
                    UUID id = UUID.fromString(this.tbl_pruefungen.getModel().getValueAt(this.tbl_pruefungen.convertRowIndexToModel(selection), 7).toString());
                    Inspection inspection = this.ctrl_inspections.find(id);
                    if (inspection == null) {
                        continue;
                    }
                    Inspector inspector;
                    if (inspection.pruefer.equals(new UUID(0, 0))) {
                        inspector = new Inspector(new UUID(0, 0), "", "");
                    } else {
                        inspector = this.ctrl_inspectors.find(inspection.pruefer);
                    }
                    Item item = this.ctrl_inventory.get_item(inspection.kennzeichen);
                    Specification specification = this.ctrl_specifications.get_specification(item.sachnr);
                    try {
                        PrinterProtocolInspectionPDF.print_pruefung(path.toAbsolutePath(), inspection, inspector, item, specification, true);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    /**
     *          Event-handler for the license button. Opens the dialog with the licensing informations
     * @param e Event
     */
    public void btn_licence_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_license) {
            WindowLicense win = new WindowLicense();
            win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            win.pack();
            win.setLocationRelativeTo(get_root_panel());
            win.setVisible(true);
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    /**
     *          Event-handler for the internet-address-info button. Opens a dialog with the inet-addr infos
     * @param e Event
     */
    public void btn_inet_addr_info_action_performed(ActionEvent e) {
        if(e.getSource() == this.btn_inet_addr_infos) {
            DialogInetInfo dialogInetInfo = new DialogInetInfo();
            dialogInetInfo.pack();
            dialogInetInfo.setLocationRelativeTo(this.get_root_panel());
            dialogInetInfo.setVisible(true);
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    /**
     * Retrieve all inventory data from the database and populate the table with it
     */
    public void populate_table_inventar() {
//        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>();
//        TableModel mdl = this.ctrl_inventar.get_data();
//        sorter.setModel(mdl);
//        this.tbl_inventar.setModel(mdl);
//        this.tbl_inventar.setRowSorter(sorter);
//        this.tbl_inventar.setModel(this.ctrl_inventar.get_data());
        this.tbl_inventar.setModel(this.ctrl_inventory.get_data());
        resize_table_column_width(this.tbl_inventar);
    }

    /**
     * Retrieve all inspectors data from the database and populate the table with it
     */
    public void populate_table_pruefer() {
        this.tbl_pruefer.setModel(this.ctrl_inspectors.get_data());
        resize_table_column_width(this.tbl_pruefer);
    }

    /**
     * Retrieve all inspections data from the database and populate the table with it
     */
    public void populate_table_pruefungen() {
        this.tbl_pruefungen.setModel(this.ctrl_inspections.get_data());
        resize_table_column_width(this.tbl_pruefungen);
    }

    /**
     * Retrieve all specifications data from the database and populate the table with it
     */
    public void populate_table_vorschriften() {
        this.tbl_vorschriften.setModel(this.ctrl_specifications.get_data());
        resize_table_column_width(this.tbl_vorschriften);
    }

    /**
     *              Resize a specified tables columns so that their data fits
     * @param table Table to resize
     */
    public static void resize_table_column_width(JTable table)
    {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = table.getGraphics().getFontMetrics().stringWidth(table.getColumnName(column)) + (2*table.getColumnModel().getColumnMargin()) + 10;
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 300) {
                width = 300;
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
    }



    private final CtrlInventory ctrl_inventory;
    private final CtrlInspectors ctrl_inspectors;
    private final CtrlInspections ctrl_inspections;
    private final CtrlSpecifications ctrl_specifications;
}

