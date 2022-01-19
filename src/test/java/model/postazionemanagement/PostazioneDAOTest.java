package model.postazionemanagement;

import model.posizionemanagement.Posizione;
import model.posizionemanagement.PosizioneDAO;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class PostazioneDAOTest {
    public PostazioneDAO postazioneDAO;
    public PosizioneDAO posizioneDAO;
    @Before
    public void setup(){
        postazioneDAO = new PostazioneDAO();
        posizioneDAO = new PosizioneDAO();
    }

    @Test
    public void insertTest(){
        Posizione pos = new Posizione(1, "umanistica", "piano 1");
        Postazione postazione = new Postazione("A10",true, pos);

        AtomicBoolean result= new AtomicBoolean(false);
        assertDoesNotThrow(()-> result.set(postazioneDAO.insert(postazione)));
        assertTrue(result.get());
    }

    @Test
    public void doRetrieveByIdTest(){
        Posizione pos = new Posizione(1, "umanistica", "piano 1");
        Postazione postazione = new Postazione("A10",true, pos);

        final Postazione[] postazione_test = new Postazione[1];
        assertDoesNotThrow(() -> postazione_test[0]=postazioneDAO.doRetrieveById("A10"));
        assertEquals(postazione_test[0].getId(), postazione.getId());
    }

    @Test
    public void doRetrieveByPosizioneTest(){
        Posizione p=new Posizione("scientifica","piano 10");
        AtomicReference<ArrayList<Postazione>> postazioni_test= new AtomicReference<>(new ArrayList<>());
        ArrayList<Postazione> postazioni=new ArrayList<>();
        try {
            posizioneDAO.insert(p);
            Posizione finalP =posizioneDAO.doRetrieveByBibliotecaZona(p.getBiblioteca(),p.getZona());
            postazioni.add(new Postazione("Z1",true, p));
            postazioni.add(new Postazione("Z2",false, p));
            postazioneDAO.insert(postazioni.get(0));
            postazioneDAO.insert(postazioni.get(1));
            assertDoesNotThrow(()-> postazioni_test.set(postazioneDAO.doRetrieveByPosizione(finalP.getBiblioteca(), finalP.getZona())));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
        assertIterableEquals(postazioni, postazioni_test.get());
    }

    @Test
    public void doRetrieveDisponibiliByPosizioneTest(){
        Posizione p=new Posizione("scientifica","piano 11");
        AtomicReference<ArrayList<Postazione>> postazioni_test= new AtomicReference<>(new ArrayList<>());
        ArrayList<Postazione> postazioni=new ArrayList<>();
        try {
            posizioneDAO.insert(p);
            Posizione finalP=posizioneDAO.doRetrieveByBibliotecaZona(p.getBiblioteca(),p.getZona());
            postazioni.add(new Postazione("Z3",true, p));
            postazioni.add(new Postazione("Z4",false, p));
            postazioneDAO.insert(postazioni.get(0));
            postazioneDAO.insert(postazioni.get(1));
            assertDoesNotThrow(()-> postazioni_test.set(postazioneDAO.doRetrieveDisponibiliByPosizione(finalP.getBiblioteca(), finalP.getZona())));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
        assertEquals(postazioni.get(0), postazioni_test.get().get(0));
    }

    @Test
    public void bloccaPostazioneTest(){
        try {
            Postazione pos=postazioneDAO.doRetrieveById("A10");
            assertTrue(postazioneDAO.bloccaPostazione(pos.getId()));
            Postazione pos_test=postazioneDAO.doRetrieveById(pos.getId());
            assertTrue(pos.isDisponibile()!=pos_test.isDisponibile());
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }

    @Test
    public void bloccoDeterminatoTest(){
        Periodo periodo=new Periodo(9,13,new GregorianCalendar());
        try {
            Postazione pos=postazioneDAO.doRetrieveById("A10");
            postazioneDAO.bloccoDeterminato(periodo,pos);
            Postazione pos_test=postazioneDAO.doRetrieveById(pos.getId());
            for(int start=periodo.getOraInizio();start<periodo.getOraFine();start+=2){
                if(start==13)
                    ++start;
                Periodo periodo_test=new Periodo(start,start+2,new GregorianCalendar());
                assertTrue(pos_test.getBlocchi().contains(periodo_test));
            }
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }

    @Test
    public void sbloccaPostazioneTest(){
        try {
            Postazione pos=postazioneDAO.doRetrieveById("A10");
            assertTrue(postazioneDAO.sbloccaPostazione(pos.getId()));
            Postazione pos_test=postazioneDAO.doRetrieveById(pos.getId());
            assertTrue(pos.isDisponibile()!=pos_test.isDisponibile());
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }

    @Test
    public void sbloccoDeterminatoTest(){
        try {
            Postazione pos=postazioneDAO.doRetrieveById("A10");
            Periodo periodo=pos.getBlocchi().get(0);
            assertTrue(postazioneDAO.sbloccaPostazione(pos.getId(),periodo));
            Postazione pos_test=postazioneDAO.doRetrieveById("A10");
            assertFalse(pos_test.getBlocchi().contains(periodo));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }
}
