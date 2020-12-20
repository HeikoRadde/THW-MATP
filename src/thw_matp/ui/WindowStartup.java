package thw_matp.ui;

import thw_matp.Main;
import thw_matp.ctrl.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class WindowStartup extends JFrame {

    public WindowStartup() {
        super("Startup");
        this.setContentPane(root_panel);

        this.txt_database.setText(Settings.getInstance().get_path_db().toString());
        this.txt_protocols.setText(Settings.getInstance().get_path_protocols().toString());

        this.btn_database.addActionListener(this::btn_select_path_database_action_performed);
        this.btn_protocols.addActionListener(this::btn_select_path_protocols_action_performed);
        this.btn_cancel.addActionListener(this::btn_cancel_action_performed);
        this.btn_ok.addActionListener(this::btn_ok_action_performed);
    }

    public void btn_select_path_database_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_database) {
            JFileChooser f = new JFileChooser();
            f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            f.setCurrentDirectory(Settings.getInstance().get_path_db().toFile());
            f.setDialogTitle("Ordner für die Datenbank auswählen");
            if (f.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                Settings.getInstance().set_path_db(java.nio.file.Paths.get(f.getSelectedFile().getAbsolutePath()));
                this.txt_database.setText(Settings.getInstance().get_path_db().toString());
            }
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }
    public void btn_select_path_protocols_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_protocols) {
            JFileChooser f = new JFileChooser();
            f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            f.setCurrentDirectory(Settings.getInstance().get_path_protocols().toFile());
            f.setDialogTitle("Ordner für Protokolle auswählen");
            if (f.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                Settings.getInstance().set_path_protocols(java.nio.file.Paths.get(f.getSelectedFile().getAbsolutePath()));
                this.txt_protocols.setText(Settings.getInstance().get_path_protocols().toString());
            }
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    public void btn_ok_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_ok) {
            Settings.getInstance().startup_done(true);
            SwingUtilities.invokeLater(Main::create_gui);
            dispose();
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }

    public void btn_cancel_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_cancel) {
            System.exit(0);
        }
        else {
            System.err.println("Handle function called from wrong GUI object!");
            new Throwable().printStackTrace();
        }
    }


    private JTextPane description;
    private JLabel title;
    private JButton btn_database;
    private JTextField txt_database;
    private JButton btn_protocols;
    private JTextField txt_protocols;
    private JButton btn_ok;
    private JButton btn_cancel;
    private JPanel root_panel;
}
