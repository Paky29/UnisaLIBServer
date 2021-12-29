package presenter.utentepresenter;

import com.google.gson.Gson;
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
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path=getPath(req);
        switch(path){
            case "/": break;
            case "/login": {
                String e=req.getParameter("email");
                String p=req.getParameter("pass");
                PrintWriter pw=resp.getWriter();
                UtenteDAO utenteDAO=new UtenteDAO();
                Utente u=null;
                try {
                    u = utenteDAO.doRetrieveByEmailAndPassword(e,p);
                    if(u!=null) {
                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("Utente", Utente.toJson(u));
                        } catch (JSONException ex) {
                            pw.write("Errore del server");
                        }
                        pw.write(jsonObject.toString());
                    }
                    else{
                        pw.write("Utente non trovato");
                    }
                } catch (SQLException ex) {
                    pw.write("Errore del server");
                }
                break;
            }
        }
    }
}
