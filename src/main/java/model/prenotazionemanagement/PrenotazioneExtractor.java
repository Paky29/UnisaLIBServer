package model.prenotazionemanagement;

import model.utentemanagement.UtenteDAO;
import utility.SwitchDate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrenotazioneExtractor {
    public static Prenotazione extract(ResultSet rs) throws SQLException {
        Prenotazione p=new Prenotazione();

        p.setData(SwitchDate.toGregorianCalendar(rs.getDate("p.data_p")));
        p.setOraInizio(rs.getInt("p.ora_inizio"));
        //p.setPostazione(new PostazioneDAO().doRetrieveById(rs.getString("p.postazione_fk")));
        p.setUtente(new UtenteDAO().doRetrieveByEmail(rs.getString("p.utente_fk")));
        p.setOraFine(rs.getInt("p.ora_fine"));

        return p;
    }
}
