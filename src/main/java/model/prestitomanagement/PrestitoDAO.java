package model.prestitomanagement;

import model.libromanagement.Libro;
import model.prenotazionemanagement.PrenotazioneExtractor;
import model.utentemanagement.Utente;
import utility.ConPool;
import utility.SwitchDate;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PrestitoDAO {

    public boolean insert(Prestito p) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement("INSERT into prestito (data_inizio, libro_fk, utente_fk, data_fine) VALUES (?, ?, ?, ?)");
            ps.setDate(1, SwitchDate.toDate(p.getDataInizio()));
            ps.setString(2, p.getLibro().getIsbn());
            ps.setString(3, p.getUtente().getEmail());
            ps.setDate(4, SwitchDate.toDate(p.getDataFine()));
            if (ps.executeUpdate() != 1) {
                conn.setAutoCommit(true);
                return false;
            }

            ps = conn.prepareStatement("UPDATE libro l SET l.n_copie=? WHERE l.isbn=?");
            ps.setInt(1, (p.getLibro().getnCopie()-1));
            ps.setString(2, p.getLibro().getIsbn());
            if (ps.executeUpdate() != 1) {
                conn.rollback();
                conn.setAutoCommit(true);
                return false;
            }

            conn.commit();
            conn.setAutoCommit(true);

            return true;
        }
    }

    public boolean attivaPrestito(Prestito p) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE prestito p SET p.is_attivo=true WHERE p.data_inizio=? AND p.libro_fk=? AND p.utente_fk=?");
            ps.setDate(1, SwitchDate.toDate(p.getDataInizio()));
            ps.setString(2, p.getLibro().getIsbn());
            ps.setString(3, p.getUtente().getEmail());

            if (ps.executeUpdate() != 1)
                throw new RuntimeException("Attivazione error");

            return true;
        }
    }

    public boolean concludiPrestito(Prestito p) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement("UPDATE prestito p SET p.data_consegna=?,p.is_attivo=true " +
                    "WHERE p.data_inizio=? AND p.libro_fk=? AND p.utente_fk=?");
            ps.setDate(1, SwitchDate.toDate(p.getDataConsegna()));
            ps.setDate(2, SwitchDate.toDate(p.getDataInizio()));
            ps.setString(3, p.getLibro().getIsbn());
            ps.setString(4, p.getUtente().getEmail());
            if (ps.executeUpdate() != 1) {
                conn.setAutoCommit(true);
                return false;
            }

            ps = conn.prepareStatement("SELECT l.n_copie FROM Libro l WHERE l.isbn=?");
            ps.setString(1, p.getLibro().getIsbn());
            ResultSet rs = ps.executeQuery();
            int n_copie=0;
            if (rs.next())
                n_copie=rs.getInt("l.n_copie");
            else{
                conn.rollback();
                conn.setAutoCommit(true);
                return false;
            }

            ps = conn.prepareStatement("UPDATE libro l SET l.n_copie=? WHERE l.isbn=?");
            ps.setInt(1, n_copie+1);
            ps.setString(2, p.getLibro().getIsbn());
            if (ps.executeUpdate() != 1) {
                conn.rollback();
                conn.setAutoCommit(true);
                return false;
            }

            conn.commit();
            conn.setAutoCommit(true);

            return true;
        }
    }

    public boolean valutaPrestito(Prestito p) throws SQLException {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("UPDATE prestito p SET p.commento=?, p.voto=? WHERE p.data_inizio=? AND p.libro_fk=? AND p.utente_fk=?");
            ps.setString(1,p.getCommento());
            ps.setFloat(2,p.getVoto());
            ps.setDate(3, SwitchDate.toDate(p.getDataInizio()));
            ps.setString(4, p.getLibro().getIsbn());
            ps.setString(5, p.getUtente().getEmail());

            if(ps.executeUpdate() != 1)
                return false;

            return true;
        }
    }

    /*public ArrayList<Prestito> doRetrieveByLibro(Libro l) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT p.data_inizio, p.libro_fk, p.utente_fk, p.data_fine, p.data_consegna, p.voto, p.is_attivo " +
                    "FROM prestito p WHERE p.libro_fk=?");
            ps.setString(1, l.getIsbn());

            ArrayList<Prestito> prestiti = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                prestiti.add(PrestitoExtractor.extract(rs));

            return prestiti;
        }
    }*/

    /*public ArrayList<Prestito> doRetrieveValidByLibro(Libro l) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT p.data_inizio, p.libro_fk, p.utente_fk, p.data_fine, p.data_consegna, p.voto, p.commento, p.is_attivo " +
                    "FROM prestito p WHERE p.libro_fk=? AND p.data_consegna is null");
            ps.setString(1, l.getIsbn());

            ArrayList<Prestito> prestiti = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                prestiti.add(PrestitoExtractor.extract(rs));

            return prestiti;
        }
    }*/

    public ArrayList<Prestito> doRetrieveValidByLibro(String isbn) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT p.data_inizio, p.libro_fk, p.utente_fk, p.data_fine, p.data_consegna, p.voto, p.commento, p.is_attivo " +
                    "FROM prestito p WHERE p.libro_fk=? AND p.data_consegna is null");
            ps.setString(1, isbn);

            ArrayList<Prestito> prestiti = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                prestiti.add(PrestitoExtractor.extract(rs));

            return prestiti;
        }
    }

    /*public ArrayList<Prestito> doRetrieveByUtente(Utente u) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT p.data_inizio, p.libro_fk, p.utente_fk, p.data_fine, p.data_consegna, p.voto, p.is_attivo " +
                    "FROM prestito p WHERE p.utente_fk=?");
            ps.setString(1, u.getEmail());

            ArrayList<Prestito> prestiti = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                prestiti.add(PrestitoExtractor.extract(rs));

            return prestiti;
        }
    }*/

    public ArrayList<Prestito> doRetrieveByUtente(String email) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT p.data_inizio, p.libro_fk, p.utente_fk, p.data_fine, p.data_consegna, p.voto, p.commento, p.is_attivo " +
                    "FROM prestito p WHERE p.utente_fk=?");
            ps.setString(1, email);

            ArrayList<Prestito> prestiti = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                prestiti.add(PrestitoExtractor.extract(rs));

            return prestiti;
        }
    }

    public Prestito doRetrieveValidByUtente(String email) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT p.data_inizio, p.libro_fk, p.utente_fk, p.data_fine, p.data_consegna, p.voto, p.commento, p.is_attivo " +
                    "FROM prestito p WHERE p.utente_fk=? AND p.data_consegna is null");
            ps.setString(1, email);

            Prestito p = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                p = (PrestitoExtractor.extract(rs));

            return p;
        }
    }


    public Prestito doRetrieveByKey(GregorianCalendar dataInizio, String isbnLibro, String emailUtente) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT p.data_inizio, p.libro_fk, p.utente_fk, p.data_fine, p.data_consegna, p.voto, p.commento, p.is_attivo " +
                    "FROM prestito p WHERE p.data_inizio=? AND p.libro_fk=? AND p.utente_fk=?");
            ps.setDate(1, SwitchDate.toDate(dataInizio));
            ps.setString(2, isbnLibro);
            ps.setString(3, emailUtente);

            Prestito p = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                p = (PrestitoExtractor.extract(rs));

            return p;
        }
    }
}
