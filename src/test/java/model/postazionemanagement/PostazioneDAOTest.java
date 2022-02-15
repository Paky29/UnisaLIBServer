package model.postazionemanagement;

import model.posizionemanagement.Posizione;
import model.posizionemanagement.PosizioneDAO;
import model.prenotazionemanagement.Prenotazione;
import model.prenotazionemanagement.PrenotazioneDAO;
import model.utentemanagement.Utente;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import utility.SwitchDate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Testa i metodi di PostazioneDAO
 * */
public class PostazioneDAOTest {
    public PostazioneDAO postazioneDAO;
    public PosizioneDAO posizioneDAO;
    public PeriodoDAO periodoDAO;
    public PrenotazioneDAO prenotazioneDAO;
    @Before
    public void setup(){
        postazioneDAO = new PostazioneDAO();
        posizioneDAO = new PosizioneDAO();
        periodoDAO=new PeriodoDAO();
        prenotazioneDAO=new PrenotazioneDAO();
    }
    /**
     * Testa il corretto inserimento
     * */
    @Test
    public void insertTest(){
        Posizione pos = new Posizione(1, "umanistica", "piano 1");
        Postazione postazione = new Postazione("testA10",true, pos);

        AtomicBoolean result= new AtomicBoolean(false);
        assertDoesNotThrow(()-> result.set(postazioneDAO.insert(postazione)));
        assertTrue(result.get());
    }
    /**
     * Testa l'inserimento di una Postazione già esistente
     * */
    @Test
    @Order(2)
    public void insertPostazioneEsistenteTest(){
        Posizione pos = new Posizione(1, "umanistica", "piano 1");
        Postazione postazione = new Postazione("test1",true, pos);

        assertThrows(SQLException.class,()->postazioneDAO.insert(postazione));
    }
    /**
     * Testa l'inserimento di una Postazione non presente
     * */
    @Test
    @Order(3)
    public void insertPosizioneNonPresenteTest() {
        Posizione pos = new Posizione(1, "linguistica", "piano 1");
        Postazione postazione = new Postazione("test1",true, pos);

        assertThrows(SQLException.class,()->postazioneDAO.insert(postazione));
    }
    /**
     * Testa l'interrogazione del DAO sull'id
     * */
    @Test
    @Order(4)
    public void doRetrieveByIdTest(){
        Posizione pos = new Posizione(1, "umanistica", "piano 1");
        Postazione postazione = new Postazione("test1",true, pos);

        final Postazione[] postazione_test = new Postazione[1];
        assertDoesNotThrow(() -> postazione_test[0]=postazioneDAO.doRetrieveById(postazione.getId()));
        assertEquals(postazione_test[0].getId(), postazione.getId());
    }
    /**
     * Testa l'interrogazione del DAO su un id non presente
     * */
    @Test
    @Order(5)
    public void doRetrieveByIdNonPresenteTest(){
        Posizione pos = new Posizione(1, "umanistica", "piano 1");
        Postazione postazione = new Postazione("testA12",true, pos);

        final Postazione[] postazione_test = new Postazione[1];
        assertDoesNotThrow(() -> postazione_test[0]=postazioneDAO.doRetrieveById(postazione.getId()));
        assertNull(postazione_test[0]);
    }
    /**
     * Testa l'interrogazione del DAO data una posizione
     * */
    @Test
    @Order(6)
    public void doRetrieveByPosizioneTest(){
        Posizione p=new Posizione("scientifica","piano 10");
        AtomicReference<ArrayList<Postazione>> postazioni_test= new AtomicReference<>(new ArrayList<>());
        ArrayList<Postazione> postazioni=new ArrayList<>();
        try {
            posizioneDAO.insert(p);
            Posizione finalP =posizioneDAO.doRetrieveByBibliotecaZona(p.getBiblioteca(),p.getZona());
            postazioni.add(new Postazione("testZ1",true, finalP));
            postazioni.add(new Postazione("testZ2",false, finalP));
            postazioneDAO.insert(postazioni.get(0));
            postazioneDAO.insert(postazioni.get(1));
            assertDoesNotThrow(()-> postazioni_test.set(postazioneDAO.doRetrieveByPosizione(finalP.getBiblioteca(), finalP.getZona())));
            posizioneDAO.delete(p.getBiblioteca(),p.getZona());
            assertIterableEquals(postazioni, postazioni_test.get());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
    /**
     * Testa l'interrogazione del DAO data una posizione con blocco
     * */
    @Test
    @Order(6)
    public void doRetrieveByPosizioneConBloccoTest(){
        Posizione pos=new Posizione("scientifica","piano 10");
        AtomicReference<ArrayList<Postazione>> postazioni_test= new AtomicReference<>(new ArrayList<>());
        ArrayList<Postazione> postazioni=new ArrayList<>();
        try {
            posizioneDAO.insert(pos);
            Posizione finalP =posizioneDAO.doRetrieveByBibliotecaZona(pos.getBiblioteca(),pos.getZona());
            postazioni.add(new Postazione("testZ1",true, finalP));
            postazioni.add(new Postazione("testZ2",false, finalP));
            postazioneDAO.insert(postazioni.get(0));
            postazioneDAO.insert(postazioni.get(1));
            Periodo p=(new Periodo(11,13,new GregorianCalendar()));
            postazioneDAO.bloccoDeterminato(p, postazioni.get(0));
            postazioni.get(0).getBlocchi().add(p);
            assertDoesNotThrow(()-> postazioni_test.set(postazioneDAO.doRetrieveByPosizione(finalP.getBiblioteca(), finalP.getZona())));
            posizioneDAO.delete(pos.getBiblioteca(),pos.getZona());
            assertIterableEquals(postazioni,postazioni_test.get());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
    /**
     * Testa l'interrogazione del DAO data una posizione non esistente
     * */
    @Test
    @Order(7)
    public void doRetrieveByPosizioneNonEsistenteTest(){
        Posizione p=new Posizione("linguistica","piano 10");
        AtomicReference<ArrayList<Postazione>> postazioni_test= new AtomicReference<>(new ArrayList<>());
        assertDoesNotThrow(()-> postazioni_test.set(postazioneDAO.doRetrieveByPosizione(p.getBiblioteca(), p.getZona())));
        assertTrue(postazioni_test.get().isEmpty());
    }
    /**
     * Testa l'interrogazione del DAO delle postazioni disponibili data una posizione
     * */
    @Test
    @Order(8)
    public void doRetrieveDisponibiliByPosizioneTest(){
        Posizione p=new Posizione("scientifica","piano 11");
        AtomicReference<ArrayList<Postazione>> postazioni_test= new AtomicReference<>(new ArrayList<>());
        ArrayList<Postazione> postazioni=new ArrayList<>();
        try {
            posizioneDAO.insert(p);
            Posizione finalP=posizioneDAO.doRetrieveByBibliotecaZona(p.getBiblioteca(),p.getZona());
            postazioni.add(new Postazione("testZ3",true, finalP));
            postazioni.add(new Postazione("testZ4",false, finalP));
            postazioneDAO.insert(postazioni.get(0));
            postazioneDAO.insert(postazioni.get(1));
            assertDoesNotThrow(()-> postazioni_test.set(postazioneDAO.doRetrieveDisponibiliByPosizione(finalP.getBiblioteca(), finalP.getZona())));
            posizioneDAO.delete(p.getBiblioteca(),p.getZona());
            assertEquals(postazioni.get(0), postazioni_test.get().get(0));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
    /**
     * Testa l'interrogazione del DAO delle postazioni disponibili data una posizione con blocco
     * */
    @Test
    @Order(6)
    public void doRetrieveDisponibiliByPosizioneConBloccoTest(){
        Posizione pos=new Posizione("scientifica","piano 10");
        AtomicReference<ArrayList<Postazione>> postazioni_test= new AtomicReference<>(new ArrayList<>());
        ArrayList<Postazione> postazioni=new ArrayList<>();
        try {
            posizioneDAO.insert(pos);
            Posizione finalP =posizioneDAO.doRetrieveByBibliotecaZona(pos.getBiblioteca(),pos.getZona());
            postazioni.add(new Postazione("testZ1",true, finalP));
            postazioni.add(new Postazione("testZ2",false, finalP));
            postazioneDAO.insert(postazioni.get(0));
            postazioneDAO.insert(postazioni.get(1));
            Periodo p=(new Periodo(11,13,new GregorianCalendar()));
            postazioneDAO.bloccoDeterminato(p, postazioni.get(0));
            postazioni.get(0).getBlocchi().add(p);
            assertDoesNotThrow(()-> postazioni_test.set(postazioneDAO.doRetrieveDisponibiliByPosizione(finalP.getBiblioteca(), finalP.getZona())));
            posizioneDAO.delete(pos.getBiblioteca(),pos.getZona());
            assertEquals(postazioni.get(0),postazioni_test.get().get(0));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
    /**
     * Testa l'interrogazione del DAO delle postazioni disponibili
     * */
    @Test
    @Order(9)
    public void doRetrieveDisponibiliByPosizioneSenzaPostazioniTest(){
        Posizione p=new Posizione("scientifica","piano 12");
        AtomicReference<ArrayList<Postazione>> postazioni_test= new AtomicReference<>(new ArrayList<>());
        ArrayList<Postazione> postazioni=new ArrayList<>();
        try {
            posizioneDAO.insert(p);
            Posizione finalP=posizioneDAO.doRetrieveByBibliotecaZona(p.getBiblioteca(),p.getZona());
            postazioni.add(new Postazione("testZ5",false, finalP));
            postazioni.add(new Postazione("testZ6",false, finalP));
            postazioneDAO.insert(postazioni.get(0));
            postazioneDAO.insert(postazioni.get(1));
            assertDoesNotThrow(()-> postazioni_test.set(postazioneDAO.doRetrieveDisponibiliByPosizione(finalP.getBiblioteca(), finalP.getZona())));
            posizioneDAO.delete(p.getBiblioteca(),p.getZona());
            assertTrue(postazioni_test.get().isEmpty());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione");
        }
    }
    /**
     * Testa il blocco di una postazione
     * */
    @Test
    @Order(10)
    public void bloccaPostazioneTest(){
        try {
            Postazione pos=postazioneDAO.doRetrieveById("test1");
            assertTrue(postazioneDAO.bloccaPostazione(pos.getId()));
            Postazione pos_test=postazioneDAO.doRetrieveById(pos.getId());
            assertTrue(pos.isDisponibile()!=pos_test.isDisponibile());
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }
    /**
     * Testa il blocco di una postazione già bloccata
     * */
    @Test
    @Order(11)
    public void bloccaPostazioneBloccataTest(){
        try {
            Postazione pos=postazioneDAO.doRetrieveById("test2");
            assertFalse(postazioneDAO.bloccaPostazione(pos.getId()));
            Postazione pos_test=postazioneDAO.doRetrieveById(pos.getId());
            assertTrue(pos.isDisponibile()==pos_test.isDisponibile());
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }
    /**
     * Testa il blocco di una postazione già bloccata
     * */
    @Test
    @Order(12)
    public void bloccaPostazioneNonPresenteTest(){
        String id="test123";
        assertFalse(postazioneDAO.bloccaPostazione(id));
    }
    /**
     * Testa il blocco determinato di una postazione
     * */
    @Test
    @Order(13)
    public void bloccoDeterminatoTest(){
        Periodo periodo=new Periodo(9,13,new GregorianCalendar());
        try {
            Postazione pos=postazioneDAO.doRetrieveById("test3");
            assertEquals("Blocchi inseriti correttamente",postazioneDAO.bloccoDeterminato(periodo,pos));
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
    /**
     * Testa il blocco determinato di una postazione
     * */
    @Test
    @Order(13)
    public void bloccoDeterminatoIntervalloTest(){
        Periodo periodo=new Periodo(11,16,new GregorianCalendar());
        try {
            Postazione pos=postazioneDAO.doRetrieveById("test10");
            assertEquals("Blocchi inseriti correttamente",postazioneDAO.bloccoDeterminato(periodo,pos));
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
    /**
     * Testa il blocco determinato non presente su una postazione
     * */
    @Test
    @Order(14)
    public void bloccoDeterminatoPostazioneNonPresenteTest(){
        Periodo periodo=new Periodo(9,13,new GregorianCalendar());
        try {
            Postazione pos=postazioneDAO.doRetrieveById("1");
            pos.setId("test123");
            assertThrows(SQLException.class,()-> postazioneDAO.bloccoDeterminato(periodo,pos));
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }
    /**
     * Testa il blocco determinato già presente su una postazione
     * */
    @Test
    @Order(15)
    public void bloccoDeterminatoPresenteTest(){
        Periodo periodo=new Periodo(9,13,new GregorianCalendar(2022,2,20));
        try {
            Postazione pos=postazioneDAO.doRetrieveById("test4");
            postazioneDAO.bloccoDeterminato(periodo,pos);
            Postazione pos1=postazioneDAO.doRetrieveById(pos.getId());
            assertEquals("Blocchi gia' presenti: 9-11; 11-13",postazioneDAO.bloccoDeterminato(periodo,pos1));
            Postazione pos2=postazioneDAO.doRetrieveById(pos.getId());
            assertIterableEquals(pos1.getBlocchi(),pos2.getBlocchi());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }
    /**
     * Testa lo sblocco della Postazione
     * */
    @Test
    @Order(16)
    public void sbloccaPostazioneTest(){
        try {
            Postazione pos=postazioneDAO.doRetrieveById("test5");
            assertTrue(postazioneDAO.sbloccaPostazione(pos.getId()));
            Postazione pos_test=postazioneDAO.doRetrieveById(pos.getId());
            assertTrue(pos.isDisponibile()!=pos_test.isDisponibile());
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }
    /**
     * Testa lo sblocco della Postazione già sbloccata
     * */
    @Test
    @Order(17)
    public void sbloccaPostazioneSbloccataTest(){
        try {
            Postazione pos=postazioneDAO.doRetrieveById("test6");
            assertFalse(postazioneDAO.sbloccaPostazione(pos.getId()));
            Postazione pos_test=postazioneDAO.doRetrieveById(pos.getId());
            assertTrue(pos.isDisponibile()==pos_test.isDisponibile());
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }
    /**
     * Testa lo sblocco di una Postazione non presente
     * */
    @Test
    @Order(18)
    public void sbloccaPostazioneNonPresenteTest(){
        try {
            String id="test123";
            assertFalse(postazioneDAO.sbloccaPostazione(id));
        } catch (SQLException e) {
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }
    /**
     * Testa lo sblocco determinato di un Periodo di una Postazione
     * */
    @Test
    @Order(19)
    public void sbloccoDeterminatoTest(){
        Periodo periodo=new Periodo(9,11,new GregorianCalendar());
        try {
            Postazione pos=postazioneDAO.doRetrieveById("test7");
            postazioneDAO.bloccoDeterminato(periodo,pos);
            Periodo periodo_test=periodoDAO.doRetrieveByInfo(periodo);
            assertTrue(postazioneDAO.sbloccaPostazione(pos.getId(),periodo_test));
            Postazione pos_test=postazioneDAO.doRetrieveById(pos.getId());
            assertFalse(pos_test.getBlocchi().contains(periodo));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }
    /**
     * Testa lo sblocco determinato di un Periodo di una Postazione non presente
     * */
    @Test
    @Order(20)
    public void sbloccoDeterminatoPostazioneNonPresenteTest(){
        try {
            Periodo periodo=new Periodo(9,11,new GregorianCalendar(2022, Calendar.JANUARY,29));
            assertFalse(postazioneDAO.sbloccaPostazione("123", periodoDAO.doRetrieveByInfo(periodo)));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }
    /**
     * Testa se una Postazione è disponibile
     * */
    @Test
    public void isDisponibileTrueTest(){
        try{
            String idPos="1";
            assertEquals(1,postazioneDAO.isDisponibile(idPos));
        }catch(SQLException e){
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }
    /**
     * Testa se una Postazione non è disponibile
     * */
    @Test
    public void isDisponibileFalseTest(){
        try{
            String idPos="10";
            assertEquals(0,postazioneDAO.isDisponibile(idPos));
        }catch(SQLException e){
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }
    /**
     * Testa se una Postazione non presente è disponibile
     * */
    @Test
    public void isDisponibilePostazioneNonPresenteTest(){
        try{
            String idPos="test123";
            assertEquals(-1,postazioneDAO.isDisponibile(idPos));
        }catch(SQLException e){
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }
    /**
     * Testa lo sblocco determinato di un Periodo di una Blocco non presente
     * */
    @Test
    public void sbloccoDeterminatoBloccoNonPresenteTest(){
        try {
            Postazione pos=postazioneDAO.doRetrieveById("test8");
            Periodo periodo=new Periodo(100,12,14,new GregorianCalendar());
            assertFalse(postazioneDAO.sbloccaPostazione(pos.getId(),periodo));
            Postazione pos_test=postazioneDAO.doRetrieveById("test8");
            assertIterableEquals(pos.getBlocchi(),pos_test.getBlocchi());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }
    /**
     * Testa il blocco determinato di una Postazione sulla quale sono state effettuate precedentemente delle Prenotazioni
     * */
    @Test
    @AfterAll
    public void bloccoDeterminatoPostazioneConPrenotazioniTest(){
        Periodo periodo=new Periodo(9,13,new GregorianCalendar());
        try {
            Postazione pos=postazioneDAO.doRetrieveById("test9");
            Utente utente=new Utente.UtenteBuilder().email("dd").password("donia").nome("daniele").cognome("donia").admin(false).matricola("0512108772").nuovo(true).genere("M").eta(21).build();
            Prenotazione pren=new Prenotazione(periodo.getData(),periodo.getOraInizio(), periodo.getOraFine(), utente, pos);
            prenotazioneDAO.insert(pren);
            assertEquals("Blocchi inseriti correttamente",postazioneDAO.bloccoDeterminato(periodo,pos));
            Postazione pos_test=postazioneDAO.doRetrieveById(pos.getId());
            for(int start=periodo.getOraInizio();start<periodo.getOraFine();start+=2){
                if(start==13)
                    ++start;
                Periodo periodo_test=new Periodo(start,start+2,new GregorianCalendar());
                assertTrue(pos_test.getBlocchi().contains(periodo_test));
            }
            assertFalse(prenotazioneDAO.doRetrieveValidByUtente(utente.getEmail()).contains(pren));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Non avrebbe dovuto lanciare l'eccezione:"+e.getLocalizedMessage());
        }
    }
}
