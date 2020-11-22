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

public class WindowEditVorschrift extends JFrame {
    private JPanel root_panel;
    private JTextField txt_sachnr;
    private JTextField txt_vorschrift;
    private JTextField txt_abschnitt;
    private JTextField txt_link;
    private JButton btn_cancel;
    private JButton btn_ok;

    public WindowEditVorschrift(CtrlVorschrift ctrl_vorschrift, Vorschrift vorschrift) {
        super("Vorschrift editieren");
        this.setContentPane(root_panel);
        this.m_ctrl_vorschrift = ctrl_vorschrift;

        this.txt_sachnr.setText(vorschrift.sachnr);
        this.txt_vorschrift.setText(vorschrift.vorschrift);
        this.txt_abschnitt.setText(vorschrift.abschnitt);
        this.txt_link.setText(vorschrift.link);

        this.btn_ok.addActionListener(this::btn_ok_action_performed);
        this.btn_cancel.addActionListener(this::btn_cancel_action_performed);
    }
    public JPanel get_root_panel() {
        return this.root_panel;
    }

    public void btn_ok_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_ok) {
            String sachnummer = this.txt_sachnr.getText();
            if (!this.m_ctrl_vorschrift.edit_vorschrift(this.txt_sachnr.getText(), this.txt_vorschrift.getText(), this.txt_abschnitt.getText(), this.txt_link.getText())) {
                JOptionPane.showMessageDialog(get_root_panel(),
                        "Bei editieren ist ein Fehler aufgetreten!",
                        "Fehler!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
        dispose();
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
