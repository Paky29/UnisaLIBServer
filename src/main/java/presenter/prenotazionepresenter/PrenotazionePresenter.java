package presenter.prenotazionepresenter;

import model.libromanagement.LibroDAO;
import model.posizionemanagement.PosizioneDAO;
import model.prenotazionemanagement.Prenotazione;
import model.prenotazionemanagement.PrenotazioneDAO;
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


@WebServlet(name="prenotazionepresenter", value = "/PrenotazionePresenter/*")
public class PrenotazionePresenter extends presenter {
    private PrintWriter pw;
    private UtenteDAO utenteDAO;
    private PrenotazioneDAO prenotazioneDAO;

    public PrenotazionePresenter() {
     utenteDAO = new UtenteDAO();
     prenotazioneDAO = new PrenotazioneDAO();
    }

    public PrenotazionePresenter(PrenotazioneDAO prenotazioneDAO, UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
        this.prenotazioneDAO = prenotazioneDAO;
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
            case "/crea-prenotazione": {
                String p = req.getParameter("prenotazione");
                Prenotazione prenotazione = Prenotazione.fromJsonToPrenotazione(p);
                creaPrenotazione(prenotazione);
                break;
            }
        }
    }

    private void creaPrenotazione(Prenotazione prenotazione){
        try {
            if (prenotazioneDAO.insert(prenotazione)) {
                Utente utenteAggiornato = utenteDAO.doRetrieveByEmail(prenotazione.getUtente().getEmail());
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("utente", Utente.toJson(utenteAggiornato));
                System.out.println("Json successo");
                pw.write(jsonObject.toString());
                System.out.println("Scritto in risposta oggetto");

            } else {
                System.out.println("Errore Salvataggio");
                pw.write("Salvataggio non andato a buon fine");
            }
        } catch (Exception e) {
            pw.write("Errore nel server");
            e.printStackTrace();
        }
    }
}
