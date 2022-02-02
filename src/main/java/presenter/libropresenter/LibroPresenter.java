package presenter.libropresenter;

import model.libromanagement.Libro;
import model.libromanagement.LibroDAO;
import model.posizionemanagement.Posizione;
import model.posizionemanagement.PosizioneDAO;
import model.utentemanagement.Utente;
import model.utentemanagement.UtenteDAO;
import org.json.JSONArray;
import org.json.JSONObject;
import presenter.http.presenter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name="libropresenter", value = "/LibroPresenter/*")
public class LibroPresenter extends presenter {
    private PrintWriter pw;
    private LibroDAO libroDAO;
    private UtenteDAO utenteDAO;
    private PosizioneDAO posizioneDAO;

    public LibroPresenter() {
        libroDAO=new LibroDAO();
        utenteDAO=new UtenteDAO();
        posizioneDAO=new PosizioneDAO();
    }

    public LibroPresenter(LibroDAO libroDAO, UtenteDAO utenteDAO, PosizioneDAO posizioneDAO) {
        this.libroDAO = libroDAO;
        this.utenteDAO = utenteDAO;
        this.posizioneDAO = posizioneDAO;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = getPath(req);
        pw=resp.getWriter();
        System.out.println(path);
        switch (path) {
            case "/mostra-ricerca-libri": {
                String ad = req.getParameter("is_admin");
                if(ad!=null){
                    boolean admin = Boolean.parseBoolean(ad);
                    mostraRicercaLibri(admin);
                }
                else
                    pw.write("Errore nella richiesta");
                break;
            }
            case "/ricerca-libri": {
                String ricerca = req.getParameter("ricerca");
                if(ricerca!=null)
                    ricercaLibri(ricerca);
                else
                    pw.write("Errore nella richiesta");
                break;
            }
            case "/ricerca-libri-categoria": {
                String categoria = req.getParameter("categoria");
                if(categoria!=null)
                    ricercaLibriCategoria(categoria);
                else
                    pw.write("Errore nella richiesta");
                break;
            }
            case "/rimuovi-interesse": {
                String emailUtente = req.getParameter("email");
                String isbnLibro = req.getParameter("isbn");
                if(emailUtente!=null && isbnLibro!=null)
                    rimuoviLibroFromInteressi(isbnLibro, emailUtente);
                else
                    pw.write("Errore nella richiesta");
                break;
            }
            case "/aggiungi-interesse": {
                String emailUtente=req.getParameter("email");
                String isbnLibro=req.getParameter("isbn");
                if(emailUtente!=null && isbnLibro!=null)
                    aggiungiLibroToInteressi(isbnLibro, emailUtente);
                else
                    pw.write("Errore nella richiesta");
                break;
            }
            case "/informazioni-aggiunta":
            case "/dettagli-libro-admin": {
                informazioniLibro();
                break;
            }
            case "/crea-libro": {
                String l = req.getParameter("libro");
                if(l!=null) {
                    Libro libro = Libro.fromJsonToLibro(l);
                    if(libro.getIsbn()!=null)
                        creaLibro(libro);
                    else
                        pw.write("Libro inviato non corretto");
                }
                else
                    pw.write("Errore nella richiesta");
                break;
            }
        }
    }

    private void mostraRicercaLibri(boolean admin){
        try {
            ArrayList<String> categorie = libroDAO.doRetrieveAllCategorie();
            if (!categorie.isEmpty()) {
                if ((categorie.contains("Consigliati") || categorie.contains("consigliati")) && admin==true){
                    categorie.remove("Consigliati");
                    categorie.remove("consigliati");
                }
                pw.write(Libro.toJsonCategorie(categorie));
            } else
                pw.write("Categorie non trovate");
        } catch (Exception e) {
            pw.write("Errore del server");
        }
    }

    private void ricercaLibri(String ricerca){
        try {
            ArrayList<Libro> libri = libroDAO.doRetrieveByTitoloAutore(ricerca);
            if (!libri.isEmpty()) {
                System.out.println(Libro.toJson(libri));
                pw.write(Libro.toJson(libri));
            } else
                pw.write("Nessun libro trovato");
        } catch (Exception e) {
            pw.write("Errore del server");
        }
    }

    private void ricercaLibriCategoria(String categoria){
        try {
            ArrayList<Libro> libri = libroDAO.doRetrieveByCategoria(categoria);
            System.out.println(categoria);
            if (!libri.isEmpty()) {
                pw.write(Libro.toJson(libri));
            } else
                pw.write("Nessun libro trovato");
        } catch (Exception e) {
            pw.write("Errore del server");
        }
    }

    private void rimuoviLibroFromInteressi(String isbn, String email){
        try {
            Libro l = libroDAO.doRetrieveByCodiceISBN(isbn);
            Utente u = utenteDAO.doRetrieveByEmailAll(email);
            if(u!=null && l!=null) {
                if (u.getInteressi().isEmpty()) {
                    pw.write("Nessun libro in interessi");
                }
                else {
                    if (!u.getInteressi().contains(l))
                        pw.write("Libro non presente in interessi");
                    else {
                        if (libroDAO.doDeleteInteresse(email, isbn)) {
                            u.getInteressi().remove(l);
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("Utente", Utente.toJson(u));
                            pw.write(jsonObject.toString());
                        } else
                            pw.write("Rimozione fallita, riprova.");
                    }
                }
            }
            else
                pw.write("Richiesta non corretta");
        } catch (Exception e) {
            pw.write("Errore del server");
        }
    }

    private void aggiungiLibroToInteressi(String isbnLibro, String emailUtente){
        try {
            Libro l = libroDAO.doRetrieveByCodiceISBN(isbnLibro);
            Utente u = utenteDAO.doRetrieveByEmailAll(emailUtente);
            if(u!=null && l!=null) {
                if (libroDAO.doAddInteresse(emailUtente, isbnLibro)) {
                    u.getInteressi().add(l);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Utente", Utente.toJson(u));
                    pw.write(jsonObject.toString());
                } else
                    pw.write("Aggiunta fallita, riprova.");
            }
            else
                pw.write("Richiesta non corretta");
        } catch (Exception e) {
            pw.write("Errore del server");
        }
    }

    private void informazioniLibro(){
        try {
            ArrayList<String> categorie=libroDAO.doRetrieveAllCategorie();
            if (categorie.contains("Consigliati"))
                categorie.remove("Consigliati");
            ArrayList<Posizione> posizioni=posizioneDAO.doRetrieveAll();
            if((!categorie.isEmpty()) && (!posizioni.isEmpty())) {
                JSONArray cat = new JSONArray();
                for (String c: categorie) {
                    JSONObject obj=new JSONObject();
                    obj.put("categoria",Libro.toJsonCategoria(c));
                    cat.put(obj);
                }
                JSONArray pos= new JSONArray();
                for(Posizione p:posizioni){
                    JSONObject obj=new JSONObject();
                    obj.put("posizione",Posizione.toJson(p));
                    pos.put(obj);
                }
                JSONObject response= new JSONObject();
                response.put("categorie", cat);
                response.put("posizioni", pos);
                pw.write(response.toString());
            }
            else
                pw.write("Informazioni non trovate");
        } catch (Exception e) {
            pw.write("Errore del server");
        }
    }

    private void creaLibro(Libro libro){
        if(LibroValidator.validate(libro)){
            try{
                Libro l=libroDAO.doRetrieveByCodiceISBN(libro.getIsbn());
                if(l!=null)
                    pw.write("ISBN libro gia' presente");
                else {
                    Posizione p=posizioneDAO.doRetrieveByBibliotecaZona(libro.getPosizione().getBiblioteca(),libro.getPosizione().getZona());
                    if (p != null && libroDAO.existCategoria(libro.getCategoria())) {
                        if (libroDAO.insert(libro)) {
                            pw.write("Salvataggio avvenuto con successo");
                        } else {
                            pw.write("Salvataggio non andato a buon fine");
                        }
                    } else
                        pw.write("Posizione o categoria inserite non corrette");
                }
            } catch (SQLException e) {
                pw.write("Errore nel server");
            }
        }
        else
            pw.write("Libro non valido");
    }
}
