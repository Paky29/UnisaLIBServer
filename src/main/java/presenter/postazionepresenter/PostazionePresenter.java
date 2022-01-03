package presenter.postazionepresenter;

import model.posizionemanagement.Posizione;
import model.postazionemanagement.Postazione;
import model.postazionemanagement.PostazioneDAO;
import model.prenotazionemanagement.Prenotazione;
import model.prenotazionemanagement.PrenotazioneDAO;
import presenter.http.presenter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

public class PostazionePresenter extends presenter{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path=getPath(req);
        switch(path){
            case "/":{
                break;
            }
            case "/ricerca-postazioni":{
                String giorno=req.getParameter("giorno");
                String mese=req.getParameter("mese");
                String anno=req.getParameter("anno");
                String posizione=req.getParameter("posizione");
                Posizione p=Posizione.fromJson(posizione);
                PrintWriter pw=resp.getWriter();
                PostazioneDAO postazioneDAO=new PostazioneDAO();
                PrenotazioneDAO prenotazioneDAO=new PrenotazioneDAO();
                try {
                    ArrayList<Postazione> postazioni=postazioneDAO.doRetrieveByPosizione(p.getBiblioteca(),p.getZona());
                    if(!postazioni.isEmpty()) {
                        ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
                        for (Postazione pt : postazioni)
                            prenotazioni.addAll(prenotazioneDAO.doRetrieveByPostazione(pt));
                        pw.write(Postazione.toJson(postazioni));
                        pw.write(Prenotazione.toJson(prenotazioni));
                        if(prenotazioni.isEmpty())
                            System.out.println("non ci sono prenotazioni");
                    }
                    else
                        pw.write("Nessuna postazione trovata");
                } catch (SQLException e) {
                    pw.write("Errore del server");
                }
                break;
            }
        }
    }
}
