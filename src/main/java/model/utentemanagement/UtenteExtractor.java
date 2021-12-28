package model.utentemanagement;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteExtractor {
    public static Utente extract(ResultSet rs) throws SQLException {
        Utente u=new Utente();
        u.setEmail(rs.getString("u.email"));
        u.setPassword(rs.getString("u.pword"));
        u.setNome(rs.getString("u.nome"));
        u.setCognome(rs.getString("u.cognome"));
        u.setAdmin(rs.getBoolean("u.is_admin"));
        u.setEta(rs.getInt("u.eta"));
        u.setGenere(rs.getString("u.genere"));
        u.setMatricola(rs.getString("u.matricola"));
        return u;
    }
}
