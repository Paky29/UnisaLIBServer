package model.postazionemanagement;

import utility.ConPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PostazioneDAO {
    public ArrayList<Postazione> doRetrieveByNotDisponibile() throws SQLException {
        ArrayList<Postazione> nonDisponibili = new ArrayList<>();
        try(Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT ps.postazione_id, ps.is_disponibile, ps.posizione_fk FROM postazione ps WHERE is_disponibile = 0");
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Postazione pst = PostazioneExtractor.extract(rs);
                nonDisponibili.add(pst);
            }
        }
        return nonDisponibili;
    }


public Postazione doRetrieveById(String id) throws SQLException{
    Postazione pst = null;
    try(Connection conn = ConPool.getConnection()){
        PreparedStatement ps = conn.prepareStatement("SELECT ps.is_disponibile, ps.posizione_fk FROM postazione ps WHERE postazione_id = ?");
        ps.setString(1,id);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            pst = PostazioneExtractor.extract(rs);
        }
    }
    return pst;
}

}
