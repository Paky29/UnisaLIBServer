package presenter.postazionepresenter;

import model.libromanagement.Libro;
import model.posizionemanagement.Posizione;
import model.posizionemanagement.PosizioneDAO;
import presenter.http.presenter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name="postazionepresenter", value = "/PostazionePresenter/*")
public class PostazionePresenter extends presenter {
    PosizioneDAO posizioneDAO = new PosizioneDAO();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = getPath(req);
        System.out.println(path);
        switch (path) {
            case "/mostra-ricerca-postazioni": {
                String admin = req.getParameter("is_admin");
                PrintWriter pw = resp.getWriter();
                try {
                    ArrayList<Posizione> posizioni = posizioneDAO.doRetrieveAll();
                    if (!posizioni.isEmpty()) {
                        pw.write(Posizione.toJson(posizioni));
                    } else
                        pw.write("Posizioni non trovate");
                } catch (SQLException e) {
                    pw.write("Errore del server");
                }
                break;
            }
        }
    }
}
