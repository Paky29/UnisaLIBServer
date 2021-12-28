package model.postazionemanagement;

import java.util.GregorianCalendar;

public class Periodo {
    private int id, oraInizio, oraFine;
    private GregorianCalendar data;

    public Periodo(int id, int oraInizio, int oraFine, GregorianCalendar data) {
        this.id = id;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.data = data;
    }

    public Periodo(int oraInizio, int oraFine, GregorianCalendar data) {
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(int oraInizio) {
        this.oraInizio = oraInizio;
    }

    public int getOraFine() {
        return oraFine;
    }

    public void setOraFine(int oraFine) {
        this.oraFine = oraFine;
    }

    public GregorianCalendar getData() {
        return data;
    }

    public void setData(GregorianCalendar data) {
        this.data = data;
    }
}
