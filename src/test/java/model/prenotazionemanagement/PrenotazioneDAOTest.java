package model.prenotazionemanagement;


import model.posizionemanagement.Posizione;
import model.postazionemanagement.Postazione;
import model.utentemanagement.Utente;
import org.junit.Before;
import org.junit.Test;
import utility.SwitchDate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class PrenotazioneDAOTest {
    private PrenotazioneDAO prenotazioneDAO;

    @Before
    public void setUp(){
        prenotazioneDAO = new PrenotazioneDAO();
    }

    @Test
    public void insertTest() throws SQLException {
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
        Postazione postazione = new Postazione("1", true, new Posizione(1, "umanistica" , "piano 1"));
        Prenotazione p = new Prenotazione(new GregorianCalendar(2022, 1, 2), 16, 18, utente, postazione);
        prenotazioneDAO.insert(p);
        Prenotazione pTest = prenotazioneDAO.doRetrieveByInfo(p.getData(), p.getOraInizio(), p.getPostazione().getId(), p.getUtente().getEmail());
        assertEquals(p.getData(), pTest.getData());
        assertEquals(p.getOraInizio(), pTest.getOraInizio());
        assertEquals(p.getPostazione(), pTest.getPostazione());
        assertEquals(p.getUtente(), pTest.getUtente());
        assertEquals(p.getOraFine(), pTest.getOraFine());
    }

    @Test
    public void doRetriveByInfoTest(){
        GregorianCalendar data = new GregorianCalendar(2022, 1 ,2);
        int oraInizio = 11;
        String postazioneId = "1";
        String utenteEmail = "s.celentano16@studenti.unisa.it";
        final Prenotazione[] pTest = new Prenotazione[1];
        assertDoesNotThrow(() -> pTest[0] = prenotazioneDAO.doRetrieveByInfo(data, oraInizio, postazioneId, utenteEmail));
        assertEquals(data, pTest[0].getData());
        assertEquals(oraInizio, pTest[0].getOraInizio());
        assertEquals(postazioneId, pTest[0].getPostazione().getId());
        assertEquals(utenteEmail, pTest[0].getUtente().getEmail());
    }

    @Test
    public void doRetrieveValidByPostazioneDateTest() throws SQLException {
        Postazione postazione = new Postazione("1", true, new Posizione(1, "umanistica" , "piano 1"));
        GregorianCalendar date = new GregorianCalendar(2022, 1, 2);
        ArrayList<Prenotazione> pTest = prenotazioneDAO.doRetrieveValidByPostazioneDate(postazione, date);
        for (Prenotazione p : pTest){
            assertTrue(SwitchDate.equalsDate(date, p.getData()));
            assertEquals(postazione, p.getPostazione());
            assertTrue(p.getData().compareTo(new GregorianCalendar())>=0);
        }
    }

    @Test
    public void doRetrieveValidByUtenteTest() throws SQLException {
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
        ArrayList<Prenotazione> pTest = prenotazioneDAO.doRetrieveValidByUtente(utente);
        for (Prenotazione p : pTest){
            assertEquals(utente, p.getUtente());
            assertTrue(p.getData().compareTo(new GregorianCalendar())>=0);
        }
    }

}
