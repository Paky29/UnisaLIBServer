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
        String path = getPath(req);
        System.out.println(path);
        switch (path) {
            case "/mostra-ricerca-libri": {
                String admin = req.getParameter("is_admin");
                PrintWriter pw = resp.getWriter();
                LibroDAO service = new LibroDAO();
                try {
                    ArrayList<String> categorie = service.doRetrieveAllCategorie();
                    if (!categorie.isEmpty()) {
                        if (categorie.contains("Consigliati") && admin.equals("true"))
                            categorie.remove("Consigliati");
                        pw.write(Libro.toJsonCategorie(categorie));
                    } else
                        pw.write("Categorie non trovate");

                } catch (Exception e) {
                    pw.write("Errore del server");
                }
                break;
            }
            case "/ricerca-libri": {
                String ricerca = req.getParameter("ricerca");
                PrintWriter pw = resp.getWriter();
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
                break;
            }
            case "/ricerca-libri-categoria": {
                String categoria = req.getParameter("categoria");
                PrintWriter pw = resp.getWriter();
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
                break;
            }
            case "/rimuovi-interesse": {
                String emailUtente = req.getParameter("email");
                String isbnLibro = req.getParameter("isbn");
                PrintWriter pw = resp.getWriter();
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
                break;
            }
            case "/aggiungi-interesse": {
                String emailUtente=req.getParameter("email");
                String isbnLibro=req.getParameter("isbn");
                PrintWriter pw=resp.getWriter();
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
                break;
            }
        }
    }
}
