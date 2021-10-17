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
import thw_matp.datatypes.Inspection;
import thw_matp.datatypes.Inspector;
import thw_matp.datatypes.Item;
import thw_matp.datatypes.Specification;
import thw_matp.util.PrinterProtocolInspectionPDF;
import thw_matp.util.PrinterProtocolInspectionsOverviewCSV;
import thw_matp.util.PrinterProtocolInspectionsOverviewPDF;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Window for entering an inspection
 */
public class WindowInspection extends JFrame {

    private JButton btn_ok;
    private JButton btn_fail;
    private JPanel root_panel;
    private JTextField inp_kennzeichen;
    private JComboBox<String> sel_pruefer;
    private JButton btn_end;
    private JTextArea txt_bemerkungen;
    private JRadioButton rb_yes;
    private JTextField txt_ov;
    private JTextField txt_einheit;
    private JTextField txt_hersteller;
    private JTextField txt_bezeichung;
    private JTextField txt_baujahr;
    private JTextField txt_pruefungsvorschrift;
    private JTextField txt_abschnitt;
    private JRadioButton rb_no;
    private JTextField txt_sachnummer;
    private JTextField txt_link;
    private JCheckBox check_create_protocol;
    private JTextField txt_save_path;
    private JTextField txt_ort;
    private JButton btn_select_save_path;
    private JCheckBox check_print_protocol;
    private JButton btn_printer_settings;

    public WindowInspection(String title, CtrlInventory ctrl_inventar, CtrlInspectors ctrl_pruefer, CtrlSpecifications ctrl_vorschrift, CtrlInspections ctrl_pruefungen) {
        super(title);
        this.setContentPane(root_panel);

        try {
            this.m_inspector_list = ctrl_pruefer.get_all();
            for (Inspector p : this.m_inspector_list) {
                this.sel_pruefer.addItem(p.vorname + " " + p.name);
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }


        this.btn_ok.addActionListener(this::btn_ok_action_performed);
        this.btn_fail.addActionListener(this::btn_fail_action_performed);
        this.btn_end.addActionListener(this::btn_end_action_performed);
        this.inp_kennzeichen.addActionListener(this::inp_kennzeichen_action_performed);
        this.btn_select_save_path.addActionListener(this::btn_select_save_path_action_performed);
        this.sel_pruefer.addActionListener(this::sel_pruefer_action_performed);
        this.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                inp_kennzeichen.requestFocus();
            }
        });
        this.txt_save_path.setText(Settings.getInstance().get_path_protocols().toString());
        this.btn_printer_settings.addActionListener(this::btn_printer_settings_action_performed);

        this.m_ctrl_inventar = ctrl_inventar;
        this.m_ctrl_vorschrift = ctrl_vorschrift;
        this.m_ctrl_pruefungen = ctrl_pruefungen;
    }


    public void btn_ok_action_performed(ActionEvent e) {
        if (e.getSource() == btn_ok) {
            if(_enter_pruefung(true)) {
                _clear_fields();
                this.inp_kennzeichen.setText("");
                this.inp_kennzeichen.requestFocus();
            }
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    public void btn_fail_action_performed(ActionEvent e) {
        if (e.getSource() == btn_fail) {
            if(_enter_pruefung(false)) {
                _clear_fields();
                this.inp_kennzeichen.setText("");
                this.inp_kennzeichen.requestFocus();
            }
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    public void btn_end_action_performed(ActionEvent e) {
        if (e.getSource() == btn_end) {
            dispose();
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    public void inp_kennzeichen_action_performed(ActionEvent e) {
        if (e.getSource() == inp_kennzeichen) {
            _clear_fields();
            _fill_fields();
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    public void btn_select_save_path_action_performed(ActionEvent e) {
        if (e.getSource() == btn_select_save_path) {
            JFileChooser f = new JFileChooser();
            f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            f.setCurrentDirectory(Settings.getInstance().get_path_protocols().toFile());
            f.setDialogTitle("Ordner auswählen");
            if (f.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                Settings.getInstance().set_path_protocols(java.nio.file.Paths.get(f.getSelectedFile().getAbsolutePath()));
                this.txt_save_path.setText(Settings.getInstance().get_path_protocols().toString());
            }
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    public void btn_printer_settings_action_performed(ActionEvent e) {
        if (e.getSource() == btn_printer_settings) {
            PrinterJob printJob = PrinterJob.getPrinterJob();
            if(printJob.printDialog()) {
                Settings.getInstance().set_printer(printJob.getPrintService());
            }
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }


    public void sel_pruefer_action_performed(ActionEvent e) {
        if (e.getSource() == sel_pruefer) {
            this.sel_pruefer.setBackground(Color.WHITE);
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    private void _fill_fields() {
        this.m_current_item = this.m_ctrl_inventar.get_item(this.inp_kennzeichen.getText());
        if (m_current_item == null) {
            _error_kennzeichen(this.inp_kennzeichen.getText());
            return;
        }
        this.m_current_specification = this.m_ctrl_vorschrift.get_specification(this.m_current_item.sachnr);
        if (this.m_current_specification == null) {
            _error_sachnummer(this.m_current_item.sachnr);
            return;
        }

        this.txt_ov.setText(this.m_current_item.ov);
        this.txt_einheit.setText(this.m_current_item.einheit);
        this.txt_bezeichung.setText(this.m_current_item.bezeichnung);
        this.txt_hersteller.setText(this.m_current_item.hersteller);
        this.txt_sachnummer.setText(this.m_current_item.sachnr);
        this.txt_baujahr.setText(Integer.toString(this.m_current_item.baujahr));
        this.txt_pruefungsvorschrift.setText(this.m_current_specification.vorschrift);
        this.txt_abschnitt.setText(this.m_current_specification.abschnitt);
        if(this.m_current_specification.link != null) {
            this.txt_link.setText(this.m_current_specification.link);
        }
    }

    private void _clear_fields() {
        this.txt_ov.setText("");
        this.txt_einheit.setText("");
        this.txt_bezeichung.setText("");
        this.txt_hersteller.setText("");
        this.txt_sachnummer.setText("");
        this.txt_baujahr.setText("");
        this.txt_pruefungsvorschrift.setText("");
        this.txt_abschnitt.setText("");
        this.txt_link.setText("");
        this.rb_no.setSelected(true);
        this.rb_yes.setSelected(false);

        this.m_current_item = null;
        this.m_current_specification = null;

        this.inp_kennzeichen.setBackground(Color.WHITE);
    }

    private boolean _enter_pruefung(boolean bestanden) {
        boolean ausgesondert = false;
        if (rb_yes.isSelected()) ausgesondert = true;
        this.m_current_item = this.m_ctrl_inventar.get_item(this.inp_kennzeichen.getText());
        if (m_current_item == null) {
            _error_kennzeichen(this.inp_kennzeichen.getText());
            return false;
        }
        this.m_current_specification = this.m_ctrl_vorschrift.get_specification(this.m_current_item.sachnr);
        if (this.m_current_specification == null) {
            _error_sachnummer(this.m_current_item.sachnr);
            return false;
        }
        int selected_pruefer = this.sel_pruefer.getSelectedIndex();
        if (selected_pruefer == -1) {
            _error_pruefer();
            return false;
        }
        String kennzeichen = this.inp_kennzeichen.getText();
        if (this.m_ctrl_inventar.get_item(kennzeichen) == null) {
            _error_kennzeichen(kennzeichen);
            return false;
        }
        String ov = this.txt_ort.getText();
        if (ov.isEmpty()) {
            _error_ort();
            return false;
        }
        else {
            Inspection p = this.m_ctrl_pruefungen.add_inspection(kennzeichen, this.m_inspector_list.get(selected_pruefer).id, bestanden, this.txt_bemerkungen.getText(), ausgesondert, ov);
            if (this.check_create_protocol.isSelected()) {
                try {
                    PrinterProtocolInspectionsOverviewPDF.set_path(Settings.getInstance().get_path_protocols());
                    PrinterProtocolInspectionPDF.print_pruefung(Settings.getInstance().get_path_protocols(), p, this.m_inspector_list.get(this.sel_pruefer.getSelectedIndex()), this.m_current_item, this.m_current_specification, this.check_print_protocol.isSelected());
                    PrinterProtocolInspectionsOverviewCSV.add_pruefung_event(Settings.getInstance().get_path_protocols(), p, this.m_inspector_list.get(this.sel_pruefer.getSelectedIndex()));
                } catch (IOException e) {
                    e.printStackTrace();
                    _error_pdf();
                }
            }
            return true;
        }
    }


    private void _error_kennzeichen(String kennzeichen) {
        Object[] options = {"Ja, neu anlegen!",
                "Nein"};
        int reply = JOptionPane.showOptionDialog(this.root_panel,
                "Das Gerät mit dem Kennzeichen " + kennzeichen + " ist unbekannt! Soll es neu angelegt werden?",
                "Gerät " + kennzeichen + " unbekannt!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title
        if (reply == JOptionPane.YES_OPTION) {
            WindowAddItem win = new WindowAddItem("Neues Inventar", this.m_ctrl_inventar, this.m_ctrl_vorschrift);
            win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            win.pack();
            win.setLocationRelativeTo(this.root_panel);
            win.setVisible(true);
        }
        this.inp_kennzeichen.setBackground(Color.RED);
    }

    private void _error_sachnummer(String sachnummer) {
        JOptionPane.showMessageDialog(this.root_panel,
                "Keine Vorschrift zur Sachnummer " + sachnummer + " gefunden!",
                "Fehlende Sachnummer!",
                JOptionPane.ERROR_MESSAGE);
    }

    private void _error_pruefer() {
        JOptionPane.showMessageDialog(this.root_panel,
                "Kein Prüfer ausgewählt!",
                "Fehlender Prüfer!",
                JOptionPane.ERROR_MESSAGE);
        this.sel_pruefer.setBackground(Color.RED);
    }

    private void _error_pdf() {
        JOptionPane.showMessageDialog(this.root_panel,
                "Fehler beim Erstellen der PDF Datei!",
                "Fehler!",
                JOptionPane.ERROR_MESSAGE);
    }

    private void _error_ort() {
        JOptionPane.showMessageDialog(this.root_panel,
                "Der Ort der Prüfung fehlt!",
                "Fehler!",
                JOptionPane.ERROR_MESSAGE);
    }


    private final CtrlInventory m_ctrl_inventar;
    private final CtrlSpecifications m_ctrl_vorschrift;
    private final CtrlInspections m_ctrl_pruefungen;

    private Item m_current_item;
    private Specification m_current_specification;

    private List<Inspector> m_inspector_list;
}
