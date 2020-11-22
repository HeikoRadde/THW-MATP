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
package thw_matp.ui;

import thw_matp.ctrl.CtrlVorschrift;
import thw_matp.datatypes.Vorschrift;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class WindowAddVorschrift extends JFrame {

    private JPanel root_panel;
    private JTextField txt_sachnr;
    private JTextField txt_vorschrift;
    private JTextField txt_abschnitt;
    private JTextField txt_link;
    private JButton btn_cancel;
    private JButton btn_ok;

    public WindowAddVorschrift(CtrlVorschrift ctrl_vorschrift) {
        super("Vorschrift hinzufügen");
        this.setContentPane(root_panel);
        this.m_ctrl_vorschrift = ctrl_vorschrift;

        this.btn_ok.addActionListener(this::btn_ok_action_performed);
        this.btn_cancel.addActionListener(this::btn_cancel_action_performed);
    }
    public JPanel get_root_panel() {
        return this.root_panel;
    }

    public void btn_ok_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_ok) {
            if (this.txt_sachnr.getText().isEmpty()) {
                JOptionPane.showMessageDialog(get_root_panel(),
                        "Mindestens die Sachnummer wird benötigt!",
                        "Fehler!",
                        JOptionPane.ERROR_MESSAGE);
            }
            else {
                String sachnummer = this.txt_sachnr.getText();
                int ret = this.m_ctrl_vorschrift.add_vorschrift(sachnummer, this.txt_vorschrift.getText(), this.txt_abschnitt.getText(), this.txt_link.getText());
                switch (ret) {
                    case 1: {
                        Object [] options = {"Ja", "Nein"};
                        int reply = JOptionPane.showOptionDialog(this,
                                "Die Sachnummer " + sachnummer + " ist existiert bereits!\nSoll die Sachnummer editiert werden?",
                                "Sachnummer bereits bekannt",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.ERROR_MESSAGE,
                                null,
                                options,
                                options[0]);
                        if (reply == JOptionPane.YES_OPTION) {
                            Vorschrift v = this.m_ctrl_vorschrift.get_vorschrift(sachnummer);
                            WindowEditVorschrift win = new WindowEditVorschrift(this.m_ctrl_vorschrift, v);
                            win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            win.pack();
                            win.setLocationRelativeTo(get_root_panel());
                            win.setVisible(true);
                        }
                    }
                }
                this.txt_sachnr.setText("");
                this.txt_vorschrift.setText("");
                this.txt_abschnitt.setText("");
                this.txt_link.setText("");
            }
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }


    public void btn_cancel_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_cancel) {
            dispose();
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }



    private CtrlVorschrift m_ctrl_vorschrift;
}
