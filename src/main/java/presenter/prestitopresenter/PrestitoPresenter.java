package presenter.prestitopresenter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

@WebServlet(name="prestitopresenter", value = "/PrestitoPresenter/*")
public class PrestitoPresenter extends presenter {
    private PrintWriter pw;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = getPath(req);
        pw = resp.getWriter();
        System.out.println(path);
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
                cercaPrestitiAttiviPerLibro(isbn);
                break;
            }
            case "/crea-prestito": {
                System.out.println("Sono nella servlet");
                String p = req.getParameter("prestito");
                Prestito prestito = Prestito.fromJsonToPrestito(p);
                creaPrestito(prestito);
                break;
            }

            case "/valuta-prestito": {
                String p = req.getParameter("prestito");
                Prestito prestito = Prestito.fromJsonToPrestito(p);
                valutaPrestito(prestito);
                break;
            }

            case "/attiva-prestito": {
                String p = req.getParameter("prestito");
                Prestito prestito = Prestito.fromJsonToPrestito(p);
                attivaPrestito(prestito);
                break;
            }

            case "/concludi-prestito": {
                String p = req.getParameter("prestito");
                Prestito prestito = Prestito.fromJsonToPrestito(p);
                System.out.println("DataC prestito: " + prestito.getDataConsegna());
                concludiPrestito(prestito);
                break;
            }
        }
    }

    private void cercaPrestitiPerUtente(String email){
        PrestitoDAO prestitoDAO = new PrestitoDAO();
        try {
            ArrayList<Prestito> prestiti = prestitoDAO.doRetrieveByUtente(email);
            if (!prestiti.isEmpty()) {
                System.out.println("okkkk");
                pw.write(Prestito.toJson(prestiti));
            } else
                pw.write("Non sono presenti prestiti");
        } catch (SQLException e) {
            pw.write("Errore del server");
        }
    }

    private void cercaPrestitiAttiviPerLibro(String codiceISBN){
        PrestitoDAO prestitoDAO = new PrestitoDAO();
        System.out.println("Sono in metodo cercaPAttivi");
        try {
            ArrayList<Prestito> prestiti = prestitoDAO.doRetrieveValidByLibro(codiceISBN);
            if (!prestiti.isEmpty()) {
                System.out.println("okkkk");
                pw.write(Prestito.toJson(prestiti));
            } else
                pw.write("Non sono presenti prestiti");
        } catch (SQLException e) {
            pw.write("Errore del server");
            System.out.println("SQL Exception");
            e.printStackTrace();
        }
    }

    private void creaPrestito(Prestito prestito){
        PrestitoDAO prestitoDAO = new PrestitoDAO();
        UtenteDAO utenteDAO = new UtenteDAO();
        Utente utentePrestito = prestito.getUtente();
        try{
        Prestito prestitoAttivo = prestitoDAO.doRetrieveValidByUtente(prestito.getUtente().getEmail());
            if (prestitoAttivo==null) {
                    if (prestito.getLibro().getnCopie() > 0) {
                        if (prestitoDAO.insert(prestito)) {
                            Utente utente = utenteDAO.doRetrieveByEmailAll(prestito.getUtente().getEmail());
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("Utente", Utente.toJson(utente));
                                System.out.println("Json successo");
                            } catch (JSONException ex) {
                                System.out.println("Errore JSON");
                                pw.write("Errore del server. Riprovare più tardi");
                            }
                            pw.write(jsonObject.toString());
                            System.out.println("Scritto in risposta oggetto");
                        } else {
                            System.out.println("Errore Salvataggio");
                            pw.write("Salvataggio non andato a buon fine");
                        }
                    } else {
                        System.out.println("Errore libro gia in prestito");
                        pw.write("Non ci sono copie disponibili. Riprovare più tardi");
                    }

            } else {
                System.out.println("Errore libro gia in prestito");
                if (prestitoAttivo.getLibro().equals(prestito.getLibro()))
                    pw.write("Hai il libro in prestito. Controlla nella sezione Miei Prestiti");
                else
                    pw.write("Limite prestiti ecceduto: puoi prendere in prestito solo un libro alla volta");
            }
        } catch (Exception ex) {
            pw.write("Errore del server. Riprovare più tardi");
            System.out.println("Errore" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void valutaPrestito(Prestito prestito){
        PrestitoDAO prestitoDAO = new PrestitoDAO();
        UtenteDAO utenteDAO = new UtenteDAO();
        try {
            Utente utentePrestito = prestito.getUtente();
            if(prestitoDAO.valutaPrestito(prestito)) {
                Utente utente = utenteDAO.doRetrieveByEmailAll(prestito.getUtente().getEmail());

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Utente", Utente.toJson(utente));
                    System.out.println("Json successo");
                } catch (JSONException ex) {
                    System.out.println("Errore JSON");
                    pw.write("Errore del server");
                }
                pw.write(jsonObject.toString());
                System.out.println("Scritto in risposta oggetto");
            }
            else{
                pw.write("Errore del server");
            }
        } catch (Exception ex) {
            pw.write("Errore del server");
            System.out.println("Errore" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void attivaPrestito(Prestito prestito){
        PrestitoDAO prestitoDAO = new PrestitoDAO();
        System.out.println("Sono in attiva prestito");
        try {
            Libro libro = prestito.getLibro();
            System.out.println("Ho preso il libro");
            if(prestitoDAO.attivaPrestito(prestito)) {
                ArrayList<Prestito> prestitiLibro = prestitoDAO.doRetrieveValidByLibro(libro.getIsbn());
                System.out.println("Vuoto?" + prestitiLibro.isEmpty());
                if (!prestitiLibro.isEmpty()) {
                    System.out.println("ok");
                    for(Prestito p: prestitiLibro)
                        System.out.println("Prestito: Ut=" + p.getUtente().getEmail() + " Lib=" + p.getLibro().getIsbn());
                    pw.write(Prestito.toJson(prestitiLibro));
                }
                else
                    System.out.println("prestiti vuoto");
                    pw.write("Non sono presenti prestiti");
            }
            else{
                System.out.println("errore in database ");
                pw.write("Errore del server");
            }
        } catch (Exception ex) {
            pw.write("Errore del server");
            System.out.println("Errore" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void concludiPrestito(Prestito prestito){
        PrestitoDAO prestitoDAO = new PrestitoDAO();
        System.out.println("Sono in concludi prestito");
        try {
        Libro libro = prestito.getLibro();
        if(prestitoDAO.concludiPrestito(prestito)) {
            ArrayList<Prestito> prestitiLibro = prestitoDAO.doRetrieveValidByLibro(libro.getIsbn());
            System.out.println("Vuoto?" + prestitiLibro.isEmpty());
            if (!prestitiLibro.isEmpty()) {
                System.out.println("ok");
                for(Prestito p: prestitiLibro)
                    System.out.println("Prestito: Ut=" + p.getUtente().getEmail() + " Lib=" + p.getLibro().getIsbn());
                pw.write(Prestito.toJson(prestitiLibro));
            }
            else {
                pw.write(Prestito.toJson(new ArrayList<Prestito>()));
            }
        }
        else{
            System.out.println("errore in database ");
            pw.write("Errore del server");
        }
    } catch (Exception ex) {
        pw.write("Errore del server");
        System.out.println("Errore" + ex.getMessage());
        ex.printStackTrace();
    }
}
}
