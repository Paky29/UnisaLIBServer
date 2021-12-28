package model.utentemanagement;

import model.libromanagement.Libro;
import utility.ConPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UtenteDAO {
    public Utente doRetrieveByEmailAndPassword(String email, String password) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT u.email, u.pword, u.nome, u.cognome, u.is_admin, u.eta, u.genere, u.matricola FROM Utente u WHERE u.email=? AND u.pw=?");
            ps.setString(1, email);
            ps.setString(2, password);

            Utente u=null;
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                u=UtenteExtractor.extract(rs);

            return u;
        }
    }

    public List<Utente> doRetrieveByLibro(Libro b) throws SQLException{
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT u.email, u.pword, u.nome, u.cognome, u.is_admin, u.eta, u.genere, u.matricola FROM Utente u WHERE u.email=? AND u.pw=?");
            ps.setString(1, email);
            ps.setString(2, password);

            Utente u=null;
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                u=UtenteExtractor.extract(rs);

            return u;
        }
    }
}
