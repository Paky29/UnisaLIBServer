package presenter.prestitopresenter;

import model.libromanagement.Libro;
import model.libromanagement.LibroDAO;
import model.posizionemanagement.Posizione;
import model.prestitomanagement.Prestito;
import model.prestitomanagement.PrestitoDAO;
import model.utentemanagement.Utente;
import model.utentemanagement.UtenteDAO;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import presenter.utentepresenter.UtentePresenter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

public class PrestitoPresenterTest {
    PrestitoDAO prestitoDAO;
    UtenteDAO utenteDAO;
    LibroDAO libroDAO;
    PrestitoPresenter prestitoPresenter;
    HttpServletRequest request;
    HttpServletResponse response;
    PrintWriter pw;
    BufferedReader br;

    @Before
    public void setUp(){
        prestitoDAO= Mockito.mock(PrestitoDAO.class);
        utenteDAO=Mockito.mock(UtenteDAO.class);
        libroDAO=Mockito.mock(LibroDAO.class);
        prestitoPresenter=new PrestitoPresenter(prestitoDAO, utenteDAO, libroDAO);
        response= Mockito.mock(HttpServletResponse.class);
        request=Mockito.mock(HttpServletRequest.class);
        try {
            pw=new PrintWriter("src/test/java/testing.txt");
            br = new BufferedReader(new FileReader("src/test/java/testing.txt"));
        } catch (FileNotFoundException e) {
            fail("File testing.txt non trovato");
        }
    }

    @Test
    public void AllPrestitiTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/all-prestiti");
        when(request.getParameter("utente")).thenReturn("test_email@studenti.unisa.it");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2022, 4, 12)).attivo(true).build();
        ArrayList<Prestito> prestiti= new ArrayList<>();
        prestiti.add(p);

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveByUtente("test_email@studenti.unisa.it")).thenReturn(prestiti);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals(Prestito.toJson(prestiti), linea);

        } catch (IOException | SQLException  ex) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }

    @Test
    public void AllPrestitiEmptyTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/all-prestiti");
        when(request.getParameter("utente")).thenReturn("test_email@studenti.unisa.it");
        ArrayList<Prestito> prestiti= new ArrayList<>();

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveByUtente("test_email@studenti.unisa.it")).thenReturn(prestiti);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals("Non sono presenti prestiti", linea);

        } catch (IOException | SQLException  ex) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }

    @Test
    public void CercaPrestitiAttiviPerLibroTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/lista-prestiti-libro");
        when(request.getParameter("libro")).thenReturn("isbn_test");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2022, 4, 12)).attivo(true).build();
        ArrayList<Prestito> prestiti= new ArrayList<>();
        prestiti.add(p);

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveValidByLibro("isbn_test")).thenReturn(prestiti);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals(Prestito.toJson(prestiti), linea);

        } catch (IOException | SQLException  ex) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }

    @Test
    public void CreaPrestitoUtenteNotExistTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_emailnotexist@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/crea-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2022, 4, 12)).attivo(true).build();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveValidByUtente(utente.getEmail())).thenReturn(null);
            when(libroDAO.doRetrieveByCodiceISBN(lib.getIsbn())).thenReturn(lib);
            when(utenteDAO.doRetrieveByEmail(utente.getEmail())).thenReturn(null);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals("Utente o libro non trovato", linea);

        } catch (IOException | SQLException  ex) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }

    @Test
    public void CreaPrestitoLibroInPossessoTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/crea-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2022, 4, 12)).attivo(false).build();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveValidByUtente(utente.getEmail())).thenReturn(p);
            when(libroDAO.doRetrieveByCodiceISBN(lib.getIsbn())).thenReturn(lib);
            when(utenteDAO.doRetrieveByEmail(utente.getEmail())).thenReturn(utente);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals("Hai il libro in prestito. Controlla nella sezione Miei Prestiti", linea);

        } catch (IOException | SQLException  ex) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }

    @Test
    public void CreaPrestitoWithPrestitoAttivoTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/crea-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro libPossesso = new Libro.LibroBuilder().isbn("isbn_test_2").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 12);
        Prestito p_attivo = new Prestito.PrestitoBuilder().utente(utente).libro(libPossesso).dataInizio(dataInizio).dataFine(new GregorianCalendar(2022, 4, 12)).attivo(true).build();
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2022, 4, 12)).attivo(false).build();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveValidByUtente(utente.getEmail())).thenReturn(p_attivo);
            when(libroDAO.doRetrieveByCodiceISBN(lib.getIsbn())).thenReturn(lib);
            when(utenteDAO.doRetrieveByEmail(utente.getEmail())).thenReturn(utente);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals("Limite prestiti ecceduto: puoi prendere in prestito solo un libro alla volta", linea);

        } catch (IOException | SQLException  ex) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }

    @Test
    public void CreaPrestitoInsertErrorTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/crea-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro libPossesso = new Libro.LibroBuilder().isbn("isbn_test_2").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 12);
        Prestito p_attivo = new Prestito.PrestitoBuilder().utente(utente).libro(libPossesso).dataInizio(dataInizio).dataFine(new GregorianCalendar(2022, 4, 12)).attivo(true).build();
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2022, 4, 12)).attivo(false).build();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveValidByUtente(utente.getEmail())).thenReturn(null);
            when(prestitoDAO.insert(p)).thenReturn(false);
            when(libroDAO.doRetrieveByCodiceISBN(lib.getIsbn())).thenReturn(lib);
            when(utenteDAO.doRetrieveByEmail(utente.getEmail())).thenReturn(utente);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals("Salvataggio non andato a buon fine", linea);

        } catch (IOException | SQLException  ex) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }

    @Test
    public void CreaPrestitoDataInizioErrorTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/crea-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 5, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2023, 4, 12)).attivo(false).build();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveValidByUtente(utente.getEmail())).thenReturn(null);
            when(prestitoDAO.insert(p)).thenReturn(false);
            when(libroDAO.doRetrieveByCodiceISBN(lib.getIsbn())).thenReturn(lib);
            when(utenteDAO.doRetrieveByEmail(utente.getEmail())).thenReturn(utente);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals("Data inizio successiva alla data corrente", linea);

        } catch (IOException | SQLException  ex) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }

    @Test
    public void CreaPrestitoLibroNonDisponibileTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/crea-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro libNonDisp = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(0).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(libNonDisp).dataInizio(dataInizio).dataFine(new GregorianCalendar(2023, 4, 12)).attivo(false).build();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveValidByUtente(utente.getEmail())).thenReturn(null);
            when(libroDAO.doRetrieveByCodiceISBN(p.getLibro().getIsbn())).thenReturn(libNonDisp);
            when(utenteDAO.doRetrieveByEmail(utente.getEmail())).thenReturn(utente);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals("Non ci sono copie disponibili. Riprovare più tardi", linea);

        } catch (IOException | SQLException  ex) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }

    @Test
    public void CreaPrestitoTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/crea-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2023, 4, 12)).attivo(false).build();
        JSONObject jsonObject=new JSONObject();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));

        ArrayList<Prestito> prestiti = new ArrayList<>();
        prestiti.add(p);
        Utente utente_final=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).prestiti(prestiti).build();

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveValidByUtente(utente.getEmail())).thenReturn(null);
            when(prestitoDAO.insert(p)).thenReturn(true);
            when(libroDAO.doRetrieveByCodiceISBN(p.getLibro().getIsbn())).thenReturn(lib);
            when(utenteDAO.doRetrieveByEmail(utente.getEmail())).thenReturn(utente);
            when(utenteDAO.doRetrieveByEmailAll(utente.getEmail())).thenReturn(utente_final);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            System.out.println(linea);
            jsonObject.put("Utente", Utente.toJson(utente_final));
            System.out.println(jsonObject.toString());
            assertEquals(jsonObject.toString(), linea);

        } catch (IOException | SQLException  | JSONException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }


}
