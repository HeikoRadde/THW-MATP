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
package thw_matp.util;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import thw_matp.datatypes.Pruefung;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PrinterProtocolPDF {
    protected static float doc_underline_text(PDPageContentStream content, String text, PDType1Font font, float font_size, float line_width, float x, float y, float line_y_offset) {
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

    protected static float doc_add_divider_line(PDPageContentStream content, float y) {
        final float line_size = LINE_SIZE;
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

    protected static String create_file_path_name(Path path, Pruefung pruefung) {
        return Paths.get(path.toString(), PrinterProtocolTestingPDF.get_log_filename(pruefung)).toString();
    }

    protected static float calc_font_height(PDType1Font font, float font_size) {
        return ((font.getFontDescriptor().getCapHeight() / 1000.0f) * font_size);
    }

    protected static float calc_row_offset(PDType1Font font, float font_size) {
        return calc_font_height(font, font_size) + (font_size/2);
    }

    protected static float calc_x_right_align_txt(String text, PDType1Font font, float font_size, float x_end) throws IOException {
        return x_end - ((font.getStringWidth(text) / 1000.f) * font_size);
    }

    protected static float print_txt_box(PDPageContentStream content, String text, PDType1Font font, float font_size, float x_start, float x_stop, float pos_y) throws IOException {
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



    protected static final float PAGE_W = PDRectangle.A4.getWidth();
    protected static final float PAGE_H = PDRectangle.A4.getHeight();
    protected static final float PAGE_MARGIN_W = 25;
    protected static final float PAGE_MARGIN_H = 50;
    protected static final float PAGE_MAX_W = PAGE_W - PAGE_MARGIN_W;
    protected static final float PAGE_MAX_H = PAGE_H - PAGE_MARGIN_H;
    protected static final PDType1Font FONT_NORMAL = PDType1Font.HELVETICA;
    protected static final PDType1Font FONT_BOLD = PDType1Font.HELVETICA_BOLD;
    protected static final float TXT_SIZE_TITLE = 35;
    protected static final float TXT_SIZE_CHAPTER = 20;
    protected static final float TXT_SIZE_SECTION = 15;
    protected static final float TXT_SIZE_TEXT = 12;
    protected static final float TXT_SIZE_SMALL = 8;
    protected static final float POS_COL_0 = PAGE_MARGIN_W;
    protected static final float POS_COL_1 = PAGE_MAX_W / 4;
    protected static final float POS_COL_2 = PAGE_MAX_W / 2;
    protected static final float POS_COL_3 = PAGE_MAX_W - (PAGE_MAX_W / 4);
    protected static final float LINE_SIZE = 1.7f;
}
