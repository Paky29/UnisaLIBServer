package model.utentemanagement;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import model.libromanagement.Libro;
import model.postazionemanagement.Postazione;
import model.prenotazionemanagement.Prenotazione;
import model.prestitomanagement.Prestito;
import org.json.JSONException;
import org.json.JSONObject;

public class Utente {
    private String email, password, nome, cognome, matricola, genere;
    private int eta;
    private boolean admin, nuovo;
    private ArrayList<Libro> interessi;
    private ArrayList<Prestito> prestiti;
    private ArrayList<Prenotazione> prenotazioni;

    public String getEmail() {
        return email;
    }

    public String getPassword() { return password; }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getMatricola() {
        return matricola;
    }

    public String getGenere() {
        return genere;
    }

    public int getEta() {
        return eta;
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean isNuovo() { return nuovo; }

    public ArrayList<Libro> getInteressi() {
        return interessi;
    }

    public ArrayList<Prestito> getPrestiti() {
        return prestiti;
    }

    public ArrayList<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utente utente = (Utente) o;

        if(matricola!=null)
            if(!matricola.equals(utente.matricola))
                return false;
        if(genere!=null)
            if(!genere.equals(utente.genere))
                return false;
        return email.equals(utente.email) && nome.equals(utente.nome) && cognome.equals(utente.cognome) && eta == utente.eta && nuovo == utente.nuovo;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", matricola='" + matricola + '\'' +
                ", genere='" + genere + '\'' +
                ", eta=" + eta +
                ", admin=" + admin +
                ", nuovo=" + nuovo +
                ", interessi=" + interessi +
                ", prestiti=" + prestiti +
                ", prenotazioni=" + prenotazioni +
                '}';
    }

    public static class UtenteBuilder{
        private String email, password, nome, cognome, matricola, genere;
        private int eta;
        private boolean admin, nuovo;
        private ArrayList<Libro> interessi=new ArrayList<>();
        private ArrayList<Prestito> prestiti=new ArrayList<>();
        private ArrayList<Prenotazione> prenotazioni=new ArrayList<>();

        public UtenteBuilder email(String email){
            this.email=email;
            return this;
        }

        public UtenteBuilder password(String password){
            this.password=password;
            return this;
        }

        public UtenteBuilder nome(String nome){
            this.nome=nome;
            return this;
        }

        public UtenteBuilder cognome(String cognome){
            this.cognome=cognome;
            return this;
        }

        public UtenteBuilder matricola(String matricola){
            this.matricola=matricola;
            return this;
        }

        public UtenteBuilder genere(String genere){
            this.genere=genere;
            return this;
        }

        public UtenteBuilder eta(int eta){
            this.eta=eta;
            return this;
        }

        public UtenteBuilder admin(boolean admin){
            this.admin=admin;
            return this;
        }

        public UtenteBuilder nuovo(boolean nuovo){
            this.nuovo=nuovo;
            return this;
        }

        public UtenteBuilder interessi(ArrayList<Libro> interessi){
            this.interessi=interessi;
            return this;
        }

        public UtenteBuilder prestiti(ArrayList<Prestito> prestiti){
            this.prestiti=prestiti;
            return this;
        }

        public UtenteBuilder prenotazioni(ArrayList<Prenotazione> prenotazioni){
            this.prenotazioni=prenotazioni;
            return this;
        }

        public Utente build(){
            return new Utente(this);
        }


    }

    private Utente(UtenteBuilder ub){
        this.email=ub.email;
        this.password=ub.password;
        this.nome=ub.nome;
        this.cognome=ub.cognome;
        this.matricola=ub.matricola;
        this.genere=ub.genere;
        this.eta=ub.eta;
        this.admin=ub.admin;
        this.nuovo=ub.nuovo;
        this.interessi=ub.interessi;
        this.prestiti=ub.prestiti;
        this.prenotazioni=ub.prenotazioni;

    }

    public static String toJson(Utente u){
        Gson gson = new Gson();
        Type fooType = new TypeToken<Utente>() {}.getType();
        String json = gson.toJson(u,fooType);
        return json;
    }

    public static String toJson(List<Utente> utenti){
        Gson gson = new Gson();
        return gson.toJson(utenti);
    }

    /*public static Utente fromJson(String json) throws JsonSyntaxException {
        Gson gson = new Gson();
        Utente p = gson.fromJson(json,Utente.class);
        return p;
    }

    public static Utente fromJson(JsonObject json) {
        Gson gson = new Gson();
        Utente p = gson.fromJson(""+json.get("Utente"),Utente.class);
        return p;
    }*/
}
