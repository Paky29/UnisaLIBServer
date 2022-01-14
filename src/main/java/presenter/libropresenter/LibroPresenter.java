package presenter.libropresenter;

import model.libromanagement.Libro;
import model.libromanagement.LibroDAO;
import model.posizionemanagement.Posizione;
import model.posizionemanagement.PosizioneDAO;
import model.postazionemanagement.Postazione;
import model.prenotazionemanagement.Prenotazione;
import model.prenotazionemanagement.PrenotazioneDAO;
import model.prestitomanagement.Prestito;
import model.prestitomanagement.PrestitoDAO;
import model.utentemanagement.Utente;
import model.utentemanagement.UtenteDAO;
import org.json.JSONArray;
import org.json.JSONException;
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
                boolean admin = Boolean.parseBoolean(ad);
                mostraRicercaLibri(admin);
                break;
            }
            case "/ricerca-libri": {
                String ricerca = req.getParameter("ricerca");
                ricercaLibri(ricerca);
                break;
            }
            case "/ricerca-libri-categoria": {
                String categoria = req.getParameter("categoria");
                ricercaLibriCategoria(categoria);
                break;
            }
            case "/rimuovi-interesse": {
                String emailUtente = req.getParameter("email");
                String isbnLibro = req.getParameter("isbn");
                rimuoviLibroFromInteressi(isbnLibro, emailUtente);
                break;
            }
            case "/aggiungi-interesse": {
                String emailUtente=req.getParameter("email");
                String isbnLibro=req.getParameter("isbn");
                aggiungiLibroFromInteressi(isbnLibro, emailUtente);
                break;
            }
            case "/informazioni-aggiunta":{
                informazioniAggiuntaLibro();
                break;
            }
            case "/dettagli-libro-admin":{
                mostraDettagliLibro();
                break;
            }
            case "/crea-libro": {
                String l = req.getParameter("libro");
                Libro libro = Libro.fromJsonToLibro(l);
                creaLibro(libro);
                break;
            }
        }
    }

    private void mostraRicercaLibri(boolean admin){
        LibroDAO service = new LibroDAO();
        try {
            ArrayList<String> categorie = service.doRetrieveAllCategorie();
            if (!categorie.isEmpty()) {
                if (categorie.contains("Consigliati") && admin==true)
                    categorie.remove("Consigliati");
                pw.write(Libro.toJsonCategorie(categorie));
            } else
                pw.write("Categorie non trovate");

        } catch (Exception e) {
            pw.write("Errore del server");
        }
    }

    private void ricercaLibri(String ricerca){
        LibroDAO service = new LibroDAO();
        try {
            ArrayList<Libro> libri = service.doRetrieveByTitoloAutore(ricerca);
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
        LibroDAO service = new LibroDAO();
        try {
            ArrayList<Libro> libri = service.doRetrieveByCategoria(categoria);
            System.out.println(categoria);
            if (!libri.isEmpty()) {
                pw.write(Libro.toJson(libri));
            } else
                pw.write("Nessun libro trovato");
        } catch (Exception e) {
            pw.write("Errore del server");
        }
    }

    private void rimuoviLibroFromInteressi(String isbnLibro, String emailUtente){
        UtenteDAO utenteDAO = new UtenteDAO();
        LibroDAO libroDAO = new LibroDAO();
        try {
            Libro l = libroDAO.doRetrieveByCodiceISBN(isbnLibro);
            Utente u = utenteDAO.doRetrieveByEmailAll(emailUtente);
            if (u.getInteressi().isEmpty()) {
                pw.write("Nessun libro in interessi");
            }
            if (!u.getInteressi().contains(l))
                pw.write("Libro non presente in interesse");

            if (libroDAO.doDeleteInteresse(emailUtente, isbnLibro)) {
                u.getInteressi().remove(l);
                JSONObject jsonObject = new JSONObject();
                try {
                    System.out.println("sono dentro");
                    jsonObject.put("Utente", Utente.toJson(u));
                } catch (JSONException ex) {
                    pw.write("Errore del server");
                }
                pw.write(jsonObject.toString());
            } else
                pw.write("Rimozione fallita, riprova.");

        } catch (Exception e) {
            pw.write("Errore del server");
        }
    }

    private void aggiungiLibroFromInteressi(String isbnLibro, String emailUtente){
        UtenteDAO utenteDAO=new UtenteDAO();
        LibroDAO libroDAO=new LibroDAO();
        try {
            if(libroDAO.doAddInteresse(emailUtente, isbnLibro)) {
                Utente u = utenteDAO.doRetrieveByEmailAll(emailUtente);
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("Utente", Utente.toJson(u));
                } catch (JSONException ex) {
                    pw.write("Errore del server");
                }
                pw.write(jsonObject.toString());
            }
            else
                pw.write("Rimozione fallita, riprova.");

        } catch (Exception e) {
            pw.write("Errore del server");
        }
    }

    private void informazioniAggiuntaLibro(){
        LibroDAO libroDAO=new LibroDAO();
        PosizioneDAO posizioneDAO=new PosizioneDAO();
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
        LibroDAO libroDAO = new LibroDAO();
        try{
            if (libroDAO.insert(libro)){
                pw.write("Salvataggio avvenuto con successo");
            } else {
                System.out.println("Errore Salvataggio");
                pw.write("Salvataggio non andato a buon fine");
            }
        } catch (SQLException e) {
            pw.write("Errore nel server");
            e.printStackTrace();
        }
    }

    private void mostraDettagliLibro(){
        LibroDAO libroDAO=new LibroDAO();
        PosizioneDAO posizioneDAO=new PosizioneDAO();
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
}
