package presenter.postazionepresenter;

import model.posizionemanagement.Posizione;
import model.posizionemanagement.PosizioneDAO;
import model.postazionemanagement.*;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
/**
 * Questa classe definisce le operazioni relative
 * alla gestione delle postazioni
 */
@WebServlet(name="postazionepresenter", value = "/PostazionePresenter/*")
public class PostazionePresenter extends presenter {
    private PrintWriter pw;
    PostazioneDAO postazioneDAO;
    PeriodoDAO periodoDAO;
    PosizioneDAO posizioneDAO;
    PrenotazioneDAO prenotazioneDAO;

    public PostazionePresenter(){
        this.postazioneDAO = new PostazioneDAO();
        this.periodoDAO =  new PeriodoDAO();
        this.posizioneDAO = new PosizioneDAO();
        this.prenotazioneDAO =  new PrenotazioneDAO();
    }

    /**
     * Crea un oggetto di tipo PostazionePresenter con i seguenti parametri
     * @param prenotazioneDAO il DAO che si occupa della gestione degli oggetti Prenotazione
     * @param postazioneDAO il DAO che si occupa della gestione degli oggetti Postazione
     * @param posizioneDAO il DAO che si occupa della gestione degli oggetti Posizione
     * @param periodoDAO il DAO che si occupa della gestione degli oggetti Periodo
     */
    public PostazionePresenter(PostazioneDAO postazioneDAO, PeriodoDAO periodoDAO, PosizioneDAO posizioneDAO, PrenotazioneDAO prenotazioneDAO){
        this.postazioneDAO = postazioneDAO;
        this.periodoDAO = periodoDAO;
        this.posizioneDAO = posizioneDAO;
        this.prenotazioneDAO = prenotazioneDAO;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = getPath(req);
        pw = resp.getWriter();
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
                String posizione = req.getParameter("posizione");
                if(giorno!=null && mese!=null && anno!=null && posizione!=null) {
                    Posizione p = Posizione.fromJson(posizione);
                    if(p.getBiblioteca()!=null && p.getZona()!=null) {
                        GregorianCalendar gc = new GregorianCalendar();
                        gc.set(Integer.parseInt(anno), Integer.parseInt(mese), Integer.parseInt(giorno), 0, 0,0);
                        mostraElencoPostazioni(gc, p);
                    } else
                        pw.write("Posizione inviata non corretta");
                } else
                    pw.write("Errore nella richiesta");
                break;
            }
            case "/mostra-elenco-postazioni-admin": {
                String p = req.getParameter("posizione");
                if (p!=null) {
                    Posizione pos = Posizione.fromJson(p);
                    if(pos.getBiblioteca()!=null && pos.getZona()!=null)
                        mostraElencoPostazioniAdmin(pos);
                    else
                        pw.write("Posizone inviata non corretta");
                } else
                    pw.write("Errore nella richiesta");
                break;
            }
            case "/blocco-indeterminato": {
                String idPos = req.getParameter("idPos");
                if (idPos!=null)
                    bloccoIndeterminato(idPos);
                else
                    pw.write("Errore nella richiesta");
                break;
            }
            case "/blocco-determinato": {
                String idPos = req.getParameter("idPos");
                String periodo = req.getParameter("periodo");
                if(idPos!=null && periodo!=null) {
                    Periodo p = Periodo.fromJson(periodo);
                    bloccoDeterminato(idPos, p, resp);
                } else
                    pw.write("Errore nella richiesta");
                break;
            }
            case "/sblocca-postazione": {
                String idPos = req.getParameter("idPos");
                if (idPos!=null)
                    sbloccaPostazione(idPos);
                else
                    pw.write("Errore nella richiesta");
                break;
            }
            case "/sblocca-postazione-periodo":{
                String idPos = req.getParameter("idPos");
                String periodo = req.getParameter("periodo");
                if(idPos!=null && periodo!=null) {
                    Periodo p = Periodo.fromJson(periodo);
                    sbloccaPostazionePeriodo(idPos,p);
                } else
                    pw.write("Errore nella richiesta");
                break;
            }
            case "/cerca-blocchi":{
                String idPos=req.getParameter("idPos");
                if (idPos!=null)
                    cercaBlocchi(idPos);
                else
                    pw.write("Errore nella richiesta");
                break;
            }
        }
    }
    /**
     * Restituisce le posizioni contenute nella basi di dati
     */
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
    /**
     * Restituisce le postazioni contenute nella basi di dati specificando data e posizione
     * @param gc GregorianCalendar rappresentante la data
     * @param p posizione della postazione
     */
    private void mostraElencoPostazioni(GregorianCalendar gc, Posizione p) {
        try {
            ArrayList<Postazione> postazioni=postazioneDAO.doRetrieveDisponibiliByPosizione(p.getBiblioteca(),p.getZona());
            if(!postazioni.isEmpty()) {
                ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
                JSONArray pos = new JSONArray();
                for (Postazione pt : postazioni) {
                    prenotazioni.addAll(prenotazioneDAO.doRetrieveValidByPostazioneDate(pt, gc.get(Calendar.DATE), gc.get(Calendar.MONTH), gc.get(Calendar.YEAR)));
                    JSONObject obj=new JSONObject();
                    obj.put("postazione",Postazione.toJson(pt));
                    pos.put(obj);
                }
                JSONArray pren= new JSONArray();
                for(Prenotazione pr: prenotazioni){
                    JSONObject obj=new JSONObject();
                    obj.put("prenotazione",Prenotazione.toJson(pr));
                    pren.put(obj);
                }
                JSONObject response= new JSONObject();
                response.put("postazioni", pos);
                response.put("prenotazioni", pren);
                pw.write(response.toString());
            }
            else
                pw.write("Nessuna postazione trovata");
        } catch (Exception e) {
            e.printStackTrace();
            pw.write("Errore del server");
        }
    }
    /**
     * Restituisce le postazioni contenute nella base di dati specificando la posizione
     * @param pos posizione delle postazioni ricercate
     */
    private void mostraElencoPostazioniAdmin(Posizione pos) {
        try {
            ArrayList<Postazione> postazioni = postazioneDAO.doRetrieveByPosizione(pos.getBiblioteca(), pos.getZona());
            if (!(postazioni==null || postazioni.isEmpty())) {
                pw.write(Postazione.toJson(postazioni));
            } else
                pw.write("Non ci sono postazioni");
        } catch (SQLException e) {
            e.printStackTrace();
            pw.write("Errore del server");
        }
    }
    /**
     * Crea un blocco a tempo indeterminato su una postazione dato il suo identificativo
     * @param idPos id postazione
     */
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
    /**
     * Sblocca una postazione bloccata a tempo indeterminato dato il suo identificativo
     * @param idPos id postazione
     */
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
    /**
     * Sblocca una postazione bloccata a tempo determinato dato il suo identificativoe il periodo
     * @param p periodo di blocco
     * @param idPos id postazione
     */
    private void sbloccaPostazionePeriodo(String idPos, Periodo p) {
        JSONObject string = new JSONObject();
        try {
            Periodo periodo=periodoDAO.doRetrieveByInfo(p);
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
    /**
     * Blocca una postazione a tempo determinato dato il suo identificativo e il periodo
     * @param per periodo di blocco
     * @param idPos id postazione
     */
    private void bloccoDeterminato(String idPos, Periodo per, HttpServletResponse resp) {
        if(PeriodoValidator.validate(per)){
            JSONObject rsp = new JSONObject();
            try {
                Postazione pos = postazioneDAO.doRetrieveById(idPos);
                if (pos.isDisponibile()) {
                    String str = postazioneDAO.bloccoDeterminato(per, pos);
                    rsp.put("messaggio", str);
                    pw.write(rsp.toString());
                } else
                    pw.write("Postazione bloccata in modo indeterminato");
            } catch (Exception e) {
                resp.setStatus(505);
                e.printStackTrace();
                pw.write("Errore del server");
            }
        } else
            pw.write("Periodo non valido");
    }
    /**
     * Ricerca i blocchi riguardanti una postazione dato il suo identificativo
     * @param idPos id postazione
     */
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
