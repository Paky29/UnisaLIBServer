package model.prestitomanagement;

import utility.ConPool;
import utility.SwitchDate;

import java.sql.*;

public class PrestitoDAO {

    public boolean insert(Prestito p) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT into prestito (data_inizio, libro_fk, utente_fk, data_fine) VALUES (?, ?, ?, ?)");
            ps.setDate(1, SwitchDate.toDate(p.getDataInizio()));
            ps.setString(2, p.getLibro().getIsbn());
            ps.setString(3, p.getUtente().getEmail());
            ps.setDate(4, SwitchDate.toDate(p.getDataFine()));

            if (ps.executeUpdate() != 1)
                throw new RuntimeException("INSERT error");

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
            PreparedStatement ps = conn.prepareStatement("UPDATE prestito p SET p.data_consegna=?,p.is_attivo=true WHERE p.data_inizio=? AND p.libro_fk=? AND p.utente_fk=?");
            ps.setDate(1, SwitchDate.toDate(p.getDataConsegna()));
            ps.setDate(2, SwitchDate.toDate(p.getDataInizio()));
            ps.setString(3, p.getLibro().getIsbn());
            ps.setString(4, p.getUtente().getEmail());

            if (ps.executeUpdate() != 1)
                throw new RuntimeException("Chiusura prestito error");

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
                throw new RuntimeException("Valutazione prestito error");

            return true;
        }
    }

}
