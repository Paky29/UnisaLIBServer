package model.posizionemanagement;

import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import java.util.ArrayList;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
/**
 * Testa i metodi di PosizioneDAO
 * */
public class PosizioneDAOTest {
    PosizioneDAO posizioneDAO;

    @Before
    public void setUp() {
        posizioneDAO = new PosizioneDAO();
    }
    /**
     * Testa il corretto inserimento
     * */
    @Test
    public void insertTest() throws SQLException {
        Posizione p = new Posizione("scientifica", "piano 7");
        posizioneDAO.insert(p);
        Posizione p1 = posizioneDAO.doRetrieveByBibliotecaZona(p.getBiblioteca(), p.getZona());
        assertEquals(p.getBiblioteca(), p1.getBiblioteca());
        assertEquals(p.getZona(), p1.getZona());
        posizioneDAO.delete(p.getBiblioteca(), p.getZona());
    }
    /**
     * Testa l'inserimento di una postazione già esistente
     * */
    @Test
    public void insertAlreadyExistsPosizioneTest(){
        Posizione p = new Posizione("scientifica", "piano 1");
        assertThrows(SQLException.class,()->posizioneDAO.insert(p));
    }
    /**
     * Testa l'interrogazione del DAO su biblioteca e zona
     * */
    @Test
    public void doRetrieveByBibliotecaZonaTest() throws SQLException {
        String biblioteca = "scientifica";
        String zona = "piano 1";
        final Posizione[] pTest = new Posizione[1];
        assertDoesNotThrow(() -> pTest[0] = posizioneDAO.doRetrieveByBibliotecaZona(biblioteca, zona));
        assertEquals(biblioteca, pTest[0].getBiblioteca());
        assertEquals(zona, pTest[0].getZona());
    }
    /**
     * Testa l'interrogazione del DAO su biblioteca errata e zona
     * */
    @Test
    public void doRetrieveByIncorrectBibliotecaZonaTest() throws SQLException {
        String biblioteca = "linguistica";
        String zona = "piano 1";
        Posizione p = posizioneDAO.doRetrieveByBibliotecaZona(biblioteca, zona);
        assertNull(p);
    }
    /**
     * Testa l'interrogazione del DAO su biblioteca corretta e zona errata
     * */
    @Test
    public void doRetrieveByBibliotecaIncorrectZonaTest() throws SQLException {
        String biblioteca = "scientifica";
        String zona = "piano 20";
        Posizione p = posizioneDAO.doRetrieveByBibliotecaZona(biblioteca, zona);
        assertNull(p);
    }
    /**
     * Testa la rimozione di una posizione
     * */
    @Test
    public void deleteTest() throws SQLException {
        String biblioteca = "scientifica";
        String zona = "piano 5";
        posizioneDAO.delete(biblioteca, zona);
        assertNull(posizioneDAO.doRetrieveByBibliotecaZona(biblioteca, zona));
        posizioneDAO.insert(new Posizione(biblioteca, zona));
    }
    /**
     * Testa l'interrogazione del DAO sulla richiesta di tutte le posizioni
     * */
    @Test
    public void doRetrieveAllTest() throws SQLException {
        ArrayList<Posizione> posizioni = new ArrayList<>();
        boolean flag=true;
        posizioni.add(new Posizione("scientifica", "piano 1"));
        posizioni.add(new Posizione("scientifica", "piano 2"));
        posizioni.add(new Posizione("scientifica", "piano 3"));
        posizioni.add(new Posizione("umanistica", "piano 1"));
        posizioni.add(new Posizione("umanistica", "piano 2"));
        posizioni.add(new Posizione("umanistica", "piano 3"));
        ArrayList<Posizione> retrieve = posizioneDAO.doRetrieveAll();
        for(Posizione p:posizioni){
            if(!retrieve.contains(p))
                flag=false;
        }
        assertTrue(flag);
    }
}
