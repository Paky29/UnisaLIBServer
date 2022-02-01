package model.postazionemanagement;

import model.libromanagement.Libro;
import model.posizionemanagement.Posizione;
import model.prestitomanagement.Prestito;
import model.utentemanagement.Utente;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PeriodoTest {
    @Test
    public void equalsTest(){
        GregorianCalendar data = new GregorianCalendar();
        data.set(2022, Calendar.DECEMBER,22);
        int oraInizio = 11;
        int oraFine = 13;
        Periodo p1 = new Periodo(oraInizio, oraFine, data);
        Periodo p2 = new Periodo(oraInizio, oraFine, data);
        assertTrue(p1.equals(p2));
    }


    @Test
    public void equalsSamePeriodoTest(){
        GregorianCalendar data = new GregorianCalendar();
        data.set(2022, Calendar.DECEMBER,22);
        int oraInizio = 11;
        int oraFine = 13;
        Periodo p1 = new Periodo(oraInizio, oraFine, data);
        assertTrue(p1.equals(p1));

    }

    @Test
    public void equalsPrestitoNullErrorTest(){
        GregorianCalendar data = new GregorianCalendar();
        data.set(2022, Calendar.DECEMBER,22);
        int oraInizio = 11;
        int oraFine = 13;
        Periodo p1 = new Periodo(oraInizio, oraFine, data);
        Prestito p2 = null;
        assertFalse(p1.equals(p2));
    }
    @Test
    public void equalsOraInizioErrorTest(){
        GregorianCalendar data = new GregorianCalendar();
        data.set(2022, Calendar.DECEMBER,22);
        int oraInizio1 = 11;
        int oraInizio2 = 12;
        int oraFine = 13;
        Periodo p1 = new Periodo(oraInizio1, oraFine, data);
        Periodo p2 = new Periodo(oraInizio2, oraFine, data);
        assertFalse(p1.equals(p2));
    }
    @Test
    public void equalsOraFineErrorTest(){
        GregorianCalendar data = new GregorianCalendar();
        data.set(2022, Calendar.DECEMBER,22);
        int oraInizio = 11;
        int oraFine1 = 13;
        int oraFine2 = 14;
        Periodo p1 = new Periodo(oraInizio, oraFine1, data);
        Periodo p2 = new Periodo(oraInizio, oraFine2, data);
        assertFalse(p1.equals(p2));
    }

    @Test
    public void equalsOraDataErrorTest(){
        GregorianCalendar data1 = new GregorianCalendar();
        data1.set(2022, Calendar.DECEMBER,22);
        GregorianCalendar data2 = new GregorianCalendar();
        data2.set(2022, Calendar.MARCH,22);
        int oraInizio = 11;
        int oraFine = 13;
        Periodo p1 = new Periodo(oraInizio, oraFine, data1);
        Periodo p2 = new Periodo(oraInizio, oraFine, data2);
        assertFalse(p1.equals(p2));
    }


}

