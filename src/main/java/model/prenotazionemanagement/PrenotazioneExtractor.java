package model.prenotazionemanagement;

import model.postazionemanagement.PostazioneDAO;
import model.utentemanagement.UtenteDAO;
import utility.SwitchDate;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Questa classe si occupa di estrarre si occupa di estrarre dal ResultSet i valori riguardanti Prenotazione
 * e di creare e restituire un oggetto di tipo Prenotazione
 */
public class PrenotazioneExtractor {
    public static Prenotazione extract(ResultSet rs) throws SQLException {
        Prenotazione p=new Prenotazione();

        p.setData(SwitchDate.toGregorianCalendar(rs.getDate("p.data_p")));
        p.setOraInizio(rs.getInt("p.ora_inizio"));
        p.setPostazione(new PostazioneDAO().doRetrieveById(rs.getString("p.postazione_fk")));
        p.setUtente(new UtenteDAO().doRetrieveByEmail(rs.getString("p.utente_fk")));
        p.setOraFine(rs.getInt("p.ora_fine"));

        return p;
    }
}
