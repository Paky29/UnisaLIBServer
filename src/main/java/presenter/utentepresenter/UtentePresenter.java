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

                UtenteDAO utenteDAO=new UtenteDAO();
                Utente u=null;
                try {
                    u = utenteDAO.doRetrieveByEmailAndPassword(e,p);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                if(u!=null) {
                    System.out.println("Login successful");
                    Gson gson=new Gson();
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("Utente",gson.toJson(u));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println(gson.toJson(u));
                    PrintWriter pw=resp.getWriter();
                    pw.write(jsonObject.toString());
                    pw.print(jsonObject.toString());
                    System.out.println("Login successful "+jsonObject.toString());
                }
                break;
            }
        }
    }
}
