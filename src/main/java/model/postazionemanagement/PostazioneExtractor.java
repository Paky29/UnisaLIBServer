package model.postazionemanagement;

import model.posizionemanagement.Posizione;
import model.posizionemanagement.PosizioneExtractor;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Questa classe si occupa di estrarre si occupa di estrarre dal ResultSet i valori riguardanti Postazione
 * e di creare e restituire un oggetto di tipo Postazione
 */
public class PostazioneExtractor {
    public static Postazione extract(ResultSet rs) throws SQLException {
        Posizione p = PosizioneExtractor.extract(rs);
        Postazione ps= new Postazione();
        ps.setId(rs.getString("ps.postazione_id"));
        ps.setPosizione(p);
        ps.setDisponibile(rs.getBoolean("ps.is_disponibile"));

        return ps;
    }
}
