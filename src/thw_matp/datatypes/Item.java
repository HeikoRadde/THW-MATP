package thw_matp.datatypes;

public class Item {
    public String kennzeichen;
    public String ov;
    public String einheit;
    public int baujahr;
    public String hersteller;
    public String bezeichnung;
    public String sachnr;

    public Item(String kennzeichen, String ov, String einheit, int baujahr, String hersteller, String bezeichnung, String sachnr) {
        this.kennzeichen = kennzeichen;
        this.ov = ov;
        this.einheit = einheit;
        this.baujahr = baujahr;
        this.hersteller = hersteller;
        this.bezeichnung = bezeichnung;
        this.sachnr = sachnr;
    }
}
