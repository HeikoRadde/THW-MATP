package thw_matp.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class PanelSignature extends JPanel implements MouseMotionListener {
    private JPanel root_panel;
    private JPanel draw_panel;
    private JButton btn_clear;
    private JButton btn_load;


    public PanelSignature() {
        super();
        this.m_signature = new BufferedImage(IMG_W, IMG_H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = this.m_signature.createGraphics();
        g2d.setBackground(Color.WHITE);
        g2d.setPaint(Color.BLACK);
        g2d.dispose();
        this.draw_panel.setBackground(Color.WHITE);

        this.btn_clear.addActionListener(this::btn_clear_action_performed);
        this.btn_load.addActionListener(this::btn_load_action_performed);
        this.draw_panel.addMouseMotionListener(this);
        this.init = false;
    }

    public JPanel get_root_panel() {
        return this.root_panel;
    }

    public BufferedImage get_signature() {
        return this.m_signature;
    }

    public void set_signature(BufferedImage signature) {
        if (signature != null) {
            final Graphics2D g2d = this.m_signature.createGraphics();
            g2d.setBackground(Color.WHITE);
            g2d.clearRect(0, 0, IMG_W, IMG_H);
            g2d.setPaint(Color.BLACK);
            g2d.drawImage(signature.getScaledInstance(IMG_W, IMG_H, Image.SCALE_SMOOTH), 0, 0, null);
            g2d.dispose();
        }
    }

    public void btn_clear_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_clear) {
            _clear_signature();
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    public void btn_load_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_load) {
            System.out.println("TODO: implement PanelSignature::btn_load_action_performed()");
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    public void mouseMoved(MouseEvent e) {
//        System.out.println("Mouse moved " + e.getX() + "|" + e.getY());
        if (!this.init) {
            Graphics g = this.draw_panel.getGraphics();
            if (g != null) {
                this.draw_panel.getGraphics().drawImage(this.m_signature, 0, 0, null);
                this.init = true;
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
//        System.out.println("Mouse dragged " + e.getX() + "|" + e.getY());
        int x = e.getX() - (BRUSH_SIZE/2);
        int y = e.getY() - (BRUSH_SIZE/2);
        final Graphics2D g2d = this.m_signature.createGraphics();
        g2d.setPaint(Color.BLACK);
        g2d.fillOval(x, y, BRUSH_SIZE, BRUSH_SIZE);
        g2d.dispose();
        this.draw_panel.getGraphics().drawImage(this.m_signature, 0, 0, null);
    }

    private void _clear_signature() {
        final Graphics2D g2d = this.m_signature.createGraphics();
        g2d.setBackground(Color.WHITE);
        g2d.clearRect(0, 0, IMG_W, IMG_H);
        g2d.setPaint(Color.BLACK);
        g2d.dispose();
        this.draw_panel.getGraphics().drawImage(this.m_signature, 0, 0, null);
    }

    private final int BRUSH_SIZE = 4;
    private final int IMG_W = 500;
    private final int IMG_H = 100;

    private BufferedImage m_signature;
    private boolean init;
}
