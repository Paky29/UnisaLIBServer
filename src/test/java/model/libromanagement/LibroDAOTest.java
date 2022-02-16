package model.libromanagement;

import model.posizionemanagement.Posizione;
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.fail;
/**
 * Testa i metodi di LibroDAO
 * */
public class LibroDAOTest {
    private LibroDAO libroDAO;

    @Before
    public void setUp(){
        libroDAO=new LibroDAO();
    }
/**
 * Testa il corretto inserimento
 * */
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
    /**
     * Testa  inserimento non corretto
     * */
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
    /**
     * Testa l'interrogazione del DAO sull'ISBN
     * */
    @Test
    public void doRetrieveByCodiceISBNTest() {
        String isbn="0000000000";
        final Libro[] libro_test = new Libro[1];
        assertDoesNotThrow(() -> libro_test[0] =libroDAO.doRetrieveByCodiceISBN(isbn));
        assertEquals(isbn, libro_test[0].getIsbn());
    }

    /**
     * Testa l'interrogazione del DAO sull'autore
     * */
    @Test
    public void doRetrieveByAutoreTest(){
        String autore="autore_test";
        AtomicReference<ArrayList<Libro>> libri_test=new AtomicReference<>();
        assertDoesNotThrow(()-> libri_test.set(libroDAO.doRetrieveByTitoloAutore(autore)));
        for(Libro l: libri_test.get())
            assertEquals(autore,l.getAutore());
    }
    /**
     * Testa l'interrogazione del DAO sul titolo
     * */
    @Test
    public void doRetrieveByTitoloTest(){
        String titolo="titolo_test";
        AtomicReference<ArrayList<Libro>> libri_test=new AtomicReference<>();
        assertDoesNotThrow(()-> libri_test.set(libroDAO.doRetrieveByTitoloAutore(titolo)));
        for(Libro l:libri_test.get())
            assertEquals(titolo,l.getTitolo());
    }
    /**
     * Testa l'interrogazione del DAO sulla categoria
     * */
    @Test
    public void doRetrieveByCategoriaTest(){
        String categoria="test";
        AtomicReference<ArrayList<Libro>> libri_test=new AtomicReference<>();
        assertDoesNotThrow(()-> libri_test.set(libroDAO.doRetrieveByCategoria(categoria)));
        for(Libro l:libri_test.get())
            assertEquals(categoria,l.getCategoria());
    }
    /**
     * Testa l'aggiunta alla lista degli interessi
     * */
    @Test
    public void doAddInteresseTest(){
        String email="ps";
        Posizione pos = new Posizione(9, "test", "test");
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(2021)
                .autore("autore_test")
                .categoria("test")
                .editore("editore_test")
                .posizione(pos)
                .urlCopertina("url_test")
                .titolo("titolo_test")
                .isbn("0000000000")
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
    /**
     * Testa l'interrogazione del DAO sugli interessi
     * */
    @Test
    public void doRetrieveInteresseTest(){
        String email="ps";
        Posizione pos = new Posizione(9, "test", "test");
        ArrayList<Libro> interessi=new ArrayList<>();
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(2021)
                .autore("autore_test")
                .categoria("test")
                .editore("editore_test")
                .posizione(pos)
                .urlCopertina("url_test")
                .titolo("titolo_test")
                .isbn("0000000001")
                .nCopie(5)
                .build();
        interessi.add(l);
        try {
            libroDAO.doAddInteresse(email,l.getIsbn());
            AtomicReference<ArrayList<Libro>> libri_test=new AtomicReference<>();
            assertDoesNotThrow(()-> libri_test.set(libroDAO.doRetrieveInteresse(email)));
            assertIterableEquals(interessi,libri_test.get());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
    /**
     * Testa l'aggiunta alla lista degli interessi con ISBN errato
     * */
    @Test
    public void doAddInteresseIncorretISBNTest(){
        String email="ps";
        Posizione pos = new Posizione(9, "test", "test");
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(2021)
                .autore("autore_test")
                .categoria("test")
                .editore("editore_test")
                .posizione(pos)
                .urlCopertina("url_test")
                .titolo("titolo_test")
                .isbn("0000000002")
                .nCopie(5)
                .build();
        assertThrows(SQLException.class,()->libroDAO.doAddInteresse(email,l.getIsbn()));
        try {
            assertFalse(libroDAO.doRetrieveInteresse(email).contains(l));
        }catch (SQLException e){
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
    /**
     * Testa l'aggiunta alla lista degli interessi con email errata
     * */
    @Test
    public void doAddInteresseIncorretEmailTest() throws SQLException{
        String email="ps_incorretta";
        Posizione pos = new Posizione(9, "test", "test");
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(2021)
                .autore("autore_test")
                .categoria("test")
                .editore("editore_test")
                .posizione(pos)
                .urlCopertina("url_test")
                .titolo("titolo_test")
                .isbn("0000000000")
                .nCopie(5)
                .build();
        assertThrows(SQLException.class,()->libroDAO.doAddInteresse(email,l.getIsbn()));
    }
    /**
     * Testa l'eliminazione dalla lista degli interessi
     * */
    @Test
    public void doDeleteInteresseTest(){
        String email="ps";
        Posizione pos = new Posizione(9, "test", "test");
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(2021)
                .autore("autore_test")
                .categoria("test")
                .editore("editore_test")
                .posizione(pos)
                .urlCopertina("url_test")
                .titolo("titolo_test")
                .isbn("0000000000")
                .nCopie(5)
                .build();
        ArrayList<Libro> libri_test;
        try {
            assertTrue(libroDAO.doDeleteInteresse(email,l.getIsbn()));
            libri_test = libroDAO.doRetrieveInteresse(email);
            assertFalse(libri_test.contains(l));
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
    /**
     * Testa la rimozione dalla lista degli interessi con ISBN errato
     * */
    @Test
    public void doDeleteInteresseIncorrectISBNTest() {
        String email="ps";
        Posizione pos = new Posizione(9, "test", "test");
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(2021)
                .autore("autore_test")
                .categoria("test")
                .editore("editore_test")
                .posizione(pos)
                .urlCopertina("url_test")
                .titolo("titolo_test")
                .isbn("0000000000")
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
    /**
     * Testa la rimozione dalla lista degli interessi con email errata
     * */
    @Test
    public void doDeleteInteresseIncorrectEmailTest(){
        String email="ps_incorretta";
        Posizione pos = new Posizione(9, "test", "test");
        Libro l= new Libro.LibroBuilder()
                .annoPubbl(2021)
                .autore("autore_test")
                .categoria("test")
                .editore("editore_test")
                .posizione(pos)
                .urlCopertina("url_test")
                .titolo("titolo_test")
                .isbn("0000000000")
                .nCopie(5)
                .build();
        try {
            assertFalse(libroDAO.doDeleteInteresse(email,l.getIsbn()));
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
    /**
     * Testa l'esistenza di una categoria esistente
     * */
    @Test
    public void existCategoriaTest(){
        String categoria="lettere";
        try {
            boolean risultato=libroDAO.existCategoria(categoria);
            assertTrue(risultato);
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
    /**
     * Testa l'esistenza di una categoria non esistente
     * */
    @Test
    public void existCategoriaInsesistenteTest(){
        String categoria="astronomia";
        try {
            boolean risultato=libroDAO.existCategoria(categoria);
            assertFalse(risultato);
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
    /**
     * Testa l'interrogazione del DAO sul tutte le categorie
     * */
    @Test
    public void doRetrieveAllCategorieTest(){
        ArrayList<String> categorie=new ArrayList<>();
        categorie.add("informatica");
        categorie.add("lettere");
        categorie.add("filosofia");
        categorie.add("biologia");
        categorie.add("fisica");
        categorie.add("politica");
        categorie.add("arte");
        categorie.add("test");
        categorie.add("consigliati");
        categorie.add(("matematica"));
        try {
            ArrayList<String> categorie_test=libroDAO.doRetrieveAllCategorie();
            assertIterableEquals(new TreeSet<>(categorie),new TreeSet<>(categorie_test));
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
}
