package thw_matp.ui;

import thw_matp.ctrl.CtrlPruefer;
import thw_matp.datatypes.Pruefer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class WindowEditPruefer extends JFrame {
    private JPanel root_panel;
    private PanelSignature panel_signature;
    private JTextField txt_name;
    private JTextField txt_vorname;
    private JButton btn_cancel;
    private JButton btn_ok;

    public WindowEditPruefer(String title, CtrlPruefer ctrl_pruefer, Pruefer pruefer) {
        super(title);
        this.setContentPane(root_panel);
        this.m_ctrl_pruefer = ctrl_pruefer;
        this.m_pruefer = pruefer;

        this.txt_name.setText(pruefer.name);
        this.txt_vorname.setText(pruefer.vorname);
        this.panel_signature.set_signature(pruefer.unterschrift);

        this.btn_ok.addActionListener(this::btn_ok_action_performed);
        this.btn_cancel.addActionListener(this::btn_cancel_action_performed);
    }

    public JPanel get_root_panel() {
        return this.root_panel;
    }

    public void btn_ok_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_ok) {
            BufferedImage signature = this.panel_signature.get_signature();
            if(!this.m_ctrl_pruefer.update(this.m_pruefer.id, this.txt_name.getText(), this.txt_vorname.getText(), signature)) {
                JOptionPane.showMessageDialog(get_root_panel(),
                        "Fehler Editieren der Daten zum Prüfer " + this.txt_name.getText(),
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

    private CtrlPruefer m_ctrl_pruefer;
    private Pruefer m_pruefer;
}
