package thw_matp.ctrl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;

public class CSVImporter {

    public CSVImporter(String filepath) {
        this.m_filepath = filepath;
    }


    public void read(Database db) throws IOException {
        Reader in = new InputStreamReader(new FileInputStream(this.m_filepath), "windows-1252");
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().withDelimiter(';').parse(in);
        for (CSVRecord record : records) {
//            for (int i = 0; i < record.size(); ++i) {
//                System.out.print(record.get(i) + " | ");
//            }
//            System.out.println("");

            String kennzeichen = record.get(6);
            String ov = record.get(0);
            String einheit = record.get(1);
            String baujahr = record.get(3);
            String hersteller = record.get(4);
            String bezeichnung = record.get(2);
            String sachnr = record.get(5);
            String letzte_pruefung = record.get(10);
            if(baujahr.isEmpty()) {
                baujahr = "1900";
            }
            try {
                db.inventar_add(kennzeichen, ov, einheit, Integer.parseInt(baujahr), hersteller, bezeichnung, sachnr);
                if(!letzte_pruefung.isEmpty())
                {
                    int day = Integer.parseInt(letzte_pruefung.substring(0, letzte_pruefung.indexOf(".")));
                    int month = Integer.parseInt(letzte_pruefung.substring(letzte_pruefung.indexOf(".")+1, letzte_pruefung.lastIndexOf(".")));
                    int year = Integer.parseInt(letzte_pruefung.substring(letzte_pruefung.lastIndexOf(".")+1));
                    db.puefung_add_event(kennzeichen, LocalDate.of(year, month, day), true);
                }
            } catch (SQLException e) {
                if(e.getErrorCode() == Database.ERROR_CODE_REFERENTIAL_INTEGRITY_VIOLATED_PARENT_MISSING_1) {
                    System.err.println("Sachnr " + sachnr +  " not known!");
                }
                else {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }


    private final String m_filepath;
}
