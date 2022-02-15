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
/**
 * Questa classe definisce un Utente. Un utente è identificato da un email, ha una password,
 * un nome, un cognome, una matricola, genere ed età.
 * Inoltre ci sono due valori che specificano se l'utente è un amministratore o è un nuovo utente,
 * ha una lista di interessi, una lista di prestiti e una lista di prenotazioni
 */
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
    /**
     * Questa classe definisce un UtenteBuilder. Un UtenteBuilder ha un'email, una password, un nome,
     * un cognome, una matricola, il genere, l'età, un valore che indica se l'utente è un admin o un nuovo utente,
     * una lista di interessi, una lista di prestiti e una lista di prenotazioni.
     * Settato questi valori ritorna un oggetto di tipo Utente
     */
    public static class UtenteBuilder{
        private String email, password, nome, cognome, matricola, genere;
        private int eta;
        private boolean admin, nuovo;
        private ArrayList<Libro> interessi=new ArrayList<>();
        private ArrayList<Prestito> prestiti=new ArrayList<>();
        private ArrayList<Prenotazione> prenotazioni=new ArrayList<>();
        /**
         * Setta il parametro nell'oggetto UtenteBuilder
         * @param email l'identificativo dell'oggetto Utente
         * @return oggetto di tipo UtenteBuilder con email settata
         */
        public UtenteBuilder email(String email){
            this.email=email;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto UtenteBuilder
         * @param password stringa contenente la password d'accesso dell'utente
         * @return oggetto di tipo UtenteBuilder con password settata
         */
        public UtenteBuilder password(String password){
            this.password=password;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto UtenteBuilder
         * @param nome stringa contenente il nome dell'Utente
         * @return oggetto di tipo UtenteBuilder con nome settato
         */
        public UtenteBuilder nome(String nome){
            this.nome=nome;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto UtenteBuilder
         * @param cognome stringa contenente il cognome dell'Utente
         * @return oggetto di tipo UtenteBuilder con cognome settato
         */
        public UtenteBuilder cognome(String cognome){
            this.cognome=cognome;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto UtenteBuilder
         * @param matricola stringa contenente la matricola dell'Utente
         * @return oggetto di tipo UtenteBuilder con matricola settata
         */
        public UtenteBuilder matricola(String matricola){
            this.matricola=matricola;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto UtenteBuilder
         * @param genere stringa contenente il genere dell'Utente
         * @return oggetto di tipo UtenteBuilder con genere settato
         */
        public UtenteBuilder genere(String genere){
            this.genere=genere;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto UtenteBuilder
         * @param eta stringa contenente l'età dell'Utente
         * @return oggetto di tipo UtenteBuilder con eta settata
         */
        public UtenteBuilder eta(int eta){
            this.eta=eta;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto UtenteBuilder
         * @param admin boolean che indica se l'utente è un admin o meno
         * @return oggetto di tipo UtenteBuilder con admin settato
         */
        public UtenteBuilder admin(boolean admin){
            this.admin=admin;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto UtenteBuilder
         * @param nuovo boolean che indica se l'utente è nuovo
         * @return oggetto di tipo UtenteBuilder con nuovo settato
         */
        public UtenteBuilder nuovo(boolean nuovo){
            this.nuovo=nuovo;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto UtenteBuilder
         * @param interessi lista contenente gli interessi
         * @return oggetto di tipo UtenteBuilder con lista di interessi settata
         */
        public UtenteBuilder interessi(ArrayList<Libro> interessi){
            this.interessi=interessi;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto UtenteBuilder
         * @param prestiti lista contenente i prestiti
         * @return oggetto di tipo UtenteBuilder con lista di prestiti settato
         */
        public UtenteBuilder prestiti(ArrayList<Prestito> prestiti){
            this.prestiti=prestiti;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto UtenteBuilder
         * @param prenotazioni lista contenente le prenotazioni
         * @return oggetto di tipo UtenteBuilder con lista di prenotazioni settata
         */
        public UtenteBuilder prenotazioni(ArrayList<Prenotazione> prenotazioni){
            this.prenotazioni=prenotazioni;
            return this;
        }
        /**
         * Genera un oggetto di tipo Utente invocando il costruttore passando come argomento LibroBuilder
         * @return oggetto di tipo Libro
         */
        public Utente build(){
            return new Utente(this);
        }


    }
    /**
     * Crea un nuovo Utente settando gli opportuni parametri ottenuti dall'UtenteBuilder passato come argomento
     * @param ub UtenteBuilder da cui si estraggono i valori precedentemente settati
     */
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
