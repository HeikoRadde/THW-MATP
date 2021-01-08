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

import thw_matp.datatypes.Item;
import thw_matp.datatypes.Pruefer;
import thw_matp.datatypes.Pruefung;
import thw_matp.datatypes.Vorschrift;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Database {

    public Database(String db_name) throws SQLException {
        String db_absolute_path = get_db_full_path(db_name);

//        this.m_connection = DriverManager.getConnection("jdbc:h2:mem:db1");
        this.m_connection = DriverManager.getConnection("jdbc:h2:file:" + db_absolute_path + ";AUTO_SERVER=TRUE", "sa", "sa");

        db_start();
    }

    public Database(String ip, String port, String db_name) throws SQLException {
        System.out.println("URL: " + "jdbc:h2:tcp://" + ip + ":" + port + "/" + db_name);
        this.m_connection = DriverManager.getConnection("jdbc:h2:tcp://" + ip + ":" + port + "/" + db_name, "sa", "sa");
        db_start();
    }

    protected Database() {
        ;
    }

    protected String get_db_full_path(String db_name) {
        return Paths.get(Settings.getInstance().get_path_db().toAbsolutePath().toString(), db_name).toString();
    }

    protected void connect(String url, String db_name) throws SQLException {
        System.out.println("DB-Connection: " + "jdbc:h2:" + url + "/" + db_name);
        this.m_connection = DriverManager.getConnection("jdbc:h2:" + url + "/" + db_name, "sa", "sa");
        db_start();
    }

    private void db_start() {
        try {
            this.m_connection.createStatement().executeUpdate(CREATE_TABLE_VORSCHRIFTEN_SQL);
            this.m_connection.createStatement().executeUpdate(CREATE_TABLE_INVENTAR_SQL);
            this.m_connection.createStatement().executeUpdate(CREATE_TABLE_PRUEFER_SQL);
            this.m_connection.createStatement().executeUpdate(CREATE_TABLE_PRUEFUNGEN_SQL);
        } catch (SQLException throwables) {
            System.err.println("New database - error during table creation!");
        }
    }



    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------



    public void pruefer_add(String name, String vorname) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("INSERT INTO pruefer (id, Name, Vorname) VALUES(?, ?, ?)");
        this.m_connection.setAutoCommit(false);
        pstmt.setString(1, UUID.randomUUID().toString());
        pstmt.setString(2, name);
        pstmt.setString(3, vorname);
        pstmt.executeUpdate();
        this.m_connection.commit();
        this.m_connection.setAutoCommit(true);
    }

    public void pruefer_add(UUID id, String name, String vorname) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("INSERT INTO pruefer (id, Name, Vorname) VALUES(?, ?, ?)");
        this.m_connection.setAutoCommit(false);
        pstmt.setString(1, id.toString());
        pstmt.setString(2, name);
        pstmt.setString(3, vorname);
        pstmt.executeUpdate();
        this.m_connection.commit();
        this.m_connection.setAutoCommit(true);
    }

    public Pruefer pruefer_get(UUID id) throws SQLException, IOException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("SELECT * FROM pruefer WHERE id = ?");
        pstmt.setString(1, id.toString());
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        if (rs.getObject(4) == null) return new Pruefer(UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3));
        else {
            InputStream is = rs.getBinaryStream(4);
            BufferedImage bsignature = ImageIO.read(is);
            return new Pruefer(UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3), bsignature);
        }
    }

    public List<Pruefer> pruefer_get_all() throws SQLException, IOException {
        ResultSet rs = this.m_connection.createStatement().executeQuery("SELECT * FROM pruefer");
        List<Pruefer> list = new ArrayList<>();
        while(rs.next()) {
            if (rs.getObject(4) == null) list.add(new Pruefer(UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3)));
            else {
                InputStream is = rs.getBinaryStream(4);
                BufferedImage bsignature = ImageIO.read(is);
                list.add(new Pruefer(UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3), bsignature));
            }
        }
        return list;
    }

    public void pruefer_print_all() throws SQLException {
        ResultSet rs = this.m_connection.createStatement().executeQuery("SELECT * FROM pruefer");
        System.out.println("UUID | Name | Vorname");
        while (rs.next()) {
            System.out.println(rs.getString(1) + " | " + rs.getString(2) + " | " + rs.getString(3));
        }
    }

    public void pruefer_print(UUID id) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("SELECT * FROM pruefer WHERE id = ?");
        pstmt.setString(1, id.toString());
        ResultSet rs = pstmt.executeQuery();
        System.out.println("UUID | Name | Vorname");
        while (rs.next()) {
            System.out.println(rs.getString(1) + " | " + rs.getString(2) + " | " + rs.getString(3));
        }
    }

    public UUID pruefer_find(String name) throws SQLException {
        ResultSet rs = this.m_connection.createStatement().executeQuery("SELECT * FROM pruefer WHERE Name = '" + name + "'");
        rs.next();
        return UUID.fromString(rs.getString(1));
    }

    public void pruefer_add_signature(BufferedImage signature, UUID id) throws SQLException, IOException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("UPDATE pruefer SET Unterschrift = ? WHERE id = ?");
        File signature_tmp = File.createTempFile("tmp", "signature");
        signature.createGraphics().drawImage(signature, null, null);
        ImageIO.write(signature, "png", signature_tmp);
        InputStream is = new FileInputStream(signature_tmp);
        pstmt.setBinaryStream(1, is);
        pstmt.setString(2, id.toString());
        pstmt.executeUpdate();
        signature_tmp.delete();
    }

    public Image pruefer_get_signature(UUID id) throws SQLException, IOException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("SELECT Unterschrift from pruefer WHERE id = ?");
        pstmt.setString(1, id.toString());
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        InputStream is = rs.getBinaryStream(1);
        return ImageIO.read(is);
    }

    public void pruefer_update(UUID id, String name, String vorname, BufferedImage signature) throws SQLException, IOException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("UPDATE pruefer SET Name = ?, Vorname = ?, Unterschrift = ? WHERE id = ?");
        pstmt.setString(1, name);
        pstmt.setString(2, vorname);
        File signature_tmp = File.createTempFile("tmp", "signature");
        signature.createGraphics().drawImage(signature, null, null);
        ImageIO.write(signature, "png", signature_tmp);
        InputStream is = new FileInputStream(signature_tmp);
        pstmt.setBinaryStream(3, is);
        pstmt.setString(4, id.toString());
        pstmt.executeUpdate();
        signature_tmp.delete();
    }

    public void pruefer_remove(UUID id) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("DELETE FROM pruefer WHERE id = ?");
        pstmt.setString(1, id.toString());
        pstmt.executeUpdate();
    }



    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------



    public void inventar_add(String kennzeichen, String ov, String einheit, int baujahr, String hersteller, String bezeichnung, String sachnr) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("INSERT INTO inventar (Kennzeichen, OV, Einheit, Baujahr, Hersteller, Bezeichnung, Sachnr) VALUES (?, ?, ?, ?, ?, ?, ?)");
        pstmt.setString(1, kennzeichen);
        pstmt.setString(2, ov);
        pstmt.setString(3, einheit);
        pstmt.setInt(4, baujahr);
        pstmt.setString(5, hersteller);
        pstmt.setString(6, bezeichnung);
        pstmt.setString(7, sachnr);
        pstmt.executeUpdate();
    }

    public void inventar_update(String kennzeichen, String ov, String einheit, int baujahr, String hersteller, String bezeichnung, String sachnr) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("UPDATE inventar SET OV = ?, Einheit = ?, Baujahr = ?, Hersteller = ?, Bezeichnung = ?, Sachnr = ? WHERE Kennzeichen = ?");
        pstmt.setString(1, ov);
        pstmt.setString(2, einheit);
        pstmt.setInt(3, baujahr);
        pstmt.setString(4, hersteller);
        pstmt.setString(5, bezeichnung);
        pstmt.setString(6, sachnr);
        pstmt.setString(7, kennzeichen);
        pstmt.executeUpdate();
    }

    public void inventar_print_all() throws SQLException {
        ResultSet rs = this.m_connection.createStatement().executeQuery("SELECT * FROM inventar");
        System.out.println("Kennzeichen | OV | Einheit | Baujahr | Hersteller | Bezeichung | Sachnr");
        while (rs.next()) {
            System.out.println(rs.getString(1)
                    + " | " + rs.getString(2)
                    + " | " + rs.getString(3)
                    + " | " + rs.getString(4)
                    + " | " + rs.getString(5)
                    + " | " + rs.getString(6)
                    + " | " + rs.getString(7));
        }
    }

    public Item inventar_get(String kennzeichen) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("SELECT * FROM inventar WHERE kennzeichen = ?");
        pstmt.setString(1, kennzeichen);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return new Item(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getString(7));
    }

    public List<Item> inventar_get_by_sachnr(String sachnr) throws SQLException {
        List<Item> list = new ArrayList<>();
        PreparedStatement pstmt = this.m_connection.prepareStatement("SELECT * FROM inventar WHERE Sachnr = ?");
        pstmt.setString(1, sachnr);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            list.add(new Item(rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getInt(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7)));
        }
        return list;
    }

    public List<Item> inventar_get_all() throws SQLException {
        ResultSet rs = this.m_connection.createStatement().executeQuery("SELECT * FROM inventar");
        List<Item> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Item(rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getInt(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7)));
        }
        return list;
    }

    public void inventar_remove(String kennzeichen) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("DELETE FROM inventar WHERE Kennzeichen = ?");
        pstmt.setString(1, kennzeichen);
        pstmt.executeUpdate();
    }



    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------



    public Pruefung puefung_add_event(String kennzeichen, UUID pruefer, boolean bestanden, String bemerkungen, boolean ausgesondert, String ov) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("INSERT INTO pruefungen (id, Kennzeichen, Datum, Bestanden, Pruefer, Bemerkungen, Ausgesondert, OV) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        Pruefung p = new Pruefung(UUID.randomUUID(), kennzeichen, LocalDate.now(), bestanden, pruefer, bemerkungen, ausgesondert, ov);
        pstmt.setString(1, p.id.toString());
        pstmt.setString(2, p.kennzeichen);
        pstmt.setObject(3, p.datum);
        pstmt.setBoolean(4, p.bestanden);
        pstmt.setString(5, p.pruefer.toString());
        pstmt.setString(6, p.bemerkungen);
        pstmt.setBoolean(7, p.ausgesondert);
        pstmt.setString(8, p.ov);
        pstmt.executeUpdate();
        return p;
    }

    public void puefung_add_event(String kennzeichen, boolean bestanden) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("INSERT INTO pruefungen (id, kennzeichen, datum, bestanden, ausgesondert) VALUES (?, ?, ?, ?, ?, ?)");
        pstmt.setString(1, UUID.randomUUID().toString());
        pstmt.setString(2, kennzeichen);
        pstmt.setObject(3, LocalDate.now());
        pstmt.setBoolean(4, bestanden);
        pstmt.setBoolean(5, false);
        pstmt.executeUpdate();
    }

    public void puefung_add_event(String kennzeichen, LocalDate datum, boolean bestanden) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("INSERT INTO pruefungen (id, kennzeichen, datum, bestanden, ausgesondert) VALUES (?, ?, ?, ?, ?)");
        pstmt.setString(1, UUID.randomUUID().toString());
        pstmt.setString(2, kennzeichen);
        pstmt.setObject(3, datum);
        pstmt.setBoolean(4, bestanden);
        pstmt.setBoolean(5, false);
        pstmt.executeUpdate();
    }

    public List<Pruefung> pruefungen_get_all() throws SQLException {
        ResultSet rs = this.m_connection.createStatement().executeQuery("SELECT * FROM pruefungen");
        List<Pruefung> list = new ArrayList<>();
        while (rs.next()) {
            Pruefung p;
            String pruefer = rs.getString(5);
            String bemerkungen = rs.getString(6);
            boolean ausgesondert = rs.getBoolean(7);
            String ov = rs.getString(8);
            if(bemerkungen == null) bemerkungen = "";
            if(ov == null) ov = "";
            if(pruefer == null) { //Pruefer might be null
                p = new Pruefung(UUID.fromString(rs.getString(1)),
                        rs.getString(2),
                        rs.getObject(3, LocalDate.class),
                        rs.getBoolean(4),
                        bemerkungen,
                        ausgesondert,
                        ov);
            }
            else {
                p = new Pruefung(UUID.fromString(rs.getString(1)),
                        rs.getString(2),
                        rs.getObject(3, LocalDate.class),
                        rs.getBoolean(4),
                        UUID.fromString(rs.getString(5)),
                        bemerkungen,
                        ausgesondert,
                        ov);
            }
            list.add(p);
        }
        return list;
    }

    public List<Pruefung> pruefungen_get(String kennzeichen) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("SELECT * FROM pruefungen WHERE Kennzeichen = ?");
        pstmt.setString(1, kennzeichen);
        ResultSet rs = pstmt.executeQuery();
        List<Pruefung> list = new ArrayList<>();
        while (rs.next()) {
            Pruefung p;
            String pruefer = rs.getString(5);
            String bemerkungen = rs.getString(6);
            boolean ausgesondert = rs.getBoolean(7);
            String ov = rs.getString(8);
            if(bemerkungen == null) bemerkungen = "";
            if(ov == null) ov = "";
            if(pruefer == null) { //Pruefer might be null
                p = new Pruefung(UUID.fromString(rs.getString(1)),
                        rs.getString(2),
                        rs.getObject(3, LocalDate.class),
                        rs.getBoolean(4),
                        bemerkungen,
                        ausgesondert,
                        ov);
            }
            else {
                p = new Pruefung(UUID.fromString(rs.getString(1)),
                        rs.getString(2),
                        rs.getObject(3, LocalDate.class),
                        rs.getBoolean(4),
                        UUID.fromString(rs.getString(5)),
                        bemerkungen,
                        ausgesondert,
                        ov);
            }
            list.add(p);
        }
        return list;
    }

    public Pruefung pruefungen_get_last(String kennzeichen) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement(
                "SELECT * FROM pruefungen WHERE Kennzeichen = ? ORDER BY Datum DESC NULLS LAST");
        pstmt.setString(1, kennzeichen);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        String pruefer = rs.getString(5);
        String bemerkungen = rs.getString(6);
        boolean ausgesondert = rs.getBoolean(7);
        String ov = rs.getString(8);
        if(bemerkungen == null) bemerkungen = "";
        if(ov == null) ov = "";
        if(pruefer == null) { //Pruefer might be null
            return new Pruefung(UUID.fromString(rs.getString(1)),
                    rs.getString(2),
                    rs.getObject(3, LocalDate.class),
                    rs.getBoolean(4),
                    bemerkungen,
                    ausgesondert,
                    ov);
        }
        else {
            return new Pruefung(UUID.fromString(rs.getString(1)),
                    rs.getString(2),
                    rs.getObject(3, LocalDate.class),
                    rs.getBoolean(4),
                    UUID.fromString(rs.getString(5)),
                    bemerkungen,
                    ausgesondert,
                    ov);
        }
    }

    public void pruefungen_remove(UUID id) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("DELETE FROM pruefungen WHERE id = ?");
        pstmt.setString(1, id.toString());
        pstmt.executeUpdate();
    }

    public void pruefungen_remove(String kennzeichen) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("DELETE FROM pruefungen WHERE Kennzeichen = ?");
        pstmt.setString(1, kennzeichen);
        pstmt.executeUpdate();
    }

    public Pruefung pruefungen_find(UUID id) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("SELECT * FROM pruefungen WHERE id = ?");
        pstmt.setString(1, id.toString());
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        String pruefer = rs.getString(5);
        String bemerkungen = rs.getString(6);
        boolean ausgesondert = rs.getBoolean(7);
        String ov = rs.getString(8);
        if(bemerkungen == null) bemerkungen = "";
        if(ov == null) ov = "";
        if(pruefer == null) { //Pruefer might be null
            return new Pruefung(UUID.fromString(rs.getString(1)),
                    rs.getString(2),
                    rs.getObject(3, LocalDate.class),
                    rs.getBoolean(4),
                    bemerkungen,
                    ausgesondert,
                    ov);
        }
        else {
            return new Pruefung(UUID.fromString(rs.getString(1)),
                    rs.getString(2),
                    rs.getObject(3, LocalDate.class),
                    rs.getBoolean(4),
                    UUID.fromString(rs.getString(5)),
                    bemerkungen,
                    ausgesondert,
                    ov);
        }
    }

    public void puefung_update_event(UUID id, String kennzeichen, LocalDate datum, UUID pruefer, boolean bestanden, String bemerkungen, boolean ausgesondert) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("UPDATE pruefungen SET Kennzeichen = ?, Datum = ?, Bestanden = ?, Pruefer = ?, Bemerkungen = ?, Ausgesondert = ? WHERE id = ?");
        pstmt.setString(1, kennzeichen);
        pstmt.setObject(2, datum);
        pstmt.setBoolean(3, bestanden);
        pstmt.setString(4, pruefer.toString());
        pstmt.setString(5, bemerkungen);
        pstmt.setBoolean(6, ausgesondert);
        pstmt.setString(7, id.toString());
        pstmt.executeUpdate();
    }



    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------



    public void vorschriften_add(String sachnr, String link, String vorschrift, String abschnitt) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("INSERT INTO vorschriften (Sachnr, Vorschrift, Abschnitt, Link) VALUES (?, ?, ?, ?)");
        pstmt.setString(1, sachnr);
        pstmt.setString(2, vorschrift);
        pstmt.setString(3, abschnitt);
        pstmt.setString(4, link);
        pstmt.executeUpdate();
    }

    public void vorschriften_add(String sachnr, String vorschrift, String abschnitt) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("INSERT INTO vorschriften (Sachnr, Vorschrift, Abschnitt) VALUES (?, ?, ?)");
        pstmt.setString(1, sachnr);
        pstmt.setString(2, vorschrift);
        pstmt.setString(3, abschnitt);
        pstmt.executeUpdate();
    }

    public void vorschriften_update(String sachnr, String link, String vorschrift, String abschnitt) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("UPDATE vorschriften SET  Vorschrift = ?, Abschnitt = ?, Link = ? WHERE Sachnr = ?");
        pstmt.setString(1, vorschrift);
        pstmt.setString(2, abschnitt);
        pstmt.setString(3, link);
        pstmt.setString(4, sachnr);
        pstmt.executeUpdate();
    }

    public List<Vorschrift> vorschriften_get_all() throws SQLException {
        ResultSet rs = this.m_connection.createStatement().executeQuery("SELECT * FROM vorschriften");
        List<Vorschrift> list = new ArrayList<>();
        while (rs.next()) {
            Vorschrift p;
            if(rs.getString(4) == null) { //Link might be null
                p = new Vorschrift(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3));
            }
            else {
                p = new Vorschrift(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4));
            }
            list.add(p);
        }
        return list;
    }

    public Vorschrift vorschriften_get(String sachnr) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("SELECT * FROM vorschriften WHERE Sachnr = ?");
        pstmt.setString(1, sachnr);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        if(rs.getString(4) == null) { //Link might be null
            return new Vorschrift(rs.getString(1),
                    rs.getString(2),
                    rs.getString(3));
        }
        else {
            return new Vorschrift(rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4));
        }
    }

    public void vorschriften_remove(String sachnr) throws SQLException {
        PreparedStatement pstmt = this.m_connection.prepareStatement("DELETE FROM vorschriften WHERE Sachnr = ?");
        pstmt.setString(1, sachnr);
        pstmt.executeUpdate();
    }



    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------



    public String execute_querry(String sql) throws SQLException {
        StringBuilder ret = new StringBuilder();
        ResultSet rs = this.m_connection.createStatement().executeQuery(sql);
        while (rs.next()) {
            ret.append(rs.toString());
            ret.append("\r\n");
        }
        return ret.toString();
    }



    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------



    protected Connection m_connection;
    private static final String CREATE_TABLE_INVENTAR_SQL="CREATE TABLE IF NOT EXISTS inventar ("
            + "Kennzeichen CHAR(11) NOT NULL,"
            + "OV CHAR(510),"
            + "Einheit CHAR(510),"
            + "Baujahr INT,"
            + "Hersteller CHAR(510),"
            + "Bezeichnung CHAR(510),"
            + "Sachnr CHAR(510),"
            + "PRIMARY KEY (Kennzeichen),"
            + "FOREIGN KEY (Sachnr) REFERENCES vorschriften(Sachnr)"
            + ")";
    private static final String CREATE_TABLE_PRUEFER_SQL="CREATE TABLE IF NOT EXISTS pruefer ("
            + "id UUID NOT NULL,"
            + "Name CHAR(510) NOT NULL,"
            + "Vorname CHAR(510) NOT NULL,"
            + "Unterschrift IMAGE,"
            + "PRIMARY KEY (id)"
            + ")";
    private static final String CREATE_TABLE_PRUEFUNGEN_SQL="CREATE TABLE IF NOT EXISTS pruefungen ("
            + "id UUID NOT NULL,"
            + "Kennzeichen CHAR(11) NOT NULL,"
            + "Datum DATE NOT NULL,"
            + "Bestanden BOOLEAN NOT NULL,"
            + "Pruefer UUID,"
            + "Bemerkungen CHAR(2048),"
            + "Ausgesondert BOOLEAN,"
            + "OV CHAR(512),"
            + "PRIMARY KEY (id),"
            + "FOREIGN KEY (Kennzeichen) REFERENCES inventar(Kennzeichen),"
            + "FOREIGN KEY (Pruefer) REFERENCES pruefer(id)"
            + ")";
    private static final String CREATE_TABLE_VORSCHRIFTEN_SQL="CREATE TABLE IF NOT EXISTS vorschriften ("
            + "Sachnr CHAR(510) NOT NULL,"
            + "Vorschrift CHAR(510) NOT NULL,"
            + "Abschnitt CHAR(510) NOT NULL,"
            + "Link CHAR(510),"
            + "PRIMARY KEY (Sachnr)"
            + ")";


    public static final int ERROR_CODE_REFERENTIAL_INTEGRITY_VIOLATED_PARENT_MISSING_1 = 23506;
    public static final int DUPLICATE_KEY_1 = 23505;
}
