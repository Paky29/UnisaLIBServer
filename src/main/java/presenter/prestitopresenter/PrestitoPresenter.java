package presenter.prestitopresenter;

import model.libromanagement.Libro;
import model.libromanagement.LibroDAO;
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
import java.util.GregorianCalendar;
import java.util.regex.Pattern;
/**
 * Questa classe definisce le operazioni relative
 * alla gestione del prestito
 */
@WebServlet(name="prestitopresenter", value = "/PrestitoPresenter/*")
public class PrestitoPresenter extends presenter {
    private PrintWriter pw;
    private PrestitoDAO prestitoDAO;
    private UtenteDAO utenteDAO;
    private LibroDAO libroDAO;

    public PrestitoPresenter(){
        prestitoDAO=new PrestitoDAO();
        utenteDAO=new UtenteDAO();
        libroDAO=new LibroDAO();
    }

    public PrestitoPresenter(PrestitoDAO prestitoDAO, UtenteDAO utenteDAO, LibroDAO libroDAO) {
        this.prestitoDAO=prestitoDAO;
        this.utenteDAO=utenteDAO;
        this.libroDAO=libroDAO;

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = getPath(req);
        pw = resp.getWriter();
        switch (path) {
            case "/":
                break;
            case "/all-prestiti": {
                String u = req.getParameter("utente");
                cercaPrestitiPerUtente(u);
                break;
            }
            case "/lista-prestiti-libro": {
                String isbn = req.getParameter("libro");
                cercaPrestitiValidiPerLibro(isbn);
                break;
            }
            case "/crea-prestito": {
                String p = req.getParameter("prestito");
                Prestito prestito = Prestito.fromJsonToPrestito(p);
                if(prestito.getDataInizio().compareTo(new GregorianCalendar())>0)
                    pw.write("Data inizio successiva alla data corrente");
                else
                    creaPrestito(prestito);
                break;
            }

            case "/valuta-prestito": {
                String p = req.getParameter("prestito");
                Prestito prestito = Prestito.fromJsonToPrestito(p);
                int voto=prestito.getVoto();
                String commento=prestito.getCommento();
                if((voto<1 || voto>5) || (commento!=null && !Pattern.matches("^(\\w|\\s|[è,à,ò,ù,ì,À, Ò, È, Ù, Ì]|'|\\.){1,100}$", commento)))
                    pw.write("Voto o commento non rispettano il formato");
                else
                    valutaPrestito(prestito);
                break;
            }

            case "/attiva-prestito": {
                String p = req.getParameter("prestito");
                Prestito prestito = Prestito.fromJsonToPrestito(p);
                GregorianCalendar dataInizio=prestito.getDataInizio();
                GregorianCalendar dataFineAttivazione= (GregorianCalendar) dataInizio.clone();
                dataFineAttivazione.add(GregorianCalendar.DATE, +7);
                if(dataFineAttivazione.compareTo(new GregorianCalendar())<0)
                    pw.write("Prestito non attivabile. Settimana scaduta.");
                else
                    attivaPrestito(prestito);
                break;
            }

            case "/concludi-prestito": {
                String p = req.getParameter("prestito");
                Prestito prestito = Prestito.fromJsonToPrestito(p);
                GregorianCalendar dataConsegna=prestito.getDataConsegna();
                GregorianCalendar dataCorrente=new GregorianCalendar();
                if(dataConsegna.compareTo(dataCorrente)>0)
                    pw.write("Data consegna non valida");
                else
                    concludiPrestito(prestito);
                break;
            }
        }
    }

    private void cercaPrestitiPerUtente(String email){
        try {
            ArrayList<Prestito> prestiti = prestitoDAO.doRetrieveByUtente(email);
            if (!prestiti.isEmpty()) {
                pw.write(Prestito.toJson(prestiti));
            } else
                pw.write("Non sono presenti prestiti");
        } catch (SQLException e) {
            pw.write("Errore del server");
        }
    }

    private void cercaPrestitiValidiPerLibro(String codiceISBN){
        try {
            ArrayList<Prestito> prestiti = prestitoDAO.doRetrieveValidByLibro(codiceISBN);
            if (!prestiti.isEmpty()) {
                pw.write(Prestito.toJson(prestiti));
            } else
                pw.write("Non sono presenti prestiti");
        } catch (SQLException e) {
            pw.write("Errore del server");
            e.printStackTrace();
        }
    }

    private void creaPrestito(Prestito prestito){
        try{
            Utente utentePrestito = utenteDAO.doRetrieveByEmail(prestito.getUtente().getEmail());
            Libro libroPrestito = libroDAO.doRetrieveByCodiceISBN(prestito.getLibro().getIsbn());

            if(utentePrestito==null || libroPrestito==null)
                pw.write("Utente o libro non trovato");
            else{
                Prestito prestitoAttivo = prestitoDAO.doRetrieveValidByUtente(prestito.getUtente().getEmail());
                if (prestitoAttivo==null) {
                        if (libroPrestito.getnCopie() > 0) {
                            if (prestitoDAO.insert(prestito)) {
                                Utente utente = utenteDAO.doRetrieveByEmailAll(prestito.getUtente().getEmail());
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("Utente", Utente.toJson(utente));
                                } catch (JSONException ex) {
                                    pw.write("Errore del server. Riprovare più tardi");
                                }
                                pw.write(jsonObject.toString());
                            } else {
                                pw.write("Salvataggio non andato a buon fine");
                            }
                        } else {
                            pw.write("Non ci sono copie disponibili. Riprovare più tardi");
                        }

                } else {
                    if (prestitoAttivo.getLibro().equals(prestito.getLibro()))
                        pw.write("Hai il libro in prestito. Controlla nella sezione Miei Prestiti");
                    else
                        pw.write("Limite prestiti ecceduto: puoi prendere in prestito solo un libro alla volta");
                }
            }
        } catch (Exception ex) {
            pw.write("Errore del server. Riprovare più tardi");
            ex.printStackTrace();
        }

    }

    public void valutaPrestito(Prestito prestito){
        try {
            if(prestitoDAO.doRetrieveByKey(prestito.getDataInizio(), prestito.getLibro().getIsbn(), prestito.getUtente().getEmail())==null)
                pw.write("Prestito non trovato");
            else {
                if (prestitoDAO.valutaPrestito(prestito)) {
                    Utente utente = utenteDAO.doRetrieveByEmailAll(prestito.getUtente().getEmail());
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("Utente", Utente.toJson(utente));
                    } catch (JSONException ex) {
                        pw.write("Errore del server");
                    }
                    pw.write(jsonObject.toString());
                } else {
                    pw.write("Errore del server");
                }
            }
        } catch (Exception ex) {
            pw.write("Errore del server");
            ex.printStackTrace();
        }
    }

    private void attivaPrestito(Prestito prestito){
        try {
            Libro libro = prestito.getLibro();
            Prestito prestitoExist=prestitoDAO.doRetrieveByKey(prestito.getDataInizio(), prestito.getLibro().getIsbn(), prestito.getUtente().getEmail());
            if(prestitoExist==null || prestitoExist.isAttivo())
                pw.write("Prestito attivo o non trovato");
            else {
                if (prestitoDAO.attivaPrestito(prestito)) {
                    ArrayList<Prestito> prestitiLibro = prestitoDAO.doRetrieveValidByLibro(libro.getIsbn());
                    if (!prestitiLibro.isEmpty()) {
                        pw.write(Prestito.toJson(prestitiLibro));
                    } else {
                        pw.write("Non sono presenti prestiti");
                    }
                } else {
                    pw.write("Errore del server");
                }
            }
        } catch (Exception ex) {
            pw.write("Errore del server");
            ex.printStackTrace();
        }
    }

    private void concludiPrestito(Prestito prestito){
        try {
        Libro libro = prestito.getLibro();
        Prestito prestitoExist=prestitoDAO.doRetrieveByKey(prestito.getDataInizio(), prestito.getLibro().getIsbn(), prestito.getUtente().getEmail());
        if(prestitoExist==null || prestitoExist.getDataConsegna()!=null || !prestitoExist.isAttivo())
            pw.write("Prestito concluso o non trovato");
        else {
            if (prestitoDAO.concludiPrestito(prestito)) {
                ArrayList<Prestito> prestitiLibro = prestitoDAO.doRetrieveValidByLibro(libro.getIsbn());
                if (!prestitiLibro.isEmpty()) {
                    pw.write(Prestito.toJson(prestitiLibro));
                } else {
                    pw.write(Prestito.toJson(new ArrayList<Prestito>()));
                }
            } else {
                pw.write("Errore del server");
            }
        }
    } catch (Exception ex) {
        pw.write("Errore del server");
        ex.printStackTrace();
    }
}
}
