package presenter.prenotazionepresenter;

import com.google.gson.JsonObject;
import model.libromanagement.Libro;
import model.libromanagement.LibroDAO;
import model.posizionemanagement.Posizione;
import model.posizionemanagement.PosizioneDAO;
import model.postazionemanagement.Postazione;
import model.prenotazionemanagement.Prenotazione;
import model.prenotazionemanagement.PrenotazioneDAO;
import model.utentemanagement.Utente;
import model.utentemanagement.UtenteDAO;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import presenter.libropresenter.LibroPresenter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PrenotazionePresenterTest {

        private PrenotazionePresenter prenP;
        private HttpServletResponse response;
        private HttpServletRequest request;
        private PrenotazioneDAO prenotazioneDAO;
        private UtenteDAO utenteDAO;
        private PrintWriter pw;
        private BufferedReader br;

        @Before
        public void setUp() {
            prenotazioneDAO= Mockito.mock(PrenotazioneDAO.class);
            utenteDAO=Mockito.mock(UtenteDAO.class);
            prenP = new PrenotazionePresenter(prenotazioneDAO, utenteDAO);
            response= Mockito.mock(HttpServletResponse.class);
            request=Mockito.mock(HttpServletRequest.class);
            try {
                pw = new PrintWriter("src/test/java/testing.txt");
                br = new BufferedReader(new FileReader("src/test/java/testing.txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    @Test
    public void creaPrenotazione(){
        Utente utente = new Utente.UtenteBuilder().
                email("s.celentano16@studenti.unisa.it").
                password("s.cele").
                nome("Sabato").
                cognome("Celentano").
                matricola("0512107757").
                genere("M").
                eta(23).
                admin(false).
                nuovo(false).
                build();
        Postazione postazione = new Postazione("B3", true, new Posizione( "scientifica" , "Piano 1"));
        Prenotazione p = new Prenotazione(new GregorianCalendar(2022, 1, 2), 16, 18, utente, postazione);
        JSONObject jsonObject = new JSONObject();
        when(request.getPathInfo()).thenReturn("/crea-prenotazione");
        when(request.getParameter("prenotazione")).thenReturn(Prenotazione.toJson(p));
        try {
            when(response.getWriter()).thenReturn(pw);
            when(prenotazioneDAO.insert(p)).thenReturn(true);
            utente.getPrenotazioni().add(p);
            when(utenteDAO.doRetrieveByEmail(utente.getEmail())).thenReturn(utente);
            assertDoesNotThrow(()->prenP.doPost(request,response));
            pw.flush();
            String linea = br.readLine();
            jsonObject.put("utente", Utente.toJson(utente));
            assertEquals(jsonObject.toString(), linea);
        } catch (Exception e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
}
