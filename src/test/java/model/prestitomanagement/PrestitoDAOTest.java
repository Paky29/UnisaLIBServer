package model.prestitomanagement;

import model.libromanagement.Libro;
import model.libromanagement.LibroDAO;
import model.posizionemanagement.Posizione;
import model.utentemanagement.Utente;
import org.junit.Before;
import org.junit.Test;
import utility.SwitchDate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Testa i metodi di PostazioneDAO
 * */
public class PrestitoDAOTest {
    PrestitoDAO prestitoDAO;
    LibroDAO libroDAO;

    @Before
    public void setUp(){
        prestitoDAO=new PrestitoDAO();
        libroDAO=new LibroDAO();
    }
    /**
     * Testa il corretto inserimento
     * */
    @Test
    public void insertTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(9, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("0000000000").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 2, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(false).build();

        AtomicBoolean success = new AtomicBoolean(false);
        assertDoesNotThrow(() -> success.set(prestitoDAO.insert(p)));
        assertTrue(Boolean.parseBoolean(success.toString()));

        try {
            Prestito prestito_test=prestitoDAO.doRetrieveByKey(dataInizio, lib.getIsbn(), utente.getEmail());
            assertTrue(SwitchDate.equalsDate(p.getDataInizio(), prestito_test.getDataInizio()));
            assertTrue(SwitchDate.equalsDate(p.getDataFine(), prestito_test.getDataFine()));
            assertNull(p.getDataConsegna());
            assertEquals(p.isAttivo(), prestito_test.isAttivo());
            assertEquals(p.getVoto(), prestito_test.getVoto());
            assertEquals(p.getCommento(), prestito_test.getCommento());
            assertEquals(1, p.getLibro().getnCopie()-prestito_test.getLibro().getnCopie());

        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }
    /**
     * Testa l'inserimento di un Prestito già esistente
     * */
    @Test
    public void insertPrestitoExistTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(9, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("0000000000").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 3, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(false).build();

        try{
            prestitoDAO.insert(p);
            AtomicBoolean success = new AtomicBoolean(false);
            assertThrows(SQLException.class, () -> success.set(prestitoDAO.insert(p)));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }


    /**
     * Testa l'attivazione di un Prestito
     * */
    @Test
    public void attivaPrestitoTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(9, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("0000000000").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 4, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(false).build();
        try{
            prestitoDAO.insert(p);
            AtomicBoolean success = new AtomicBoolean(false);
            assertDoesNotThrow(() -> success.set(prestitoDAO.attivaPrestito(p)));

            Prestito prestito_test=prestitoDAO.doRetrieveByKey(dataInizio, lib.getIsbn(), utente.getEmail());
            assertTrue(prestito_test.isAttivo());

        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }
    /**
     * Testa l'attivazione di un Prestito non esistente
     * */
    @Test
    public void attivaPrestitoNotExistTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(9, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test_not_exist").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 3, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(false).build();

        assertThrows(RuntimeException.class, () -> prestitoDAO.attivaPrestito(p));

    }



    /**
     * Testa la conclusione di un prestito
     * */
    @Test
    public void concludiPrestitoTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(9, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("0000000000").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 6, 12);
        GregorianCalendar dataConsegna=new GregorianCalendar(2022, 6, 21);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(true).build();
        Prestito p_concluso = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataConsegna(dataConsegna).attivo(false).build();

        try{
            prestitoDAO.insert(p);
            System.out.println(p.getLibro().getnCopie());
            Prestito pre_conclusione=prestitoDAO.doRetrieveByKey(dataInizio, lib.getIsbn(), utente.getEmail());
            System.out.println(pre_conclusione.getLibro().getnCopie());
            AtomicBoolean success = new AtomicBoolean(false);
            assertDoesNotThrow(() -> success.set(prestitoDAO.concludiPrestito(p_concluso)));
            Prestito prestito_test=prestitoDAO.doRetrieveByKey(dataInizio, lib.getIsbn(), utente.getEmail());
            System.out.println(prestito_test.getLibro().getnCopie());
            assertFalse(prestito_test.isAttivo());
            assertTrue(SwitchDate.equalsDate(p_concluso.getDataConsegna(), prestito_test.getDataConsegna()));
            assertEquals(-1,pre_conclusione.getLibro().getnCopie()-prestito_test.getLibro().getnCopie());

        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }

    /**
     * Testa la conclusione di un prestito non esistente
     * */
    @Test
    public void concludiPrestitoNotExistTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(9, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test_not_exist").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(4).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 3, 12);
        GregorianCalendar dataConsegna=new GregorianCalendar(2022, 3, 21);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2022, 4, 12)).dataConsegna(dataConsegna).attivo(false).build();

        AtomicBoolean success = new AtomicBoolean(false);
        assertDoesNotThrow(() -> success.set(prestitoDAO.concludiPrestito(p)));
        assertFalse(Boolean.parseBoolean(success.toString()));
    }
    /**
     * Testa la valutazione di un prestito
     * */
    @Test
    public void valutaPrestitoTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(9, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("0000000000").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 7, 12);
        GregorianCalendar dataConsegna=new GregorianCalendar(2022, 7, 21);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2022, 4, 12)).dataConsegna(dataConsegna).attivo(false).build();
        Prestito p_valutato = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataConsegna(dataConsegna).attivo(false).voto(5).commento("ok").build();

        try{
            prestitoDAO.insert(p);
            AtomicBoolean success = new AtomicBoolean(false);
            assertDoesNotThrow(() -> success.set(prestitoDAO.valutaPrestito(p)));
            Prestito prestito_test=prestitoDAO.doRetrieveByKey(dataInizio, lib.getIsbn(), utente.getEmail());
            assertEquals(p.getVoto(), prestito_test.getVoto());
            assertEquals(p.getCommento(), prestito_test.getCommento());

        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
    /**
     * Testa la valutazione di un prestito non esistente
     * */
    @Test
    public void valutaPrestitoNotExistTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(9, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test_not_exist").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 3, 12);
        GregorianCalendar dataConsegna=new GregorianCalendar(2022, 3, 21);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataConsegna(dataConsegna).attivo(false).voto(5).commento("ok").build();

        try {
            assertFalse(prestitoDAO.valutaPrestito(p));
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
    /**
     * Testa l'interrogazione del DAO chiedendo i prestiti validi dato un libro
     * */
    @Test
    public void doRetrieveValidByLibroTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email2@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(9, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("0000000000").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 9, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(false).build();

        try{
            prestitoDAO.insert(p);
            AtomicReference<ArrayList<Prestito>> prestiti = new AtomicReference<>();
            assertDoesNotThrow(() -> prestiti.set(prestitoDAO.doRetrieveValidByLibro(lib.getIsbn())));
            for(Prestito pr_test: prestiti.get()){
                assertNull(pr_test.getDataConsegna());
                assertEquals(lib, pr_test.getLibro());
            }
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }
    /**
     * Testa l'interrogazione del DAO chiedendo i prestiti validi dato un libro non esistente
     * */
    @Test
    public void doRetrieveValidByLibroNotExistTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email3@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(9, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test_not_exist").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 3, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(true).build();

        AtomicReference<ArrayList<Prestito>> prestiti = new AtomicReference<>();
        assertDoesNotThrow(() -> prestiti.set(prestitoDAO.doRetrieveValidByLibro(lib.getIsbn())));
        assertTrue(prestiti.get().isEmpty());
    }
    /**
     * Testa l'interrogazione del DAO chiedendo i prestiti validi dato un libro consegnato
     * */
    @Test
    public void doRetrieveValidByLibroConsegnaNotNullTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email4@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(9, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("0000000000").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 10, 12);
        GregorianCalendar dataConsegna=new GregorianCalendar(2022, 10, 21);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(false).voto(5).commento("test").build();
        Prestito p_concluso = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataConsegna(dataConsegna).attivo(false).voto(5).commento("test").build();

        try{
            prestitoDAO.insert(p);
            prestitoDAO.concludiPrestito(p_concluso);
            prestitoDAO.attivaPrestito(p);
            AtomicReference<ArrayList<Prestito>> prestiti = new AtomicReference<>();
            assertDoesNotThrow(() -> prestiti.set(prestitoDAO.doRetrieveValidByLibro(lib.getIsbn())));
            assertTrue(prestiti.get().isEmpty());

        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
    /**
     * Testa l'interrogazione del DAO chiedendo i prestiti dato un Utente
     * */
    @Test
    public void doRetrieveByUtenteTest(){
        String email="test_email@studenti.unisa.it";
        AtomicReference<ArrayList<Prestito>> prestiti = new AtomicReference<>();
        assertDoesNotThrow(() -> prestiti.set(prestitoDAO.doRetrieveByUtente(email)));
        for(Prestito pr_test: prestiti.get()){
            assertEquals(email, pr_test.getUtente().getEmail());
        }

    }
    /**
     * Testa l'interrogazione del DAO chiedendo i prestiti dato un Utente non esistente
     * */
    @Test
    public void doRetrieveByUtenteNotExistTest(){
        String email="test_email_not_exist";
        AtomicReference<ArrayList<Prestito>> prestiti = new AtomicReference<>();
        assertDoesNotThrow(() -> prestiti.set(prestitoDAO.doRetrieveByUtente(email)));
        assertTrue(prestiti.get().isEmpty());
    }
    /**
     * Testa l'interrogazione del DAO chiedendo i prestiti attivi dato un Utente
     * */
    @Test
    public void doRetrieveValidByUtenteTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email5@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(9, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("0000000000").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 10, 30);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(false).voto(5).commento("test").build();

        try{
            prestitoDAO.insert(p);
            final Prestito[] prestito_test= new Prestito[1];
            assertDoesNotThrow(() -> prestito_test[0]=prestitoDAO.doRetrieveValidByUtente(utente.getEmail()));
            assertNull(prestito_test[0].getDataConsegna());
            assertEquals(utente.getEmail(), prestito_test[0].getUtente().getEmail());
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }
    /**
     * Testa l'interrogazione del DAO chiedendo i prestiti attivi dato un Utente non esistente
     * */
    @Test
    public void doRetrieveValidByUtenteNotExistTest(){
        String email="test_email_not_exist";
        Utente utente=new Utente.UtenteBuilder().email("test_email6@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(9, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("0000000000").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 21);
        GregorianCalendar dataConsegna=new GregorianCalendar(2022, 0, 31);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2022, 4, 12)).dataConsegna(dataConsegna).attivo(false).voto(5).commento("test").build();

        try{
            prestitoDAO.insert(p);
            prestitoDAO.attivaPrestito(p);
            final Prestito[] prestito_test= new Prestito[1];
            assertDoesNotThrow(() -> prestito_test[0]=prestitoDAO.doRetrieveValidByUtente(email));
            assertNull(prestito_test[0]);
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }
    /**
     * Testa l'interrogazione del DAO chiedendo i prestiti attivi dato: isbn ed email
     * */
    @Test
    public void doRetrieveByKeyTest(){
        String email="test_email@studenti.unisa.it";
        String isbn="0000000000";
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(9, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("0000000000").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 1, 21);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(false).voto(5).commento("test").build();


        try {
            prestitoDAO.insert(p);
            final Prestito[] prestito_test= new Prestito[1];
            assertDoesNotThrow(() -> prestito_test[0]=prestitoDAO.doRetrieveByKey(dataInizio, isbn, email));
            assertEquals(email, prestito_test[0].getUtente().getEmail());
            assertEquals(isbn, prestito_test[0].getLibro().getIsbn());
            assertTrue(SwitchDate.equalsDate(dataInizio, prestito_test[0].getDataInizio()));
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }
    /**
     * Testa l'interrogazione del DAO chiedendo i prestiti attivi dato: isbn ed email non esistenti
     * */
    @Test
    public void doRetrieveByKeyNotExistTest(){
        String email="test_email_not_exist";
        String isbn="0000000000";
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(9, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("0000000000").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 3, 21);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(false).voto(5).commento("test").build();


        try {
            prestitoDAO.insert(p);
            final Prestito[] prestito_test= new Prestito[1];
            assertDoesNotThrow(() -> prestito_test[0]=prestitoDAO.doRetrieveByKey(dataInizio, isbn, email));
            assertNull(prestito_test[0]);
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }

}
