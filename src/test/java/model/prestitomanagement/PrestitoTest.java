package model.prestitomanagement;

import model.libromanagement.Libro;
import model.posizionemanagement.Posizione;
import model.utentemanagement.Utente;
import org.junit.Test;
import java.util.GregorianCalendar;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PrestitoTest {
    @Test
    public void equalsTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2020, 2, 1);
        Prestito p1 = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(false).voto(5).commento("ok").build();
        Prestito p2 = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(false).voto(5).commento("ok").build();
        assertTrue(p1.equals(p2));
    }

    @Test
    public void equalsTestWithDataConsegnaNotNullTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2020, 2, 1);
        GregorianCalendar dataConsegna=new GregorianCalendar(2020, 2, 10);
        Prestito p1 = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataConsegna(dataConsegna).attivo(false).voto(5).commento("ok").build();
        Prestito p2 = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataConsegna(dataConsegna).attivo(false).voto(5).commento("ok").build();
        assertTrue(p1.equals(p2));
    }

    @Test
    public void equalsSamePrestitoTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2020, 1, 1);
        Prestito p1 = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(false).voto(5).commento("ok").build();
        assertTrue(p1.equals(p1));

    }

    @Test
    public void equalsPrestitoNullErrorTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2020, 1, 1);
        Prestito p1 = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(false).voto(5).commento("ok").build();
        Prestito p2=null;
        assertFalse(p1.equals(p2));
    }
    @Test
    public void equalsDataInizioErrorTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio1=new GregorianCalendar(2020, 2, 1);
        GregorianCalendar dataInizio2=new GregorianCalendar(2020, 1, 1);
        Prestito p1 = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio1).attivo(false).voto(5).commento("ok").build();
        Prestito p2 = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio2).attivo(false).voto(5).commento("ok").build();
        assertFalse(p1.equals(p2));
    }

    @Test
    public void equalsErrorWithBothDataConsegnaNotNullTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2020, 2, 1);
        GregorianCalendar dataConsegna1=new GregorianCalendar(2020, 2, 10);
        GregorianCalendar dataConsegna2=new GregorianCalendar(2020, 3, 10);
        Prestito p1 = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataConsegna(dataConsegna1).attivo(false).voto(5).commento("ok").build();
        Prestito p2 = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataConsegna(dataConsegna2).attivo(false).voto(5).commento("ok").build();
        assertFalse(p1.equals(p2));
    }

    @Test
    public void equalsErrorWithOneDataConsegnaNotNullTest(){
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2020, 2, 1);
        GregorianCalendar dataConsegna=new GregorianCalendar(2020, 2, 10);
        Prestito p1 = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataConsegna(dataConsegna).attivo(false).voto(5).commento("ok").build();
        Prestito p2 = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(false).voto(5).commento("ok").build();
        assertFalse(p1.equals(p2));
    }
}
