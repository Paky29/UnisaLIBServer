package presenter.libropresenter;

import model.libromanagement.Libro;
import model.libromanagement.LibroDAO;
import model.utentemanagement.Utente;
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

@WebServlet(name="libropresenter", value = "/LibroPresenter/*")
public class LibroPresenter extends presenter {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String path=getPath(req);
    switch (path){
            case "/mostra-ricerca-libri":{
                String admin=req.getParameter("is_admin");
                System.out.println(admin);
                PrintWriter pw=resp.getWriter();
                LibroDAO service=new LibroDAO();
                try {
                    ArrayList<String> categorie=service.doRetrieveAllCategorie();
                    if(!categorie.isEmpty()) {
                        if (categorie.contains("Consigliati") && admin.equals("true"))
                            categorie.remove("Consigliati");
                        System.out.println(Libro.toJsonCategorie(categorie));
                        pw.write(Libro.toJsonCategorie(categorie));
                    }
                    else
                        pw.write("Caetgorie non trovate");

                } catch (SQLException e) {
                    pw.write("Errore del server");
                }
                break;
            }
        }
    }
}
