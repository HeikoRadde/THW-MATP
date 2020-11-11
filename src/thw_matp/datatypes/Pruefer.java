package thw_matp.datatypes;

import java.awt.image.BufferedImage;
import java.util.UUID;

public class Pruefer {
    public UUID id;
    public String name;
    public String vorname;
    public BufferedImage unterschrift;

    public Pruefer(UUID id, String name, String vorname) {
        this.id = id;
        this.name = name;
        this.vorname = vorname;
    }

    public Pruefer(UUID id, String name, String vorname, BufferedImage unterschrift) {
        this.id = id;
        this.name = name;
        this.vorname = vorname;
        this.unterschrift = unterschrift;
        System.out.println(unterschrift.getWidth(null));
        System.out.println(unterschrift.getHeight(null));
    }

    public String toString() {
        return this.vorname + " " + this.name;
    }
}
