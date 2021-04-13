package thw_matp.ui;

import thw_matp.Main;
import thw_matp.ctrl.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;

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

        resourceBundle = ResourceBundle.getBundle("lang.ui.windowStartup", Locale.getDefault());
    }

    public void btn_select_path_database_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_database) {
            JFileChooser f = new JFileChooser();
            f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            f.setCurrentDirectory(Settings.getInstance().get_path_db().toFile());
            f.setDialogTitle(resourceBundle.getString("filechooser_db_title"));
            if (f.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                this.txt_database.setText(Settings.getInstance().get_path_db().toString());
            }
        }
        else {
            System.err.println(resourceBundle.getString("error_handle_fct"));
            new Throwable().printStackTrace();
        }
    }
    public void btn_select_path_protocols_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_protocols) {
            JFileChooser f = new JFileChooser();
            f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            f.setCurrentDirectory(Settings.getInstance().get_path_protocols().toFile());
            f.setDialogTitle(resourceBundle.getString("filechooser_protocols_title"));
            if (f.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                Settings.getInstance().set_path_protocols(java.nio.file.Paths.get(f.getSelectedFile().getAbsolutePath()));
                this.txt_protocols.setText(Settings.getInstance().get_path_protocols().toString());
            }
        }
        else {
            System.err.println(resourceBundle.getString("error_handle_fct"));
            new Throwable().printStackTrace();
        }
    }

    public void btn_ok_action_performed(ActionEvent e) {
        if (e.getSource() == this.btn_ok) {
            switch (this.tabbedPane1.getSelectedIndex()) {
                case 0 : //local
                    if (!this.txt_database.getText().isEmpty()) {
                        Settings.getInstance().db_is_local(true);
                        Path path = java.nio.file.Paths.get(this.txt_database.getText());
                        if (Files.notExists(path)) {
                            JOptionPane.showMessageDialog(this.root_panel,
                                    "Ordner '" + this.txt_database.getText() +  "' existiert nicht!",
                                    "Fehler!",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        Settings.getInstance().set_path_db(path);
                    }
                    else {
                        JOptionPane.showMessageDialog(this.root_panel,
                                "Pfad zur Datenbank fehlt!",
                                "Fehler!",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;
                case 1: //remote
                    if (!this.txt_ip.getText().isEmpty() && !this.txt_port.getText().isEmpty()) {
                        Settings.getInstance().db_is_local(false);
                        Settings.getInstance().set_remote(this.txt_ip.getText(), this.txt_port.getText());
                    }
                    else {
                        JOptionPane.showMessageDialog(this.root_panel,
                                "IP und/oder Port fehlen!",
                                "Fehler!",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;
                default:
                    System.err.print("Invalid tabbedPane1 value!");
                    return;
            }
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
    private JTabbedPane tabbedPane1;
    private JTextField txt_ip;
    private JTextField txt_port;
    private ResourceBundle resourceBundle;
}
