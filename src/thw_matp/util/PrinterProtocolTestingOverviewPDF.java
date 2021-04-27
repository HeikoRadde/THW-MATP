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
import org.apache.commons.csv.CSVRecord;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.h2.util.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.GregorianCalendar;

/**
 * Printer for creating a PDF version of the CSV file with overview-data of all Prüfungen of the day
 */
public class PrinterProtocolTestingOverviewPDF extends PrinterProtocolPDF {

    public static void set_path(Path path) {
        PrinterProtocolTestingOverviewPDF.path = path;
    }

    public static void create_pdf() throws IOException {
        if (path == null)
        {
            return;
        }
        if (!new File(PrinterProtocolTestingOverviewCSV.create_file_path_name(path)).isFile()) {
            return;
        }
        Reader in = new InputStreamReader(new FileInputStream(PrinterProtocolTestingOverviewCSV.create_file_path_name(path)), StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(new PDRectangle(PAGE_W, PAGE_H));
        document.addPage(page);
        PDPageContentStream content = new PDPageContentStream(document, page);

        float pos_y;
        doc_set_properties(document);
        pos_y = doc_create_header(document, content);
        for (CSVRecord record : records) {
            if (pos_y <= (PAGE_MARGIN_H + calc_entry_height())) {
                page = new PDPage(new PDRectangle(PAGE_W, PAGE_H));
                document.addPage(page);
                content.close();
                content = new PDPageContentStream(document, page);
                pos_y = doc_create_header(document, content);
            }
            pos_y = doc_add_entry(content, pos_y, record.get(0), record.get(1), Boolean.parseBoolean(record.get(2)), Boolean.parseBoolean(record.get(3)), record.get(4));
        }

        content.close();
        document.save(create_file_path_name(path));
        document.close();
    }

    private static void doc_set_properties(PDDocument document) {
        PDDocumentInformation pdd = document.getDocumentInformation();

        String title = "Prüfprotokollübersicht vom " + LocalDate.now().toString();
        pdd.setTitle(title);
        String subject = "Übersicht der Prüfungen am " + LocalDate.now().toString();
        pdd.setSubject(subject);
        pdd.setCreationDate(new GregorianCalendar(LocalDate.now().getYear(), LocalDate.now().getMonthValue()-1, LocalDate.now().getDayOfMonth()));
        pdd.setModificationDate(new GregorianCalendar());
        pdd.setKeywords("Materialprüfung, Übersicht, Prüfprotokolle");
    }

    private static float doc_create_header(PDDocument document, PDPageContentStream content) {
        final String title = "Protokollübersicht";
        float pos_y = PAGE_MAX_H-40;
        try {
            InputStream is = PrinterProtocolTestingPDF.class.getClassLoader().getResourceAsStream("logo_thw_blau.png");
            assert is != null;
            File thw_logo_tmp = File.createTempFile("tmp", "thw_logo");
            IOUtils.copy(is, new FileOutputStream(thw_logo_tmp));
            PDImageXObject thw_logo_pdf = PDImageXObject.createFromFileByContent(thw_logo_tmp, document);

            content.drawImage(thw_logo_pdf, PAGE_MAX_W-200, pos_y, 200, 40);
            content.setFont(FONT_BOLD, TXT_SIZE_TITLE);
            content.beginText();
            content.newLineAtOffset(PAGE_MARGIN_W, pos_y);
            content.showText(title);
            content.endText();
            content.setFont(FONT_BOLD, TXT_SIZE_CHAPTER);
            content.beginText();
            content.newLineAtOffset(PAGE_W/2, pos_y);
            content.showText(LocalDate.now().toString());
            content.endText();
            pos_y = doc_underline_text(content, title, FONT_BOLD, TXT_SIZE_TITLE, 4, PAGE_MARGIN_W, pos_y, (float) -4.5);
            pos_y -= calc_row_offset(FONT_BOLD, TXT_SIZE_TEXT);
            thw_logo_tmp.delete();

            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_CHAPTER);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return pos_y;
    }

    private static float doc_add_entry(PDPageContentStream content, float pos_y, String kennzeichen, String pruefer, boolean bestanden, boolean ausgesondert, String protocol_filename) throws IOException {
        float kennzeichen_pos_y = print_txt_box(content, kennzeichen, FONT_NORMAL, TXT_SIZE_SMALL, POS_COL_0, POS_COL_1, pos_y);
        float pruefer_pos_y = print_txt_box(content, pruefer, FONT_NORMAL, TXT_SIZE_SMALL, POS_COL_1, POS_COL_2, pos_y);
        float bestanden_pos_y = print_txt_box(content, bestanden ? "Bestanden" : "Nicht Bestanden", FONT_NORMAL, TXT_SIZE_SMALL, POS_COL_2, POS_COL_3, pos_y);
        float ausgesondert_pos_y = print_txt_box(content, ausgesondert ? "Ausgesondert" : "Nicht Ausgesondert", FONT_NORMAL, TXT_SIZE_SMALL, POS_COL_3, POS_COL_4, pos_y);
        float filename_pos_y = print_txt_box(content, protocol_filename, FONT_NORMAL, TXT_SIZE_SMALL, POS_COL_4, PAGE_MAX_W, pos_y);
        pos_y = Math.max(kennzeichen_pos_y, Math.max(pruefer_pos_y, Math.max(bestanden_pos_y, Math.max(ausgesondert_pos_y, filename_pos_y))));
        pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_SMALL);

        pos_y = doc_add_divider_line(content, pos_y);
        pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_SMALL);

        return pos_y;
    }

    private static float calc_entry_height() {
        return (calc_row_offset(FONT_NORMAL, TXT_SIZE_SMALL) * 4) + LINE_SIZE;
    }

    protected static float doc_add_divider_line(PDPageContentStream content, float y) {
        final float line_size = LINE_SIZE/2.f;
        try {
            content.setLineWidth(line_size);
            content.moveTo(PAGE_MARGIN_W, y);
            content.lineTo(PAGE_MAX_W, y);
            content.stroke();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return y - line_size;
    }

    private static String create_file_path_name(Path path) {
        return Paths.get(path.toString(), LocalDate.now().toString()).toString() +  "_Prüfübersicht.pdf";
    }


    protected static final float PAGE_W = PrinterProtocolPDF.PAGE_H;
    protected static final float PAGE_H = PrinterProtocolPDF.PAGE_W;
    protected static final float PAGE_MARGIN_W = PrinterProtocolPDF.PAGE_MARGIN_H;
    protected static final float PAGE_MARGIN_H = PrinterProtocolPDF.PAGE_MARGIN_W;
    protected static final float PAGE_MAX_W = PAGE_W - PAGE_MARGIN_W;
    protected static final float PAGE_MAX_H = PAGE_H - PAGE_MARGIN_H;
    protected static final float COL_WITH_STD = PAGE_MAX_W/9;
    protected static final float POS_COL_0 = PAGE_MARGIN_W;
    protected static final float POS_COL_1 = POS_COL_0 + (COL_WITH_STD * 1);
    protected static final float POS_COL_2 = POS_COL_1 + (COL_WITH_STD * 1.5f);
    protected static final float POS_COL_3 = POS_COL_2 + (COL_WITH_STD * 1);
    protected static final float POS_COL_4 = POS_COL_3 + (COL_WITH_STD * 1);

    private static Path path;
}
