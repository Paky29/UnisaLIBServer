package model.posizionemanagement;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Questa classe si occupa di estrarre dal ResultSet i valori riguardanti la Posizione
 * e di creare e restituire un oggetto di tipo Libro
 */
public class PosizioneExtractor {
    public static Posizione extract(ResultSet rs) throws SQLException {
        Posizione p = new Posizione();
        p.setId(rs.getInt("p.posizione_id"));
        p.setBiblioteca(rs.getString("p.biblioteca"));
        p.setZona(rs.getString("p.zona"));

        return p;
    }
}
