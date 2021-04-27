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
package thw_matp.datatypes;

/**
 * Class for transfering data of Vorschriften
 */
public class Vorschrift {
    public final String sachnr;
    public final String vorschrift;
    public final String abschnitt;
    public final String link;

    public Vorschrift(String sachnr, String vorschrift, String abschnitt, String link) {
        this.sachnr = sachnr;
        this.vorschrift = vorschrift;
        this.abschnitt = abschnitt;
        this.link = link;
    }

    public Vorschrift(String sachnr, String vorschrift, String abschnitt) {
        this.sachnr = sachnr;
        this.vorschrift = vorschrift;
        this.abschnitt = abschnitt;
        this.link = "";
    }
}
