package presenter.libropresenter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.libromanagement.Libro;
import model.libromanagement.LibroDAO;
import model.posizionemanagement.Posizione;
import model.posizionemanagement.PosizioneDAO;
import model.utentemanagement.Utente;
import model.utentemanagement.UtenteDAO;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class LibroPresenterTest {
    private LibroPresenter lp;
    private HttpServletResponse response;
    private HttpServletRequest request;
    private LibroDAO libroDAO;
    private UtenteDAO utenteDAO;
    private PosizioneDAO posizioneDAO;
    private PrintWriter pw;
    private BufferedReader br;

    @Before
    public void setUp() {
        libroDAO=Mockito.mock(LibroDAO.class);
        utenteDAO=Mockito.mock(UtenteDAO.class);
        posizioneDAO=Mockito.mock(PosizioneDAO.class);
        lp=new LibroPresenter(libroDAO,utenteDAO,posizioneDAO);
        response= Mockito.mock(HttpServletResponse.class);
        request=Mockito.mock(HttpServletRequest.class);
        try {
            pw=new PrintWriter("src/test/java/testing.txt");
            br = new BufferedReader(new FileReader("src/test/java/testing.txt"));
        } catch (FileNotFoundException e) {
            fail("Il file dovrebbe essere presente");
        }
    }

    @Test
    public void mostraRicercaLibriTest(){
        when(request.getPathInfo()).thenReturn("/mostra-ricerca-libri");
        when(request.getParameter("is_admin")).thenReturn("true");
        ArrayList<String> categorie=new ArrayList<>();
        categorie.add("lettere");
        categorie.add("informatica");
        try {
            when(response.getWriter()).thenReturn(pw);
            when(libroDAO.doRetrieveAllCategorie()).thenReturn(categorie);
            assertDoesNotThrow(()->lp.doPost(request,response));
            pw.flush();
            String linea = br.readLine();
            assertEquals(Libro.toJsonCategorie(categorie),linea);
        } catch (Exception e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }

    @Test
    public void ricercaLibriTest(){
        when(request.getPathInfo()).thenReturn("/ricerca-libri");
        String autore="lettere";
        when(request.getParameter("ricerca")).thenReturn("Alessandro Manzoni");
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l1= new Libro.LibroBuilder().annoPubbl(1980).autore("Alessandro Manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("ciao").titolo("Promessi Sposi").isbn("9788891904454C").nCopie(5).build();
        Libro l2= new Libro.LibroBuilder().annoPubbl(1980).autore("Alessandro Manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("ciao").titolo("Promessi Sposi").isbn("9788891904454A").nCopie(5).build();
        Libro l3= new Libro.LibroBuilder().annoPubbl(1980).autore("Alessandro Manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("ciao").titolo("Promessi Sposi").isbn("9788891904454B").nCopie(5).build();
        ArrayList<Libro> libri=new ArrayList<>();
        libri.add(l1);
        libri.add(l2);
        libri.add(l3);
        try {
            when(response.getWriter()).thenReturn(pw);
            when(libroDAO.doRetrieveByTitoloAutore(autore)).thenReturn(libri);
            assertDoesNotThrow(()->lp.doPost(request,response));
            pw.flush();
            String linea = br.readLine();
            assertEquals(Libro.toJson(libri),linea);
        } catch (Exception e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }

    @Test
    public void ricercaLibriPerCategoriaTest(){
        when(request.getPathInfo()).thenReturn("/ricerca-libri-categoria");
        String categoria="lettere";
        when(request.getParameter("ricerca")).thenReturn(categoria);
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l1= new Libro.LibroBuilder().annoPubbl(1980).autore("Alessandro Manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("ciao").titolo("Promessi Sposi").isbn("9788891904454C").nCopie(5).build();
        Libro l2= new Libro.LibroBuilder().annoPubbl(1980).autore("Alessandro Manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("ciao").titolo("Promessi Sposi").isbn("9788891904454A").nCopie(5).build();
        Libro l3= new Libro.LibroBuilder().annoPubbl(1980).autore("Alessandro Manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("ciao").titolo("Promessi Sposi").isbn("9788891904454B").nCopie(5).build();
        ArrayList<Libro> libri=new ArrayList<>();
        libri.add(l1);
        libri.add(l2);
        libri.add(l3);
        try {
            when(response.getWriter()).thenReturn(pw);
            when(libroDAO.doRetrieveByCategoria(categoria)).thenReturn(libri);
            assertDoesNotThrow(()->lp.doPost(request,response));
            pw.flush();
            String linea = br.readLine();
            assertEquals(Libro.toJson(libri),linea);
        } catch (Exception e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }

    @Test
    public void rimuoviInteresseTest(){
        when(request.getPathInfo()).thenReturn("/rimuovi-interesse");
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(1980).autore("Alessandro Manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("ciao").titolo("Promessi Sposi").isbn("9788891904454C").nCopie(5).build();
        Utente utente=new Utente.UtenteBuilder().email("pasquale@studenti.unisa.it").password("test_pword").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        utente.getInteressi().add(l);
        when(request.getParameter("email")).thenReturn(utente.getEmail());
        when(request.getParameter("isbn")).thenReturn(l.getIsbn());
        try {
            when(response.getWriter()).thenReturn(pw);
            when(utenteDAO.doRetrieveByEmailAll(utente.getEmail())).thenReturn(utente);
            when(libroDAO.doRetrieveByCodiceISBN(l.getIsbn())).thenReturn(l);
            when(libroDAO.doDeleteInteresse(utente.getEmail(),l.getIsbn())).thenReturn(true);
            assertDoesNotThrow(()->lp.doPost(request,response));
            assertFalse(utente.getInteressi().contains(l));
            pw.flush();
            String linea = br.readLine();
            JSONObject json=new JSONObject();
            json.put("Utente",Utente.toJson(utente));
            assertEquals(json.toString(),linea);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }

    @Test
    public void aggiungiInteresse(){
        when(request.getPathInfo()).thenReturn("/aggiungi-interesse");
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(1980).autore("Alessandro Manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("ciao").titolo("Promessi Sposi").isbn("9788891904454C").nCopie(5).build();
        Utente utente=new Utente.UtenteBuilder().email("pasquale@studenti.unisa.it").password("test_pword").nome("test_nome").cognome("test_cognome").admin(false).nuovo(true).build();
        when(request.getParameter("email")).thenReturn(utente.getEmail());
        when(request.getParameter("isbn")).thenReturn(l.getIsbn());
        try {
            when(response.getWriter()).thenReturn(pw);
            when(utenteDAO.doRetrieveByEmailAll(utente.getEmail())).thenReturn(utente);
            when(libroDAO.doRetrieveByCodiceISBN(l.getIsbn())).thenReturn(l);
            when(libroDAO.doAddInteresse(utente.getEmail(),l.getIsbn())).thenReturn(true);
            assertDoesNotThrow(()->lp.doPost(request,response));
            assertTrue(utente.getInteressi().contains(l));
            pw.flush();
            String linea = br.readLine();
            JSONObject json=new JSONObject();
            json.put("Utente",Utente.toJson(utente));
            assertEquals(json.toString(),linea);
        } catch (Exception e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }

    @Test
    public void creaLibro(){
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(1980).autore("alessandro manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").titolo("Promessi Sposi").isbn("978190445Z").nCopie(5).build();
        when(request.getPathInfo()).thenReturn("/crea-libro");
        when(request.getParameter("libro")).thenReturn(Libro.toJson(l));
        try {
            when(response.getWriter()).thenReturn(pw);
            when(posizioneDAO.doRetrieveByBibliotecaZona(l.getPosizione().getBiblioteca(),l.getPosizione().getZona())).thenReturn(p);
            when(libroDAO.existCategoria(l.getCategoria())).thenReturn(true);
            when(libroDAO.insert(l)).thenReturn(true);
            assertDoesNotThrow(()->lp.doPost(request,response));
            pw.flush();
            String linea = br.readLine();
            assertEquals("Salvataggio avvenuto con successo",linea);
        } catch (Exception e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
}
