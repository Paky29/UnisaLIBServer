package model.postazionemanagement;

import model.libromanagement.Libro;
import model.posizionemanagement.Posizione;
import org.junit.Test;
import presenter.libropresenter.LibroValidator;
import presenter.postazionepresenter.PeriodoValidator;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PeriodoValidatorTest {
    @Test
    public void PeriodoValidoTest() {
        GregorianCalendar data1 = new GregorianCalendar();
        data1.set(2022, Calendar.DECEMBER, 22);
        int oraInizio = 11;
        int oraFine = 13;
        Periodo p1 = new Periodo(oraInizio, oraFine, data1);
        assertTrue(PeriodoValidator.validate(p1));
    }

    @Test
    public void DataPrecedenteErrorTest() {
        GregorianCalendar data1 = new GregorianCalendar();
        data1.set(2022, Calendar.JANUARY, 1);
        int oraInizio = 11;
        int oraFine = 13;
        Periodo p1 = new Periodo(oraInizio, oraFine, data1);
        assertFalse(PeriodoValidator.validate(p1));
    }

    @Test
    public void OraInizioErrorTest() {
        GregorianCalendar data1 = new GregorianCalendar();
        data1.set(2022, Calendar.MARCH, 1);
        int oraInizio = 10;
        int oraFine = 13;
        Periodo p1 = new Periodo(oraInizio, oraFine, data1);
        assertFalse(PeriodoValidator.validate(p1));
    }

    @Test
    public void OraFineErrorTest() {
        GregorianCalendar data1 = new GregorianCalendar();
        data1.set(2022, Calendar.MARCH, 1);
        int oraInizio = 11;
        int oraFine = 15;
        Periodo p1 = new Periodo(oraInizio, oraFine, data1);
        assertFalse(PeriodoValidator.validate(p1));
    }
}
