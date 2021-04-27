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

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.h2.util.IOUtils;
import thw_matp.datatypes.Item;
import thw_matp.datatypes.Inspector;
import thw_matp.datatypes.Inspection;
import thw_matp.datatypes.Specification;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.GregorianCalendar;


/**
 * Printer for creating a PDF file detailling the results of a inspection
 */
public class PrinterProtocolInspectionPDF extends PrinterProtocolPDF {

    public static void print_pruefung(Path path, Inspection inspection, Inspector inspector, Item item, Specification specification) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        PDPageContentStream content = new PDPageContentStream(document, page);

        float pos_y = 0.0f;
        doc_set_properties(document, inspection, inspector);
        pos_y = doc_create_header(document, content);
        pos_y = doc_create_txt_item(content, item, pos_y);
        pos_y = doc_create_txt_meta(content, item, specification, pos_y);
        pos_y = doc_create_txt_test(content, inspection, inspector, pos_y);
        pos_y = doc_create_footer(document, content, inspector, pos_y);

        content.close();
        document.save(create_file_path_name(path, inspection));
        document.close();
    }

    public static String get_log_filename(Inspection inspection) {
        return inspection.datum.toString() + "_Kennzeichen_" + inspection.kennzeichen + "_Prüf-ID_" + inspection.id.toString() + ".pdf";
    }

    private static void doc_set_properties(PDDocument document, Inspection inspection, Inspector inspector) {
        PDDocumentInformation pdd = document.getDocumentInformation();

        pdd.setAuthor(inspector.vorname + " " + inspector.name);
        String title = "Prüfprotokoll zu " + inspection.kennzeichen + " vom " + inspection.datum.toString();
        pdd.setTitle(title);
        String subject = "Prüfung des Gerätes " + inspection.kennzeichen + " am " + inspection.datum.toString();
        pdd.setSubject(subject);
        pdd.setCreationDate(new GregorianCalendar(inspection.datum.getYear(), inspection.datum.getMonthValue()-1, inspection.datum.getDayOfMonth()));
        pdd.setModificationDate(new GregorianCalendar());
        pdd.setKeywords("Materialprüfung, Prüfprotokoll, " + inspection.kennzeichen);
    }

    private static float doc_create_header(PDDocument document, PDPageContentStream content) {
        final String title = "Prüfprotokoll";
        float pos_y = PAGE_MAX_H-40;
        try {
            InputStream is = PrinterProtocolInspectionPDF.class.getClassLoader().getResourceAsStream("logo_thw_blau.png");
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
            pos_y = doc_underline_text(content, title, FONT_BOLD, TXT_SIZE_TITLE, 4, PAGE_MARGIN_W, pos_y, (float) -4.5);
            pos_y -= calc_row_offset(FONT_BOLD, TXT_SIZE_TITLE);
            thw_logo_tmp.delete();

            pos_y = doc_add_divider_line(content, pos_y);
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_CHAPTER);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return pos_y;
    }

    private static float doc_create_txt_meta(PDPageContentStream content, Item item, Specification specification, float pos_y) {
        try {
            content.setFont(FONT_NORMAL, TXT_SIZE_SECTION);
            content.beginText();
            content.newLineAtOffset(calc_x_right_align_txt("Zuordnung: ", FONT_NORMAL, TXT_SIZE_SECTION, POS_COL_1), pos_y);
            content.showText("Zuordnung:");
            content.endText();
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_SECTION);

            content.setFont(FONT_NORMAL, TXT_SIZE_TEXT);
            content.beginText();
            content.newLineAtOffset(calc_x_right_align_txt("Ortsverband: ", FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_1), pos_y);
            content.showText("Ortsverband:");
            content.endText();
            content.beginText();
            content.newLineAtOffset(POS_COL_1, pos_y);
            content.showText(item.ov);
            content.endText();
            content.beginText();
            content.newLineAtOffset(calc_x_right_align_txt("AN/Einheit: ", FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_3), pos_y);
            content.showText("AN/Einheit:");
            content.endText();
            content.beginText();
            content.newLineAtOffset(POS_COL_3, pos_y);
            content.showText(item.einheit);
            content.endText();
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TEXT);

            content.beginText();
            content.newLineAtOffset(calc_x_right_align_txt("Prüfungsvorschrift: ", FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_1), pos_y);
            content.showText("Prüfungsvorschrift:");
            content.endText();
            float vorschrift_pos_y = print_txt_box(content, specification.vorschrift, FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_1, POS_COL_2, pos_y);
            content.beginText();
            content.newLineAtOffset(calc_x_right_align_txt("Abschnitt: ", FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_3), pos_y);
            content.showText("Abschnitt:");
            content.endText();
            float abschnitt_pos_y = print_txt_box(content, specification.abschnitt, FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_3, PAGE_MAX_W, pos_y);
            pos_y = Math.min(vorschrift_pos_y, abschnitt_pos_y);
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TEXT);

            pos_y = doc_add_divider_line(content, pos_y);
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_CHAPTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pos_y;
    }

    private static float doc_create_txt_item(PDPageContentStream content, Item item, float pos_y) {
        try {
            content.setFont(FONT_NORMAL, TXT_SIZE_SECTION);
            content.beginText();
            content.newLineAtOffset(calc_x_right_align_txt("Gerät: ", FONT_NORMAL, TXT_SIZE_SECTION, POS_COL_1), pos_y);
            content.showText("Gerät:");
            content.endText();
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_SECTION);

            content.setFont(FONT_NORMAL, TXT_SIZE_TEXT);
            content.beginText();
            content.newLineAtOffset(calc_x_right_align_txt("Bezeichnung: ", FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_1), pos_y);
            content.showText("Bezeichnung:");
            content.endText();
            pos_y = print_txt_box(content, item.bezeichnung, FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_1, PAGE_MAX_W, pos_y);
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TEXT);

            content.beginText();
            content.newLineAtOffset(calc_x_right_align_txt("Hersteller: ", FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_1), pos_y);
            content.showText("Hersteller:");
            content.endText();
            pos_y = print_txt_box(content, item.hersteller, FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_1, PAGE_MAX_W, pos_y);
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TEXT);

            content.beginText();
            content.newLineAtOffset(calc_x_right_align_txt("Sachnummer: ", FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_1), pos_y);
            content.showText("Sachnummer:");
            content.endText();
            content.beginText();
            content.newLineAtOffset(POS_COL_1, pos_y);
            content.showText(item.sachnr);
            content.endText();
            content.beginText();
            content.newLineAtOffset(calc_x_right_align_txt("Kennzeichen: ", FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_3), pos_y);
            content.showText("Kennzeichen:");
            content.endText();
            content.beginText();
            content.setFont(FONT_BOLD, TXT_SIZE_TEXT);
            content.newLineAtOffset(POS_COL_3, pos_y);
            content.showText(item.kennzeichen);
            content.endText();
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TEXT);

            pos_y = doc_add_divider_line(content, pos_y);
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_CHAPTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pos_y;
    }

    private static float doc_create_txt_test(PDPageContentStream content, Inspection inspection, Inspector inspector, float pos_y) {
        try {
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TEXT) * 0.5f;

            content.setFont(FONT_BOLD, TXT_SIZE_CHAPTER);
            content.beginText();
            content.newLineAtOffset(PAGE_MARGIN_W, pos_y);
            content.showText("Prüfungsergebnis:");
            content.endText();
            content.beginText();
            content.newLineAtOffset(POS_COL_2, pos_y);
            if (inspection.bestanden) content.showText("BESTANDEN");
            else content.showText("NICHT BESTANDEN");
            content.endText();
            pos_y = doc_underline_text(content, "Prüfungsergebnis:", FONT_BOLD, TXT_SIZE_CHAPTER, 2, PAGE_MARGIN_W, pos_y, (float) -2.5);
            pos_y -= calc_row_offset(FONT_BOLD, TXT_SIZE_CHAPTER);

            content.setFont(FONT_NORMAL, TXT_SIZE_TEXT);
            content.beginText();
            content.newLineAtOffset(calc_x_right_align_txt("Datum: ", FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_1), pos_y);
            content.showText("Datum:");
            content.endText();
            content.setFont(FONT_BOLD, TXT_SIZE_TEXT);
            content.beginText();
            content.newLineAtOffset(POS_COL_1, pos_y);
            content.showText(inspection.datum.toString());
            content.endText();
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TEXT);

            content.setFont(FONT_NORMAL, TXT_SIZE_TEXT);
            content.beginText();
            content.newLineAtOffset(calc_x_right_align_txt("Prüfer/in: ", FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_1), pos_y);
            content.showText("Prüfer/in:");
            content.endText();
            content.setFont(FONT_BOLD, TXT_SIZE_TEXT);
            content.beginText();
            content.newLineAtOffset(POS_COL_1, pos_y);
            content.showText(inspector.vorname + " " + inspector.name);
            content.endText();
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TEXT);

            content.setFont(FONT_NORMAL, TXT_SIZE_TEXT);
            content.beginText();
            content.newLineAtOffset(calc_x_right_align_txt("Im Ortsverband: ", FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_1), pos_y);
            content.showText("Im Ortsverband:");
            content.endText();
            content.setFont(FONT_BOLD, TXT_SIZE_TEXT);
            content.beginText();
            content.newLineAtOffset(POS_COL_1, pos_y);
            content.showText(inspection.ov);
            content.endText();
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TEXT);

            content.setFont(FONT_NORMAL, TXT_SIZE_TEXT);
            content.beginText();
            content.newLineAtOffset(calc_x_right_align_txt("Ausgesondert: ", FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_1), pos_y);
            content.showText("Ausgesondert:");
            content.endText();
            content.beginText();
            content.newLineAtOffset(POS_COL_1, pos_y);
            if (inspection.ausgesondert) {
                content.setFont(FONT_BOLD, TXT_SIZE_TEXT);
                content.showText("JA");
            }
            else {
                content.showText("NEIN");
            }
            content.endText();
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TEXT);

            content.setFont(FONT_NORMAL, TXT_SIZE_TEXT);
            content.beginText();
            content.newLineAtOffset(calc_x_right_align_txt("Bemerkungen: ", FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_1), pos_y);
            content.showText("Bemerkungen:");
            content.endText();
            if (inspection.bemerkungen.isEmpty()) {
                content.beginText();
                content.newLineAtOffset(POS_COL_1, pos_y);
                content.showText(" —");
                content.endText();
            }
            else {
                pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TEXT);
                pos_y = print_txt_box(content, inspection.bemerkungen, FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_1, PAGE_MAX_W, pos_y);
            }
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TEXT);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return pos_y;
    }

    private static float doc_create_footer(PDDocument document, PDPageContentStream content, Inspector inspector, float pos_y) {
        final float signature_height = 40f;
        final float line_width = 1f;
        float x_start = PAGE_MAX_W-400;
        try {
            pos_y = doc_add_divider_line(content, pos_y);
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TITLE) * 2;

            if (inspector.unterschrift != null) {
                float ratio = (inspector.unterschrift.getWidth(null) * 1.0f) / (inspector.unterschrift.getHeight(null) * 1.0f);
                File signature_tmp = File.createTempFile("tmp", "unterschrift");
                BufferedImage buffered_image = new BufferedImage(inspector.unterschrift.getWidth(null), inspector.unterschrift.getHeight(null), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = buffered_image.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, buffered_image.getWidth(), buffered_image.getHeight());
                buffered_image.getGraphics().drawImage(inspector.unterschrift, 0, 0, null);
                ImageIO.write(buffered_image, "png", signature_tmp);

                PDImageXObject signature_pdf = PDImageXObject.createFromFileByContent(signature_tmp, document);
                x_start = PAGE_MAX_W-signature_height*ratio;
                content.drawImage(signature_pdf, x_start, pos_y, signature_height*ratio, signature_height);
                signature_tmp.delete();
            }
            else pos_y -= signature_height;
            pos_y -= 5;

            content.setLineWidth(line_width);
            content.moveTo(x_start, pos_y);
            content.lineTo(PAGE_MAX_W, pos_y);
            content.stroke();
            pos_y -= line_width;
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TEXT);

            content.setFont(FONT_NORMAL, TXT_SIZE_TEXT);
            content.beginText();
            content.newLineAtOffset(x_start, pos_y);
            content.showText("Unterschrift Prüfer/in");
            content.endText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pos_y;
    }
}
