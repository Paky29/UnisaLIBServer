package model.utentemanagement;

import model.libromanagement.Libro;
import model.posizionemanagement.Posizione;
import model.postazionemanagement.Postazione;
import model.prenotazionemanagement.Prenotazione;
import model.prestitomanagement.Prestito;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class UtenteDAOTest {
    UtenteDAO utenteDAO;

    @Before
    public void setUp(){
        utenteDAO=new UtenteDAO();
    }


    //fare prestiti , interessi e prenotazioni ad hoc per utente
    @Test
    public void doRetrieveByEmailAndPasswordAllTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email_utente_dao@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(false).genere("T").eta(21).build();
        Posizione pos = new Posizione(9, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("0000000000").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("url_test").categoria("test").posizione(pos).build();
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(new GregorianCalendar(2022, 0, 17)).dataConsegna(new GregorianCalendar(2022, 0, 19)).voto(5).commento("ok").attivo(false).build();
        Postazione post = new Postazione("test10", true, pos);
        Prenotazione pr = new Prenotazione(new GregorianCalendar(2022, 0, 31), 14, 16, utente, post);

        ArrayList<Prestito> prestitiUtente=new ArrayList<>();
        prestitiUtente.add(p);
        ArrayList<Prenotazione> prenotazioniUtente=new ArrayList<>();
        prenotazioniUtente.add(pr);
        ArrayList<Libro> interessiUtente=new ArrayList<>();
        interessiUtente.add(lib);

        Utente utenteFinal=new Utente.UtenteBuilder().email("test_email_utente_dao@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(false).genere("T").eta(21).prestiti(prestitiUtente).prenotazioni(prenotazioniUtente).interessi(interessiUtente).build();


        final Utente[] utente_test = new Utente[1];
        assertDoesNotThrow(() -> utente_test[0] =utenteDAO.doRetrieveByEmailAndPasswordAll("test_email_utente_dao@studenti.unisa.it", "Testpword1?"));
        assertEquals(utenteFinal, utente_test[0]);

    }

    //fare prestiti, interessi e prenotazioni ad hoc per utente
    @Test
    public void doRetrieveByEmailAllTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email_utente_dao@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(false).genere("T").eta(21).build();
        Posizione pos = new Posizione(9, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("0000000000").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("url_test").categoria("test").posizione(pos).build();
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(new GregorianCalendar(2022, 0, 17)).dataConsegna(new GregorianCalendar(2022, 0, 19)).voto(5).commento("ok").attivo(false).build();
        Postazione post = new Postazione("test10", true, pos);
        Prenotazione pr = new Prenotazione(new GregorianCalendar(2022, 0, 31), 14, 16, utente, post);

        ArrayList<Prestito> prestitiUtente=new ArrayList<>();
        prestitiUtente.add(p);
        ArrayList<Prenotazione> prenotazioniUtente=new ArrayList<>();
        prenotazioniUtente.add(pr);
        ArrayList<Libro> interessiUtente=new ArrayList<>();
        interessiUtente.add(lib);

        Utente utenteFinal=new Utente.UtenteBuilder().email("test_email_utente_dao@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(false).genere("T").eta(21).prestiti(prestitiUtente).prenotazioni(prenotazioniUtente).interessi(interessiUtente).build();


        final Utente[] utente_test = new Utente[1];
        assertDoesNotThrow(() -> utente_test[0] =utenteDAO.doRetrieveByEmailAll("test_email_utente_dao@studenti.unisa.it"));
        assertEquals(utenteFinal, utente_test[0]);

    }

    @Test
    public void doRetrieveByWrongEmailAllTest() {
        String email="dr";
        final Utente[] utente_test = new Utente[1];
        assertDoesNotThrow(() -> utente_test[0] =utenteDAO.doRetrieveByEmailAll(email));
        assertNull(utente_test[0]);

    }

    @Test
    public void doRetrieveByWrongEmailandPasswordAllTest() {
        String email="dr";
        String password="doria";
        final Utente[] utente_test = new Utente[1];
        assertDoesNotThrow(() -> utente_test[0] =utenteDAO.doRetrieveByEmailAndPasswordAll(email, password));
        assertNull(utente_test[0]);

    }

    @Test
    public void doUpdateTest(){
        String email="test_email_utente_dao@studenti.unisa.it";
        Utente utente=new Utente.UtenteBuilder().email("test_email_utente_dao@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(false).genere("T").eta(21).build();

        AtomicBoolean success = new AtomicBoolean(false);
        assertDoesNotThrow(()-> success.set(utenteDAO.doUpdate(utente)));
        assertTrue(Boolean.parseBoolean(success.toString()));

        try {
            Utente utente_test=utenteDAO.doRetrieveByEmail(email);
            assertEquals(utente.getEmail(), utente_test.getEmail());
            assertEquals(utente.getPassword(), utente_test.getPassword());
            assertEquals(utente.getNome(), utente_test.getNome());
            assertEquals(utente.getCognome(), utente_test.getCognome());
            assertEquals(utente.isAdmin(), utente_test.isAdmin());
            assertEquals(utente.getMatricola(), utente_test.getMatricola());
            assertEquals(utente.isNuovo(), utente_test.isNuovo());
            assertEquals(utente.getGenere(), utente_test.getGenere());
            assertEquals(utente.getEta(), utente_test.getEta());
        } catch (SQLException e) {
           fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }

    @Test
    public void doUpdateUtenteNotExistTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email_not_exist").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(false).genere("T").eta(21).build();
        assertThrows(RuntimeException.class, ()-> utenteDAO.doUpdate(utente));
    }




}
