package thw_matp.datatypes;

import java.time.LocalDate;
import java.util.UUID;

public class Pruefung {
    public UUID id;
    public String kennzeichen;
    public LocalDate datum;
    public boolean bestanden;
    public UUID pruefer;
    public String bemerkungen;
    public boolean ausgesondert;

    public Pruefung(UUID id, String kennzeichen, LocalDate datum, boolean bestanden) {
        this.id = id;
        this.kennzeichen = kennzeichen;
        this.datum = datum;
        this.bestanden = bestanden;
        this.pruefer = new UUID(0, 0);
        this.bemerkungen = "";
        this.ausgesondert = false;
    }

    public Pruefung(UUID id, String kennzeichen, LocalDate datum, boolean bestanden, String bemerkungen, boolean ausgesondert) {
        this.id = id;
        this.kennzeichen = kennzeichen;
        this.datum = datum;
        this.bestanden = bestanden;
        this.pruefer = new UUID(0, 0);
        this.bemerkungen = bemerkungen;
        this.ausgesondert = ausgesondert;
    }

    public Pruefung(UUID id, String kennzeichen, LocalDate datum, boolean bestanden, UUID pruefer, String bemerkungen, boolean ausgesondert) {
        this.id = id;
        this.kennzeichen = kennzeichen;
        this.datum = datum;
        this.bestanden = bestanden;
        this.pruefer = pruefer;
        this.bemerkungen = bemerkungen;
        this.ausgesondert = ausgesondert;
    }
}
