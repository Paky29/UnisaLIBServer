package model.posizionemanagement;

import java.util.ArrayList;

import model.postazionimanagement.Postazione;

public class Posizione {
    private String id;
    private String biblioteca, zona;
    private ArrayList<Postazione> postazioni;

    public Posizione(String id, String biblioteca, String zona, ArrayList<Postazione> postazioni) {
        this.id = id;
        this.biblioteca = biblioteca;
        this.zona = zona;
        this.postazioni = postazioni;
    }

    public Posizione(String id, String biblioteca, String zona) {
        this.id = id;
        this.biblioteca = biblioteca;
        this.zona = zona;
    }

    public Posizione(String biblioteca, String zona) {
        this.biblioteca = biblioteca;
        this.zona = zona;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
