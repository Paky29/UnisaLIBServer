package presenter.prenotazionepresenter;

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
                creaPrenotazione(p);
                break;
            }
        }
    }

    private void creaPrenotazione(String p){
        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
        UtenteDAO utenteDAO = new UtenteDAO();
        Prenotazione prenotazione = Prenotazione.fromJsonToPrenotazione(p);
        try {
            if (prenotazioneDAO.insert(prenotazione)) {
                Utente utenteAggiornato = utenteDAO.doRetrieveByEmail(prenotazione.getUtente().getEmail());
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("utente", Utente.toJson(utenteAggiornato));
                    System.out.println("Json successo");
                    pw.write(jsonObject.toString());
                    System.out.println("Scritto in risposta oggetto");
                } catch (JSONException ex) {
                    System.out.println("Errore JSON");
                    pw.write("Errore del server");
                }
            } else {
                System.out.println("Errore Salvataggio");
                pw.write("Salvataggio non andato a buon fine");
            }
        } catch (SQLException e) {
            pw.write("Errore nel server");
            e.printStackTrace();
        }
    }
}
