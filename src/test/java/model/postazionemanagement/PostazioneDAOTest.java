package model.postazionemanagement;

import model.posizionemanagement.Posizione;
import model.utentemanagement.Utente;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostazioneDAOTest {
    public PostazioneDAO postazioneDAO;

    @Before
    public void setup(){
        postazioneDAO = new PostazioneDAO();
    }

    @Test
    public void doRetrieveById(){
        Posizione pos = new Posizione(1, "umanistica", "piano 1");
        Postazione postazione = new Postazione("1",true, pos);

        final Postazione[] postazione_test = new Postazione[1];
        assertDoesNotThrow(() -> postazione_test[0]=postazioneDAO.doRetrieveById("1"));
        assertEquals(postazione_test, postazione);
    }


}
