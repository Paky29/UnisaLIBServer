package model.posizionemanagement;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class PosizioneDAOTest {
    PosizioneDAO posizioneDAO;

    @Before
    public void setUp() {
        posizioneDAO = new PosizioneDAO();
    }

    @Test
    public void insertTest() throws SQLException {
        Posizione p = new Posizione("scientifica", "piano 8");
        posizioneDAO.insert(p);
        Posizione p1 = posizioneDAO.doRetrieveByBibliotecaZona(p.getBiblioteca(), p.getZona());
        assertEquals(p.getBiblioteca(), p1.getBiblioteca());
        assertEquals(p.getZona(), p1.getZona());
    }

    @Test
    public void doRetrieveByBibliotecaZonaTest() throws SQLException {
        String biblioteca = "scientifica";
        String zona = "piano 2";
        Posizione p = posizioneDAO.doRetrieveByBibliotecaZona(biblioteca, zona);
        assertEquals(biblioteca, p.getBiblioteca());
        assertEquals(zona, p.getZona());
    }
}
