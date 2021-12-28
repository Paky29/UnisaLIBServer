package model.utentemanagement;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteExtractor {
    public static Utente extract(ResultSet rs) throws SQLException {
        Utente u= new Utente.UtenteBuilder().
                email(rs.getString("u.email")).
                password(rs.getString("u.pword")).
                nome(rs.getString("u.nome")).
                cognome(rs.getString("u.cognome")).
                admin(rs.getBoolean("u.is_admin")).
                eta(rs.getInt("u.eta")).
                genere(rs.getString("u.genere")).
                matricola(rs.getString("u.matricola")).
                build();
        return u;
    }
}
