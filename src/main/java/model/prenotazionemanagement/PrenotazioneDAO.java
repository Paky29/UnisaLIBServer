package model.prenotazionemanagement;

import model.postazionemanagement.Postazione;
import model.prestitomanagement.Prestito;
import model.utentemanagement.Utente;
import utility.ConPool;
import utility.SwitchDate;

import java.sql.*;
import java.util.ArrayList;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PrenotazioneDAO {

    public boolean insert(Prenotazione p) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT into prenotazione (data_p, ora_inizio, postazione_fk, utente_fk, ora_fine) " +
                    "VALUES (?, ?, ?, ?, ?)");
            ps.setDate(1, SwitchDate.toDate(p.getData()));
            System.out.println(SwitchDate.toDate(p.getData()));
            ps.setInt(2, p.getOraInizio());
            System.out.println(p.getOraInizio());
            ps.setString(3, p.getPostazione().getId());
            System.out.println(p.getPostazione().getId());
            ps.setString(4, p.getUtente().getEmail());
            System.out.println(p.getUtente().getEmail());
            ps.setInt(5, p.getOraFine());
            System.out.println(p.getOraFine());

            if (ps.executeUpdate() != 1)
                throw new RuntimeException("INSERT error");

            return true;
        }
    }

    public Prenotazione doRetrieveByInfo(GregorianCalendar data, int oraInizio, String postazioneId, String utenteEmail) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT p.data_p, p.ora_inizio, p.postazione_fk, p.utente_fk, p.ora_fine " +
                    "FROM prenotazione p WHERE  p.data_p=? AND p.ora_inizio=? AND p.postazione_fk=? AND p.utente_fk=?");
            ps.setDate(1, SwitchDate.toDate(data));
            ps.setInt(2, oraInizio);
            ps.setString(3, postazioneId);
            ps.setString(4, utenteEmail);

            Prenotazione p = null;
            ResultSet rs = ps.executeQuery();

            if(rs.next())
                p = PrenotazioneExtractor.extract(rs);

            return p;
        }
    }


    public ArrayList<Prenotazione> doRetrieveValidByPostazioneDate(Postazione post, GregorianCalendar gc) throws SQLException{
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps=conn.prepareStatement("SELECT p.data_p, p.ora_inizio, p.postazione_fk, p.utente_fk, p.ora_fine " +
                    "FROM prenotazione p WHERE p.postazione_fk=? AND data_p=?");
            ps.setString(1, post.getId());
            ps.setDate(2, SwitchDate.toDate(gc));

            ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                prenotazioni.add(PrenotazioneExtractor.extract(rs));

            return prenotazioni;
        }
    }

    public ArrayList<Prenotazione> doRetrieveValidByUtente(Utente u) throws SQLException{
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps=conn.prepareStatement("SELECT p.data_p, p.ora_inizio, p.postazione_fk, p.utente_fk, p.ora_fine " +
                    "FROM prenotazione p WHERE p.utente_fk=? AND data_p>=?");
            ps.setString(1, u.getEmail());
            ps.setDate(2, SwitchDate.toDate(new GregorianCalendar()));

            ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                prenotazioni.add(PrenotazioneExtractor.extract(rs));

            return prenotazioni;
        }
    }

    public ArrayList<Prenotazione> doRetrieveValidByUtente(String email) throws SQLException{
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps=conn.prepareStatement("SELECT p.data_p, p.ora_inizio, p.postazione_fk, p.utente_fk, p.ora_fine " +
                    "FROM prenotazione p WHERE p.utente_fk=? AND data_p>=?");
            ps.setString(1, email);
            ps.setDate(2, SwitchDate.toDate(new GregorianCalendar()));

            ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                prenotazioni.add(PrenotazioneExtractor.extract(rs));

            return prenotazioni;
        }
    }

}
