package model.posizionemanagement;

import utility.ConPool;

import java.sql.*;
import java.util.ArrayList;

public class PosizioneDAO {
    /**
     * Recupera tutte le posizione dalla base di dati dato Biblioteca e zona
     * @param biblioteca biblioteca della posizione
     * @param zona zona della posizione
     * @return posizione
     */
    public Posizione doRetrieveByBibliotecaZona(String biblioteca, String zona) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT p.posizione_id, p.biblioteca, p.zona FROM posizione p WHERE p.biblioteca=? AND p.zona=?");
            ps.setString(1, biblioteca);
            ps.setString(2, zona);
            Posizione p = null;
            ResultSet rs = ps.executeQuery();

            if(rs.next())
                p = PosizioneExtractor.extract(rs);

            return p;
        }
    }
    /**
     * Inserisce una posizione all'interno della base di dati
     * @param p la posizione da inserire
     * @return boolean rappresentante l'esito
     */
    public boolean insert(Posizione p) throws SQLException{
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT into posizione (biblioteca, zona) VALUES (?, ?)");
            ps.setString(1, p.getBiblioteca());
            ps.setString(2, p.getZona());

            if (ps.executeUpdate() != 1)
                return false;

            return true;
        }
    }
    /**
     * ELimina una posizione dalla base di dati dato biblioteca e zona
     * @param biblioteca biblioteca della posizione
     * @param zona zona della posizione
     * @return boolean rappresentante l'esito
     */
    public boolean delete(String biblioteca, String zona) throws SQLException{
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Posizione p WHERE p.biblioteca=? AND p.zona=?");
            ps.setString(1, biblioteca);
            ps.setString(2, zona);

            if (ps.executeUpdate() != 1)
                throw new RuntimeException("DELETE error");

            return true;
        }
    }

    /**
     * Recupera tutte le posizione dalla base di dati
     * @return lista di posizioni
     */

    public ArrayList<Posizione> doRetrieveAll() throws SQLException {
        try(Connection conn= ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT p.posizione_id, p.biblioteca, p.zona FROM posizione p");

            ArrayList<Posizione> posizioni = new ArrayList<>();
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                posizioni.add(PosizioneExtractor.extract(rs));

            return posizioni;
        }
    }
}
