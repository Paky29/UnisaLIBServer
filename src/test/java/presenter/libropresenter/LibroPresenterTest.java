package presenter.libropresenter;

import model.libromanagement.Libro;
import model.libromanagement.LibroDAO;
import model.posizionemanagement.PosizioneDAO;
import model.utentemanagement.UtenteDAO;
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
            e.printStackTrace();
        }
    }

    @Test
    public void mostraRicercaLibri(){
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
            assertEquals(linea,Libro.toJsonCategorie(categorie));
        } catch (Exception e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
}
