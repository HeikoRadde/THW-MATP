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
import thw_matp.datatypes.Pruefer;
import thw_matp.datatypes.Pruefung;
import thw_matp.datatypes.Vorschrift;
import thw_matp.util.PrinterProtocolTestingOverviewCSV;
import thw_matp.util.PrinterProtocolTestingOverviewPDF;
import thw_matp.util.PrinterProtocolTestingPDF;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Window for editing existing Prüfungen in the database
 */
public class WindowEditPruefung extends JFrame {
    private JPanel root_panel;
    private JButton btn_ok;
    private JButton btn_fail;
    private JButton btn_end;
    private JTextField inp_kennzeichen;
    private JTextArea txt_bemerkungen;
    private JRadioButton rb_yes;
    private JTextField txt_ov;
    private JTextField txt_einheit;
    private JTextField txt_baujahr;
    private JTextField txt_pruefungsvorschrift;
    private JTextField txt_abschnitt;
    private JRadioButton rb_no;
    private JComboBox<String> sel_pruefer;
    private JTextField txt_bezeichung;
    private JTextField txt_hersteller;
    private JTextField txt_sachnummer;
    private JTextField txt_link;
    private JCheckBox check_create_protocol;
    private JTextField txt_save_path;
    private JTextField txt_id;
    private JTextField txt_tag;
    private JTextField txt_monat;
    private JTextField txt_jahr;
    private JButton btn_select_save_path;

    public WindowEditPruefung(Pruefung pruefung, CtrlInventar ctrl_inventar, CtrlPruefer ctrl_pruefer, CtrlVorschrift ctrl_vorschrift, CtrlPruefungen ctrl_pruefungen) {
        super("Prüfung editieren");
        this.setContentPane(root_panel);

        this.m_ctrl_inventar = ctrl_inventar;
        this.m_ctrl_vorschrift = ctrl_vorschrift;
        this.m_ctrl_pruefungen = ctrl_pruefungen;

        int selected_pruefer = -1;
        try {
            this.m_pruefer_list = ctrl_pruefer.get_all();
            int i = 0;
            for (Pruefer p : this.m_pruefer_list) {
                this.sel_pruefer.addItem(p.vorname + " " + p.name);
                if(p.id == pruefung.pruefer) {
                    selected_pruefer = i;
                }
                ++i;
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        this.sel_pruefer.setSelectedIndex(selected_pruefer);

        this.m_current_item = ctrl_inventar.get_item(pruefung.kennzeichen);

        this.txt_id.setText(pruefung.id.toString());
        this.inp_kennzeichen.setText(pruefung.kennzeichen);
        _fill_fields();
        this.txt_bemerkungen.setText(pruefung.bemerkungen);
        this.txt_tag.setText(Integer.toString(pruefung.datum.getDayOfMonth()));
        this.txt_monat.setText(Integer.toString(pruefung.datum.getMonthValue()));
        this.txt_jahr.setText(Integer.toString(pruefung.datum.getYear()));


        this.btn_ok.addActionListener(this::btn_ok_action_performed);
        this.btn_fail.addActionListener(this::btn_fail_action_performed);
        this.btn_end.addActionListener(this::btn_end_action_performed);
        this.btn_select_save_path.addActionListener(this::btn_select_save_path_action_performed);
        this.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                inp_kennzeichen.requestFocus();
            }
        });
        this.txt_save_path.setText(Settings.getInstance().get_path_protocols().toString());
    }


    public void btn_ok_action_performed(ActionEvent e) {
        if (e.getSource() == btn_ok) {
            if (_enter_pruefung(true)) dispose();
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    public void btn_fail_action_performed(ActionEvent e) {
        if (e.getSource() == btn_fail) {
            if (_enter_pruefung(false)) dispose();
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

    private void _fill_fields() {
        if(this.m_current_item == null) {
            _error_kennzeichen(this.inp_kennzeichen.getText());
            return;
        }
        this.m_current_vorschrift = this.m_ctrl_vorschrift.get_vorschrift(this.m_current_item.sachnr);
        if(this.m_current_vorschrift == null) {
            _error_sachnummer(this.m_current_item.sachnr);
            return;
        }

        this.txt_ov.setText(this.m_current_item.ov);
        this.txt_einheit.setText(this.m_current_item.einheit);
        this.txt_bezeichung.setText(this.m_current_item.bezeichnung);
        this.txt_hersteller.setText(this.m_current_item.hersteller);
        this.txt_sachnummer.setText(this.m_current_item.sachnr);
        this.txt_baujahr.setText(Integer.toString(this.m_current_item.baujahr));
        this.txt_pruefungsvorschrift.setText(this.m_current_vorschrift.vorschrift);
        this.txt_abschnitt.setText(this.m_current_vorschrift.abschnitt);
        if(this.m_current_vorschrift.link != null) {
            this.txt_link.setText(this.m_current_vorschrift.link);
        }
    }

    private boolean _enter_pruefung(boolean bestanden) {
        boolean ausgesondert = false;
        if (rb_yes.isSelected()) ausgesondert = true;
        this.m_current_item = this.m_ctrl_inventar.get_item(this.inp_kennzeichen.getText());
        if(m_current_item == null) {
            _error_kennzeichen(this.inp_kennzeichen.getText());
            return false;
        }
        this.m_current_vorschrift = this.m_ctrl_vorschrift.get_vorschrift(this.m_current_item.sachnr);
        if(this.m_current_vorschrift == null) {
            _error_sachnummer(this.m_current_item.sachnr);
            return false;
        }
        int day, month, year;
        try {
            day = Integer.parseInt(this.txt_tag.getText());
        }
        catch (NumberFormatException e) {
            _error_day();
            return false;
        }
        try {
            month = Integer.parseInt(this.txt_monat.getText());
        }
        catch (NumberFormatException e) {
            _error_month();
            return false;
        }
        try {
            year = Integer.parseInt(this.txt_jahr.getText());
        }
        catch (NumberFormatException e) {
            _error_year();
            return false;
        }
        LocalDate datum;
        try {
            datum = LocalDate.of(year, month, day);
        }
        catch (DateTimeException e) {
            _error_date();
            return false;
        }
        int pruefer_selected = this.sel_pruefer.getSelectedIndex();
        if(pruefer_selected == -1) {
            _error_pruefer();
            return false;
        }
        if(!this.m_ctrl_pruefungen.edit_pruefung(UUID.fromString(this.txt_id.getText()), this.inp_kennzeichen.getText(), datum, this.m_pruefer_list.get(pruefer_selected).id, bestanden, this.txt_bemerkungen.getText(), ausgesondert)) {
            _error_edit();
            return false;
        }
        Pruefung p = this.m_ctrl_pruefungen.find(UUID.fromString(this.txt_id.getText()));
        if (this.check_create_protocol.isSelected() && p != null) {
            try {
                PrinterProtocolTestingOverviewPDF.set_path(Settings.getInstance().get_path_protocols());
                PrinterProtocolTestingPDF.print_pruefung(Settings.getInstance().get_path_protocols(), p, this.m_pruefer_list.get(this.sel_pruefer.getSelectedIndex()), this.m_current_item, this.m_current_vorschrift);
                PrinterProtocolTestingOverviewCSV.add_pruefung_event(Settings.getInstance().get_path_protocols(), p, this.m_pruefer_list.get(this.sel_pruefer.getSelectedIndex()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    private void _error_kennzeichen(String kennzeichen) {
        JOptionPane.showMessageDialog(this.root_panel,
                "Kein Gerät mit dem Kennzeichen " + kennzeichen + " gefunden!",
                "Fehlerhaftes Kennzeichen",
                JOptionPane.ERROR_MESSAGE);
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
    }

    private void _error_day() {
        JOptionPane.showMessageDialog(this.root_panel,
                "Keine korrekter Tag eingegeben!",
                "Fehler!",
                JOptionPane.ERROR_MESSAGE);
    }

    private void _error_month() {
        JOptionPane.showMessageDialog(this.root_panel,
                "Keine korrekter Monat eingegeben!",
                "Fehler!",
                JOptionPane.ERROR_MESSAGE);
    }

    private void _error_year() {
        JOptionPane.showMessageDialog(this.root_panel,
                "Keine korrektes Jahr eingegeben!",
                "Fehler!",
                JOptionPane.ERROR_MESSAGE);
    }

    private void _error_date() {
        JOptionPane.showMessageDialog(this.root_panel,
                "Keine korrektes Datum eingegeben!",
                "Fehler!",
                JOptionPane.ERROR_MESSAGE);
    }

    private  void _error_edit() {
        JOptionPane.showMessageDialog(this.root_panel,
                "Fehler beim Editieren des Eintrages!",
                "Fehler!",
                JOptionPane.ERROR_MESSAGE);
    }


    private final CtrlInventar m_ctrl_inventar;
    private final CtrlVorschrift m_ctrl_vorschrift;
    private final CtrlPruefungen m_ctrl_pruefungen;

    private Item m_current_item;
    private Vorschrift m_current_vorschrift;

    private List<Pruefer> m_pruefer_list;
}
