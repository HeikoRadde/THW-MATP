package thw_matp.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.h2.util.IOUtils;
import thw_matp.datatypes.Item;
import thw_matp.datatypes.Pruefer;
import thw_matp.datatypes.Pruefung;
import thw_matp.datatypes.Vorschrift;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.GregorianCalendar;


public class PrinterProtocolTesting {

    public static void print_pruefung(Path path, Pruefung pruefung, Pruefer pruefer, Item item, Vorschrift vorschrift) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        PDPageContentStream content = new PDPageContentStream(document, page);

        float pos_y = 0;
        doc_set_properties(document, pruefung, pruefer);
        pos_y = doc_create_header(document, content, pruefer);
        pos_y = doc_create_txt_item(document, content, item, pos_y);
        pos_y = doc_create_txt_meta(document, content, item, vorschrift, pos_y);
        pos_y = doc_create_txt_test(document, content, pruefung, pruefer, pos_y);
        pos_y = doc_create_footer(document, content, pruefer, pos_y);

        content.close();
        document.save(create_file_path_name(path, pruefung));
        document.close();
    }

    public static String get_log_filename(Pruefung pruefung) {
        return pruefung.datum.toString() + "_Kennzeichen_" + pruefung.kennzeichen + "_Prüf-ID_" + pruefung.id.toString() + ".pdf";
    }

    private static void doc_set_properties(PDDocument document, Pruefung pruefung, Pruefer pruefer) {
        PDDocumentInformation pdd = document.getDocumentInformation();

        pdd.setAuthor(pruefer.vorname + " " + pruefer.name);
        String title = "Prüfprotokoll zu " + pruefung.kennzeichen + " vom " + pruefung.datum.toString();
        pdd.setTitle(title);
        String subject = "Prüfung des Gerätes " + pruefung.kennzeichen + " am " + pruefung.datum.toString();
        pdd.setSubject(subject);
        pdd.setCreationDate(new GregorianCalendar(pruefung.datum.getYear(), pruefung.datum.getMonthValue()-1, pruefung.datum.getDayOfMonth()));
        pdd.setModificationDate(new GregorianCalendar());
        pdd.setKeywords("Materialprüfung, Prüfprotokoll, " + pruefung.kennzeichen);
    }

    private static float doc_create_header(PDDocument document, PDPageContentStream content, Pruefer pruefer) {
        final String title = "Prüfprotokoll";
        final int font_size = 35;
        float pos_y = PAGE_MAX_H-40;
        try {
            InputStream is = PrinterProtocolTesting.class.getClassLoader().getResourceAsStream("logo_thw_blau.png");
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

    private static float doc_create_txt_meta(PDDocument document, PDPageContentStream content, Item item, Vorschrift vorschrift, float pos_y) {
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
            float vorschrift_pos_y = print_txt_box(content, vorschrift.vorschrift, FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_1, POS_COL_2, pos_y);
            content.beginText();
            content.newLineAtOffset(calc_x_right_align_txt("Abschnitt: ", FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_3), pos_y);
            content.showText("Abschnitt:");
            content.endText();
            float abschnitt_pos_y = print_txt_box(content, vorschrift.abschnitt, FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_3, PAGE_MAX_W, pos_y);
            pos_y = Math.min(vorschrift_pos_y, abschnitt_pos_y);
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TEXT);

            pos_y = doc_add_divider_line(content, pos_y);
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_CHAPTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pos_y;
    }

    private static float doc_create_txt_item(PDDocument document, PDPageContentStream content, Item item, float pos_y) {
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

    private static float doc_create_txt_test(PDDocument document, PDPageContentStream content, Pruefung pruefung, Pruefer pruefer, float pos_y) {
        try {
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TEXT) * 0.5f;

            content.setFont(FONT_BOLD, TXT_SIZE_CHAPTER);
            content.beginText();
            content.newLineAtOffset(PAGE_MARGIN_W, pos_y);
            content.showText("Prüfungsergebnis:");
            content.endText();
            content.beginText();
            content.newLineAtOffset(POS_COL_2, pos_y);
            if (pruefung.bestanden) content.showText("BESTANDEN");
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
            content.showText(pruefung.datum.toString());
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
            content.showText(pruefer.vorname + " " + pruefer.name);
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
            content.showText("OV_NAME");
            content.endText();
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TEXT);

            content.setFont(FONT_NORMAL, TXT_SIZE_TEXT);
            content.beginText();
            content.newLineAtOffset(calc_x_right_align_txt("Ausgesondert: ", FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_1), pos_y);
            content.showText("Ausgesondert:");
            content.endText();
            content.beginText();
            content.newLineAtOffset(POS_COL_1, pos_y);
            if (pruefung.ausgesondert) {
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
            if (pruefung.bemerkungen.isEmpty()) {
                content.beginText();
                content.newLineAtOffset(POS_COL_1, pos_y);
                content.showText(" —");
                content.endText();
            }
            else {
                pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TEXT);
                pos_y = print_txt_box(content, pruefung.bemerkungen, FONT_NORMAL, TXT_SIZE_TEXT, POS_COL_1, PAGE_MAX_W, pos_y);
            }
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TEXT);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return pos_y;
    }

    private static float doc_create_footer(PDDocument document, PDPageContentStream content, Pruefer pruefer, float pos_y) {
        final float signature_height = 40f;
        final float line_width = 1f;
        float x_start = PAGE_MAX_W-400;
        try {
            pos_y = doc_add_divider_line(content, pos_y);
            pos_y -= calc_row_offset(FONT_NORMAL, TXT_SIZE_TITLE) * 2;

            if (pruefer.unterschrift != null) {
                float ratio = (pruefer.unterschrift.getWidth(null) * 1.0f) / (pruefer.unterschrift.getHeight(null) * 1.0f);
                File signature_tmp = File.createTempFile("tmp", "unterschrift");
                BufferedImage buffered_image = new BufferedImage(pruefer.unterschrift.getWidth(null), pruefer.unterschrift.getHeight(null), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = buffered_image.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, buffered_image.getWidth(), buffered_image.getHeight());
                buffered_image.getGraphics().drawImage(pruefer.unterschrift, 0, 0, null);
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

    private static float doc_underline_text(PDPageContentStream content, String text, PDType1Font font, float font_size, float line_width, float x, float y, float line_y_offset) {
        try {
            float string_width  = font_size * font.getStringWidth(text) / 1000;
            float line_end = x + string_width;
            content.setLineWidth(line_width);
            content.moveTo(x, y + line_y_offset);
            content.lineTo(line_end, y + line_y_offset);
            content.stroke();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return y - (line_y_offset + line_width);
    }

    private static float doc_add_divider_line(PDPageContentStream content, float y) {
        final float line_size = 1.7f;
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

    private static String create_file_path_name(Path path, Pruefung pruefung) {
        return Paths.get(path.toString(), PrinterProtocolTesting.get_log_filename(pruefung)).toString();
    }

    private static float calc_font_height(PDType1Font font, float font_size) {
        return ((font.getFontDescriptor().getCapHeight() / 1000.0f) * font_size);
    }

    private static float calc_row_offset(PDType1Font font, float font_size) {
        return calc_font_height(font, font_size) + (font_size/2);
    }

    private static float calc_x_right_align_txt(String text, PDType1Font font, float font_size, float x_end) throws IOException {
        return x_end - ((font.getStringWidth(text) / 1000.f) * font_size);
    }

    private static float print_txt_box(PDPageContentStream content, String text, PDType1Font font, float font_size, float x_start, float x_stop, float pos_y) throws IOException {
        content.setFont(font, font_size);
        while (!text.isEmpty()) {
            String line = "";
            float line_length = 0.0f;
            text = text.strip();
            do {
                line_length = (font.getStringWidth(line) / 1000.f) * font_size;
                line += text.charAt(0);
                text = text.substring(1);
            } while ((line_length < (x_stop - x_start)) && (!text.isEmpty()));
            int idx_line_last_space = line.lastIndexOf(' ');
            if ((idx_line_last_space != line.length()) && (idx_line_last_space != -1) && (!text.isEmpty())) {
                text = line.substring(idx_line_last_space) + text;
                line = line.substring(0, idx_line_last_space);
            }
            line = line.strip();
            content.beginText();
            content.newLineAtOffset(x_start, pos_y);
            content.showText(line);
            content.endText();
            if (!text.isEmpty()) pos_y -= calc_row_offset(font, font_size);
        }
        return pos_y;
    }


    private static final float PAGE_W = 595;
    private static final float PAGE_H = 842;
    private static final float PAGE_MARGIN_W = 25;
    private static final float PAGE_MARGIN_H = 50;
    private static final float PAGE_MAX_W = PAGE_W - PAGE_MARGIN_W;
    private static final float PAGE_MAX_H = PAGE_H - PAGE_MARGIN_H;
    private static final PDType1Font FONT_NORMAL = PDType1Font.HELVETICA;
    private static final PDType1Font FONT_BOLD = PDType1Font.HELVETICA_BOLD;
    private static final float TXT_SIZE_TITLE = 35;
    private static final float TXT_SIZE_CHAPTER = 20;
    private static final float TXT_SIZE_SECTION = 15;
    private static final float TXT_SIZE_TEXT = 12;
    private static final float POS_COL_0 = PAGE_MARGIN_W;
    private static final float POS_COL_1 = PAGE_MAX_W / 4;
    private static final float POS_COL_2 = PAGE_MAX_W / 2;
    private static final float POS_COL_3 = PAGE_MAX_W - (PAGE_MAX_W / 4);
}