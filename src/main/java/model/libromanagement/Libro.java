package model.libromanagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import model.posizionemanagement.Posizione;
import model.prestitomanagement.Prestito;
import presenter.http.presenter;

public class Libro extends presenter implements Serializable {
    private String isbn, titolo, autore, editore, urlCopertina, categoria;
    private int nCopie, annoPubbl;
    private float rating;
    private Posizione posizione;
    private ArrayList<Prestito> prestiti;

    public String getIsbn() {
        return isbn;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getAutore() {
        return autore;
    }

    public String getEditore() {
        return editore;
    }

    public String getUrlCopertina() {
        return urlCopertina;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getnCopie() {
        return nCopie;
    }

    public int getAnnoPubbl() {
        return annoPubbl;
    }

    public float getRating() {
        return rating;
    }

    public Posizione getPosizione() {
        return posizione;
    }

    public ArrayList<Prestito> getPrestiti() {
        return prestiti;
    }

    public static class LibroBuilder{
        private String isbn, titolo, autore, editore, urlCopertina, categoria;
        private int nCopie, annoPubbl;
        private float rating;
        private Posizione posizione;
        private ArrayList<Prestito> prestiti;

        /**
         * Setta il parametro nell'oggetto LibroBuilder
         * @param isbn l'identificativo dell'oggetto Libro
         * @return oggetto di tipo LibroBuilder con isbn settato
         */
        public LibroBuilder isbn(String isbn){
            this.isbn=isbn;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto LibroBuilder
         * @param titolo il titolo dell'oggetto Libro
         * @return oggetto di tipo LibroBuilder con isbn settato
         */
        public LibroBuilder titolo(String titolo){
            this.titolo=titolo;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto LibroBuilder
         * @param autore l'autore dell'oggetto libro
         * @return oggetto di tipo LibroBuilder con isbn settato
         */
        public LibroBuilder autore(String autore){
            this.autore=autore;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto LibroBuilder
         * @param editore l'editore dell'oggetto libro
         * @return oggetto di tipo LibroBuilder con isbn settato
         */
        public LibroBuilder editore(String editore){
            this.editore=editore;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto LibroBuilder
         * @param urlCopertina l'url a cui è situata la copertina
         * @return oggetto di tipo LibroBuilder con isbn settato
         */
        public LibroBuilder urlCopertina(String urlCopertina){
            this.urlCopertina=urlCopertina;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto LibroBuilder
         * @param categoria la categoria dell'oggetto Libro
         * @return oggetto di tipo LibroBuilder con isbn settato
         */
        public LibroBuilder categoria(String categoria){
            this.categoria=categoria;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto LibroBuilder
         * @param nCopie il numero delle copie dell'oggetto libro
         * @return oggetto di tipo LibroBuilder con isbn settato
         */
        public LibroBuilder nCopie(int nCopie){
            this.nCopie=nCopie;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto LibroBuilder
         * @param annoPubbl l'anno di pubblicazione dell'oggetto libro
         * @return oggetto di tipo LibroBuilder con isbn settato
         */
        public LibroBuilder annoPubbl(int annoPubbl){
            this.annoPubbl=annoPubbl;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto LibroBuilder
         * @param rating il rating dell'oggetto libro
         * @return oggetto di tipo LibroBuilder con isbn settato
         */
        public LibroBuilder rating(float rating){
            this.rating=rating;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto LibroBuilder
         * @param posizione il rating dell'oggetto libro
         * @return oggetto di tipo LibroBuilder con isbn settato
         */
        public LibroBuilder posizione(Posizione posizione){
            this.posizione=posizione;
            return this;
        }
        /**
         * Setta il parametro nell'oggetto LibroBuilder
         * @param prestiti la lista di prestiti riguardante l'oggetto libro
         * @return oggetto di tipo LibroBuilder con lista prestiti settato
         */
        public LibroBuilder prestiti(ArrayList<Prestito> prestiti){
            this.prestiti=prestiti;
            return this;
        }

        /**
         * Genera un oggetto di tipo Libro invocando il costruttore passando come argomento LibroBuilder
         * @return oggetto di tipo LibroBuilder con lista prestiti settato
         */
        public Libro build(){
            return new Libro(this);
        }


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Libro)) return false;
        Libro libro = (Libro) o;
        return  getIsbn().equals(libro.getIsbn());
    }
    /**
     * Crea un nuovo Libro settando gli opportuni parametri ottenuti dal LibroBuilder passato come argomento
     * @param lb LibroBuilder da cui si estraggono i valori precedentemente settati
     */
    private Libro(LibroBuilder lb){
        this.isbn = lb.isbn;
        this.titolo = lb.titolo;
        this.autore = lb.autore;
        this.editore = lb.editore;
        this.urlCopertina = lb.urlCopertina;
        this.categoria = lb.categoria;
        this.nCopie = lb.nCopie;
        this.annoPubbl = lb.annoPubbl;
        this.rating = lb.rating;
        this.posizione = lb.posizione;
        this.prestiti = lb.prestiti;
    }

    public static String toJsonCategorie(List<String> categorie){
        Gson gson = new Gson();
        return gson.toJson(categorie);
    }

    public static String toJson(ArrayList<Libro> libri) {
        Gson gson = new Gson();
        return gson.toJson(libri);
    }

    public static String toJsonCategoria(String c) {
        Gson gson = new Gson();
        return gson.toJson(c);
    }

    public static Libro fromJsonToLibro(String json) throws JsonSyntaxException {
        Gson gson = new Gson();
        Libro libro= gson.fromJson(json, Libro.class);
        return libro;
    }

    public static String toJson(Libro l) {
        Gson gson = new Gson();
        return gson.toJson(l);
    }
}
