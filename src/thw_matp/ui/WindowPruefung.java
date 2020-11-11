package thw_matp.ui;

import thw_matp.ctrl.CtrlInventar;
import thw_matp.ctrl.CtrlPruefer;
import thw_matp.ctrl.CtrlPruefungen;
import thw_matp.ctrl.CtrlVorschrift;
import thw_matp.datatypes.Item;
import thw_matp.datatypes.Pruefer;
import thw_matp.datatypes.Pruefung;
import thw_matp.datatypes.Vorschrift;
import thw_matp.util.PrinterProtocolTestingOverviewCSV;
import thw_matp.util.PrinterProtocolTesting;
import thw_matp.util.PrinterProtocolTestingOverviewPDF;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

public class WindowPruefung extends JFrame {

    private JButton btn_ok;
    private JButton btn_fail;
    private JPanel root_panel;
    private JTextField inp_kennzeichen;
    private JComboBox<String> sel_pruefer;
    private JButton btn_end;
    private JTextArea txt_bemerkungen;
    private JRadioButton rb_yes;
    private JTextField txt_oe;
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

    public WindowPruefung(String title, CtrlInventar ctrl_inventar, CtrlPruefer ctrl_pruefer, CtrlVorschrift ctrl_vorschrift, CtrlPruefungen ctrl_pruefungen) {
        super(title);
        this.setContentPane(root_panel);

        try {
            this.m_pruefer_list = ctrl_pruefer.get_all();
            for (Pruefer p : this.m_pruefer_list) {
                this.sel_pruefer.addItem(p.vorname + " " + p.name);
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }


        this.btn_ok.addActionListener(this::btn_ok_action_performed);
        this.btn_fail.addActionListener(this::btn_fail_action_performed);
        this.btn_end.addActionListener(this::btn_end_action_performed);
        this.inp_kennzeichen.addActionListener(this::inp_kennzeichen_action_performed);
        this.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                inp_kennzeichen.requestFocus();
            }
        });

        this.m_ctrl_inventar = ctrl_inventar;
        this.m_ctrl_vorschrift = ctrl_vorschrift;
        this.m_ctrl_pruefungen = ctrl_pruefungen;
    }


    public void btn_ok_action_performed(ActionEvent e) {
        if (e.getSource() == btn_ok) {
            System.out.println("OK");
            _enter_pruefung(true);
            _clear_fields();
            this.inp_kennzeichen.setText("");
            this.inp_kennzeichen.requestFocus();
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    public void btn_fail_action_performed(ActionEvent e) {
        if (e.getSource() == btn_fail) {
            System.out.println("FAIL");
            _enter_pruefung(false);
            _clear_fields();
            this.inp_kennzeichen.setText("");
            this.inp_kennzeichen.requestFocus();
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
            System.out.println(this.inp_kennzeichen.getText());
            _clear_fields();
            _fill_fields();
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    private void _fill_fields() {
        this.m_current_item = this.m_ctrl_inventar.get_item(this.inp_kennzeichen.getText());
        if(m_current_item == null) {
            _error_kennzeichen(this.inp_kennzeichen.getText());
            return;
        }
        this.m_current_vorschrift = this.m_ctrl_vorschrift.get_vorschrift(this.m_current_item.sachnr);
        if(this.m_current_vorschrift == null) {
            _error_sachnummer(this.m_current_item.sachnr);
            return;
        }

        this.txt_oe.setText(this.m_current_item.ov);
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

    private void _clear_fields() {
        this.txt_oe.setText("");
        this.txt_einheit.setText("");
        this.txt_bezeichung.setText("");
        this.txt_hersteller.setText("");
        this.txt_sachnummer.setText("");
        this.txt_baujahr.setText("");
        this.txt_pruefungsvorschrift.setText("");
        this.txt_abschnitt.setText("");
        this.txt_link.setText("");

        this.m_current_item = null;
        this.m_current_vorschrift = null;
    }

    private void _enter_pruefung(boolean bestanden) {
        Path path;
        if (this.check_create_protocol.isSelected()) {
            if (txt_save_path.getText().isEmpty())
            {
                JFileChooser f = new JFileChooser();
                f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                f.setCurrentDirectory(new java.io.File("."));
                f.setDialogTitle("Ordner auswählen");
                f.showSaveDialog(this);
                path = java.nio.file.Paths.get(f.getSelectedFile().getAbsolutePath());
                this.txt_save_path.setText(path.toString());
            }
        }
        boolean ausgesondert = false;
        if (rb_yes.isSelected()) ausgesondert = true;
        this.m_current_item = this.m_ctrl_inventar.get_item(this.inp_kennzeichen.getText());
        if(m_current_item == null) {
            _error_kennzeichen(this.inp_kennzeichen.getText());
            return;
        }
        this.m_current_vorschrift = this.m_ctrl_vorschrift.get_vorschrift(this.m_current_item.sachnr);
        if(this.m_current_vorschrift == null) {
            _error_sachnummer(this.m_current_item.sachnr);
            return;
        }
        Pruefung p = this.m_ctrl_pruefungen.add_pruefung(this.inp_kennzeichen.getText(), this.m_pruefer_list.get(this.sel_pruefer.getSelectedIndex()).id, bestanden, this.txt_bemerkungen.getText(), ausgesondert);
        if (this.check_create_protocol.isSelected()) {
            try {
                path = Paths.get(this.txt_save_path.getText());
                PrinterProtocolTesting.print_pruefung(path, p, this.m_pruefer_list.get(this.sel_pruefer.getSelectedIndex()), this.m_current_item, this.m_current_vorschrift);
                PrinterProtocolTestingOverviewCSV.add_pruefung_event(path, p, this.m_pruefer_list.get(this.sel_pruefer.getSelectedIndex()));
                PrinterProtocolTestingOverviewPDF.set_path(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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


    private final CtrlInventar m_ctrl_inventar;
    private final CtrlVorschrift m_ctrl_vorschrift;
    private final CtrlPruefungen m_ctrl_pruefungen;

    private Item m_current_item;
    private Vorschrift m_current_vorschrift;

    private List<Pruefer> m_pruefer_list;
}
