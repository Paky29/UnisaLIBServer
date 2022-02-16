package model.postazionemanagement;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import utility.SwitchDate;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa i metodi di PeriodoDAO
 * */
public class PeriodoDAOTest {
    public PeriodoDAO pdao;

    @Before
    public void setup() throws Exception {
        pdao = new PeriodoDAO();
    }
    /**
     * Testa il corretto inserimento
     * */
    @Test
    public void insertTest() {
        GregorianCalendar data = new GregorianCalendar();
        data.set(2022, Calendar.DECEMBER,25);
        int oraInizio = 11;
        int oraFine = 13;
        Periodo p = new Periodo(oraInizio, oraFine, data);

        AtomicBoolean success = new AtomicBoolean(false);
        assertDoesNotThrow(() -> success.set(pdao.insertPeriodo(p)));
        Assertions.assertTrue(Boolean.parseBoolean(success.toString()));

        try {
            Periodo p1 = pdao.doRetrieveByInfo(p.getData(), p.getOraInizio(), p.getOraFine());
            assertTrue(SwitchDate.equalsDate(p.getData(), p1.getData()));
            assertEquals(p.getOraFine(), p1.getOraFine());
            assertEquals(p.getOraInizio(), p1.getOraInizio());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }

    }
    /**
     * Testa l'inserimento di un periodo già esistente
     * */
    @Test
    public void insertAlreadyExistsPeriodoTest() {
        GregorianCalendar data = new GregorianCalendar();
        data.set(2022, Calendar.JANUARY,3);
        int oraInizio = 16;
        int oraFine = 18;
        Periodo p = new Periodo(oraInizio, oraFine, data);
        try{
            pdao.insertPeriodo(p);
            AtomicBoolean success = new AtomicBoolean(false);
            assertThrows(SQLException.class, () -> success.set(pdao.insertPeriodo(p)));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
    /**
     * Testa l'interrogazione del DAO sulle info del periodo
     * */
    @Test
    public void doRetrieveByInfoTest(){
        GregorianCalendar data = new GregorianCalendar();
        data.set(2022, Calendar.JANUARY,31);
        int oraInizio = 16;
        int oraFine = 18;
        final Periodo[] pTest = new Periodo[1];
        assertDoesNotThrow(() -> pTest[0] = pdao.doRetrieveByInfo(data,oraInizio,oraFine));
        assertTrue(SwitchDate.equalsDate(data, pTest[0].getData()));
        assertEquals(oraInizio, pTest[0].getOraInizio());
        assertEquals(oraFine, pTest[0].getOraFine());
    }
    /**
     * Testa l'interrogazione del DAO sulle info del periodo
     * */
    @Test
    public void doRetrieveByInfoPeriodoTest()
    {
        GregorianCalendar data = new GregorianCalendar();
        data.set(2022, Calendar.JANUARY,31);
        int oraInizio = 16;
        int oraFine = 18;
        Periodo p = new Periodo(oraInizio, oraFine, data);
        final Periodo[] pTest = new Periodo[1];
        assertDoesNotThrow(() -> pTest[0] = pdao.doRetrieveByInfo(p));
        assertTrue(SwitchDate.equalsDate(data, pTest[0].getData()));
        assertEquals(oraInizio, pTest[0].getOraInizio());
        assertEquals(oraFine, pTest[0].getOraFine());
    }
    /**
     * Testa l'interrogazione del DAO sulle info di un periodo non esistente
     * */
    @Test
    public void doRetrieveByInfoPeriodoDoestNotExistTest(){
        GregorianCalendar data = new GregorianCalendar();
        data.set(2022, Calendar.JANUARY,31);
        int oraInizio = 17;
        int oraFine = 18;
        Periodo p = new Periodo(oraInizio, oraFine, data);
        final Periodo[] pTest = new Periodo[1];
        assertDoesNotThrow(() -> pTest[0] = pdao.doRetrieveByInfo(p));
        assertNull(pTest[0]);

    }
}
