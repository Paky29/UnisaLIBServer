package model.posizionemanagement;

import utility.ConPool;

import java.sql.*;

public class PosizioneDAO {

    public Posizione doRetrieveById(int id) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT p.posizione_id, p.biblioteca, p.zona FROM posizione p WHERE p.posizione_id=?");
            ps.setInt(1, id);
            Posizione p=null;
            ResultSet rs = ps.executeQuery();

            if(rs.next())
                p= PosizioneExtractor.extract(rs);

            return p;
        }
    }

    public boolean insert(Posizione p) throws SQLException{
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT into posizione (posizione_id, biblioteca, zona) VALUES (?, ?, ?)");
            ps.setInt(1,p.getId());
            ps.setString(2, p.getBiblioteca());
            ps.setString(3, p.getZona());

            if (ps.executeUpdate() != 1)
                throw new RuntimeException("INSERT error");

            return true;
        }
    }

    public boolean delete(int id) throws SQLException{
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Posizione p WHERE p.id=?");
            ps.setInt(1, id);

            if (ps.executeUpdate() != 1)
                throw new RuntimeException("DELETE error");

            return true;
        }
    }

}
