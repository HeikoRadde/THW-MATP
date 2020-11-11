package thw_matp.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import thw_matp.datatypes.Pruefer;
import thw_matp.datatypes.Pruefung;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;

public class PrinterProtocolTestingOverview {

    public static void add_pruefung_event(Path path, Pruefung pruefung, Pruefer pruefer) throws IOException {
        String file_path_name = create_file_path_name(path);

        boolean new_file = !Files.exists(Paths.get(file_path_name));

        Writer writer = Files.newBufferedWriter(Paths.get(file_path_name), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        CSVPrinter csv_printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

        if(new_file)
        {
            table_add_header(csv_printer);
        }

        csv_printer.printRecord(pruefung.kennzeichen, pruefer.toString(), pruefung.bestanden, pruefung.ausgesondert, PrinterProtocolTesting.get_log_filename(pruefung));

        csv_printer.close();
        writer.close();
    }

    private static void table_add_header(CSVPrinter csv_printer ) throws IOException {
        csv_printer.printRecord("Kennzeichen", "Prüfer", "Bestanden", "Ausgesondert", "Prüfprotokoll");
    }

    private static String create_file_path_name(Path path) {
        return Paths.get(path.toString(), LocalDate.now().toString()).toString() +  "_Prüfübersicht.csv";
    }
}
