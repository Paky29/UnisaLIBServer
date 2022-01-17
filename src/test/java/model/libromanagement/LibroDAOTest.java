package model.libromanagement;

import model.posizionemanagement.Posizione;
import org.junit.Before;
import org.junit.Test;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class LibroDAOTest {
    private LibroDAO libroDAO;

    @Before
    public void setUp(){
        libroDAO=new LibroDAO();
    }

    @Test
    public void insertTest() throws SQLException {
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
        assertDoesNotThrow(() -> libroDAO.insert(l));
        Libro libro_test=libroDAO.doRetrieveByCodiceISBN(l.getIsbn());
        assertEquals(l.getIsbn(),libro_test.getIsbn());
        assertEquals(l.getAutore(),libro_test.getAutore());
        assertEquals(l.getnCopie(),libro_test.getnCopie());
        assertEquals(l.getCategoria(),libro_test.getCategoria());
        assertEquals(l.getAnnoPubbl(),libro_test.getAnnoPubbl());
        assertEquals(l.getUrlCopertina(),libro_test.getUrlCopertina());
        assertEquals(l.getEditore(),libro_test.getEditore());
        assertEquals(l.getPosizione(),libro_test.getPosizione());
    }

    @Test(expected = SQLException.class)
    public void insertIncorrectPosizioneTest() throws SQLException {
        Posizione p=new Posizione(1,"umanistica","piano 11");
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(1980)
                .autore("Alessandro Manzoni")
                .categoria("lettere")
                .editore("Mondadori")
                .posizione(p)
                .urlCopertina("ciao")
                .titolo("Promessi Sposi")
                .isbn("9788891904451C")
                .nCopie(5)
                .build();
        libroDAO.insert(l);
        Libro libro_test=libroDAO.doRetrieveByCodiceISBN(l.getIsbn());
        assertEquals(l.getIsbn(),libro_test.getIsbn());
        assertEquals(l.getAutore(),libro_test.getAutore());
        assertEquals(l.getnCopie(),libro_test.getnCopie());
        assertEquals(l.getCategoria(),libro_test.getCategoria());
        assertEquals(l.getAnnoPubbl(),libro_test.getAnnoPubbl());
        assertEquals(l.getUrlCopertina(),libro_test.getUrlCopertina());
        assertEquals(l.getEditore(),libro_test.getEditore());
        assertEquals(l.getPosizione(),libro_test.getPosizione());
    }

    @Test(expected = SQLException.class)
    public void insertIncorrectCategoriaTest() throws SQLException {
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(1980)
                .autore("Alessandro Manzoni")
                .categoria("fantascienza")
                .editore("Mondadori")
                .posizione(p)
                .urlCopertina("ciao")
                .titolo("Promessi Sposi")
                .isbn("9788891904454C")
                .nCopie(5)
                .build();
        libroDAO.insert(l);
        Libro libro_test=libroDAO.doRetrieveByCodiceISBN(l.getIsbn());
        assertEquals(l.getIsbn(),libro_test.getIsbn());
        assertEquals(l.getAutore(),libro_test.getAutore());
        assertEquals(l.getnCopie(),libro_test.getnCopie());
        assertEquals(l.getCategoria(),libro_test.getCategoria());
        assertEquals(l.getAnnoPubbl(),libro_test.getAnnoPubbl());
        assertEquals(l.getUrlCopertina(),libro_test.getUrlCopertina());
        assertEquals(l.getEditore(),libro_test.getEditore());
        assertEquals(l.getPosizione(),libro_test.getPosizione());
    }

    @Test
    public void doRetrieveByCodiceISBNTest() throws SQLException {
        String isbn="9788891904451C";
        final Libro[] libro_test = new Libro[1];
        assertDoesNotThrow(() -> libro_test[0] =libroDAO.doRetrieveByCodiceISBN(isbn));
        assertEquals(isbn, libro_test[0].getIsbn());
    }

    @Test
    public void doRetrieveByAutoreTest() throws SQLException {
        String autore="Alessandro Manzoni";
        ArrayList<Libro> libri_test=libroDAO.doRetrieveByTitoloAutore(autore);
        for(Libro l:libri_test)
            assertEquals(autore,l.getAutore());
    }

    @Test
    public void doRetrieveByTitoloTest() throws SQLException {
        String titolo="Promessi Sposi";
        ArrayList<Libro> libri_test=libroDAO.doRetrieveByTitoloAutore(titolo);
        for(Libro l:libri_test)
            assertEquals(titolo,l.getTitolo());
    }

    @Test
    public void doRetrieveByCategoriaTest() throws SQLException {
        String categoria="lettere";
        ArrayList<Libro>libri_test=libroDAO.doRetrieveByCategoria(categoria);
        for(Libro l:libri_test)
            assertEquals(categoria,l.getCategoria());
    }

    @Test
    public void doRetrieveByInteresseTest() throws SQLException {
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
                .isbn("9788891904451C")
                .nCopie(5)
                .build();
        interessi.add(l);
        ArrayList<Libro>libri_test=libroDAO.doRetrieveInteresse(email);
        assertIterableEquals(interessi,libri_test);
    }

    @Test
    public void doAddInteresseTest() throws SQLException {
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
                .isbn("9788891904454C")
                .nCopie(5)
                .build();
        libroDAO.doAddInteresse(email,l.getIsbn());
        ArrayList<Libro> libri_test=libroDAO.doRetrieveInteresse(email);
        assertTrue(libri_test.contains(l));
    }

    @Test
    public void doDeleteInteresseTest() throws SQLException {
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
                .isbn("9788891904454C")
                .nCopie(5)
                .build();
        libroDAO.doDeleteInteresse(email,l.getIsbn());
        ArrayList<Libro> libri_test=libroDAO.doRetrieveInteresse(email);
        assertFalse(libri_test.contains(l));
    }
}
