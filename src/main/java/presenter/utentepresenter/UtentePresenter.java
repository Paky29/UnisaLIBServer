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

@WebServlet(name="utentepresenter", value = "/UtentePresenter/*")
public class UtentePresenter extends presenter {
    private PrintWriter pw;

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
                login(e,p);
                break;
            }
        }
    }

    public void login(String email, String password){
        UtenteDAO utenteDAO=new UtenteDAO();
        Utente u=null;
        try {
            u = utenteDAO.doRetrieveByEmailAndPasswordAll(email,password);
            if(u!=null) {
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("Utente", Utente.toJson(u));
                    pw.write(jsonObject.toString());
                } catch (JSONException ex) {
                    pw.write("Errore del server");
                }
            }
            else{
                pw.write("Utente non trovato");
            }
        } catch (Exception ex) {
            pw.write("Errore del server");
        }

    }
}
