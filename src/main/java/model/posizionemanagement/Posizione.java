package model.posizionemanagement;

import java.util.ArrayList;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import model.postazionemanagement.Postazione;

public class Posizione {
    private int id;
    private String biblioteca, zona;
    private ArrayList<Postazione> postazioni;
    /**
     * Crea una nuova Posizione
     */
    public Posizione() {
        this.postazioni=new ArrayList<>();
    }
    /**
     * Crea una nuova Posizione settando gli opportuni parametri
     * @param id l'identificativo della posizione
     * @param biblioteca il nome della biblioteca della posizione
     * @param zona la zona della posizione
     * @param postazioni postazioni di quella posizione
     */
    public Posizione(int id, String biblioteca, String zona, ArrayList<Postazione> postazioni) {
        this.id = id;
        this.biblioteca = biblioteca;
        this.zona = zona;
        this.postazioni = postazioni;
    }
    /**
     * Crea una nuova Posizione settando gli opportuni parametri
     * @param id l'identificativo della posizione
     * @param biblioteca il nome della biblioteca della posizione
     * @param zona la zona della posizione
     */
    public Posizione(int id, String biblioteca, String zona) {
        this.id = id;
        this.biblioteca = biblioteca;
        this.zona = zona;
        this.postazioni=new ArrayList<>();
    }
    /**
     * Crea una nuova Posizione settando gli opportuni parametri
     * @param biblioteca il nome della biblioteca della posizione
     * @param zona la zona della posizione
     */
    public Posizione(String biblioteca, String zona) {
        this.biblioteca = biblioteca;
        this.zona = zona;
    }

    public static String toJson(ArrayList<Posizione> posizioni) {
        Gson gson = new Gson();
        return gson.toJson(posizioni);
    }

    public static String toJson(Posizione p) {
        Gson gson = new Gson();
        return gson.toJson(p);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBiblioteca() {
        return biblioteca;
    }

    public void setBiblioteca(String biblioteca) {
        this.biblioteca = biblioteca;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public ArrayList<Postazione> getPostazioni() {
        return postazioni;
    }

    public void setPostazioni(ArrayList<Postazione> postazioni) {
        this.postazioni = postazioni;
    }

    public static Posizione fromJson(String json) throws JsonSyntaxException {
        Gson gson = new Gson();
        Posizione libro= gson.fromJson(json, Posizione.class);
        return libro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posizione posizione = (Posizione) o;
        return biblioteca.equals(posizione.biblioteca) && zona.equals(posizione.zona);
    }
}
