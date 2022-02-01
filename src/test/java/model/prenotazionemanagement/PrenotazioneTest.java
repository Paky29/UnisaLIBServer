package model.prenotazionemanagement;

import model.posizionemanagement.Posizione;
import model.postazionemanagement.Periodo;
import model.postazionemanagement.Postazione;
import model.prestitomanagement.Prestito;
import model.utentemanagement.Utente;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrenotazioneTest {

        @Test
        public void equalsTest(){
            GregorianCalendar data = new GregorianCalendar();
            data.set(2022, Calendar.DECEMBER,22);
            int oraInizio = 11;
            int oraFine = 13;
            Utente utente = new Utente.UtenteBuilder().
                    email("sc").
                    password("s.cele").
                    nome("sabato").
                    cognome("celentano").
                    admin(false).
                    nuovo(true).
                    build();
            Postazione postazione = new Postazione("B1", true, new Posizione( "scientifica" , "piano 1"));
            Prenotazione p1 = new Prenotazione(data, oraInizio, oraFine, utente, postazione);
            Prenotazione p2 = new Prenotazione(data, oraInizio, oraFine, utente, postazione);
            assertTrue(p1.equals(p2));
        }


        @Test
        public void equalsSamePrenotazioneTest(){
            GregorianCalendar data = new GregorianCalendar();
            data.set(2022, Calendar.DECEMBER,22);
            int oraInizio = 11;
            int oraFine = 13;
            Utente utente = new Utente.UtenteBuilder().
                    email("sc").
                    password("s.cele").
                    nome("sabato").
                    cognome("celentano").
                    admin(false).
                    nuovo(true).
                    build();
            Postazione postazione = new Postazione("B1", true, new Posizione( "scientifica" , "piano 1"));
            Prenotazione p1 = new Prenotazione(data, oraInizio, oraFine, utente, postazione);
            assertTrue(p1.equals(p1));

        }

        @Test
        public void equalsPrenotazioneNullErrorTest(){
            Utente utente = new Utente.UtenteBuilder().
                    email("sc").
                    password("s.cele").
                    nome("sabato").
                    cognome("celentano").
                    admin(false).
                    nuovo(true).
                    build();
            Postazione postazione = new Postazione("B1", true, new Posizione( "scientifica" , "piano 1"));
            Prenotazione p1 = new Prenotazione(new GregorianCalendar(2022, 0, 31), 16, 18, utente, postazione);
            Prenotazione p2 = null;
            assertFalse(p1.equals(p2));
        }
        @Test
        public void equalsOraInizioErrorTest(){
            GregorianCalendar data = new GregorianCalendar();
            data.set(2022, Calendar.DECEMBER,22);
            int oraInizio1 = 11;
            int oraInizio2 = 12;
            int oraFine = 13;
            Utente utente = new Utente.UtenteBuilder().
                    email("sc").
                    password("s.cele").
                    nome("sabato").
                    cognome("celentano").
                    admin(false).
                    nuovo(true).
                    build();
            Postazione postazione = new Postazione("B1", true, new Posizione( "scientifica" , "piano 1"));
            Prenotazione p1 = new Prenotazione(data, oraInizio1, oraFine, utente, postazione);
            Prenotazione p2 = new Prenotazione(data, oraInizio2, oraFine, utente, postazione);
            assertFalse(p1.equals(p2));
        }
        @Test
        public void equalsOraFineErrorTest(){
            GregorianCalendar data = new GregorianCalendar();
            data.set(2022, Calendar.DECEMBER,22);
            int oraInizio = 11;
            int oraFine1 = 13;
            int oraFine2 = 14;
            Utente utente = new Utente.UtenteBuilder().
                    email("sc").
                    password("s.cele").
                    nome("sabato").
                    cognome("celentano").
                    admin(false).
                    nuovo(true).
                    build();
            Postazione postazione = new Postazione("B1", true, new Posizione( "scientifica" , "piano 1"));
            Prenotazione p1 = new Prenotazione(data, oraInizio, oraFine1, utente, postazione);
            Prenotazione p2 = new Prenotazione(data, oraInizio, oraFine2, utente, postazione);
            assertFalse(p1.equals(p2));
        }

        @Test
        public void equalsOnDataErrorTest(){
            GregorianCalendar data1 = new GregorianCalendar();
            GregorianCalendar data2 = new GregorianCalendar();
            data1.set(2022, Calendar.DECEMBER,22);
            data2.set(2022, Calendar.DECEMBER,19);
            int oraInizio = 11;
            int oraFine = 13;
            Utente utente = new Utente.UtenteBuilder().
                    email("sc").
                    password("s.cele").
                    nome("sabato").
                    cognome("celentano").
                    admin(false).
                    nuovo(true).
                    build();
            Postazione postazione = new Postazione("B1", true, new Posizione( "scientifica" , "piano 1"));
            Prenotazione p1 = new Prenotazione(data1, oraInizio, oraFine, utente, postazione);
            Prenotazione p2 = new Prenotazione(data2, oraInizio, oraFine, utente, postazione);
            assertFalse(p1.equals(p2));
        }


    }


