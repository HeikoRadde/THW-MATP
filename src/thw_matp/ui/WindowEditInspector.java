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

import thw_matp.ctrl.CtrlInspectors;
import thw_matp.datatypes.Inspector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * Window for editing existing inspector in the database
 */
public class WindowEditInspector extends JFrame {
    private JPanel root_panel;
    private PanelSignature panel_signature;
    private JTextField txt_name;
    private JTextField txt_vorname;
    private JButton btn_cancel;
    private JButton btn_ok;
    private JButton btn_signature_clear;
    private JButton btn_signature_load;

    public WindowEditInspector(String title, CtrlInspectors ctrl_pruefer, Inspector inspector) {
        super(title);
        this.setContentPane(root_panel);
        this.m_ctrl_pruefer = ctrl_pruefer;
        this.m_inspector = inspector;

        this.btn_ok.addActionListener(this::btn_ok_action_performed);
        this.btn_cancel.addActionListener(this::btn_cancel_action_performed);
        this.btn_signature_clear.addActionListener(this::btn_clear_signature_action_performed);
        this.btn_signature_load.addActionListener(this::btn_load_signature_action_performed);

        // Here, we can safely update the GUI
        // because we'll be called from the
        // event dispatch thread
        EventQueue.invokeLater(this::update_fields);
    }

    public JPanel get_root_panel() {
        return this.root_panel;
    }

    public void update_fields() {
        this.txt_name.setText(this.m_inspector.name);
        this.txt_vorname.setText(this.m_inspector.vorname);
        this.panel_signature.set_signature(this.m_inspector.unterschrift);
        this.pack();
        this.revalidate();
    }

    public void btn_ok_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_ok) {
            BufferedImage signature = this.panel_signature.get_signature();
            {
                JDialog dialog = new JDialog();
                dialog.setUndecorated(true);
            }
            if(!this.m_ctrl_pruefer.update(this.m_inspector.id, this.txt_name.getText(), this.txt_vorname.getText(), signature)) {
                JOptionPane.showMessageDialog(get_root_panel(),
                        "Fehler beim Editieren der Daten zum Pr??fer " + this.txt_name.getText(),
                        "Fehler!",
                        JOptionPane.ERROR_MESSAGE);
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

    private final CtrlInspectors m_ctrl_pruefer;
    private final Inspector m_inspector;
}
