package model.postazionemanagement;

import model.posizionemanagement.Posizione;
import org.junit.Before;
import org.junit.Test;

import java.util.GregorianCalendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PostazioneTest {
    public Posizione posizione;

    @Before
    public void setUp(){
        posizione = new Posizione(3, "scientifica", "Piano 1");
    }
    @Test
    public void stessoOggetto(){
        Postazione p1 = new Postazione("A1", true, posizione);
        assertTrue(p1.equals(p1));
    }

    @Test
    public void oggettoNull(){
        Postazione p1 = new Postazione("A1", true, posizione);
        assertFalse(p1.equals(null));
    }

    @Test
    public void oggettoAltraClasse(){
        Postazione p1 = new Postazione("A1", true, posizione);
        assertFalse(p1.equals(posizione));
    }

    @Test
    public void sizeBlocchiDiverse(){
        Postazione p1 = new Postazione("A1", true, posizione);
        Postazione p2 = new Postazione("A1", true, posizione);
        p1.getBlocchi().add(new Periodo(11,13,new GregorianCalendar()));
        assertFalse(p1.equals(p2));
    }

    @Test
    public void blocchiDiverse(){
        Postazione p1 = new Postazione("A1", true, posizione);
        Postazione p2 = new Postazione("A1", true, posizione);
        p1.getBlocchi().add(new Periodo(11,13,new GregorianCalendar()));
        p2.getBlocchi().add(new Periodo(14,16,new GregorianCalendar()));
        assertFalse(p1.equals(p2));
    }

    @Test
    public void diversaDisponibilita(){
        Postazione p1 = new Postazione("A1", true, posizione);
        Postazione p2 = new Postazione("A1", false, posizione);
        p1.getBlocchi().add(new Periodo(11,13,new GregorianCalendar()));
        p2.getBlocchi().add(new Periodo(11,13,new GregorianCalendar()));
        assertFalse(p1.equals(p2));
    }

    @Test
    public void diversoId(){
        Postazione p1 = new Postazione("A1", true, posizione);
        Postazione p2 = new Postazione("A2", true, posizione);
        p1.getBlocchi().add(new Periodo(11,13,new GregorianCalendar()));
        p2.getBlocchi().add(new Periodo(11,13,new GregorianCalendar()));
        assertFalse(p1.equals(p2));
    }

    @Test
    public void diversaPosizione(){
        Postazione p1 = new Postazione("A1", true, posizione);
        Postazione p2 = new Postazione("A1", true, posizione);
        p1.getBlocchi().add(new Periodo(11,13,new GregorianCalendar()));
        p2.getBlocchi().add(new Periodo(11,13,new GregorianCalendar()));
        p2.setPosizione(new Posizione(3, "scientifica", "Piano Terra"));
        assertFalse(p1.equals(p2));
    }

    @Test
    public void postazioniUguali(){
        Postazione p1 = new Postazione("A1", true, posizione);
        Postazione p2 = new Postazione("A1", true, posizione);
        p1.getBlocchi().add(new Periodo(11,13,new GregorianCalendar()));
        p2.getBlocchi().add(new Periodo(11,13,new GregorianCalendar()));
        assertTrue(p1.equals(p2));
    }





}
