package presenter.postazionepresenter;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import model.posizionemanagement.Posizione;
import model.posizionemanagement.PosizioneDAO;
import model.postazionemanagement.Periodo;
import model.postazionemanagement.PeriodoDAO;
import model.postazionemanagement.Postazione;
import model.postazionemanagement.PostazioneDAO;
import model.prenotazionemanagement.Prenotazione;
import model.prenotazionemanagement.PrenotazioneDAO;
import org.json.JSONArray;
import org.json.JSONException;
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
import java.util.GregorianCalendar;

@WebServlet(name="postazionepresenter", value = "/PostazionePresenter/*")
public class PostazionePresenter extends presenter {
    private PrintWriter pw;
    PostazioneDAO postazioneDAO;
    PeriodoDAO periodoDAO;
    PosizioneDAO posizioneDAO;

    public PostazionePresenter(){
        this.postazioneDAO = new PostazioneDAO();
        this.periodoDAO =  new PeriodoDAO();
        this.posizioneDAO = new PosizioneDAO();
    }


    public PostazionePresenter(PostazioneDAO postazioneDAO, PeriodoDAO periodoDAO, PosizioneDAO posizioneDAO){
        this.postazioneDAO = postazioneDAO;
        this.periodoDAO = periodoDAO;
        this.posizioneDAO = posizioneDAO;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = getPath(req);
        pw = resp.getWriter();
        System.out.println(path);
        switch (path) {
            case "/": {
                break;
            }
            case "/mostra-ricerca-postazioni": {
                mostraRicercaPostazioni();
                break;
            }
            case "/mostra-elenco-postazioni": {
                String giorno = req.getParameter("giorno");
                String mese = req.getParameter("mese");
                String anno = req.getParameter("anno");
                GregorianCalendar gc = new GregorianCalendar(Integer.valueOf(anno), Integer.valueOf(mese), Integer.valueOf(giorno));
                String posizione = req.getParameter("posizione");
                Posizione p=Posizione.fromJson(posizione);
                mostraElencoPostazioni(gc, p);
                break;
            }
            case "/mostra-elenco-postazioni-admin": {
                String p = req.getParameter("posizione");
                Posizione pos = Posizione.fromJson(p);
                mostraElencoPostazioniAdmin(pos);
                break;
            }
            case "/blocco-indeterminato": {
                String idPos = req.getParameter("idPos");
                bloccoIndeterminato(idPos);
                break;
            }
            case "/blocco-determinato": {
                String idPos = req.getParameter("idPos");
                Periodo per=Periodo.fromJson(req.getParameter("periodo"));
                bloccoDeterminato(idPos, per, resp);
                break;
            }
            case "/sblocca-postazione": {
                String idPos = req.getParameter("idPos");
                sbloccaPostazione(idPos);
                break;
            }
            case "/sblocca-postazione-periodo":{
                String idPos = req.getParameter("idPos");
                Periodo p=Periodo.fromJson(req.getParameter("periodo"));
                sbloccaPostazionePeriodo(idPos,p);
                break;
            }
            case "/cerca-blocchi":{
                String idPos=req.getParameter("idPos");
                cercaBlocchi(idPos);
            }
        }
    }

    private void mostraRicercaPostazioni(){
        try {
            ArrayList<Posizione> posizioni = posizioneDAO.doRetrieveAll();
            if (!posizioni.isEmpty()) {
                pw.write(Posizione.toJson(posizioni));
            } else
                pw.write("Posizioni non trovate");
        } catch (SQLException e) {
            pw.write("Errore del server");
        }
    }

    private void mostraElencoPostazioni(GregorianCalendar gc, Posizione p) {
        PrenotazioneDAO prenotazioneDAO=new PrenotazioneDAO();
        try {
            ArrayList<Postazione> postazioni=postazioneDAO.doRetrieveDisponibiliByPosizione(p.getBiblioteca(),p.getZona());
            if(!postazioni.isEmpty()) {
                ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
                JSONArray pos = new JSONArray();
                for (Postazione pt : postazioni) {
                    prenotazioni.addAll(prenotazioneDAO.doRetrieveValidByPostazioneDate(pt, gc));
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
            e.printStackTrace();
            pw.write("Errore del server");
        }
    }

    private void mostraElencoPostazioniAdmin(Posizione pos) {
        try {
            ArrayList<Postazione> postazioni = postazioneDAO.doRetrieveByPosizione(pos.getBiblioteca(), pos.getZona());
            if (!(postazioni==null || postazioni.isEmpty())) {
                System.out.println("okokokookok");
                pw.write(Postazione.toJson(postazioni));
            } else
                pw.write("Non ci sono postazioni");
        } catch (SQLException e) {
            e.printStackTrace();
            pw.write("Errore del server");
        }
    }

    private void bloccoIndeterminato(String idPos) {
        JSONObject string = new JSONObject();
        try {
            if (postazioneDAO.isDisponibile(idPos) == 0) {
                try {
                    string.put("messaggio", "Postazione gia' bloccata");
                    pw.write(string.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    pw.write("Errore del server");
                }
            } else {
                if (postazioneDAO.bloccaPostazione(idPos)) {
                    try {
                        string.put("messaggio", "blocco effettuato con successo");
                        pw.write(string.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pw.write("Blocco non effettuato");

                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
            pw.write("Errore del server");
        }
    }

    private void sbloccaPostazione(String idPos) {
        JSONObject string = new JSONObject();
        try {
            if(postazioneDAO.sbloccaPostazione(idPos)){
                try{
                    string.put("messaggio", "sblocco effettuato con successo");
                    pw.write(string.toString());
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else
                string.put("messaggio","sblocco non effettuato");
        } catch (Exception e) {
            e.printStackTrace();
            pw.write("Errore del server");
        }
    }

    private void sbloccaPostazionePeriodo(String idPos, Periodo p) {
        JSONObject string = new JSONObject();
        try {
            Periodo periodo=periodoDAO.doRetrieveByInfo(p);
            System.out.println(idPos+" "+p.getId());
            if(postazioneDAO.sbloccaPostazione(idPos,periodo)){
                try{
                    string.put("messaggio", "sblocco effettuato con successo");
                    pw.write(string.toString());
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else
                string.put("messaggio","sblocco non effettuato");
        } catch (Exception e) {
            e.printStackTrace();
            pw.write("Errore del server");
        }
    }

    private void bloccoDeterminato(String idPos, Periodo per, HttpServletResponse resp) {
        JSONObject rsp=new JSONObject();
        try{
            Postazione pos=postazioneDAO.doRetrieveById(idPos);
            if(pos.isDisponibile()){
                System.out.println("dentro");
                String str=postazioneDAO.bloccoDeterminato(per,pos);
                rsp.put("messaggio",str);
                pw.write(rsp.toString());
            }
            else
                pw.write("Postazione bloccata in modo indeterminato");
        }catch (Exception e) {
            resp.setStatus(505);
            e.printStackTrace();
            pw.write("Errore del server");
        }
    }

    private void cercaBlocchi(String idPos) {
        try {
            Postazione p=postazioneDAO.doRetrieveById(idPos);
            if(p!=null){
                JSONObject string = new JSONObject();
                string.put("postazione",Postazione.toJson(p));
                pw.write(string.toString());
            }
            else
                pw.write("Postazione non trovata");
        } catch (Exception e) {
            e.printStackTrace();
            pw.write("Errore del server");
        }
    }

}
