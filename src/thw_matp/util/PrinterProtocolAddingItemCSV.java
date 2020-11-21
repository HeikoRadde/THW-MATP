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
