package model.posizionemanagement;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PosizioneExtractor {
    public static Posizione extract(ResultSet rs) throws SQLException {
        Posizione p = new Posizione();
        p.setId(rs.getInt("p.posizione_id"));
        p.setBiblioteca(rs.getString("p.biblioteca"));
        p.setZona(rs.getString("p.zona"));

        return p;
    }
}
