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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Class for importing CSV files into the database
 */
public class CSVImporter {

    /**
     * @param filepath Path to the CSV file to import
     */
    public CSVImporter(String filepath) {
        this.m_filepath = filepath;
        this.m_new_specifications = new ArrayList<>();
    }

    /**
     *              Read the CSV-file specified in {@link #m_filepath} and add its content to the database
     * @param db    Database to add the content of the CSV-file to
     * @throws IOException
     */
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

    /**
     *          Retrieve all specifications added during {@link #read(Database)}
     * @return  List of all new specifications
     */
    public ArrayList<String> get_added_inspections() {
        return this.m_new_specifications;
    }

    /**
     *                  Add a single CSV record to the database
     * @param db        Database to modify
     * @param record    Record to add
     * @return          0 on success, 1 if unknown Sachnummer couldn't be added, 2 on other error
     */
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
            db.inventory_add(kennzeichen, ov, einheit, Integer.parseInt(baujahr), hersteller, bezeichnung, sachnr);
            if (!letzte_pruefung.isEmpty())
            {
                int day = Integer.parseInt(letzte_pruefung.substring(0, letzte_pruefung.indexOf(".")));
                int month = Integer.parseInt(letzte_pruefung.substring(letzte_pruefung.indexOf(".")+1, letzte_pruefung.lastIndexOf(".")));
                int year = Integer.parseInt(letzte_pruefung.substring(letzte_pruefung.lastIndexOf(".")+1));
                db.inspection_add_event(kennzeichen, LocalDate.of(year, month, day), true);
            }
            return 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == Database.ERROR_CODE_REFERENTIAL_INTEGRITY_VIOLATED_PARENT_MISSING_1) {
                System.err.println("Sachnr " + sachnr +  " not known!");
                try {
                    db.specification_add(sachnr, "", "", "");
                    this.m_new_specifications.add(sachnr);
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
    private final ArrayList<String> m_new_specifications;
}
