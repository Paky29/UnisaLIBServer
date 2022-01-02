package presenter.libropresenter;

import model.libromanagement.Libro;
import model.libromanagement.LibroDAO;
import model.prenotazionemanagement.Prenotazione;
import model.prenotazionemanagement.PrenotazioneDAO;
import model.prestitomanagement.Prestito;
import model.prestitomanagement.PrestitoDAO;
import model.utentemanagement.Utente;
import model.utentemanagement.UtenteDAO;
import org.json.JSONException;
import org.json.JSONObject;
import presenter.http.presenter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name="libropresenter", value = "/LibroPresenter/*")
public class LibroPresenter extends presenter {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String path=getPath(req);
    switch (path){
            case "/mostra-ricerca-libri":{
                String admin=req.getParameter("is_admin");
                PrintWriter pw=resp.getWriter();
                LibroDAO service=new LibroDAO();
                try {
                    ArrayList<String> categorie=service.doRetrieveAllCategorie();
                    if(!categorie.isEmpty()) {
                        if (categorie.contains("Consigliati") && admin.equals("true"))
                            categorie.remove("Consigliati");
                        pw.write(Libro.toJsonCategorie(categorie));
                    }
                    else
                        pw.write("Categorie non trovate");

                } catch (Exception e) {
                    pw.write("Errore del server");
                }
                break;
            }
            case "/ricerca-libri":{
                String ricerca=req.getParameter("ricerca");
                PrintWriter pw=resp.getWriter();
                LibroDAO service=new LibroDAO();
                try {
                    ArrayList<Libro> libri=service.doRetrieveByTitoloAutore(ricerca);
                    if(!libri.isEmpty()){
                        pw.write(Libro.toJson(libri));
                    }
                    else
                        pw.write("Nessun libro trovato");
                } catch (Exception e) {
                    pw.write("Errore del server");
                }
                break;
            }
        case "/ricerca-libri-categoria":{
            String categoria=req.getParameter("categoria");
            PrintWriter pw=resp.getWriter();
            LibroDAO service=new LibroDAO();
            try {
                ArrayList<Libro> libri=service.doRetrieveByCategoria(categoria);
                if(!libri.isEmpty()){
                    pw.write(Libro.toJson(libri));
                }
                else
                    pw.write("Nessun libro trovato");
            } catch (Exception e) {
                pw.write("Errore del server");
            }
            break;
        }
        case "/rimuovi-interesse":{
            String emailUtente=req.getParameter("email");
            String isbnLibro=req.getParameter("isbn");
            PrintWriter pw=resp.getWriter();
            PrenotazioneDAO prenotazioneDAO=new PrenotazioneDAO();
            PrestitoDAO prestitoDAO=new PrestitoDAO();
            UtenteDAO utenteDAO=new UtenteDAO();
            LibroDAO libroDAO=new LibroDAO();
            ArrayList<Prestito> prestiti;
            ArrayList<Prenotazione> prenotazioni;
            ArrayList<Libro> interesse;
            try {
                Libro l = libroDAO.doRetrieveByCodiceISBN(isbnLibro);
                ArrayList<Libro> interessi = libroDAO.doRetrieveInteresse(emailUtente);
                if(interessi.isEmpty()){
                    pw.write("Nessun libro in interessi");

                }
                if(!interessi.contains(l))
                    pw.write("Libro non presente in interesse");

                if(libroDAO.doDeleteInteresse(emailUtente, isbnLibro)) {
                    Utente u = utenteDAO.doRetrieveByEmail(emailUtente);
                    prenotazioni = prenotazioneDAO.doRetrieveByUtente(u);
                    prestiti = prestitoDAO.doRetrieveByUtente(u);
                    interesse = libroDAO.doRetrieveInteresse(u.getEmail());
                    u.getInteressi().addAll(interesse);
                    u.getPrestiti().addAll(prestiti);
                    u.getPrenotazioni().addAll(prenotazioni);
                    pw.write(Utente.toJson(u));
                }
                else
                    pw.write("Rimozione fallita, riprova.");

            } catch (Exception e) {
                pw.write("Errore del server");
            }

            break;
        }
        }
    }
}
