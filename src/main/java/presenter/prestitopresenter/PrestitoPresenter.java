package presenter.prestitopresenter;

import com.google.gson.Gson;
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
        }
    }

    public void cercaPrestitiPerUtente(String email){
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

    public void creaPrestito(Prestito prestito){
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
}
