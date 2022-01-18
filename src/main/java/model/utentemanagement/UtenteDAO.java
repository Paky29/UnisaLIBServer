package model.utentemanagement;

import model.libromanagement.Libro;
import model.libromanagement.LibroDAO;
import model.prenotazionemanagement.Prenotazione;
import model.prenotazionemanagement.PrenotazioneDAO;
import model.prestitomanagement.Prestito;
import model.prestitomanagement.PrestitoDAO;
import utility.ConPool;
import utility.SwitchDate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtenteDAO {
    public Utente doRetrieveByEmailAndPasswordAll(String email, String password) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT u.email, u.pword, u.nome, u.cognome, u.is_admin, u.is_nuovo, u.eta, u.genere, u.matricola FROM Utente u" +
                    " WHERE u.email=? AND u.pword=?");
            ps.setString(1, email);
            ps.setString(2, password);

            Utente u=null;
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                PrestitoDAO prestitoDAO=new PrestitoDAO();
                LibroDAO libroDAO=new LibroDAO();
                PrenotazioneDAO prenotazioneDAO=new PrenotazioneDAO();
                ArrayList<Prestito> prestiti=prestitoDAO.doRetrieveByUtente(email);
                ArrayList<Prenotazione> prenotazioni=prenotazioneDAO.doRetrieveValidByUtente(email);
                ArrayList<Libro> interesse= libroDAO.doRetrieveInteresse(email);
                u = UtenteExtractor.extract(rs,prestiti, prenotazioni, interesse);
            }
            return u;
        }
    }

    public Utente doRetrieveByEmailAll(String email) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT u.email, u.pword, u.nome, u.cognome, u.is_admin, u.is_nuovo, u.eta, u.genere, u.matricola FROM Utente u" +
                    " WHERE u.email=?");
            ps.setString(1, email);

            Utente u=null;
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                PrestitoDAO prestitoDAO=new PrestitoDAO();
                LibroDAO libroDAO=new LibroDAO();
                PrenotazioneDAO prenotazioneDAO=new PrenotazioneDAO();
                ArrayList<Prestito> prestiti=prestitoDAO.doRetrieveByUtente(email);
                ArrayList<Prenotazione> prenotazioni=prenotazioneDAO.doRetrieveValidByUtente(email);
                ArrayList<Libro> interesse= libroDAO.doRetrieveInteresse(email);
                u = UtenteExtractor.extract(rs,prestiti, prenotazioni, interesse);
            }
            return u;
        }
    }

    public Utente doRetrieveByEmail(String email) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT u.email, u.pword, u.nome, u.cognome, u.is_admin, u.is_nuovo, u.eta, u.genere, u.matricola FROM Utente u WHERE u.email=?");
            ps.setString(1, email);

            Utente u=null;
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                u=UtenteExtractor.extract(rs);

            return u;
        }
    }

    public boolean doUpdate(Utente u) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE utente u SET u.nome=?, u.cognome=?, u.is_nuovo=?, u.eta=?, u.genere=? WHERE u.email=?");
            ps.setString(1, u.getNome());
            ps.setString(2, u.getCognome());
            ps.setBoolean(3, u.isNuovo());
            ps.setInt(4, u.getEta());
            ps.setString(5, u.getGenere());
            ps.setString(6, u.getEmail());


            if (ps.executeUpdate() != 1)
                throw new RuntimeException("Update error");

            return true;
        }
    }
}
