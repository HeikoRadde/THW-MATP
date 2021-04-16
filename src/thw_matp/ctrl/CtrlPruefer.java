/*
    Copyright (c) 2020 Heiko Radde
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
package thw_matp.ctrl;

import thw_matp.datatypes.Pruefer;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class CtrlPruefer {


    public CtrlPruefer(Database db) {
        this.db = db;
    }

    public DefaultTableModel get_data() {
        DefaultTableModel mdl = new DefaultTableModel();
        mdl.setColumnIdentifiers(new String[]{"Name", "Vorname", "ID"});
        List<Pruefer> pruefer = null;
        try {
            pruefer = this.db.pruefer_get_all();
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        if(pruefer != null) {
            for (Pruefer p : pruefer) {
                mdl.addRow(new Object[]{p.name, p.vorname, p.id});
            }
        }
        else {
            System.err.println("No entry in table pruefer!");
        }
        return mdl;
    }

    public List<Pruefer> get_all() throws SQLException, IOException {
        return this.db.pruefer_get_all();
    }

    public Pruefer find(UUID id) {
        try {
            return this.db.pruefer_get(id);
        } catch (SQLException | IOException throwables) {
            System.err.println("Failed to find pruefer " + id.toString() + " in table pruefer!");
            throwables.printStackTrace();
            return null;
        }
    }

    public boolean add_signature(UUID id, BufferedImage signature) {
        try {
            this.db.pruefer_add_signature(signature, id);
        }
        catch (SQLException | IOException e) {
            System.err.println("Failed to add signature to pruefer " + id.toString() + " in table pruefer!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean update(UUID id, String name, String vorname, BufferedImage signature) {
        try {
            this.db.pruefer_update(id, name, vorname, signature);
        } catch (SQLException | IOException throwables) {
            System.err.println("Failed to update data of pruefer " + id.toString() + " in table pruefer!");
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public Image get_signature(UUID id) {
        Image signature = null;
        try {
            signature = this.db.pruefer_get_signature(id);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        return signature;
    }

    public boolean add_pruefer(String name, String vorname, BufferedImage signature) {
        try {
            UUID id = UUID.randomUUID();
            this.db.pruefer_add(id, name, vorname);
            this.db.pruefer_add_signature(signature, id);
        } catch (SQLException | IOException throwables) {
            System.err.println("Failed to update data of pruefer " + vorname + " " + name + " in table pruefer!");
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean add_pruefer(String name, String vorname) {
        try {
            UUID id = UUID.randomUUID();
            this.db.pruefer_add(id, name, vorname);
        } catch (SQLException throwables) {
            System.err.println("Failed to update data of pruefer " + vorname + " " + name + " in table pruefer!");
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean remove_pruefer(UUID id) {
        try {
            this.db.pruefer_remove(id);
        } catch (SQLException throwables) {
            System.err.println("Failed to remove entry " + id.toString() + " in table pruefer!");
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    private final Database db;
}
