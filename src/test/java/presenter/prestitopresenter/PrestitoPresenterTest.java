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
import java.util.Calendar;
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
    public void allPrestitiTest() {
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
    public void allPrestitiEmptyTest() {
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
    public void cercaPrestitiAttiviPerLibroTest() {
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
    public void cercaPrestitiAttiviPerLibroEmptyTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/lista-prestiti-libro");
        when(request.getParameter("libro")).thenReturn("isbn_test");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        ArrayList<Prestito> prestiti= new ArrayList<>();

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveValidByLibro(lib.getIsbn())).thenReturn(prestiti);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            assertEquals("Non sono presenti prestiti", linea);

        } catch (IOException | SQLException  ex) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }

    @Test
    public void creaPrestitoUtenteNotExistTest() {
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
    public void creaPrestitoLibroInPossessoTest() {
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
    public void creaPrestitoWithPrestitoAttivoTest() {
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
    public void creaPrestitoInsertErrorTest() {
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
    public void creaPrestitoDataInizioErrorTest() {
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
    public void creaPrestitoLibroNonDisponibileTest() {
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
    public void creaPrestitoTest() {
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

    @Test
    public void valutaPrestitoWithoutCommentTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/valuta-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2023, 4, 12)).dataConsegna(dataInizio).attivo(false).voto(5).build();
        JSONObject jsonObject=new JSONObject();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));

        ArrayList<Prestito> prestiti = new ArrayList<>();
        prestiti.add(p);
        Utente utente_final=new Utente.UtenteBuilder().email(utente.getEmail()).password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).prestiti(prestiti).build();
        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveByKey(p.getDataInizio(), p.getLibro().getIsbn(), p.getUtente().getEmail())).thenReturn(p);
            when(prestitoDAO.valutaPrestito(p)).thenReturn(true);
            when(utenteDAO.doRetrieveByEmailAll(p.getUtente().getEmail())).thenReturn(utente_final);
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

    @Test
    public void valutaPrestitoWithCommentTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/valuta-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2023, 4, 12)).dataConsegna(dataInizio).attivo(false).voto(5).commento("ok").build();
        JSONObject jsonObject=new JSONObject();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));

        ArrayList<Prestito> prestiti = new ArrayList<>();
        prestiti.add(p);
        Utente utente_final=new Utente.UtenteBuilder().email(utente.getEmail()).password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).prestiti(prestiti).build();
        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveByKey(p.getDataInizio(), p.getLibro().getIsbn(), p.getUtente().getEmail())).thenReturn(p);
            when(prestitoDAO.valutaPrestito(p)).thenReturn(true);
            when(utenteDAO.doRetrieveByEmailAll(p.getUtente().getEmail())).thenReturn(utente_final);
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

    @Test
    public void valutaPrestitoNotExistTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email_not_exist@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/valuta-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2023, 4, 12)).dataConsegna(dataInizio).attivo(false).voto(5).commento("ok").build();
        JSONObject jsonObject=new JSONObject();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveByKey(p.getDataInizio(), p.getLibro().getIsbn(), p.getUtente().getEmail())).thenReturn(null);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            System.out.println(linea);
            System.out.println(jsonObject.toString());
            assertEquals("Prestito non trovato", linea);

        } catch (IOException | SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }

    @Test
    public void valutaPrestitoVotoWrongFormatTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/valuta-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2023, 4, 12)).dataConsegna(dataInizio).attivo(false).voto(0).commento("ok").build();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));

        try {
            when(response.getWriter()).thenReturn(pw);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            System.out.println(linea);
            assertEquals("Voto o commento non rispettano il formato", linea);

        } catch (IOException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }

    @Test
    public void valutaPrestitoCommentoWrongFormatTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/valuta-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2023, 4, 12)).dataConsegna(dataInizio).attivo(false).voto(0).commento("").build();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));

        try {
            when(response.getWriter()).thenReturn(pw);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            System.out.println(linea);
            assertEquals("Voto o commento non rispettano il formato", linea);

        } catch (IOException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }

    @Test
    public void valutaPrestitoValutazioneFailedTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/valuta-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 12);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataFine(new GregorianCalendar(2023, 4, 12)).dataConsegna(dataInizio).attivo(false).voto(5).commento("ok").build();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveByKey(p.getDataInizio(), p.getLibro().getIsbn(), p.getUtente().getEmail())).thenReturn(p);
            when(prestitoDAO.valutaPrestito(p)).thenReturn(false);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            System.out.println(linea);
            assertEquals("Errore del server", linea);

        } catch (IOException | SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }

    //cambiare data con una precedente di una settimana alla data corrente
    @Test
    public void attivaPrestitoTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/attiva-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar currentDate= new GregorianCalendar();
        currentDate.add(Calendar.DATE, -4);
        GregorianCalendar dataInizio=new GregorianCalendar(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(false).voto(5).commento("ok").build();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));
        Prestito pAttivo=new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(true).voto(5).commento("ok").build();

        ArrayList<Prestito> prestiti = new ArrayList<>();
        prestiti.add(pAttivo);

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveByKey(p.getDataInizio(), p.getLibro().getIsbn(), p.getUtente().getEmail())).thenReturn(p);
            when(prestitoDAO.attivaPrestito(p)).thenReturn(true);
            when(prestitoDAO.doRetrieveValidByLibro(p.getLibro().getIsbn())).thenReturn(prestiti);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            System.out.println(linea);
            assertEquals(Prestito.toJson(prestiti), linea);

        } catch (IOException | SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }

    //cambiare data con una precedente di una settimana alla data corrente
    @Test
    public void attivaPrestitoAttivoErrorTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/attiva-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar currentDate= new GregorianCalendar();
        currentDate.add(Calendar.DATE, -4);
        GregorianCalendar dataInizio=new GregorianCalendar(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(true).voto(5).commento("ok").build();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveByKey(p.getDataInizio(), p.getLibro().getIsbn(), p.getUtente().getEmail())).thenReturn(p);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            System.out.println(linea);
            assertEquals("Prestito attivo o non trovato", linea);

        } catch (IOException | SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }

    //cambiare data con una precedente di una settimana alla data corrente
    @Test
    public void attivaPrestitoAttivazioneErrorTest() {
        Utente utente =new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/attiva-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar currentDate= new GregorianCalendar();
        currentDate.add(Calendar.DATE, -4);
        GregorianCalendar dataInizio=new GregorianCalendar(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(false).voto(5).commento("ok").build();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveByKey(p.getDataInizio(), p.getLibro().getIsbn(), p.getUtente().getEmail())).thenReturn(p);
            when(prestitoDAO.attivaPrestito(p)).thenReturn(false);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));

            pw.flush();
            String linea = br.readLine();
            System.out.println(linea);
            assertEquals("Errore del server", linea);

        } catch (IOException | SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }

    @Test
    public void concludiPrestitoTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/concludi-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        Libro lib2 = new Libro.LibroBuilder().isbn("isbn_test2").titolo("titolo_test2").autore("test_autore2").editore("test_editore2").annoPubbl(2021).nCopie(5).urlCopertina("test_url2").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 12);
        GregorianCalendar dataConsegna=new GregorianCalendar(2022, 0, 21);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataConsegna(dataConsegna).attivo(false).voto(5).commento("ok").build();
        Prestito pExist = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(true).voto(5).commento("ok").build();
        Prestito p2 = new Prestito.PrestitoBuilder().utente(utente).libro(lib2).dataInizio(dataInizio).attivo(true).voto(5).commento("ok").build();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));

        ArrayList<Prestito> prestiti = new ArrayList<>();
        prestiti.add(p2);

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveByKey(p.getDataInizio(), p.getLibro().getIsbn(), p.getUtente().getEmail())).thenReturn(pExist);
            when(prestitoDAO.concludiPrestito(p)).thenReturn(true);
            when(prestitoDAO.doRetrieveValidByLibro(p.getLibro().getIsbn())).thenReturn(prestiti);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            System.out.println(linea);
            assertEquals(Prestito.toJson(prestiti), linea);

        } catch (IOException | SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }

    @Test
    public void concludiPrestitoDataConsegnaNotValidTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/concludi-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 12);
        GregorianCalendar dataConsegna=new GregorianCalendar(2023, 0, 21);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataConsegna(dataConsegna).attivo(false).voto(5).commento("ok").build();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));


        try {
            when(response.getWriter()).thenReturn(pw);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            System.out.println(linea);
            assertEquals("Data consegna non valida", linea);

        } catch (IOException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }

    @Test
    public void concludiPrestitoDataConsegnaNotNullTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/concludi-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 12);
        GregorianCalendar dataConsegna=new GregorianCalendar(2022, 0, 21);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataConsegna(dataConsegna).attivo(false).build();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveByKey(p.getDataInizio(), p.getLibro().getIsbn(), p.getUtente().getEmail())).thenReturn(p);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            System.out.println(linea);
            assertEquals("Prestito concluso o non trovato", linea);

        } catch (IOException | SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }

    @Test
    public void concludiPrestitoConclusioneErrorTest() {
        Utente utente=new Utente.UtenteBuilder().email("test_email@studenti.unisa.it").password("Testpword1?").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getPathInfo()).thenReturn("/concludi-prestito");
        Posizione pos = new Posizione(1, "test", "test");
        Libro lib = new Libro.LibroBuilder().isbn("isbn_test").titolo("titolo_test").autore("test_autore").editore("test_editore").annoPubbl(2021).nCopie(5).urlCopertina("test_url").categoria("test").posizione(pos).build();
        GregorianCalendar dataInizio=new GregorianCalendar(2022, 0, 12);
        GregorianCalendar dataConsegna=new GregorianCalendar(2022, 0, 21);
        Prestito p = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).dataConsegna(dataConsegna).attivo(false).build();
        Prestito pExist = new Prestito.PrestitoBuilder().utente(utente).libro(lib).dataInizio(dataInizio).attivo(true).build();
        when(request.getParameter("prestito")).thenReturn(Prestito.toJson(p));

        try {
            when(response.getWriter()).thenReturn(pw);
            when(prestitoDAO.doRetrieveByKey(p.getDataInizio(), p.getLibro().getIsbn(), p.getUtente().getEmail())).thenReturn(pExist);
            when(prestitoDAO.concludiPrestito(p)).thenReturn(false);
            assertDoesNotThrow(() -> prestitoPresenter.doPost(request, response));
            pw.flush();
            String linea = br.readLine();
            System.out.println(linea);
            assertEquals("Errore del server", linea);

        } catch (IOException | SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }


    }


}
