package model.utentemanagement;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UtenteTest {
    @Test
    public void equalsTest(){
        Utente utente1=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        Utente utente2=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        assertTrue(utente1.equals(utente2));
    }

    @Test
    public void equalsSameUtenteTest(){
        Utente utente1=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        assertTrue(utente1.equals(utente1));
    }

    @Test
    public void equalsUtenteNullTest(){
        Utente utente1=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        Utente utente2=null;
        assertFalse(utente1.equals(utente2));
    }

    @Test
    public void equalsNomeErrorTest(){
        Utente utente1=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome1").cognome("test_cognome").admin(false).nuovo(true).build();
        Utente utente2=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome2").cognome("test_cognome").admin(false).nuovo(true).build();
        assertFalse(utente1.equals(utente2));
    }

    @Test
    public void equalsCognomeErrorTest(){
        Utente utente1=new Utente.UtenteBuilder().email("test_email1@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome1").admin(false).nuovo(true).build();
        Utente utente2=new Utente.UtenteBuilder().email("test_email2@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome2").admin(false).nuovo(true).build();
        assertFalse(utente1.equals(utente2));
    }

    @Test
    public void equalsEmailErrorTest(){
        Utente utente1=new Utente.UtenteBuilder().email("test_email1@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        Utente utente2=new Utente.UtenteBuilder().email("test_email2@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        assertFalse(utente1.equals(utente2));
    }

    @Test
    public void equalsIsAdminErrorTest(){
        Utente utente1=new Utente.UtenteBuilder().email("test_email1@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(true).nuovo(true).build();
        Utente utente2=new Utente.UtenteBuilder().email("test_email2@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        assertFalse(utente1.equals(utente2));
    }


}
