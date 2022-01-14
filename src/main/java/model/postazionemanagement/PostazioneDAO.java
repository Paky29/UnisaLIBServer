package model.postazionemanagement;

import model.posizionemanagement.Posizione;
import utility.ConPool;
import utility.SwitchDate;

import javax.xml.transform.Result;
import java.sql.*;
import java.sql.Date;
import java.util.*;

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

    public ArrayList<Postazione> doRetrieveDisponibiliByPosizione(String biblioteca, String zona) throws SQLException{
        try(Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT ps.postazione_id, ps.is_disponibile, ps.posizione_fk, p.posizione_id, p.biblioteca, p.zona, pe.data_p, pe.ora_inizio, pe.ora_fine " +
                    "FROM postazione ps INNER JOIN posizione p ON p.posizione_id=ps.posizione_fk LEFT JOIN blocco b ON b.postazione_fk=ps.postazione_id LEFT JOIN periodo pe ON b.periodo_fk=pe.periodo_id WHERE p.biblioteca=? AND p.zona=? AND ps.is_disponibile=true");
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

    public boolean bloccaPostazione(String idPos){
        Date dataCorrente = SwitchDate.toDate(new GregorianCalendar());

        try (Connection conn = ConPool.getConnection()) {
            int counter=0;
            boolean isBlock = true;
            ResultSet rs;
            conn.setAutoCommit(false);
            PreparedStatement ps;
            ps =conn.prepareStatement("SELECT postazione.is_disponibile as val FROM postazione WHERE postazione.postazione_id = ?");
            ps.setString(1,idPos);
            rs = ps.executeQuery();
            if(rs.next()){
                isBlock = rs.getBoolean("val");
                if(!isBlock) {
                    conn.setAutoCommit(true);
                    return false;
                }
            }

                ps = conn.prepareStatement("UPDATE postazione pos  SET pos.is_disponibile = false WHERE pos.postazione_id = ?");
                ps.setString(1, idPos);
                if (ps.executeUpdate() != 1) {
                    conn.setAutoCommit(true);
                    System.out.println("fallita la prima query");
                    return false;
                }
            ps = conn.prepareStatement("SELECT COUNT(*) as pren FROM prenotazione p WHERE p.data_p>=? AND p.ora_inizio>? AND p.postazione_fk = ?");
            ps.setDate(1, dataCorrente);
            ps.setInt(2,Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
            System.out.print(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
            ps.setString(3,idPos);
            rs = ps.executeQuery();
            if(rs.next())
                counter = rs.getInt("pren");

                ps = conn.prepareStatement("DELETE FROM prenotazione p WHERE p.data_p>=? AND p.ora_inizio>? AND p.postazione_fk = ?");
                ps.setDate(1, dataCorrente);
                ps.setInt(2, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                ps.setString(3,idPos);

            if (ps.executeUpdate() != counter) {
                conn.rollback();
                conn.setAutoCommit(true);
                System.out.println("fallita la seconda query");
                return false;
            }

            conn.commit();
            conn.setAutoCommit(true);
            System.out.println("torno true");

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
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
