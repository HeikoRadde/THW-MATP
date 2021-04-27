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

import thw_matp.ctrl.CtrlPruefer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * Window for adding new Prüfer to the database
 */
public class WindowAddPruefer extends JFrame {
    private JPanel root_panel;
    private PanelSignature panel_signature;
    private JTextField txt_name;
    private JTextField txt_vorname;
    private JButton btn_cancel;
    private JButton btn_ok;
    private JButton btn_signature_clear;
    private JButton btn_signature_load;

    public WindowAddPruefer(String title, CtrlPruefer ctrl_pruefer) {
        super(title);
        this.setContentPane(root_panel);
        this.m_ctrl_pruefer = ctrl_pruefer;

        this.btn_ok.addActionListener(this::btn_ok_action_performed);
        this.btn_cancel.addActionListener(this::btn_cancel_action_performed);
        this.btn_signature_clear.addActionListener(this::btn_clear_signature_action_performed);
        this.btn_signature_load.addActionListener(this::btn_load_signature_action_performed);
    }

    public JPanel get_root_panel() {
        return this.root_panel;
    }

    public void btn_ok_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_ok) {
            BufferedImage signature = this.panel_signature.get_signature();
            if(signature == null) {
                if(!this.m_ctrl_pruefer.add_pruefer(this.txt_name.getText(), this.txt_vorname.getText())) {
                    JOptionPane.showMessageDialog(get_root_panel(),
                            "Fehler beim Anlegen vom Prüfer " + this.txt_name.getText(),
                            "Fehler!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            else {
                if(!this.m_ctrl_pruefer.add_pruefer(this.txt_name.getText(), this.txt_vorname.getText(), signature)) {
                    JOptionPane.showMessageDialog(get_root_panel(),
                            "Fehler beim Anlegen vom Prüfer " + this.txt_name.getText(),
                            "Fehler!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            dispose();
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

    public void btn_clear_signature_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_signature_clear) {
            this.panel_signature.clear_action();
        }
        else {
            this.panel_signature.clear_action();
            new Throwable().printStackTrace();
        }
    }

    public void btn_load_signature_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_signature_load) {
            this.panel_signature.load_action();
        }
        else {
            this.panel_signature.load_action();
            new Throwable().printStackTrace();
        }
    }

    private final CtrlPruefer m_ctrl_pruefer;
}
