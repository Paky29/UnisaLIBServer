package model.posizionemanagement;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class PosizioneDAOTest {
    PosizioneDAO posizioneDAO;

    @Before
    public void setUp() {
        posizioneDAO = new PosizioneDAO();
    }

    @Test
    public void insertTest() throws SQLException {
        Posizione p = new Posizione("scientifica", "Piano 7");
        posizioneDAO.insert(p);
        Posizione p1 = posizioneDAO.doRetrieveByBibliotecaZona(p.getBiblioteca(), p.getZona());
        assertEquals(p.getBiblioteca(), p1.getBiblioteca());
        assertEquals(p.getZona(), p1.getZona());
    }

    @Test(expected = SQLException.class)
    public void insertAlreadyExistsPosizioneTest() throws SQLException {
        Posizione p = new Posizione("scientifica", "Piano 7");
        posizioneDAO.insert(p);
    }

    @Test
    public void doRetrieveByBibliotecaZonaTest() throws SQLException {
        String biblioteca = "scientifica";
        String zona = "Piano 7";
        final Posizione[] pTest = new Posizione[1];
        assertDoesNotThrow(() -> pTest[0] = posizioneDAO.doRetrieveByBibliotecaZona(biblioteca, zona));
        assertEquals(biblioteca, pTest[0].getBiblioteca());
        assertEquals(zona, pTest[0].getZona());
    }

    @Test
    public void doRetrieveByIncorrectBibliotecaZonaTest() throws SQLException {
        String biblioteca = "linguistica";
        String zona = "Piano 7";
        Posizione p = posizioneDAO.doRetrieveByBibliotecaZona(biblioteca, zona);
        assertNull(p);
    }

    @Test
    public void doRetrieveByBibliotecaIncorrectZonaTest() throws SQLException {
        String biblioteca = "scientifica";
        String zona = "Piano 20";
        Posizione p = posizioneDAO.doRetrieveByBibliotecaZona(biblioteca, zona);
        assertNull(p);
    }

    @Test
    public void deleteTest() throws SQLException {
        String biblioteca = "scientifica";
        String zona = "Piano 7";
        posizioneDAO.delete(biblioteca, zona);
        assertNull(posizioneDAO.doRetrieveByBibliotecaZona(biblioteca, zona));
    }
}
