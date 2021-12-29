package model.prestitomanagement;

import utility.ConPool;
import utility.SwitchDate;

import java.sql.*;

public class PrestitoDAO {
    SwitchDate sw = new SwitchDate();

    public boolean insert(Prestito p) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT into prestito (data_inizio, libro_fk, utente_fk, data_fine, data_consegna, voto, is_attivo) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setDate(1, sw.toDate(p.getDataInizio()));
            ps.setString(2, p.getLibro().getIsbn());
            ps.setString(3, p.getUtente().getEmail());
            ps.setDate(4, sw.toDate(p.getDataFine()));
            ps.setDate(5, sw.toDate(p.getDataConsegna()));
            ps.setInt(6, p.getVoto());
            ps.setBoolean(7, p.isAttivo());

            if (ps.executeUpdate() != 1)
                throw new RuntimeException("INSERT error");

            return true;
        }
    }

    public boolean attivaPrestito(Prestito p) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE prestito p SET p.data_fine=?, p.is_attivo=? WHERE p.data_inizio=? AND p.libro_fk=? AND p.utente_fk=?");
            ps.setDate(1, sw.toDate(p.getDataFine()));
            ps.setBoolean(2, p.isAttivo());
            ps.setDate(3, sw.toDate(p.getDataInizio()));
            ps.setString(4, p.getLibro().getIsbn());
            ps.setString(5, p.getUtente().getEmail());

            if (ps.executeUpdate() != 1)
                throw new RuntimeException("UPDATE error");

            return true;
        }
    }

    public boolean concludiPrestito(Prestito p) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE prestito p SET p.data_consegna=?, p.is_attivo=? WHERE p.data_inizio=? AND p.libro_fk=? AND p.utente_fk=?");
            ps.setDate(1, sw.toDate(p.getDataConsegna()));
            ps.setBoolean(2, p.isAttivo());
            ps.setDate(3, sw.toDate(p.getDataInizio()));
            ps.setString(4, p.getLibro().getIsbn());
            ps.setString(5, p.getUtente().getEmail());

            if (ps.executeUpdate() != 1)
                throw new RuntimeException("UPDATE error");

            return true;
        }
    }

}
