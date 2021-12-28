package model.libromanagement;

import java.util.ArrayList;

import model.posizionemanagement.Posizione;
import model.prestitomanagement.Prestito;

public class Libro {
    private String isbn, titolo, autore, editore, urlCopertina, categoria;
    private int nCopie, annoPubbl;
    private float rating;
    private Posizione posizione;
    private ArrayList<Prestito> prestiti;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getEditore() {
        return editore;
    }

    public void setEditore(String editore) {
        this.editore = editore;
    }

    public String getUrlCopertina() {
        return urlCopertina;
    }

    public void setUrlCopertina(String urlCopertina) {
        this.urlCopertina = urlCopertina;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getnCopie() {
        return nCopie;
    }

    public void setnCopie(int nCopie) {
        this.nCopie = nCopie;
    }

    public int getAnnoPubbl() {
        return annoPubbl;
    }

    public void setAnnoPubbl(int annoPubbl) {
        this.annoPubbl = annoPubbl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Posizione getPosizione() {
        return posizione;
    }

    public void setPosizione(Posizione posizione) {
        this.posizione = posizione;
    }

    public ArrayList<Prestito> getPrestiti() {
        return prestiti;
    }

    public void setPrestiti(ArrayList<Prestito> prestiti) {
        this.prestiti = prestiti;
    }
}
