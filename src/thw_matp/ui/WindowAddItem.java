package thw_matp.ui;

import thw_matp.ctrl.CtrlInventar;
import thw_matp.util.FilterInteger;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.event.ActionEvent;

public class WindowAddItem extends JFrame {
    private JPanel root_panel;
    private JTextField txt_kennzeichen;
    private JTextField txt_bezeichnung;
    private JTextField txt_hersteller;
    private JTextField txt_baujahr;
    private JTextField txt_einheit;
    private JTextField txt_ov;
    private JButton btn_ok;
    private JButton btn_cancel;
    private JTextField txt_sachnr;

    public WindowAddItem(String title, CtrlInventar ctrl_inventar) {
        super(title);
        this.setContentPane(root_panel);
        this.ctrl_inventar = ctrl_inventar;

        PlainDocument doc = (PlainDocument) this.txt_baujahr.getDocument();
        doc.setDocumentFilter(new FilterInteger());

        this.btn_ok.addActionListener(this::btn_ok_action_performed);
        this.btn_cancel.addActionListener(this::btn_cancel_action_performed);
    }

    public void btn_ok_action_performed(ActionEvent e)
    {
        if (e.getSource() == this.btn_ok) {
            String kennzeichen = this.txt_kennzeichen.getText();
            String sachnr = (String) this.txt_sachnr.getText();
            String bezeichnung = this.txt_bezeichnung.getText();
            String hersteller = this.txt_hersteller.getText();
            int baujahr = Integer.parseInt(this.txt_baujahr.getText());
            String einheit = this.txt_einheit.getText();
            String ov = this.txt_ov.getText();
            try {
                this.ctrl_inventar.add_item(kennzeichen, ov, einheit, baujahr, hersteller, bezeichnung, sachnr);
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
                if (exception.getMessage().equals("Sachnr not known!")) {
                    Object [] options = {"Ja", "Nein"};
                    int reply = JOptionPane.showOptionDialog(this,
                            "Die Sachnummer " + sachnr + " ist unbekannt!\nSoll eine neue Vorschrift für die Sachnummer angelegt werden?",
                            "Unbekannte Sachnummer",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            options,
                            options[0]);
                    if (reply == JOptionPane.YES_OPTION) {
                        //TODO: Create new Sachnummer!
                    }
                }
                else {
                    exception.printStackTrace();
                }
            }
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    public void btn_cancel_action_performed(ActionEvent e)
    {
        if (e.getSource() == this.btn_cancel) {
            dispose();
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    private final CtrlInventar ctrl_inventar;
}
