package model.posizionemanagement;

import utility.ConPool;

import java.sql.*;
import java.util.ArrayList;

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

    public ArrayList<Posizione> doRetrieveByBiblioteca(String biblioteca) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT p.posizione_id, p.biblioteca, p.zona FROM posizione p WHERE p.biblioteca=?");
            ps.setString(1, biblioteca);
            ArrayList<Posizione> posizioni = new ArrayList<>();
            ResultSet rs = ps.executeQuery();

            while(rs.next())
                posizioni.add(PosizioneExtractor.extract(rs));

            return posizioni;
        }
    }

    public boolean insert(Posizione p) throws SQLException{
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT into posizione (biblioteca, zona) VALUES (?, ?)");
            ps.setString(1, p.getBiblioteca());
            ps.setString(2, p.getZona());

            if (ps.executeUpdate() != 1)
                throw new RuntimeException("INSERT error");

            return true;
        }
    }

    public boolean delete(int id) throws SQLException{
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Posizione p WHERE p.posizione_id=?");
            ps.setInt(1, id);

            if (ps.executeUpdate() != 1)
                throw new RuntimeException("DELETE error");

            return true;
        }
    }

}
