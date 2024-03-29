package presenter.utentepresenter;

import model.utentemanagement.Utente;
import model.utentemanagement.UtenteDAO;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * Questa classe si occupa di testare PostazionePresenter
 */
public class UtentePresenterTest {
    UtenteDAO utenteDAO;
    UtentePresenter utentePresenter;
    HttpServletRequest request;
    HttpServletResponse response;
    PrintWriter pw;
    BufferedReader br;

    @Before
    public void setUp(){
        utenteDAO=Mockito.mock(UtenteDAO.class);
        utentePresenter=new UtentePresenter(utenteDAO);
        response= Mockito.mock(HttpServletResponse.class);
        request=Mockito.mock(HttpServletRequest.class);
        try {
            pw=new PrintWriter("src/test/java/testing.txt");
            br = new BufferedReader(new FileReader("src/test/java/testing.txt"));
        } catch (FileNotFoundException e) {
            fail("File testing.txt non trovato");
        }
    }
    /**
     * Testa il corretto funzionamento della servlet login
     */
    @Test
    public void loginTest() {
        JSONObject jsonObject= new JSONObject();
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("test_email@studenti.unisa.it");
        when(request.getParameter("pass")).thenReturn("Testpword1?");

        try {
            when(response.getWriter()).thenReturn(pw);
            when(utenteDAO.doRetrieveByEmailAndPasswordAll("test_email@studenti.unisa.it", "Testpword1?")).thenReturn(utente);
            assertDoesNotThrow(() -> utentePresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            jsonObject.put("Utente", Utente.toJson(utente));
            assertEquals(jsonObject.toString(), linea);

        } catch (IOException | SQLException | JSONException ex) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }
    /**
     * Testa il corretto funzionamento della servlet login in caso di formato email errato
     */
    @Test
    public void loginWrongEmailFormatTest() {
        Utente utente =new Utente.UtenteBuilder().email("test_email").password("TestPword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("test_email");
        when(request.getParameter("pass")).thenReturn("test_pword");

        try {
            when(response.getWriter()).thenReturn(pw);
            when(utenteDAO.doRetrieveByEmailAndPasswordAll("test_email", "test_pword")).thenReturn(utente);
            assertDoesNotThrow(() -> utentePresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals("Email non valida", linea);

        } catch (IOException | SQLException ex ) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }

    @Test
    public void loginWrongPathTest() {
        when(request.getPathInfo()).thenReturn(null);
        assertDoesNotThrow(() -> utentePresenter.doPost(request, response));
        try {
            verify(response, only()).getWriter();
        } catch (IOException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
    /**
     * Testa il corretto funzionamento della servlet login in caso di formato email inesistente
     */
    @Test
    public void loginEmailNotExistTest() {
        when(request.getPathInfo()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("testemail_nonpresente@studenti.unisa.it");
        when(request.getParameter("pass")).thenReturn("Testpword1?");

        try {
            when(response.getWriter()).thenReturn(pw);
            when(utenteDAO.doRetrieveByEmailAndPasswordAll("testemail_nonpresente@studenti.unisa.it", "Testpword1?")).thenReturn(null);
            assertDoesNotThrow(() -> utentePresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals("Utente non trovato", linea);

        } catch (IOException | SQLException ex ) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }
    /**
     * Testa il corretto funzionamento della servlet login in caso di password inesistente
     */
    @Test
    public void loginPasswordNotExistTest() {
        when(request.getPathInfo()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("test_email@studenti.unisa.it");
        when(request.getParameter("pass")).thenReturn("TestpwordNotExist1?");

        try {
            when(response.getWriter()).thenReturn(pw);
            when(utenteDAO.doRetrieveByEmailAndPasswordAll("testemail_nonpresente@studenti.unisa.it", "TestpwordNotExist1?")).thenReturn(null);
            assertDoesNotThrow(() -> utentePresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals("Utente non trovato", linea);

        } catch (IOException | SQLException ex ) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }
    /**
     * Testa il corretto funzionamento della servlet login in caso di email e password null
     */
    @Test
    public void loginUtenteEmailAndPWordNullTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn(null);
        when(request.getParameter("pass")).thenReturn(null);

        try {
            when(response.getWriter()).thenReturn(pw);
            when(utenteDAO.doRetrieveByEmailAndPasswordAll("test_email@studenti.unisa.it", "Testpword1?")).thenReturn(utente);
            assertDoesNotThrow(() -> utentePresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals("Email o password non inserita", linea);

        } catch (IOException | SQLException ex ) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }

}
