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

import thw_matp.Main;
import thw_matp.ctrl.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Window shown at startup, for selecting location of database, where to save the protocols and giving
 * a quick introduction to the program
 */
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
                this.txt_database.setText(f.getSelectedFile().getAbsolutePath());
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
                this.txt_protocols.setText(f.getSelectedFile().getAbsolutePath());
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
                        Path path_db   = java.nio.file.Paths.get(this.txt_database.getText());
                        Path path_logs = java.nio.file.Paths.get(this.txt_protocols.getText());
                        if (Files.notExists(path_db)) {
                            JOptionPane.showMessageDialog(this.root_panel,
                                    "Der Ordner für die Datenbank '" + this.txt_database.getText() +  "' existiert nicht!",
                                    "Fehler!",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (Files.notExists(path_logs)) {
                            JOptionPane.showMessageDialog(this.root_panel,
                                    "Ordner für die Protokolle '" + this.txt_protocols.getText() +  "' existiert nicht!",
                                    "Fehler!",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        Settings.getInstance().set_path_db(path_db);
                        Settings.getInstance().set_path_protocols(path_logs);
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
