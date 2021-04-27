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
package thw_matp.ctrl;

import thw_matp.datatypes.Inspector;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Controller for actions related to inspectors
 */
public class CtrlInspectors {


    /**
     * @param db Database where the inspectors are saved
     */
    public CtrlInspectors(Database db) {
        this.db = db;
    }

    /**
     * @return {@link javax.swing.table.DefaultTableModel} with all inspectors in the database, ready for display
     */
    public DefaultTableModel get_data() {
        DefaultTableModel mdl = new DefaultTableModel();
        mdl.setColumnIdentifiers(new String[]{"Name", "Vorname", "ID"});
        List<Inspector> inspector = null;
        try {
            inspector = this.db.inspector_get_all();
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        if(inspector != null) {
            for (Inspector p : inspector) {
                mdl.addRow(new Object[]{p.name, p.vorname, p.id});
            }
        }
        else {
            System.err.println("No entry in table inspectors!");
        }
        return mdl;
    }

    /**
     * @return              Get all inspectors in the database
     * @throws SQLException On SQL query errors
     * @throws IOException  On SQL database access errors
     */
    public List<Inspector> get_all() throws SQLException, IOException {
        return this.db.inspector_get_all();
    }

    /**
     *              Find a specific inspectors in the database
     * @param id    Unique ID of the inspectors to find
     * @return      {@link Inspector} identified by `id` or null if none was found
     */
    public Inspector find(UUID id) {
        try {
            return this.db.inspector_get(id);
        } catch (SQLException | IOException throwables) {
            System.err.println("Failed to find pruefer " + id.toString() + " in table pruefer!");
            throwables.printStackTrace();
            return null;
        }
    }

    /**
     *                  Add the signature to a specific inspectors
     * @param id        Unique ID of the inspectors
     * @param signature Signature to add
     * @return          True on success, false on failure
     */
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

    /**
     *                  Update a specific inspectors data
     * @param id        Unique ID of the inspectors
     * @param name      Name of the inspectors
     * @param vorname   First name of the inspectors
     * @param signature Signature of the inspectors
     * @return          True on success, false on failure
     */
    public boolean update(UUID id, String name, String vorname, BufferedImage signature) {
        try {
            this.db.inspector_update(id, name, vorname, signature);
        } catch (SQLException | IOException throwables) {
            System.err.println("Failed to update data of pruefer " + id.toString() + " in table pruefer!");
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *              Retrieve the signature of a specific inspectors
     * @param id    Unique ID of the inspectors
     * @return      {@link java.awt.Image} if found, null if not found
     */
    public Image get_signature(UUID id) {
        Image signature = null;
        try {
            signature = this.db.inspector_get_signature(id);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        return signature;
    }

    /**
     *                  Add a new inspectors with a signature to the database. A unique ID will be picked at random
     * @param name      Name of the inspectors
     * @param vorname   First name of the inspectors
     * @param signature Signature of the inspectors
     * @return          True on success, false on failure
     */
    public boolean add_inspector(String name, String vorname, BufferedImage signature) {
        try {
            UUID id = UUID.randomUUID();
            this.db.inspector_add(id, name, vorname);
            this.db.pruefer_add_signature(signature, id);
        } catch (SQLException | IOException throwables) {
            System.err.println("Failed to update data of pruefer " + vorname + " " + name + " in table pruefer!");
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *                  Add a new inspectors without a signature to the database. A unique ID will be picked at random
     * @param name      Name of the inspectors
     * @param vorname   First name of the inspectors
     * @return          True on success, false on failure
     */
    public boolean add_inspector(String name, String vorname) {
        try {
            UUID id = UUID.randomUUID();
            this.db.inspector_add(id, name, vorname);
        } catch (SQLException throwables) {
            System.err.println("Failed to update data of pruefer " + vorname + " " + name + " in table pruefer!");
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *              Remove a specific inspectors
     * @param id    Unique ID of the inspectors to remove
     * @return      True on success, false on failure
     */
    public boolean remove_inspector(UUID id) {
        try {
            this.db.inspector_remove(id);
        } catch (SQLException throwables) {
            System.err.println("Failed to remove entry " + id.toString() + " in table pruefer!");
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    private final Database db;
}
