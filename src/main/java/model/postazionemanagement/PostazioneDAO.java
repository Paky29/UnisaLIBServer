package model.postazionemanagement;

import model.posizionemanagement.Posizione;
import utility.ConPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PostazioneDAO {

    public ArrayList<Postazione> doRetrieveByNotDisponibile() throws SQLException {
        try(Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT ps.postazione_id, ps.is_disponibile, ps.posizione_fk, p.posizione_id, p.biblioteca, p.zona " +
                    "FROM postazione ps AND posizione p WHERE p.posizione_id=ps.posizione_fk AND is_disponibile = 0");

            ArrayList<Postazione> nonDisponibili = new ArrayList<>();
            ResultSet rs = ps.executeQuery();

            while(rs.next())
                nonDisponibili.add(PostazioneExtractor.extract(rs));

            return nonDisponibili;
        }
    }


    public Postazione doRetrieveById(String id) throws SQLException{

        try(Connection conn = ConPool.getConnection()){
            PreparedStatement ps = conn.prepareStatement("SELECT ps.postazione_id, ps.is_disponibile, ps.posizione_fk , p.posizione_id, p.biblioteca, p.zona " +
                    "FROM postazione ps AND posizione p WHERE p.posizione_id=ps.posizione_fk AND ps.postazione_id = ?");
            ps.setString(1,id);

            ResultSet rs = ps.executeQuery();
            Postazione pst = null;

            if(rs.next())
                pst = PostazioneExtractor.extract(rs);
            return pst;
        }
    }

    public Postazione doRetrieveByPosizione(String biblioteca, String zona) throws SQLException{
        try(Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT ps.postazione_id, ps.is_disponibile, ps.posizione_fk, p.posizione_id, p.biblioteca, p.zona " +
                    "FROM postazione ps AND posizione p WHERE p.posizione_id=ps.posizione_fk AND p.biblioteca=? AND p.zona=?");
            ps.setString(1, biblioteca);
            ps.setString(2, zona);
            ResultSet rs = ps.executeQuery();

            Postazione p=null;

            if(rs.next())
                p = PostazioneExtractor.extract(rs);

            return p;
        }
    }

}
