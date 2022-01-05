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
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path=getPath(req);
        switch(path){
            case "/": break;
            case "/crea-prestito": {
                System.out.println("Sono nella servlet");
                String p=req.getParameter("prestito");
                PrintWriter pw=resp.getWriter();
                PrestitoDAO prestitoDAO=new PrestitoDAO();
                UtenteDAO utenteDAO=new UtenteDAO();
                Prestito prestito=Prestito.fromJsonToPrestito(p);
                if(prestito.getLibro().getnCopie()>0) {
                    try {
                        Utente utentePrestito=prestito.getUtente();
                        ArrayList<Prestito> prestitiAttivi= utenteDAO.doRetrieveByEmailAll(utentePrestito.getEmail()).getPrestiti();
                        if (prestitiAttivi.isEmpty()) {
                            if (prestitoDAO.insert(prestito)) {
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
                            } else {
                                System.out.println("Errore Salvataggio");
                                pw.write("Salvataggio non andato a buon fine");
                            }
                        } else {
                            System.out.println("Errore libro gia in prestito");
                            pw.write("Limite prestiti ecceduto: puoi prendere in prestito solo un libro alla volta");
                        }
                    } catch (Exception ex) {
                        pw.write("Errore del server");
                        System.out.println("Errore" + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
                else{
                    System.out.println("Errore libro gia in prestito");
                    pw.write("Non ci sono copie disponibili");
                }
                break;
            }
        }
    }
}
