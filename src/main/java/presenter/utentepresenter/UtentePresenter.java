package presenter.utentepresenter;

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
import java.util.regex.Pattern;

@WebServlet(name="utentepresenter", value = "/UtentePresenter/*")
public class UtentePresenter extends presenter {
    private UtenteDAO utenteDAO;
    private PrintWriter pw;

    public UtentePresenter(){
        this.utenteDAO=new UtenteDAO();
    }
    /**
     * Crea un oggetto di tipo UtentePresenter con i seguenti parametri
     * @param utenteDAO il DAO che si occupa della gestione degli utenti
     */
    public UtentePresenter(UtenteDAO utenteDAO){
        this.utenteDAO=utenteDAO;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path=getPath(req);
        pw=resp.getWriter();
        switch(path){
            case "/": break;
            case "/login": {
                String e=req.getParameter("email");
                String p=req.getParameter("pass");
                if(e==null || p==null)
                    pw.write("Email o password non inserita");
                else
                    login(e,p);
                break;
            }
        }
    }
    /**
     * Controlla ed eventualmente valida lato server le credenziali inserite dall'utente lato client per il login
     * @param email email inserita lato client
     * @param password password inserita lato client
     */
    public void login(String email, String password){
        Utente u=null;
        String email_regex="[A-z0-9\\.\\+_-]+@(studenti.)*(unisa\\.it)";
        if(!Pattern.matches(email_regex, email)){
            pw.write("Email non valida");
        }
        else {
            try {
                u = utenteDAO.doRetrieveByEmailAndPasswordAll(email, password);
                if (u != null) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("Utente", Utente.toJson(u));
                        pw.write(jsonObject.toString());
                    } catch (JSONException ex) {
                        pw.write("Errore del server");
                    }
                } else {
                    pw.write("Utente non trovato");
                }
            } catch (Exception ex) {
                pw.write("Errore del server");
            }
        }

    }
}
