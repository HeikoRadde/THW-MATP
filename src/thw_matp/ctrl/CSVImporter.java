package thw_matp.ctrl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class CSVImporter {

    public CSVImporter(String filepath) {
        this.m_filepath = filepath;
        this.m_new_vorschriften = new ArrayList<String>();
    }


    public void read(Database db) throws IOException {
        Reader in = new InputStreamReader(new FileInputStream(this.m_filepath), "windows-1252");
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().withDelimiter(';').parse(in);
        for (CSVRecord record : records) {
            switch (_insert_record(db, record)) {
                case 1 -> System.err.println("Sachnummer " + record.get(5) + " not known and coudn't be inserted!");
                case 2 -> System.err.println("Unhandled error!");
            }
        }
    }

    public ArrayList<String> get_added_vorschriften() {
        return this.m_new_vorschriften;
    }

    private int _insert_record(Database db, CSVRecord record) {
        String kennzeichen = record.get(6);
        String ov = record.get(0);
        String einheit = record.get(1);
        String baujahr = record.get(3);
        String hersteller = record.get(4);
        String bezeichnung = record.get(2);
        String sachnr = record.get(5);
        String letzte_pruefung = record.get(10);
        if (baujahr.isEmpty()) {
            baujahr = "1900";
        }
        try {
            db.inventar_add(kennzeichen, ov, einheit, Integer.parseInt(baujahr), hersteller, bezeichnung, sachnr);
            if (!letzte_pruefung.isEmpty())
            {
                int day = Integer.parseInt(letzte_pruefung.substring(0, letzte_pruefung.indexOf(".")));
                int month = Integer.parseInt(letzte_pruefung.substring(letzte_pruefung.indexOf(".")+1, letzte_pruefung.lastIndexOf(".")));
                int year = Integer.parseInt(letzte_pruefung.substring(letzte_pruefung.lastIndexOf(".")+1));
                db.puefung_add_event(kennzeichen, LocalDate.of(year, month, day), true);
            }
            return 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == Database.ERROR_CODE_REFERENTIAL_INTEGRITY_VIOLATED_PARENT_MISSING_1) {
                System.err.println("Sachnr " + sachnr +  " not known!");
                try {
                    db.vorschriften_add(sachnr, "", "", "");
                    this.m_new_vorschriften.add(sachnr);
                    return _insert_record(db, record);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    return 1;
                }
            }
            else {
                e.printStackTrace();
                return 2;
            }
        }
    }


    private final String m_filepath;
    private ArrayList<String> m_new_vorschriften;
}
