package model.prenotazionemanagement;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.posizionemanagement.Posizione;
import model.postazionemanagement.Postazione;
import model.prestitomanagement.Prestito;
import model.utentemanagement.Utente;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Prenotazione {
    private GregorianCalendar data;
    private int oraInizio, oraFine;
    private Utente utente;
    private Postazione postazione;

    public Prenotazione(GregorianCalendar data, int oraInizio, int oraFine, Utente utente, Postazione postazione) {
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.utente = utente;
        this.postazione = postazione;
    }

    public Prenotazione() {
    }

    public static Prenotazione fromJsonToPrenotazione(String json) {
        Gson gson = new Gson();
        Prenotazione p= gson.fromJson(json, Prenotazione.class);
        return p;
    }

    public GregorianCalendar getData() {
        return data;
    }

    public void setData(GregorianCalendar data) {
        this.data = data;
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

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Postazione getPostazione() {
        return postazione;
    }

    public void setPostazione(Postazione postazione) {
        this.postazione = postazione;
    }

    public static String toJson(List<Prenotazione> prenotazioni){
        Gson gson = new Gson();
        return gson.toJson(prenotazioni);
    }

    public static String toJson(Prenotazione p){
        Gson gson = new Gson();
        Type fooType = new TypeToken<Prenotazione>() {}.getType();
        String json = gson.toJson(p,fooType);
        return json;
    }

    public static Prenotazione[] fromJson(JSONArray response) throws JSONException {
        ArrayList<Prenotazione> p=new ArrayList<>();
        for(int i=0;i<response.length();++i)
            p.add(Prenotazione.fromJson(response.getJSONObject(i)));
        Prenotazione[] array = new Prenotazione[p.size()];
        array = p.toArray(array);
        return array;
    }

    public static Prenotazione fromJson(JSONObject json) throws JSONException {
        Gson gson = new Gson();
        Prenotazione p = gson.fromJson(""+json.get("prenotazione"),Prenotazione.class);
        return p;
    }

    public static ArrayList<Prenotazione> fromJson(String json) {
        Gson gson = new Gson();
        ArrayList<Prenotazione> prenotazioni= new ArrayList<>(Arrays.asList(gson.fromJson(json,Prenotazione[].class)));
        return prenotazioni;
    }
}


