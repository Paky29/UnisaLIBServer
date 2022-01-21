package presenter.postazionepresenter;

import model.posizionemanagement.Posizione;
import model.posizionemanagement.PosizioneDAO;
import model.postazionemanagement.Periodo;
import model.postazionemanagement.PeriodoDAO;
import model.postazionemanagement.Postazione;
import model.postazionemanagement.PostazioneDAO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PostazionePresenterTest {
    PostazioneDAO postazioneDAO;
    PeriodoDAO periodoDAO;
    PosizioneDAO posizioneDAO;
    PostazionePresenter postazionePresenter;
    HttpServletRequest request;
    HttpServletResponse response;
    PrintWriter pw;
    BufferedReader br;

    @Before
    public void setUp(){
        postazioneDAO = Mockito.mock(PostazioneDAO.class);
        periodoDAO = Mockito.mock(PeriodoDAO.class);
        posizioneDAO = Mockito.mock(PosizioneDAO.class);
        postazionePresenter = new PostazionePresenter(postazioneDAO, periodoDAO, posizioneDAO);
        response= Mockito.mock(HttpServletResponse.class);
        request=Mockito.mock(HttpServletRequest.class);
        try {
            pw=new PrintWriter("src/test/java/testing.txt");
            br = new BufferedReader(new FileReader("src/test/java/testing.txt"));
        } catch (FileNotFoundException e) {
            fail("File testing.txt non trovato");
        }
    }

    @Test
    public void mostraRicercaPostazioniTest(){
        Posizione p1 = new Posizione(3, "scientifica", "Piano 1");
        Posizione p2 = new Posizione(4, "scientifica", "Piano 2");
        ArrayList<Posizione> posizioni = new ArrayList<>();
        posizioni.add(p1);
        posizioni.add(p2);
        when(request.getPathInfo()).thenReturn("/mostra-ricerca-postazioni");
        try {
            when(response.getWriter()).thenReturn(pw);
            when(posizioneDAO.doRetrieveAll()).thenReturn(posizioni);
            assertDoesNotThrow(() -> postazionePresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals(Posizione.toJson(posizioni), linea);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void mostraRicercaPostazioniIsEmptyTest(){
        ArrayList<Posizione> posizioni = new ArrayList<>();
        when(request.getPathInfo()).thenReturn("/mostra-ricerca-postazioni");
        try {
            when(response.getWriter()).thenReturn(pw);
            when(posizioneDAO.doRetrieveAll()).thenReturn(posizioni);
            assertDoesNotThrow(() -> postazionePresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals("Posizioni non trovate", linea);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void mostraElencoPostazioniAdminTest() {
        Posizione posizione = new Posizione(3, "scientifica", "Piano 1");
        Postazione p1 = new Postazione("A1", true, posizione);
        Postazione p2 = new Postazione("A2", true, posizione);
        ArrayList<Postazione> postazioni = new ArrayList<>();
        postazioni.add(p1);
        postazioni.add(p2);
        when(request.getPathInfo()).thenReturn("/mostra-elenco-postazioni-admin");
        when(request.getParameter("posizione")).thenReturn(Posizione.toJson(posizione));
        try {
            when(response.getWriter()).thenReturn(pw);
            when(postazioneDAO.doRetrieveByPosizione(posizione.getBiblioteca(), posizione.getZona())).thenReturn(postazioni);
            assertDoesNotThrow(() -> postazionePresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals(Postazione.toJson(postazioni), linea);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void mostraElencoPostazioniAdminIsEmptyTest() {
        Posizione posizione = new Posizione(3, "scientifica", "Piano 1");
        ArrayList<Postazione> postazioni = new ArrayList<>();
        when(request.getPathInfo()).thenReturn("/mostra-elenco-postazioni-admin");
        when(request.getParameter("posizione")).thenReturn(Posizione.toJson(posizione));
        try {
            when(response.getWriter()).thenReturn(pw);
            when(postazioneDAO.doRetrieveByPosizione(posizione.getBiblioteca(), posizione.getZona())).thenReturn(postazioni);
            assertDoesNotThrow(() -> postazionePresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals("Non ci sono postazioni", linea);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void mostraElencoPostazioniAdminNullTest() {
        Posizione posizione = new Posizione(3, "scientifica", "Piano 1");
        ArrayList<Postazione> postazioni = null;
        when(request.getPathInfo()).thenReturn("/mostra-elenco-postazioni-admin");
        when(request.getParameter("posizione")).thenReturn(Posizione.toJson(posizione));
        try {
            when(response.getWriter()).thenReturn(pw);
            when(postazioneDAO.doRetrieveByPosizione(posizione.getBiblioteca(), posizione.getZona())).thenReturn(postazioni);
            assertDoesNotThrow(() -> postazionePresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals("Non ci sono postazioni", linea);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bloccoIndeterminatoTest(){
        JSONObject jsonObject = new JSONObject();
        when(request.getParameter("idPos")).thenReturn("A1");
        when(request.getPathInfo()).thenReturn("/blocco-indeterminato");
        try {
            when(response.getWriter()).thenReturn(pw);
            when(postazioneDAO.isDisponibile("A1")).thenReturn(1);
            when(postazioneDAO.bloccaPostazione("A1")).thenReturn(true);
            assertDoesNotThrow(() -> postazionePresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            jsonObject.put("messaggio", "blocco effettuato con successo");
            assertEquals(jsonObject.toString(), linea);
        } catch (IOException | SQLException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bloccoIndeterminatoPostazioneBloccataTest(){
        JSONObject jsonObject = new JSONObject();
        when(request.getParameter("idPos")).thenReturn("A1");
        when(request.getPathInfo()).thenReturn("/blocco-indeterminato");
        try {
            when(response.getWriter()).thenReturn(pw);
            when(postazioneDAO.isDisponibile("A1")).thenReturn(0);
            assertDoesNotThrow(() -> postazionePresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            jsonObject.put("messaggio", "Postazione gia' bloccata");
            assertEquals(jsonObject.toString(), linea);
        } catch (IOException | SQLException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sbloccaPostazioneTest(){
        JSONObject jsonObject = new JSONObject();
        when(request.getParameter("idPos")).thenReturn("A1");
        when(request.getPathInfo()).thenReturn("/sblocca-postazione");
        try {
            when(response.getWriter()).thenReturn(pw);
            when(postazioneDAO.sbloccaPostazione("A1")).thenReturn(true);
            assertDoesNotThrow(() -> postazionePresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            jsonObject.put("messaggio", "sblocco effettuato con successo");
            assertEquals(jsonObject.toString(), linea);
        } catch (IOException | SQLException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sbloccaPostazionePeriodoTest(){
        JSONObject jsonObject = new JSONObject();
        Periodo periodo = new Periodo(1, 9, 11, new GregorianCalendar());
        when(request.getParameter("idPos")).thenReturn("A1");
        when(request.getParameter("periodo")).thenReturn(Periodo.toJson(periodo));
        when(request.getPathInfo()).thenReturn("/sblocca-postazione-periodo");
        try {
            when(response.getWriter()).thenReturn(pw);
            when(periodoDAO.doRetrieveByInfo(periodo)).thenReturn(periodo);
            when(postazioneDAO.sbloccaPostazione("A1", periodo)).thenReturn(true);
            assertDoesNotThrow(() -> postazionePresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            jsonObject.put("messaggio", "sblocco effettuato con successo");
            assertEquals(jsonObject.toString(), linea);
        } catch (IOException | SQLException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bloccoDeterminatoTest(){
        JSONObject jsonObject = new JSONObject();
        Periodo periodo = new Periodo(1, 9, 11, new GregorianCalendar());
        Posizione posizione = new Posizione(3, "scientifica", "Piano 1");
        Postazione postazione = new Postazione("A1", true, posizione);
        when(request.getParameter("idPos")).thenReturn("A1");
        when(request.getParameter("periodo")).thenReturn(Periodo.toJson(periodo));
        when(request.getPathInfo()).thenReturn("/blocco-determinato");
        try {
            when(response.getWriter()).thenReturn(pw);
            when(postazioneDAO.doRetrieveById("A1")).thenReturn(postazione);
            when(postazioneDAO.bloccoDeterminato(periodo, postazione)).thenReturn("Blocchi inseriti correttamente");
            assertDoesNotThrow(() -> postazionePresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            jsonObject.put("messaggio", "Blocchi inseriti correttamente");
            assertEquals(jsonObject.toString(), linea);
        } catch (IOException | SQLException | JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void cercaBlocchi(){
        JSONObject jsonObject = new JSONObject();
        Posizione posizione = new Posizione(3, "scientifica", "Piano 1");
        Postazione postazione = new Postazione("A1", true, posizione);
        when(request.getParameter("idPos")).thenReturn("A1");
        when(request.getPathInfo()).thenReturn("/cerca-blocchi");
        try {
            when(response.getWriter()).thenReturn(pw);
            when(postazioneDAO.doRetrieveById("A1")).thenReturn(postazione);
            assertDoesNotThrow(() -> postazionePresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            jsonObject.put("postazione", Postazione.toJson(postazione));
            assertEquals(jsonObject.toString(), linea);
        } catch (SQLException | IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
