package thw_matp.ui;

import thw_matp.ctrl.CtrlInventar;
import thw_matp.ctrl.CtrlPruefer;
import thw_matp.ctrl.CtrlPruefungen;
import thw_matp.ctrl.CtrlVorschrift;
import thw_matp.datatypes.Item;
import thw_matp.datatypes.Pruefer;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.UUID;

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

    public WindowMain(CtrlInventar ctrl_inventar, CtrlPruefer ctrl_pruefer, CtrlPruefungen ctrl_pruefungen, CtrlVorschrift ctrl_vorschriften) {
        this.ctrl_inventar = ctrl_inventar;
        this.ctrl_pruefer = ctrl_pruefer;
        this.ctrl_pruefungen = ctrl_pruefungen;
        this.ctrl_vorschriften = ctrl_vorschriften;
        this.btn_import.addActionListener(this::btn_import_action_performed);
        this.btn_pruefung.addActionListener(this::btn_pruefung_action_performed);
        this.btn_refresh.addActionListener(this::btn_refresh_action_performed);
        this.btn_inventar_add.addActionListener(this::btn_add_action_performed);
        this.btn_inventar_remove.addActionListener(this::btn_remove_action_performed);
        this.btn_inventar_edit.addActionListener(this::btn_edit_action_performed);
        this.btn_pruefer_add.addActionListener(this::btn_add_action_performed);
        this.btn_pruefer_remove.addActionListener(this::btn_remove_action_performed);
        this.btn_pruefer_edit.addActionListener(this::btn_edit_action_performed);
        this.btn_pruefung_remove.addActionListener(this::btn_remove_action_performed);
        this.btn_vorschrift_add.addActionListener(this::btn_add_action_performed);
        this.btn_vorschrift_remove.addActionListener(this::btn_remove_action_performed);
        this.inp_pruefung_kennzeichen.addActionListener(this::inp_pruefung_kennzeichen_action_performed);
        this.tbl_inventar.setAutoCreateRowSorter(true);
        this.tbl_pruefer.setAutoCreateRowSorter(true);
        this.tbl_pruefungen.setAutoCreateRowSorter(true);
        this.tbl_vorschriften.setAutoCreateRowSorter(true);
    }

    public JPanel get_root_panel() {
        return root_panel;
    }

    public void btn_import_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_import) {
            final JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
            if(fc.showOpenDialog(get_root_panel()) == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getPath();
                System.out.println("Selected: " + path);
                if(!this.ctrl_inventar.load_data(path))
                {
                    JOptionPane.showMessageDialog(get_root_panel(), "Daten konnten nicht geladen werden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
                populate_table_inventar();
            }
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    public void btn_pruefung_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_pruefung) {
            WindowPruefung win = new WindowPruefung("Prüfung", this.ctrl_inventar, this.ctrl_pruefer, this.ctrl_vorschriften, this.ctrl_pruefungen);
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

    public void btn_refresh_action_performed(ActionEvent e) {
        if(e.getSource() == this.btn_refresh) {
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

    public void btn_add_action_performed(ActionEvent e) {
        Object source = e.getSource();
        if (this.btn_inventar_add.equals(source)) {
            WindowAddItem win = new WindowAddItem("Neues Inventar", ctrl_inventar);
            win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            win.pack();
            win.setLocationRelativeTo(get_root_panel());
            win.setVisible(true);
        }
        else if (this.btn_pruefer_add.equals(source)) {
            WindowAddPruefer win = new WindowAddPruefer("Neuer Prüfer", ctrl_pruefer);
            win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            win.pack();
            win.setLocationRelativeTo(get_root_panel());
            win.setVisible(true);
        }
        else if (this.btn_vorschrift_add.equals(source)) {
            System.out.println("btn_add_action_performed() TODO: Vorschrift add");
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    public void btn_remove_action_performed(ActionEvent e) {
        Object source = e.getSource();
        Object[] options = {"Ja, unwiederruflich löschen!",
                "Nein"};
        if (this.btn_inventar_remove.equals(source)) {
            int selected_row = this.tbl_inventar.getSelectedRow();
            if(selected_row != -1) {
                String kennzeichen = this.tbl_inventar.getModel().getValueAt(this.tbl_inventar.convertRowIndexToModel(selected_row), 0).toString();
                int reply = JOptionPane.showOptionDialog(get_root_panel(),
                        "Sollen das Gerät mit dem Kennzeichen " + kennzeichen
                                + " und alle dazugehörigen Prüfungen wirklich unwiederruflich gelöscht werden?",
                        "Achtung!",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,     //do not use a custom Icon
                        options,  //the titles of buttons
                        options[0]); //default button title
                if (reply == JOptionPane.YES_OPTION) {
                    System.out.println("Inventar remove " + kennzeichen);
                    if (!this.ctrl_inventar.remove_item(kennzeichen, this.ctrl_pruefungen)) {
                        JOptionPane.showMessageDialog(get_root_panel(),
                                "Fehler beim Entfernen der Einträge zum Gerät " + kennzeichen,
                                "Fehler!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        populate_table_pruefungen();
                        populate_table_inventar();
                    }
                }
            }
        }
        else if (this.btn_pruefer_remove.equals(source)) {
            int selected_row = this.tbl_pruefer.getSelectedRow();
            if (selected_row != -1) {
                String pruefer_id = this.tbl_pruefer.getModel().getValueAt(this.tbl_pruefer.convertRowIndexToModel(selected_row), 2).toString();
                String pruefer_name   = this.tbl_pruefer.getModel().getValueAt(this.tbl_pruefer.convertRowIndexToModel(selected_row), 1).toString()
                                + " " + this.tbl_pruefer.getModel().getValueAt(this.tbl_pruefer.convertRowIndexToModel(selected_row), 0).toString();
                int reply = JOptionPane.showOptionDialog(get_root_panel(),
                        "Soll der Prüfer " + pruefer_name + " (ID: " + pruefer_id + ")\n"
                                + " wirklich unwiederruflich gelöscht werden?",
                        "Achtung!",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,     //do not use a custom Icon
                        options,  //the titles of buttons
                        options[0]); //default button title
                if (reply == JOptionPane.YES_OPTION) {
                    System.out.println("Pruefer remove" + pruefer_id);
                    if (!this.ctrl_pruefer.remove_pruefer(UUID.fromString(pruefer_id))) {
                        JOptionPane.showMessageDialog(get_root_panel(),
                                "Fehler beim Entfernen des Prüfers " + pruefer_name + " (ID: " + pruefer_id + ")!",
                                "Fehler!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        populate_table_pruefer();
                        populate_table_pruefungen();
                    }
                }
            }
        }
        else if (this.btn_pruefung_remove.equals(source)) {
            int selected_row = this.tbl_pruefungen.getSelectedRow();
            if (selected_row != -1) {
                String pruefung_id = this.tbl_pruefungen.getModel().getValueAt(this.tbl_pruefungen.convertRowIndexToModel(selected_row), 4).toString();
                int reply = JOptionPane.showOptionDialog(get_root_panel(),
                        "Soll die Prüfung mit der ID" + pruefung_id
                                + " wirklich unwiederruflich gelöscht werden?",
                        "Achtung!",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,     //do not use a custom Icon
                        options,  //the titles of buttons
                        options[0]); //default button title
                if (reply == JOptionPane.YES_OPTION) {
                    System.out.println("Pruefung remove" + pruefung_id);
                    if (!this.ctrl_pruefungen.remove_pruefung(UUID.fromString(pruefung_id))) {
                        JOptionPane.showMessageDialog(get_root_panel(),
                                "Fehler beim Entfernen der Prüfung mit der ID" + pruefung_id,
                                "Fehler!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        populate_table_pruefungen();
                    }
                }
            }
        }
        else if (this.btn_vorschrift_remove.equals(source)) {
            int selected_row = this.tbl_vorschriften.getSelectedRow();
            if (selected_row != -1) {
                String vorschrift_sachnr = this.tbl_vorschriften.getModel().getValueAt(this.tbl_vorschriften.convertRowIndexToModel(selected_row), 0).toString();
                int reply = JOptionPane.showOptionDialog(get_root_panel(),
                        "Sollen die Vorschrift zur Sachnummer " + vorschrift_sachnr
                                + " und alle dazugehörigen Prüfungen wirklich unwiederruflich gelöscht werden?",
                        "Achtung!",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,     //do not use a custom Icon
                        options,  //the titles of buttons
                        options[0]); //default button title
                if (reply == JOptionPane.YES_OPTION) {
                    System.out.println("Vorschrift remove" + vorschrift_sachnr);
                    if (!this.ctrl_vorschriften.remove_vorschrift(vorschrift_sachnr, ctrl_inventar, ctrl_pruefungen)) {
                        JOptionPane.showMessageDialog(get_root_panel(),
                                "Fehler beim Einträge zur Sachnummer " + vorschrift_sachnr,
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
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    public void btn_edit_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_inventar_edit) {
            Item i = null;
            int selected_row = this.tbl_inventar.getSelectedRow();
            String kennzeichen = this.tbl_inventar.getModel().getValueAt(this.tbl_inventar.convertRowIndexToModel(selected_row), 0).toString();
            i = this.ctrl_inventar.get_item(kennzeichen);
            if (i != null) {
                WindowEditItem win = new WindowEditItem("Editiere Inventar", ctrl_inventar, i);
                win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                win.pack();
                win.setLocationRelativeTo(get_root_panel());
                win.setVisible(true);
            }
        }
        else if (e.getSource() == this.btn_pruefer_edit) {
            Pruefer p = null;
            int selected_row = this.tbl_pruefer.getSelectedRow();
            UUID id = UUID.fromString(this.tbl_pruefer.getModel().getValueAt(this.tbl_pruefer.convertRowIndexToModel(selected_row), 2).toString());
            p = this.ctrl_pruefer.find(id);
            if(p != null) {
                WindowEditPruefer win = new WindowEditPruefer("Prüfer Editieren", this.ctrl_pruefer, p);
                win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                win.pack();
                win.setLocationRelativeTo(get_root_panel());
                win.setVisible(true);
            }
        }
        else if (e.getSource() == this.btn_pruefung_edit) {
            System.out.println("btn_edit_action_performed() TODO: Implement Edit Prüfung");
        }
        else if (e.getSource() == this.btn_vorschrift_edit) {
            System.out.println("btn_edit_action_performed() TODO: Implement Edit Vorschrift");
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    public void inp_pruefung_kennzeichen_action_performed(ActionEvent e) {
        if (e.getSource() == this.inp_pruefung_kennzeichen) {
            if (this.inp_pruefung_kennzeichen.getText().isEmpty())
            {
                populate_table_pruefungen();
            }
            else {
                this.tbl_pruefungen.setModel(this.ctrl_pruefungen.get_data(this.inp_pruefung_kennzeichen.getText()));
            }
            resize_table_column_width(this.tbl_pruefungen);
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    public void populate_table_inventar() {
//        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>();
//        TableModel mdl = this.ctrl_inventar.get_data();
//        sorter.setModel(mdl);
//        this.tbl_inventar.setModel(mdl);
//        this.tbl_inventar.setRowSorter(sorter);
//        this.tbl_inventar.setModel(this.ctrl_inventar.get_data());
        this.tbl_inventar.setModel(this.ctrl_inventar.get_data());
        resize_table_column_width(this.tbl_inventar);
    }

    public void populate_table_pruefer() {
        this.tbl_pruefer.setModel(this.ctrl_pruefer.get_data());
        resize_table_column_width(this.tbl_pruefer);
    }

    public void populate_table_pruefungen() {
        this.tbl_pruefungen.setModel(this.ctrl_pruefungen.get_data());
        resize_table_column_width(this.tbl_pruefungen);
    }

    public void populate_table_vorschriften() {
        this.tbl_vorschriften.setModel(this.ctrl_vorschriften.get_data());
        resize_table_column_width(this.tbl_vorschriften);
    }

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



    private final CtrlInventar ctrl_inventar;
    private final CtrlPruefer ctrl_pruefer;
    private final CtrlPruefungen ctrl_pruefungen;
    private final CtrlVorschrift ctrl_vorschriften;
}

