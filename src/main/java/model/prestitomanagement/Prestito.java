package model.prestitomanagement;

import java.lang.reflect.Type;
import java.util.GregorianCalendar;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import model.libromanagement.Libro;
import model.utentemanagement.Utente;

public class Prestito {
    private GregorianCalendar dataInizio, dataFine, dataConsegna;
    private Utente utente;
    private Libro libro;
    private String commento;
    private int voto;
    private boolean attivo=false;

    public GregorianCalendar getDataInizio() { return dataInizio; }

    public GregorianCalendar getDataFine() { return dataFine; }

    public Utente getUtente() { return utente; }

    public Libro getLibro() { return libro; }

    public int getVoto() { return voto; }

    public GregorianCalendar getDataConsegna() { return dataConsegna;}

    public String getCommento() {return commento;}

    public boolean isAttivo() { return attivo; }

    public static class PrestitoBuilder{
        private GregorianCalendar dataInizio, dataFine, dataConsegna;
        private Utente utente;
        private Libro libro;
        private String commento;
        private int voto;
        private boolean attivo=false;

        public Prestito.PrestitoBuilder dataInizio(GregorianCalendar dataInizio){
            this.dataInizio=dataInizio;
            dataFine=dataInizio;
            dataFine.roll(GregorianCalendar.DATE, 31);
            return this;
        }

        public Prestito.PrestitoBuilder dataFine(GregorianCalendar dataFine){
            this.dataFine=dataFine;
            return this;
        }

        public Prestito.PrestitoBuilder dataConsegna(GregorianCalendar dataConsegna){
            this.dataConsegna=dataConsegna;
            this.attivo=false;
            return this;
        }

        public Prestito.PrestitoBuilder utente(Utente utente){
            this.utente=utente;
            return this;
        }

        public Prestito.PrestitoBuilder libro(Libro libro){
            this.libro=libro;
            return this;
        }

        public Prestito.PrestitoBuilder commento(String commento){
            this.commento=commento;
            return this;
        }

        public Prestito.PrestitoBuilder voto(int voto){
            if (voto>0 && voto<=5)
                this.voto=voto;
            return this;
        }

        public Prestito.PrestitoBuilder attivo(boolean attivo){
            if(this.dataConsegna == null || attivo == false)
                this.attivo=attivo;
            return this;
        }

        public Prestito build(){
            return new Prestito(this);
        }
    }

    private Prestito(Prestito.PrestitoBuilder pb){
        this.dataInizio = pb.dataInizio;
        this.dataFine = pb.dataFine;
        this.dataConsegna = pb.dataConsegna;
        this.utente = pb.utente;
        this.libro = pb.libro;
        this.voto = pb.voto;
        this.attivo = pb.attivo;
    }

    public static Prestito fromJsonToPrestito(String json) throws JsonSyntaxException {
        Gson gson = new Gson();
        Prestito prestito= gson.fromJson(json, Prestito.class);
        return prestito;
    }

    public static String toJson(Prestito p){
        Gson gson = new Gson();
        Type fooType = new TypeToken<Prestito>() {}.getType();
        String json = gson.toJson(p,fooType);
        return json;
    }

    public static String toJson(List<Prestito> prestiti){
        Gson gson = new Gson();
        return gson.toJson(prestiti);
    }
}
