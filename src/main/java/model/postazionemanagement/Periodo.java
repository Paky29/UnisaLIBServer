package model.postazionemanagement;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

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

    public Periodo() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Periodo periodo = (Periodo) o;
        return oraInizio == periodo.oraInizio && oraFine == periodo.oraFine && data.get(Calendar.DATE)==periodo.data.get(Calendar.DATE) && data.get(Calendar.MONTH)==periodo.data.get(Calendar.MONTH) && data.get(Calendar.YEAR)==periodo.data.get(Calendar.YEAR);
    }

    public static Periodo fromJson(String json) throws JsonSyntaxException {
        Gson gson = new Gson();
        Periodo p = gson.fromJson(json,Periodo.class);
        return p;
    }

    public static String toJson(Periodo p) {
        Gson gson = new Gson();
        return gson.toJson(p);
    }
}
