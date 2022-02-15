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

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
/**
 * Testa i metodi di PrenotazioneDAO
 * */
public class PrenotazioneDAOTest {
    private PrenotazioneDAO prenotazioneDAO;

    @Before
    public void setUp(){
        prenotazioneDAO = new PrenotazioneDAO();
    }
    /**
     * Testa il corretto inserimento
     * */
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
        Postazione postazione = new Postazione("B3", true, new Posizione( "scientifica" , "piano 2"));
        Prenotazione p = new Prenotazione(new GregorianCalendar(2022, 1, 2), 16, 18, utente, postazione);
        prenotazioneDAO.insert(p);
        Prenotazione pTest = prenotazioneDAO.doRetrieveByInfo(p.getData(), p.getOraInizio(), p.getPostazione().getId(), p.getUtente().getEmail());
        assertTrue(SwitchDate.equalsDate(p.getData(), pTest.getData()));
        assertEquals(p.getOraInizio(), pTest.getOraInizio());
        assertTrue(p.getPostazione().getId().equals(pTest.getPostazione().getId()));
        assertTrue(p.getUtente().getEmail().equals(pTest.getUtente().getEmail()));
        assertEquals(p.getOraFine(), pTest.getOraFine());
    }
    /**
     * Testa l'inserimento di una postazione già esistente
     * */
    @Test(expected = SQLException.class)
    public void insertAlreadyExistsPrenotazioneTest() throws SQLException {
        Utente utente = new Utente.UtenteBuilder().
                email("sc").
                password("s.cele").
                nome("sabato").
                cognome("celentano").
                admin(false).
                nuovo(true).
                build();
        Postazione postazione = new Postazione("B1", true, new Posizione( "scientifica" , "piano 1"));
        Prenotazione p = new Prenotazione(new GregorianCalendar(2022, 0, 31), 16, 18, utente, postazione);
        prenotazioneDAO.insert(p);
    }
    /**
     * Testa l'interrogazione del DAO sulla base di info prenotazione
     * */
    @Test
    public void doRetriveByInfoTest(){
        GregorianCalendar data = new GregorianCalendar(2022, 0 ,31);
        int oraInizio = 16;
        String postazioneId = "B1";
        String utenteEmail = "sc";
        final Prenotazione[] pTest = new Prenotazione[1];
        assertDoesNotThrow(() -> pTest[0] = prenotazioneDAO.doRetrieveByInfo(data, oraInizio, postazioneId, utenteEmail));
        assertTrue(SwitchDate.equalsDate(data, pTest[0].getData()));
        assertEquals(oraInizio, pTest[0].getOraInizio());
        assertTrue(postazioneId.equals(pTest[0].getPostazione().getId()));
        assertEquals(utenteEmail, pTest[0].getUtente().getEmail());
    }
    /**
     * Testa l'interrogazione del DAO sulla base di info prenotazione sbagliate
     * */
    @Test
    public void doRetriveByIncorrectInfoTest() throws SQLException {
        GregorianCalendar data = new GregorianCalendar(2022, 1 ,2);
        int oraInizio = 17;
        String postazioneId = "Not Exists";
        String utenteEmail = "sc@studenti.unisa.it";
        Prenotazione p = prenotazioneDAO.doRetrieveByInfo(data,oraInizio,postazioneId,utenteEmail);
        assertNull(p);
    }
    /**
     * Testa l'interrogazione del DAO delle postazioni valide sulla base di una data
     * */
    @Test
    public void doRetrieveValidByPostazioneDateTest() throws SQLException {
        Postazione postazione = new Postazione("B3", true, new Posizione(1, "umanistica" , "piano 1"));
        GregorianCalendar date = new GregorianCalendar(2022, 0, 31);
        ArrayList<Prenotazione> pTest = prenotazioneDAO.doRetrieveValidByPostazioneDate(postazione, date.get(Calendar.DATE), date.get(Calendar.MONTH), date.get(Calendar.YEAR));
        for (Prenotazione p : pTest){
            assertTrue(SwitchDate.equalsDate(date, p.getData()));
            assertTrue(postazione.getId().equals(p.getPostazione().getId()));
            assertTrue(p.getData().compareTo(new GregorianCalendar())>=0);
        }
    }
    /**
     * Testa l'interrogazione del DAO delle postazioni valide non esistenti sulla base di una data
     * */
    @Test
    public void doRetrieveValidByIncorrectPostazioneDateTest() throws SQLException {
        Postazione postazione = new Postazione("Not Exists", true, new Posizione(1, "umanistica" , "piano 1"));
        GregorianCalendar date = new GregorianCalendar(2022, 1, 2);
        ArrayList<Prenotazione> pTest = prenotazioneDAO.doRetrieveValidByPostazioneDate(postazione, date.get(Calendar.DATE), date.get(Calendar.MONTH), date.get(Calendar.YEAR));
        assertTrue(pTest.isEmpty());
    }
    /**
     * Testa l'interrogazione del DAO delle postazioni valide passando un Utente formato oggetto
     * */
    @Test
    public void doRetrieveValidByUtenteTest() throws SQLException {
        Utente utente = new Utente.UtenteBuilder().
                email("sc").
                password("s.cele").
                nome("sabato").
                cognome("celentano").
                admin(false).
                nuovo(true).
                build();
        ArrayList<Prenotazione> pTest = prenotazioneDAO.doRetrieveValidByUtente(utente);
        for (Prenotazione p : pTest){
            assertTrue(p.getUtente().getEmail().equals(utente.getEmail()));
            assertTrue(p.getData().compareTo(new GregorianCalendar())>=0);
        }
    }
    /**
     * Testa l'interrogazione del DAO delle postazioni valide passando un Utente formato stringa
     * */
    @Test
    public void doRetrieveValidByUtenteStringTest() throws SQLException {
        ArrayList<Prenotazione> pTest = prenotazioneDAO.doRetrieveValidByUtente("sc");
        for (Prenotazione p : pTest){
            assertTrue(p.getUtente().getEmail().equals("sc"));
            assertTrue(p.getData().compareTo(new GregorianCalendar())>=0);
        }
    }
    /**
     * Testa l'interrogazione del DAO delle postazioni valide passando un Utente errato
     * */
    @Test
    public void doRetrieveValidByIncorrectUtenteTest() throws SQLException {
        Utente utente = new Utente.UtenteBuilder().
                email("sc@studenti.unisa.it").
                password("s.cele").
                nome("sabato").
                cognome("celentano").
                admin(false).
                nuovo(true).
                build();
        ArrayList<Prenotazione> pTest = prenotazioneDAO.doRetrieveValidByUtente(utente);
        assertTrue(pTest.isEmpty());
    }

}
