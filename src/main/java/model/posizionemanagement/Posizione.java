package model.posizionemanagement;

import java.util.ArrayList;

import com.google.gson.Gson;
import model.postazionemanagement.Postazione;

public class Posizione {
    private int id;
    private String biblioteca, zona;
    private ArrayList<Postazione> postazioni;

    public Posizione() {
    }

    public Posizione(int id, String biblioteca, String zona, ArrayList<Postazione> postazioni) {
        this.id = id;
        this.biblioteca = biblioteca;
        this.zona = zona;
        this.postazioni = postazioni;
    }

    public Posizione(int id, String biblioteca, String zona) {
        this.id = id;
        this.biblioteca = biblioteca;
        this.zona = zona;
    }

    public Posizione(String biblioteca, String zona) {
        this.biblioteca = biblioteca;
        this.zona = zona;
    }

    public static String toJson(ArrayList<Posizione> posizioni) {
        Gson gson = new Gson();
        return gson.toJson(posizioni);
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
}
