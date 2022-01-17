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

public class UtenteDAOTest {
    UtenteDAO utenteDAO;

    @Before
    public void setUp(){
        utenteDAO=new UtenteDAO();
    }


    @Test
    public void doRetrieveByEmailAndPasswordAll(){
        Utente utente=new Utente.UtenteBuilder().email("dd").password("donia").nome("daniele").cognome("donia").admin(false).build();
        Posizione pos = new Posizione(1, "umanistica", "piano 1");
        Libro lib = new Libro.LibroBuilder().isbn("0195153448").titolo("Classical Mythology").autore("Mark P. O. Morford").editore("Oxford University Press").annoPubbl(2002).nCopie(3).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").categoria("lettere").posizione(pos).build();
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(new GregorianCalendar(2022, 0, 17)).attivo(false).build();
        Postazione post = new Postazione("1", true, pos);
        Prenotazione pr = new Prenotazione(new GregorianCalendar(2022, 0, 18), 9, 11, utente, post);

    }


}
