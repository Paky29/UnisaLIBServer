package model.postazionemanagement;

import model.posizionemanagement.Posizione;
import model.prestitomanagement.Prestito;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import utility.SwitchDate;

import java.sql.SQLException;
import java.time.Period;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class PeriodoDAOTest {
    public PeriodoDAO pdao;

    @Before
    public void setup() throws Exception {
        pdao = new PeriodoDAO();
    }

    @Test
    public void insertTest() {
        GregorianCalendar data = new GregorianCalendar(2022, 1 ,2);
        int oraInizio = 11;
        int oraFine = 13;
        Periodo p = new Periodo(oraFine, oraInizio, data);

        AtomicBoolean success = new AtomicBoolean(false);
        assertDoesNotThrow(() -> success.set(pdao.insertPeriodo(p)));
        Assertions.assertTrue(Boolean.parseBoolean(success.toString()));

        try {
            pdao.insertPeriodo(p);
            Periodo p1 = pdao.doRetrieveByInfo(p.getData(), p.getOraInizio(), p.getOraFine());
            assertTrue(SwitchDate.equalsDate(p.getData(), p1.getData()));
            assertEquals(p.getOraFine(), p1.getOraFine());
            assertEquals(p.getOraInizio(), p1.getOraInizio());
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }

    @Test(expected = SQLException.class)
    public void insertAlreadyExistsPosizioneTest() throws SQLException {
        GregorianCalendar data = new GregorianCalendar(2022, Calendar.FEBRUARY ,2);
        int oraInizio = 13;
        int oraFine = 11;
        Periodo p = new Periodo(oraFine, oraInizio, data);
        pdao.insertPeriodo(p);
    }

    @Test
    public void doRetrieveByInfoTest() throws SQLException {
        GregorianCalendar data = new GregorianCalendar(2022, Calendar.FEBRUARY,2);
        int oraInizio = 13;
        int oraFine = 11;
        final Periodo[] pTest = new Periodo[1];
        assertDoesNotThrow(() -> pTest[0] = pdao.doRetrieveByInfo(data,oraInizio,oraFine));
        assertTrue(SwitchDate.equalsDate(data, pTest[0].getData()));
        assertEquals(oraInizio, pTest[0].getOraInizio());
        assertEquals(oraFine, pTest[0].getOraFine());

    }
}
