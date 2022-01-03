package model.postazionemanagement;

import model.posizionemanagement.Posizione;
import model.posizionemanagement.PosizioneExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostazioneExtractor {
    public static Postazione extract(ResultSet rs) throws SQLException {
        Posizione p = PosizioneExtractor.extract(rs);
        Postazione ps= new Postazione();
        ps.setId(rs.getString("ps.postazione_id"));
        if(p!=null)
        ps.setPosizione(p);
        ps.setDisponibile(rs.getBoolean("ps.is_disponibile"));

        return ps;
    }
}
