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

public class PrestitoDAOTest {
    PrestitoDAO prestitoDAO;
    LibroDAO libroDAO;

    @Before
    public void setUp(){
        prestitoDAO=new PrestitoDAO();
        libroDAO=new LibroDAO();
    }

    @Test
    public void insertTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email").password("test_pword").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 3, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2022, 4, 12)).attivo(false).build();

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

    @Test
    public void insertLibroExistTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email").password("test_pword").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 3, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2022, 4, 12)).attivo(false).build();

        try{
            prestitoDAO.insert(p);
            AtomicBoolean success = new AtomicBoolean(false);
            assertThrows(SQLException.class, () -> success.set(prestitoDAO.insert(p)));
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }



    @Test
    public void attivaPrestitoTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email").password("test_pword").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 3, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2022, 4, 12)).attivo(false).build();

        AtomicBoolean success = new AtomicBoolean(false);
        assertDoesNotThrow(() -> success.set(prestitoDAO.attivaPrestito(p)));
        try{
            Prestito prestito_test=prestitoDAO.doRetrieveByKey(dataInizio, lib.getIsbn(), utente.getEmail());
            assertTrue(prestito_test.isAttivo());

        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }

    @Test
    public void concludiPrestitoTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email").password("test_pword").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(4).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 3, 12);
        GregorianCalendar dataConsegna=new GregorianCalendar(2022, 3, 21);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2022, 4, 12)).dataConsegna(dataConsegna).attivo(false).build();

        try{
            Prestito pre_conclusione=prestitoDAO.doRetrieveByKey(dataInizio, lib.getIsbn(), utente.getEmail());
            AtomicBoolean success = new AtomicBoolean(false);
            assertDoesNotThrow(() -> success.set(prestitoDAO.concludiPrestito(p)));
            Prestito prestito_test=prestitoDAO.doRetrieveByKey(dataInizio, lib.getIsbn(), utente.getEmail());
            assertFalse(prestito_test.isAttivo());
            assertTrue(SwitchDate.equalsDate(p.getDataConsegna(), prestito_test.getDataConsegna()));
            assertEquals(-1,pre_conclusione.getLibro().getnCopie()-prestito_test.getLibro().getnCopie());

        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }



    }

    @Test
    public void valutaPrestitoTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email").password("test_pword").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 3, 12);
        GregorianCalendar dataConsegna=new GregorianCalendar(2022, 3, 21);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2022, 4, 12)).dataConsegna(dataConsegna).attivo(false).voto(5).commento("ok").build();

        AtomicBoolean success = new AtomicBoolean(false);
        assertDoesNotThrow(() -> success.set(prestitoDAO.valutaPrestito(p)));
        try{
            Prestito prestito_test=prestitoDAO.doRetrieveByKey(dataInizio, lib.getIsbn(), utente.getEmail());
            assertEquals(p.getVoto(), prestito_test.getVoto());
            assertEquals(p.getCommento(), prestito_test.getCommento());

        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }

    @Test
    public void doRetrieveValidByLibroTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email").password("test_pword").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 3, 12);
        GregorianCalendar dataConsegna=new GregorianCalendar(2022, 3, 21);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2022, 4, 12)).dataConsegna(dataConsegna).attivo(false).voto(5).commento("test").build();

        try{
            prestitoDAO.attivaPrestito(p);
            AtomicReference<ArrayList<Prestito>> prestiti = new AtomicReference<>();
            assertDoesNotThrow(() -> prestiti.set(prestitoDAO.doRetrieveValidByLibro(lib.getIsbn())));
            for(Prestito pr_test: prestiti.get()){
                assertTrue(pr_test.isAttivo());
                assertEquals(lib, pr_test.getLibro());
            }
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }

    @Test
    public void doRetrieveByUtenteTest(){
        String email="test_email";
        AtomicReference<ArrayList<Prestito>> prestiti = new AtomicReference<>();
        assertDoesNotThrow(() -> prestiti.set(prestitoDAO.doRetrieveByUtente(email)));
        for(Prestito pr_test: prestiti.get()){
            assertEquals(email, pr_test.getUtente().getEmail());
        }

    }

    @Test
    public void doRetrieveValidByUtenteTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email").password("test_pword").nome("test_nome").cognome("test_cognome").admin(false).matricola("test_matricola").nuovo(true).genere("test").eta(21).build();
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 3, 12);
        GregorianCalendar dataConsegna=new GregorianCalendar(2022, 3, 21);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2022, 4, 12)).dataConsegna(dataConsegna).attivo(false).voto(5).commento("test").build();

        try{
            prestitoDAO.attivaPrestito(p);
            final Prestito[] prestito_test= new Prestito[1];
            assertDoesNotThrow(() -> prestito_test[0]=prestitoDAO.doRetrieveValidByUtente(utente.getEmail()));
            assertTrue(prestito_test[0].isAttivo());
            assertEquals(utente.getEmail(), prestito_test[0].getUtente().getEmail());
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }

    @Test
    public void doRetrieveByKeyTest(){
        String email="test_email";
        String isbn="isbn_test";
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 3, 12);

        final Prestito[] prestito_test= new Prestito[1];
        assertDoesNotThrow(() -> prestito_test[0]=prestitoDAO.doRetrieveByKey(dataInizio, isbn, email));
        assertEquals(email, prestito_test[0].getUtente().getEmail());
        assertEquals(isbn, prestito_test[0].getLibro().getIsbn());
        assertTrue(SwitchDate.equalsDate(dataInizio, prestito_test[0].getDataInizio()));
    }

}
