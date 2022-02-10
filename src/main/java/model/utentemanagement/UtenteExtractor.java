package model.utentemanagement;

import model.libromanagement.Libro;
import model.prenotazionemanagement.Prenotazione;
import model.prestitomanagement.Prestito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * Questa classe si occupa di estrarre si occupa di estrarre dal ResultSet i valori riguardanti Utente
 * e di creare e restituire un oggetto di tipo Utente
 */
public class UtenteExtractor {
    public static Utente extract(ResultSet rs) throws SQLException {
        Utente u= new Utente.UtenteBuilder().
                email(rs.getString("u.email")).
                password(rs.getString("u.pword")).
                nome(rs.getString("u.nome")).
                cognome(rs.getString("u.cognome")).
                admin(rs.getBoolean("u.is_admin")).
                nuovo(rs.getBoolean("u.is_nuovo")).
                eta(rs.getInt("u.eta")).
                genere(rs.getString("u.genere")).
                matricola(rs.getString("u.matricola")).
                build();
        return u;
    }

    public static Utente extract(ResultSet rs, ArrayList<Prestito> prestiti, ArrayList<Prenotazione> prenotazioni, ArrayList<Libro> interesse) throws SQLException {
        Utente u= new Utente.UtenteBuilder().
                email(rs.getString("u.email")).
                password(rs.getString("u.pword")).
                nome(rs.getString("u.nome")).
                cognome(rs.getString("u.cognome")).
                admin(rs.getBoolean("u.is_admin")).
                nuovo(rs.getBoolean("u.is_nuovo")).
                eta(rs.getInt("u.eta")).
                genere(rs.getString("u.genere")).
                matricola(rs.getString("u.matricola")).
                prestiti(prestiti).
                interessi(interesse).
                prenotazioni(prenotazioni).
                build();
        return u;
    }
}
