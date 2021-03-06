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
package thw_matp.datatypes;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Class for transfering data of inspections
 */
public class Inspection {
    public final UUID id;
    public final String kennzeichen;
    public final LocalDate datum;
    public final boolean bestanden;
    public final UUID pruefer;
    public final String bemerkungen;
    public final boolean ausgesondert;
    public final String ov;

    public Inspection(UUID id, String kennzeichen, LocalDate datum, boolean bestanden) {
        this.id = id;
        this.kennzeichen = kennzeichen;
        this.datum = datum;
        this.bestanden = bestanden;
        this.pruefer = new UUID(0, 0);
        this.bemerkungen = "";
        this.ausgesondert = false;
        this.ov = "";
    }

    public Inspection(UUID id, String kennzeichen, LocalDate datum, boolean bestanden, String bemerkungen, boolean ausgesondert, String ov) {
        this.id = id;
        this.kennzeichen = kennzeichen;
        this.datum = datum;
        this.bestanden = bestanden;
        this.pruefer = new UUID(0, 0);
        this.bemerkungen = bemerkungen;
        this.ausgesondert = ausgesondert;
        this.ov = ov;
    }

    public Inspection(UUID id, String kennzeichen, LocalDate datum, boolean bestanden, UUID pruefer, String bemerkungen, boolean ausgesondert, String ov) {
        this.id = id;
        this.kennzeichen = kennzeichen;
        this.datum = datum;
        this.bestanden = bestanden;
        this.pruefer = pruefer;
        this.bemerkungen = bemerkungen;
        this.ausgesondert = ausgesondert;
        this.ov = ov;
    }
}
