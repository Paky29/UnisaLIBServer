package model.prestitomanagement;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import model.libromanagement.Libro;
import model.utentemanagement.Utente;

public class Prestito {
    private GregorianCalendar dataInizio, dataFine, dataConsegna;
    private Utente utente;
    private Libro libro;
    private int voto;
    private boolean attivo=false;

    public Prestito(GregorianCalendar dataInizio, Utente utente, Libro libro, GregorianCalendar dataFine, GregorianCalendar dataConsegna, int voto, boolean attivo) {
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.dataConsegna = dataConsegna;
        this.utente = utente;
        this.libro = libro;
        this.voto = voto;
        this.attivo = attivo;
    }

    public Prestito(GregorianCalendar dataInizio, Utente utente, Libro libro) {
        this.dataInizio = dataInizio;
        this.dataFine = dataInizio;
        dataFine.roll(GregorianCalendar.DATE, 31);
        this.utente = utente;
        this.libro = libro;
    }

    public GregorianCalendar getDataInizio() {
        return dataInizio;
    }

    public GregorianCalendar getDataFine() {
        return dataFine;
    }

    public void setDataFine(GregorianCalendar dataFine) {
        this.dataFine = dataFine;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public int getVoto() {
        return voto;
    }

    public boolean setVoto(int voto) {
        if (voto>0 && voto<=5) {
            this.voto = voto;
            return true;
        }
        return false;
    }

    public GregorianCalendar getDataConsegna() {
        return dataConsegna;
    }

    public boolean isAttivo() {
        return attivo;
    }

    public boolean haConsegnato(GregorianCalendar dataConsegna) {
        if (!this.attivo)
            return false;
        this.dataConsegna=dataConsegna;
        this.attivo=false;
        return true;
    }
}
