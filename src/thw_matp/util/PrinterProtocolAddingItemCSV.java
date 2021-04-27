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
package thw_matp.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import thw_matp.datatypes.Item;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;

/**
 * Printer for logging newly added Items in a CSV file
 */
public class PrinterProtocolAddingItemCSV {

    public static void add_new_item_event(Path path, Item item) throws IOException {
        String file_path_name = create_file_path_name(path);

        boolean new_file = !Files.exists(Paths.get(file_path_name));

        Writer writer = Files.newBufferedWriter(Paths.get(file_path_name), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        CSVPrinter csv_printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

        if(new_file)
        {
            table_add_header(csv_printer);
        }

        csv_printer.printRecord(item.kennzeichen, item.sachnr, item.bezeichnung, item.hersteller, item.baujahr, item.einheit, item.ov);

        csv_printer.close();
        writer.close();
    }

    private static void table_add_header(CSVPrinter csv_printer ) throws IOException {
        csv_printer.printRecord("Kennzeichen", "Sachnummer", "Bezeichnung", "Hersteller", "Baujahr", "Einheit", "OV");
    }

    public static String create_file_path_name(Path path) {
        return Paths.get(path.toString(), LocalDate.now().toString()).toString() +  "_Neue_Geräte.csv";
    }
}
