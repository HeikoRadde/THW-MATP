package thw_matp.ui;

import thw_matp.util.PrinterProtocolTestingOverviewPDF;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class SaveJFrame extends JFrame {

    public SaveJFrame () {
        super();
        this.addWindowListener(new WindowAdapter() {
            /**
             * Invoked when a window is in the process of being closed.
             * The close operation can be overridden at this point.
             *
             * @param e
             */
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    PrinterProtocolTestingOverviewPDF.create_pdf();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                super.windowClosing(e);
            }
        });
    }

}
