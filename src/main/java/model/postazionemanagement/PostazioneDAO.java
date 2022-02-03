package model.postazionemanagement;

import model.posizionemanagement.Posizione;
import utility.ConPool;
import utility.SwitchDate;

import javax.xml.transform.Result;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class PostazioneDAO {
    public boolean insert(Postazione p) throws SQLException{
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT into postazione (postazione_id, is_disponibile, posizione_fk) VALUES (?, ?, ?)");
            ps.setString(1, p.getId());
            ps.setBoolean(2, p.isDisponibile());
            ps.setInt(3,p.getPosizione().getId());

            return ps.executeUpdate() == 1;
        }
    }


    public Postazione doRetrieveById(String id) throws SQLException{
        GregorianCalendar dataCorrente =new GregorianCalendar();
        try(Connection conn = ConPool.getConnection()){
            PreparedStatement ps = conn.prepareStatement("SELECT ps.postazione_id, ps.is_disponibile, ps.posizione_fk, p.posizione_id, p.biblioteca, p.zona, pe.periodo_id, pe.data_p, pe.ora_inizio, pe.ora_fine " +
                    "FROM postazione ps INNER JOIN posizione p ON p.posizione_id=ps.posizione_fk LEFT JOIN blocco b ON b.postazione_fk=ps.postazione_id LEFT JOIN periodo pe ON b.periodo_fk=pe.periodo_id WHERE ps.postazione_id = ?");
            ps.setString(1,id);

            ResultSet rs = ps.executeQuery();
            Postazione pst = null;

            while(rs.next()) {
                if(pst==null)
                    pst = PostazioneExtractor.extract(rs);
                if(rs.getDate("pe.data_p")!=null) {
                    Periodo p=PeriodoExtractor.extract(rs);
                    if(SwitchDate.compareDate(p.getData(),dataCorrente)>=0)
                        pst.getBlocchi().add(p);
                }
            }
            return pst;
        }
    }

    public ArrayList<Postazione> doRetrieveByPosizione(String biblioteca, String zona) throws SQLException{
        GregorianCalendar dataCorrente =new GregorianCalendar();
        try(Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT ps.postazione_id, ps.is_disponibile, ps.posizione_fk, p.posizione_id, p.biblioteca, p.zona, pe.periodo_id,pe.data_p, pe.ora_inizio, pe.ora_fine " +
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
                if(rs.getDate("pe.data_p")!=null) {
                    Periodo p=PeriodoExtractor.extract(rs);
                    if(SwitchDate.compareDate(p.getData(),dataCorrente)>=0)
                        pos.get(codicepos).getBlocchi().add(p);
                }
            }
            return new ArrayList<>(pos.values());
        }
    }

    public ArrayList<Postazione> doRetrieveDisponibiliByPosizione(String biblioteca, String zona) throws SQLException{
        Date dataCorrente = SwitchDate.toDate(new GregorianCalendar());
        try(Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT ps.postazione_id, ps.is_disponibile, ps.posizione_fk, p.posizione_id, p.biblioteca, p.zona, pe.periodo_id, pe.data_p, pe.ora_inizio, pe.ora_fine " +
                    "FROM postazione ps INNER JOIN posizione p ON p.posizione_id=ps.posizione_fk LEFT JOIN blocco b ON b.postazione_fk=ps.postazione_id LEFT JOIN periodo pe ON b.periodo_fk=pe.periodo_id WHERE p.biblioteca=? AND p.zona=? AND ps.is_disponibile=true AND (pe.data_p>=? OR pe.data_p is null)");
            ps.setString(1, biblioteca);
            ps.setString(2, zona);
            ps.setDate(3,dataCorrente);

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

    public int isDisponibile(String idPos) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            boolean isBlock;
            PreparedStatement ps = conn.prepareStatement("SELECT postazione.is_disponibile as val FROM postazione WHERE postazione.postazione_id = ?");
            ps.setString(1, idPos);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                isBlock = rs.getBoolean("val");
                return isBlock ?1:0;
            }
            return -1;
        }
    }

    public boolean bloccaPostazione(String idPos){
        Date dataCorrente = SwitchDate.toDate(new GregorianCalendar());
        try (Connection conn = ConPool.getConnection()) {
            int counter=0;
            boolean isBlock = true;
            conn.setAutoCommit(false);
            PreparedStatement ps=conn.prepareStatement("SELECT postazione.is_disponibile as val FROM postazione WHERE postazione.postazione_id = ?");
            ps.setString(1,idPos);
            ResultSet rs = ps.executeQuery();
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
                return false;
            }

            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public String bloccoDeterminato(Periodo per, Postazione pos) throws SQLException {
        PeriodoDAO periodoDAO=new PeriodoDAO();
        String resp=null;
        ResultSet rs;
        PreparedStatement ps;
        try(Connection conn= ConPool.getConnection()){
            conn.setAutoCommit(false);
            for (int start = per.getOraInizio(); start < per.getOraFine(); start+=2) {
                if(start==13)
                    ++start;
                int oraFinePeriodo=start+2;
                Periodo periodo=periodoDAO.doRetrieveByInfo(per.getData(),start,oraFinePeriodo);
                if(periodo==null){
                    periodoDAO.insertPeriodo (new Periodo(start,oraFinePeriodo,per.getData()));
                    periodo=periodoDAO.doRetrieveByInfo(per.getData(),start,oraFinePeriodo);
                }
                if(!pos.getBlocchi().contains(periodo)) {
                    ps = conn.prepareStatement("INSERT INTO blocco (postazione_fk, periodo_fk) VALUES (?, ?)");
                    ps.setString(1, pos.getId());
                    ps.setInt(2, periodo.getId());

                    if (ps.executeUpdate() != 1) {
                        conn.rollback();
                        conn.setAutoCommit(true);
                        return "Errore inserimento blocchi";
                    }
                    ps = conn.prepareStatement("SELECT * FROM prenotazione p WHERE p.data_p=? AND p.ora_inizio=? AND p.postazione_fk = ?");
                    ps.setDate(1, SwitchDate.toDate(per.getData()));
                    ps.setInt(2,start);
                    ps.setString(3,pos.getId());
                    rs = ps.executeQuery();
                    if(rs.next()) {
                        ps = conn.prepareStatement("DELETE FROM prenotazione p WHERE p.data_p=? AND p.ora_inizio=? AND p.postazione_fk = ?");
                        ps.setDate(1, SwitchDate.toDate(per.getData()));
                        ps.setInt(2, start);
                        ps.setString(3, pos.getId());

                        if (ps.executeUpdate() != 1) {
                            conn.rollback();
                            conn.setAutoCommit(true);
                            return "Errore cancellazione prenotazioni";
                        }
                    }
                }
                else{
                    if(resp==null) {
                        resp = "Blocchi gia' presenti: ";
                        resp += start + "-" + oraFinePeriodo;
                    }
                    else
                        resp += "; "+start + "-" + oraFinePeriodo;
                }
            }
            conn.commit();
            conn.setAutoCommit(true);
            if(resp==null)
                return "Blocchi inseriti correttamente";
            else
                 return resp;
        }
    }

    public boolean sbloccaPostazione(String idPos) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            boolean isBlock = true;
            ResultSet rs;
            conn.setAutoCommit(false);
            PreparedStatement ps =conn.prepareStatement("SELECT postazione.is_disponibile as val FROM postazione WHERE postazione.postazione_id = ?");
            ps.setString(1,idPos);
            rs = ps.executeQuery();
            if(rs.next()){
                isBlock = rs.getBoolean("val");
                if(isBlock) {
                    conn.setAutoCommit(true);
                    return false;
                }
            }

            ps = conn.prepareStatement("UPDATE postazione pos  SET pos.is_disponibile = true WHERE pos.postazione_id = ?");
            ps.setString(1, idPos);
            if (ps.executeUpdate() != 1) {
                conn.setAutoCommit(true);
                return false;
            }

            conn.commit();
            conn.setAutoCommit(true);
            return true;
        }
    }

    public boolean sbloccaPostazione(String idPos,Periodo p) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM blocco b WHERE b.postazione_fk=? AND periodo_fk=?");
            ps.setString(1,idPos);
            ps.setInt(2,p.getId());
            return ps.executeUpdate() == 1;
        }
    }
}
