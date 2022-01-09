package presenter.postazionepresenter;

import model.posizionemanagement.Posizione;
import model.posizionemanagement.PosizioneDAO;
import model.postazionemanagement.Postazione;
import model.postazionemanagement.PostazioneDAO;
import model.prenotazionemanagement.Prenotazione;
import model.prenotazionemanagement.PrenotazioneDAO;
import org.json.JSONArray;
import org.json.JSONObject;
import presenter.http.presenter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name="postazionepresenter", value = "/PostazionePresenter/*")
public class PostazionePresenter extends presenter{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path=getPath(req);
        System.out.println(path);
        switch(path){
            case "/":{
                break;
            }
            case "/mostra-ricerca-postazioni": {
                String admin = req.getParameter("is_admin");
                PrintWriter pw = resp.getWriter();
                PosizioneDAO posizioneDAO=new PosizioneDAO();
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
            case "/mostra-elenco-postazioni":{
                String giorno=req.getParameter("giorno");
                String mese=req.getParameter("mese");
                String anno=req.getParameter("anno");
                System.out.println(anno);
                Date d=new Date(Integer.valueOf(anno)-1900,Integer.valueOf(mese),Integer.valueOf(giorno));
                System.out.println(d.getYear());
                String posizione=req.getParameter("posizione");
                Posizione p=Posizione.fromJson(posizione);
                PrintWriter pw=resp.getWriter();
                PostazioneDAO postazioneDAO=new PostazioneDAO();
                PrenotazioneDAO prenotazioneDAO=new PrenotazioneDAO();
                try {
                    ArrayList<Postazione> postazioni=postazioneDAO.doRetrieveDisponibiliByPosizione(p.getBiblioteca(),p.getZona());
                    if(!postazioni.isEmpty()) {
                        ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
                        JSONArray pos = new JSONArray();
                        for (Postazione pt : postazioni) {
                            prenotazioni.addAll(prenotazioneDAO.doRetrieveValidByPostazioneDate(pt,d));
                            JSONObject obj=new JSONObject();
                            obj.put("postazione",Postazione.toJson(pt));
                            pos.put(obj);
                        }
                        System.out.println(pos.getJSONObject(0));

                        JSONArray pren= new JSONArray();
                        for(Prenotazione pr: prenotazioni){
                            JSONObject obj=new JSONObject();
                            obj.put("prenotazione",Prenotazione.toJson(pr));
                            pren.put(obj);
                        }

                        JSONObject response= new JSONObject();
                        response.put("postazioni", pos);
                        response.put("prenotazioni", pren);
                        System.out.println(response.toString());
                        pw.write(response.toString());
                        if(prenotazioni.isEmpty())
                            System.out.println("non ci sono prenotazioni");
                    }
                    else
                        pw.write("Nessuna postazione trovata");
                } catch (Exception e) {
                    pw.write("Errore del server");
                }
                break;
            }
            case "/mostra-elenco-postazioni-admin":{
                String p=req.getParameter("posizione");
                Posizione pos=Posizione.fromJson(p);
                PrintWriter pw=resp.getWriter();
                PostazioneDAO postazioneDAO=new PostazioneDAO();
                try {
                    ArrayList<Postazione> postazioni=postazioneDAO.doRetrieveByPosizione(pos.getBiblioteca(),pos.getZona());
                    if(!postazioni.isEmpty()){
                        pw.write(Postazione.toJson(postazioni));
                    }
                    else
                        pw.write("Non ci sono postazioni");
                } catch (SQLException e) {
                    pw.write("Errore del server");
                }
                break;
            }
            case "/blocca-postazione":{
                Postazione pos;
                PrintWriter pw = resp.getWriter();
                String idPos = req.getParameter("idPos");
                PostazioneDAO pdao = new PostazioneDAO();
                if(pdao.bloccaPostazione(idPos))
                    pw.write("Blocco effettuato con successo");
                else
                    pw.write("Blocco non effettuato");
            }
        }
    }
}
