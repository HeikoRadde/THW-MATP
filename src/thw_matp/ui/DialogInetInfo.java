package thw_matp.ui;

import thw_matp.ctrl.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

public class DialogInetInfo extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextArea txt_network_info;
    private JTextPane description;

    public DialogInetInfo() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setTitle("Datenbank Verbindungsinformationen");
        this.description.setText("Nachfolgend sind die Netzwerkschnittstellen des PCs gelistet. Unter ihnen und dem Port " + Settings.getInstance().get_port() + " ist die von dieser Instanz geladenen Datenbank erreichbar.");
        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            this.txt_network_info.append("Networkinterfaces: \r\n");
            for (NetworkInterface netint : Collections.list(nets)) {
                this.txt_network_info.append("\r\n");
                this.txt_network_info.append("Display name: " + netint.getDisplayName() + "\r\n");
                this.txt_network_info.append("Name: " + netint.getName() + "\r\n");
                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    String addr = inetAddress.getHostAddress();
                    if (addr.indexOf('%') != -1) {
                        addr = addr.substring(0, addr.indexOf('%'));
                    }
                    this.txt_network_info.append("InetAddress: " + addr + "\r\n");
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    public static void main(String[] args) {
        DialogInetInfo dialog = new DialogInetInfo();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
