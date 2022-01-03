package model.postazionemanagement;

import model.posizionemanagement.Posizione;
import utility.ConPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class PostazioneDAO {

    public ArrayList<Postazione> doRetrieveByNotDisponibile() throws SQLException {
        try(Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT ps.postazione_id, ps.is_disponibile, ps.posizione_fk, p.posizione_id, p.biblioteca, p.zona " +
                    "FROM postazione ps, posizione p WHERE p.posizione_id=ps.posizione_fk AND is_disponibile = 0");

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
                    "FROM postazione ps, posizione p WHERE p.posizione_id=ps.posizione_fk AND ps.postazione_id = ?");
            ps.setString(1,id);

            ResultSet rs = ps.executeQuery();
            Postazione pst = null;

            if(rs.next())
                pst = PostazioneExtractor.extract(rs);
            return pst;
        }
    }

    public ArrayList<Postazione> doRetrieveByPosizione(String biblioteca, String zona) throws SQLException{
        try(Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT ps.postazione_id, ps.is_disponibile, ps.posizione_fk, p.posizione_id, p.biblioteca, p.zona, pe.data_p, pe.ora_inizio, pe.ora_fine " +
                    "FROM postazione ps INNER JOIN posizione p ON p.posizione_id=ps.posizione_fk LEFT JOIN blocco b ON b.postazione_fk=ps.postazione_id LEFT JOIN periodo pe ON b.periodo_fk=pe.periodo_id WHERE p.biblioteca=? AND p.zona=?");
            ps.setString(1, biblioteca);
            ps.setString(2, zona);

            Map<String,Postazione> pos=new LinkedHashMap<>();
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String codicepos=rs.getString("ps.postazione_id");
                if(!pos.containsKey(codicepos)){
                    pos.put(codicepos,PostazioneExtractor.extract(rs));
                }
                if(rs.getDate("pe.data_p")!=null)
                    pos.get(codicepos).getBlocchi().add(PeriodoExtractor.extract(rs));
            }
            return new ArrayList<>(pos.values());
        }
    }

    /*
    public ArrayList<Postazione> doRetrieveByPosizione(Posizione p) throws SQLException{
        try(Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT ps.postazione_id, ps.is_disponibile, ps.posizione_fk " +
                    "FROM postazione ps, posizione p WHERE p.posizione_id=ps.posizione_fk AND p.biblioteca=? AND p.zona=?");
            ps.setString(1, p.getBiblioteca());
            ps.setString(2, p.getZona());

            ArrayList<Postazione> postazioni=new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                postazioni.add(PostazioneExtractor.extract(rs));

            return postazioni;
        }
    }
     */

}
