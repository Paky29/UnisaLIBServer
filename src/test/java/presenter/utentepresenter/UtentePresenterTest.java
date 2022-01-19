package presenter.utentepresenter;

import model.utentemanagement.Utente;
import model.utentemanagement.UtenteDAO;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UtentePresenterTest {
    UtenteDAO utenteDAO;
    UtentePresenter utentePresenter;

    @Before
    public void setUp(){
        utentePresenter = new UtentePresenter();
        utenteDAO = mock(UtenteDAO.class);
    }

    @Test
    public void loginTest() {
        Utente utente_mock=null;
        Utente utente=new Utente.UtenteBuilder().email("test_email").password("test_pword").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        try {
            when(utenteDAO.doRetrieveByEmailAndPasswordAll("test_email", "test_pword")).thenReturn(utente);
            utente_mock=utenteDAO.doRetrieveByEmailAndPasswordAll("test_email", "test_pword");
            String login=utentePresenter.login("test_email", "test_pword");
            Utente utente_test=Utente.fromJson(login);
            System.out.println(utente_test.toString());
            System.out.println(utente_mock.toString());
            assertEquals(utente_mock, utente_test);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
