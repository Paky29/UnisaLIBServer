package model.libromanagement;

import model.posizionemanagement.Posizione;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LibroDAOTest {
    private LibroDAO libroDAO;

    @Before
    public void setUp(){
        libroDAO=new LibroDAO();
    }

    @Test
    public void insertTest(){
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(1980)
                .autore("Alessandro Manzoni")
                .categoria("lettere")
                .editore("Mondadori")
                .posizione(p)
                .urlCopertina("ciao")
                .titolo("Promessi Sposi")
                .isbn("test")
                .nCopie(5)
                .build();
        assertDoesNotThrow(() -> libroDAO.insert(l));
        try {
            Libro libro_test = libroDAO.doRetrieveByCodiceISBN(l.getIsbn());
            assertEquals(l.getIsbn(),libro_test.getIsbn());
            assertEquals(l.getAutore(),libro_test.getAutore());
            assertEquals(l.getnCopie(),libro_test.getnCopie());
            assertEquals(l.getCategoria(),libro_test.getCategoria());
            assertEquals(l.getAnnoPubbl(),libro_test.getAnnoPubbl());
            assertEquals(l.getUrlCopertina(),libro_test.getUrlCopertina());
            assertEquals(l.getEditore(),libro_test.getEditore());
            assertEquals(l.getPosizione(),libro_test.getPosizione());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertIncorrectPosizioneTest(){
        Posizione p=new Posizione(23,"linguistica","piano 11");
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(1980)
                .autore("Alessandro Manzoni")
                .categoria("lettere")
                .editore("Mondadori")
                .posizione(p)
                .urlCopertina("ciao")
                .titolo("Promessi Sposi")
                .isbn("test_incorretto")
                .nCopie(5)
                .build();
        assertThrows(SQLException.class, ()->libroDAO.insert(l));
        try {
            assertNull(libroDAO.doRetrieveByCodiceISBN(l.getIsbn()));
        }catch(SQLException e){
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }

    @Test(expected = SQLException.class)
    public void insertIncorrectCategoriaTest() throws SQLException{
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(1980)
                .autore("Alessandro Manzoni")
                .categoria("fantascienza")
                .editore("Mondadori")
                .posizione(p)
                .urlCopertina("ciao")
                .titolo("Promessi Sposi")
                .isbn("test_incorretto")
                .nCopie(5)
                .build();
        assertFalse(libroDAO.insert(l));
        assertNull(libroDAO.doRetrieveByCodiceISBN(l.getIsbn()));
    }

    @Test
    public void doRetrieveByCodiceISBNTest() {
        String isbn="test";
        final Libro[] libro_test = new Libro[1];
        assertDoesNotThrow(() -> libro_test[0] =libroDAO.doRetrieveByCodiceISBN(isbn));
        assertEquals(isbn, libro_test[0].getIsbn());
    }

    @Test
    public void doRetrieveByAutoreTest(){
        String autore="Alessandro Manzoni";
        AtomicReference<ArrayList<Libro>> libri_test=new AtomicReference<>();
        assertDoesNotThrow(()-> libri_test.set(libroDAO.doRetrieveByTitoloAutore(autore)));
        for(Libro l: libri_test.get())
            assertEquals(autore,l.getAutore());
    }

    @Test
    public void doRetrieveByTitoloTest(){
        String titolo="Promessi Sposi";
        AtomicReference<ArrayList<Libro>> libri_test=new AtomicReference<>();
        assertDoesNotThrow(()-> libri_test.set(libroDAO.doRetrieveByTitoloAutore(titolo)));
        for(Libro l:libri_test.get())
            assertEquals(titolo,l.getTitolo());
    }

    @Test
    public void doRetrieveByCategoriaTest(){
        String categoria="lettere";
        AtomicReference<ArrayList<Libro>> libri_test=new AtomicReference<>();
        assertDoesNotThrow(()-> libri_test.set(libroDAO.doRetrieveByCategoria(categoria)));
        for(Libro l:libri_test.get())
            assertEquals(categoria,l.getCategoria());
    }

    @Test
    public void doAddInteresseTest(){
        String email="ps";
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(1980)
                .autore("Alessandro Manzoni")
                .categoria("lettere")
                .editore("Mondadori")
                .posizione(p)
                .urlCopertina("ciao")
                .titolo("Promessi Sposi")
                .isbn("test")
                .nCopie(5)
                .build();
        assertDoesNotThrow(()->libroDAO.doAddInteresse(email,l.getIsbn()));
        try {
            ArrayList<Libro>libri_test = libroDAO.doRetrieveInteresse(email);
            assertTrue(libri_test.contains(l));
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }

    @Test
    public void doRetrieveInteresseTest(){
        String email="ps";
        Posizione p=new Posizione(1,"umanistica","piano 1");
        ArrayList<Libro> interessi=new ArrayList<>();
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(1980)
                .autore("Alessandro Manzoni")
                .categoria("lettere")
                .editore("Mondadori")
                .posizione(p)
                .urlCopertina("ciao")
                .titolo("Promessi Sposi")
                .isbn("test")
                .nCopie(5)
                .build();
        interessi.add(l);
        AtomicReference<ArrayList<Libro>> libri_test=new AtomicReference<>();
        assertDoesNotThrow(()-> libri_test.set(libroDAO.doRetrieveInteresse(email)));
        assertIterableEquals(interessi,libri_test.get());
    }

    @Test(expected = SQLException.class)
    public void doAddInteresseIncorretISBNTest() throws SQLException{
        String email="ps";
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(1980)
                .autore("Alessandro Manzoni")
                .categoria("lettere")
                .editore("Mondadori")
                .posizione(p)
                .urlCopertina("ciao")
                .titolo("Promessi Sposi")
                .isbn("test_incorretto")
                .nCopie(5)
                .build();
        assertFalse(libroDAO.doAddInteresse(email,l.getIsbn()));
        AtomicReference<ArrayList<Libro>> libri_test=new AtomicReference<>();
        assertThrows(SQLException.class,()-> libri_test.set(libroDAO.doRetrieveInteresse(email)));
        assertFalse(libri_test.get().contains(l));
    }

    @Test(expected = SQLException.class)
    public void doAddInteresseIncorretEmailTest() throws SQLException{
        String email="ps_incorretta";
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(1980)
                .autore("Alessandro Manzoni")
                .categoria("lettere")
                .editore("Mondadori")
                .posizione(p)
                .urlCopertina("ciao")
                .titolo("Promessi Sposi")
                .isbn("9788891904454C")
                .nCopie(5)
                .build();
        assertFalse(libroDAO.doAddInteresse(email,l.getIsbn()));
    }

    @Test
    public void doDeleteInteresseTest(){
        String email="ps";
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(1980)
                .autore("Alessandro Manzoni")
                .categoria("lettere")
                .editore("Mondadori")
                .posizione(p)
                .urlCopertina("ciao")
                .titolo("Promessi Sposi")
                .isbn("test")
                .nCopie(5)
                .build();
        ArrayList<Libro> libri_test= null;
        try {
            assertTrue(libroDAO.doDeleteInteresse(email,l.getIsbn()));
            libri_test = libroDAO.doRetrieveInteresse(email);
            assertFalse(libri_test.contains(l));
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }

    @Test
    public void doDeleteInteresseIncorrectISBNTest() {
        String email="ps";
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(1980)
                .autore("Alessandro Manzoni")
                .categoria("lettere")
                .editore("Mondadori")
                .posizione(p)
                .urlCopertina("ciao")
                .titolo("Promessi Sposi")
                .isbn("test_incorretto")
                .nCopie(5)
                .build();
        ArrayList<Libro> libri= null;
        try {
            libri = libroDAO.doRetrieveInteresse(email);
            assertFalse(libroDAO.doDeleteInteresse(email,l.getIsbn()));
            ArrayList<Libro> libri_test=libroDAO.doRetrieveInteresse(email);
            assertIterableEquals(libri,libri_test);
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }

    @Test
    public void doDeleteInteresseIncorrectEmailTest(){
        String email="ps_incorretta";
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(1980)
                .autore("Alessandro Manzoni")
                .categoria("lettere")
                .editore("Mondadori")
                .posizione(p)
                .urlCopertina("ciao")
                .titolo("Promessi Sposi")
                .isbn("9788891904454C")
                .nCopie(5)
                .build();
        try {
            assertFalse(libroDAO.doDeleteInteresse(email,l.getIsbn()));
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }

    @Test
    public void existCategoriaTest(){
        String categoria="lettere";
        boolean risultato;
        risultato=libroDAO.existCategoria(categoria);
        assertTrue(risultato);
    }
}
