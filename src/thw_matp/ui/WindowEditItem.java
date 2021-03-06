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

import thw_matp.ctrl.CtrlInventory;
import thw_matp.ctrl.CtrlSpecifications;
import thw_matp.datatypes.Item;
import thw_matp.util.FilterInteger;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.event.ActionEvent;

/**
 * Window for editing existing items in the database
 */
public class WindowEditItem extends JFrame {
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

    public WindowEditItem(String title, CtrlInventory ctrl_inventar, Item item, CtrlSpecifications ctrl_vorschriften) {
        super(title);
        this.setContentPane(root_panel);
        this.ctrl_inventar = ctrl_inventar;
        this.ctrl_vorschriften = ctrl_vorschriften;

        PlainDocument doc = (PlainDocument) this.txt_baujahr.getDocument();
        doc.setDocumentFilter(new FilterInteger());

        this.btn_ok.addActionListener(this::btn_ok_action_performed);
        this.btn_cancel.addActionListener(this::btn_cancel_action_performed);

        this.txt_kennzeichen.setText(item.kennzeichen);
        this.txt_bezeichnung.setText(item.bezeichnung);
        this.txt_hersteller.setText(item.hersteller);
        this.txt_baujahr.setText(Integer.toString(item.baujahr));
        this.txt_einheit.setText(item.einheit);
        this.txt_ov.setText(item.ov);
        this.txt_sachnr.setText(item.sachnr);
        this.txt_kennzeichen.setEnabled(false);
        this.pack();
    }

    public void btn_ok_action_performed(ActionEvent e)
    {
        if (e.getSource() == this.btn_ok) {
            String kennzeichen = this.txt_kennzeichen.getText();
            String sachnr = this.txt_sachnr.getText();
            String bezeichnung = this.txt_bezeichnung.getText();
            String hersteller = this.txt_hersteller.getText();
            int baujahr = Integer.parseInt(this.txt_baujahr.getText());
            String einheit = this.txt_einheit.getText();
            String ov = this.txt_ov.getText();
            try {
                this.ctrl_inventar.update(kennzeichen, ov, einheit, baujahr, hersteller, bezeichnung, sachnr);
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
                if (exception.getMessage().equals("Sachnr not known!")) {
                    Object [] options = {"Ja", "Nein"};
                    int reply = JOptionPane.showOptionDialog(this,
                            "Die Sachnummer " + sachnr + " ist unbekannt!\nSoll eine neue Vorschrift f??r die Sachnummer angelegt werden?",
                            "Unbekannte Sachnummer",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            options,
                            options[0]);
                    if (reply == JOptionPane.YES_OPTION) {
                        WindowAddSpecification win = new WindowAddSpecification(this.ctrl_vorschriften, sachnr);
                        win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        win.pack();
                        win.setLocationRelativeTo(root_panel);
                        win.setVisible(true);
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

    private final CtrlInventory ctrl_inventar;
    private final CtrlSpecifications ctrl_vorschriften;
}
