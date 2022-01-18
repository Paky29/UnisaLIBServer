package model.utentemanagement;

import model.libromanagement.Libro;
import model.posizionemanagement.Posizione;
import model.postazionemanagement.Postazione;
import model.prenotazionemanagement.Prenotazione;
import model.prestitomanagement.Prestito;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

public class UtenteDAOTest {
    UtenteDAO utenteDAO;

    @Before
    public void setUp(){
        utenteDAO=new UtenteDAO();
    }


    @Test
    public void doRetrieveByEmailAndPasswordAll() {
        Utente utente=new Utente.UtenteBuilder().email("dd").password("donia").nome("daniele").cognome("donia").admin(false).nuovo(true).build();
        Posizione pos = new Posizione(1, "umanistica", "piano 1");
        Libro lib = new Libro.LibroBuilder().isbn("0195153448").titolo("Classical Mythology").autore("Mark P. O. Morford").editore("Oxford University Press").annoPubbl(2002).nCopie(3).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").categoria("lettere").posizione(pos).build();
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(new GregorianCalendar(2022, 0, 17)).dataFine(new GregorianCalendar(2022, 1, 17)).dataConsegna(new GregorianCalendar(2022, 0, 19)).voto(5).commento("ok").attivo(false).build();
        Postazione post = new Postazione("1", true, pos);
        Prenotazione pr = new Prenotazione(new GregorianCalendar(2022, 0, 18), 9, 11, utente, post);
        utente.getPrestiti().add(p);
        utente.getPrenotazioni().add(pr);
        utente.getInteressi().add(lib);

        final Utente[] utente_test = new Utente[1];
        assertDoesNotThrow(() -> utente_test[0] =utenteDAO.doRetrieveByEmailAndPasswordAll("dd", "donia"));
        assertEquals(utente, utente_test[0]);

    }

    @Test
    public void doRetrieveByEmailAll() {
        Utente utente=new Utente.UtenteBuilder().email("dd").password("donia").nome("daniele").cognome("donia").admin(false).nuovo(true).build();
        Posizione pos = new Posizione(1, "umanistica", "piano 1");
        Libro lib = new Libro.LibroBuilder().isbn("0195153448").titolo("Classical Mythology").autore("Mark P. O. Morford").editore("Oxford University Press").annoPubbl(2002).nCopie(3).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").categoria("lettere").posizione(pos).build();
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(new GregorianCalendar(2022, 0, 17)).dataFine(new GregorianCalendar(2022, 1, 17)).dataConsegna(new GregorianCalendar(2022, 0, 19)).voto(5).commento("ok").attivo(false).build();
        Postazione post = new Postazione("1", true, pos);
        Prenotazione pr = new Prenotazione(new GregorianCalendar(2022, 0, 18), 9, 11, utente, post);
        utente.getPrestiti().add(p);
        utente.getPrenotazioni().add(pr);
        utente.getInteressi().add(lib);

        final Utente[] utente_test = new Utente[1];
        assertDoesNotThrow(() -> utente_test[0] =utenteDAO.doRetrieveByEmailAll("dd"));
        assertEquals(utente, utente_test[0]);

    }

    @Test
    public void doRetrieveByWrongEmailAll() {
        String email="dr";
        final Utente[] utente_test = new Utente[1];
        assertDoesNotThrow(() -> utente_test[0] =utenteDAO.doRetrieveByEmailAll(email));
        assertNull(utente_test[0]);

    }

    @Test
    public void doRetrieveByWrongEmailandPasswordAll() {
        String email="dr";
        String password="doria";
        final Utente[] utente_test = new Utente[1];
        assertDoesNotThrow(() -> utente_test[0] =utenteDAO.doRetrieveByEmailAndPasswordAll(email, password));
        assertNull(utente_test[0]);

    }




}
