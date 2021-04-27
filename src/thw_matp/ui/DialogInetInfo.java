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

import thw_matp.ctrl.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Dialog listing the information about the network interface(s) of the current PC
 */
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
