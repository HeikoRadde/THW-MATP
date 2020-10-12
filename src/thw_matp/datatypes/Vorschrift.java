package thw_matp.datatypes;

public class Vorschrift {
    public String sachnr;
    public String vorschrift;
    public String abschnitt;
    public String link;

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
    }
}
