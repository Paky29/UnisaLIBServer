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

    @Test(expected = SQLException.class)
    public void insertPostazioneEsistenteTest() throws SQLException {
        Posizione pos = new Posizione(1, "umanistica", "piano 1");
        Postazione postazione = new Postazione("A10",true, pos);

        postazioneDAO.insert(postazione);
    }

    @Test(expected = SQLException.class)
    public void insertPosizioneNonPresenteTest() throws SQLException {
        Posizione pos = new Posizione(1, "linguistica", "piano 1");
        Postazione postazione = new Postazione("A11",true, pos);

        postazioneDAO.insert(postazione);
    }

    @Test
    public void doRetrieveByIdTest(){
        Posizione pos = new Posizione(1, "umanistica", "piano 1");
        Postazione postazione = new Postazione("A10",true, pos);

        final Postazione[] postazione_test = new Postazione[1];
        assertDoesNotThrow(() -> postazione_test[0]=postazioneDAO.doRetrieveById(postazione.getId()));
        assertEquals(postazione_test[0].getId(), postazione.getId());
    }

    @Test
    public void doRetrieveByIdNonPresenteTest(){
        Posizione pos = new Posizione(1, "umanistica", "piano 1");
        Postazione postazione = new Postazione("A12",true, pos);

        final Postazione[] postazione_test = new Postazione[1];
        assertDoesNotThrow(() -> postazione_test[0]=postazioneDAO.doRetrieveById(postazione.getId()));
        assertNull(postazione_test[0]);
    }

    @Test
    public void doRetrieveByPosizioneTest(){
        Posizione p=new Posizione("scientifica","piano 10");
        AtomicReference<ArrayList<Postazione>> postazioni_test= new AtomicReference<>(new ArrayList<>());
        ArrayList<Postazione> postazioni=new ArrayList<>();
        try {
            posizioneDAO.insert(p);
            Posizione finalP =posizioneDAO.doRetrieveByBibliotecaZona(p.getBiblioteca(),p.getZona());
            postazioni.add(new Postazione("Z1",true, finalP));
            postazioni.add(new Postazione("Z2",false, finalP));
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
    public void doRetrieveByPosizioneNonEsistenteTest(){
        Posizione p=new Posizione("linguistica","piano 10");
        AtomicReference<ArrayList<Postazione>> postazioni_test= new AtomicReference<>(new ArrayList<>());
        assertDoesNotThrow(()-> postazioni_test.set(postazioneDAO.doRetrieveByPosizione(p.getBiblioteca(), p.getZona())));
        assertTrue(postazioni_test.get().isEmpty());
    }

    @Test
    public void doRetrieveDisponibiliByPosizioneTest(){
        Posizione p=new Posizione("scientifica","piano 11");
        AtomicReference<ArrayList<Postazione>> postazioni_test= new AtomicReference<>(new ArrayList<>());
        ArrayList<Postazione> postazioni=new ArrayList<>();
        try {
            posizioneDAO.insert(p);
            Posizione finalP=posizioneDAO.doRetrieveByBibliotecaZona(p.getBiblioteca(),p.getZona());
            postazioni.add(new Postazione("Z3",true, finalP));
            postazioni.add(new Postazione("Z4",false, finalP));
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
    public void doRetrieveDisponibiliByPosizioneNoPostazioniTest(){
        Posizione p=new Posizione("scientifica","piano 12");
        AtomicReference<ArrayList<Postazione>> postazioni_test= new AtomicReference<>(new ArrayList<>());
        ArrayList<Postazione> postazioni=new ArrayList<>();
        try {
            posizioneDAO.insert(p);
            Posizione finalP=posizioneDAO.doRetrieveByBibliotecaZona(p.getBiblioteca(),p.getZona());
            postazioni.add(new Postazione("Z5",false, finalP));
            postazioni.add(new Postazione("Z6",false, finalP));
            postazioneDAO.insert(postazioni.get(0));
            postazioneDAO.insert(postazioni.get(1));
            assertDoesNotThrow(()-> postazioni_test.set(postazioneDAO.doRetrieveDisponibiliByPosizione(finalP.getBiblioteca(), finalP.getZona())));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
        assertTrue(postazioni_test.get().isEmpty());
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
    public void bloccaPostazioneBloccataTest(){
        try {
            Postazione pos=postazioneDAO.doRetrieveById("A10");
            assertFalse(postazioneDAO.bloccaPostazione(pos.getId()));
            Postazione pos_test=postazioneDAO.doRetrieveById(pos.getId());
            assertTrue(pos.isDisponibile()==pos_test.isDisponibile());
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }

    @Test
    public void bloccaPostazioneNonPresenteTest(){
        String id="123";
        assertFalse(postazioneDAO.bloccaPostazione(id));
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
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }

    @Test
    public void bloccoDeterminatoPostazioneNonPresenteTest(){
        Periodo periodo=new Periodo(9,13,new GregorianCalendar());
        try {
            Postazione pos=postazioneDAO.doRetrieveById("A10");
            pos.setId("123");
            assertThrows(SQLException.class,()-> postazioneDAO.bloccoDeterminato(periodo,pos));
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }

    @Test
    public void bloccoDeterminatoPresenteTest(){
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
            e.printStackTrace();
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
    public void sbloccaPostazioneSbloccataTest(){
        try {
            Postazione pos=postazioneDAO.doRetrieveById("A10");
            assertFalse(postazioneDAO.sbloccaPostazione(pos.getId()));
            Postazione pos_test=postazioneDAO.doRetrieveById(pos.getId());
            assertTrue(pos.isDisponibile()==pos_test.isDisponibile());
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }

    @Test
    public void sbloccaPostazioneNonPresenteTest(){
        try {
            String id="123";
            assertFalse(postazioneDAO.sbloccaPostazione(id));
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
            Postazione pos_test=postazioneDAO.doRetrieveById(pos.getId());
            assertFalse(pos_test.getBlocchi().contains(periodo));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }

    @Test
    public void sbloccoDeterminatoPostazioneNonPresenteTest(){
        try {
            Postazione pos=postazioneDAO.doRetrieveById("A10");
            Periodo periodo=pos.getBlocchi().get(0);
            assertFalse(postazioneDAO.sbloccaPostazione("123",periodo));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }

    @Test
    public void sbloccoDeterminatoBloccoNonPresenteTest(){
        try {
            Postazione pos=postazioneDAO.doRetrieveById("A10");
            Periodo periodo=new Periodo(100,12,14,new GregorianCalendar());
            assertFalse(postazioneDAO.sbloccaPostazione(pos.getId(),periodo));
            Postazione pos_test=postazioneDAO.doRetrieveById("A10");
            assertIterableEquals(pos.getBlocchi(),pos_test.getBlocchi());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }
}
