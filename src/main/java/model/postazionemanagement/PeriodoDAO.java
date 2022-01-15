package model.postazionemanagement;

import utility.ConPool;
import utility.SwitchDate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class PeriodoDAO {

    public Periodo doRetrieveById(int id) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT pe.periodo_id, pe.data_p, pe.ora_inizio, pe.ora_fine " +
                    "FROM periodo pe WHERE pe.periodo_id=?");
            ps.setInt(1, id);
            Periodo p = null;
            ResultSet rs = ps.executeQuery();

            if(rs.next())
                p = PeriodoExtractor.extract(rs);

            return p;
        }
    }

    public  Periodo doRetrieveByInfo(GregorianCalendar date, int oraInizio, int oraFine) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT pe.periodo_id, pe.data_p, pe.ora_inizio, pe.ora_fine " +
                    "FROM periodo pe WHERE pe.data_p=? AND pe.ora_inizio=? AND pe.ora_fine=?");
            System.out.println(oraInizio+oraFine);
            ps.setDate(1, SwitchDate.toDate(date));
            ps.setInt(2, oraInizio);
            ps.setInt(3, oraFine);

            Periodo p = null;
            ResultSet rs = ps.executeQuery();
            System.out.println("non trovato vabbe");
            if(rs.next())
                p = PeriodoExtractor.extract(rs);


            return p;
        }
    }

    public ArrayList<Periodo> doRetrieveByPostazione(String idPostazione) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT pe.periodo_id, pe.data_p, pe.ora_inizio, pe.ora_fine " +
                    "FROM periodo pe AND blocco b WHERE b.postazione_fk=?");
            ps.setString(1, idPostazione);
            ArrayList<Periodo> p = new ArrayList<>();
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                p.add(PeriodoExtractor.extract(rs));

            return p;
        }
    }

    public ArrayList<Periodo> doRetrieveAll() throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT pe.periodo_id, pe.data_p, pe.ora_inizio, pe.ora_fine " +
                    "FROM periodo pe");

            ArrayList<Periodo> p = new ArrayList<>();
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                p.add(PeriodoExtractor.extract(rs));

            return p;
        }
    }

    public Periodo doRetrieveByInfo(Periodo periodo) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT pe.periodo_id, pe.data_p, pe.ora_inizio, pe.ora_fine " +
                    "FROM periodo pe WHERE pe.data=? AND pe.ora_inizio=? AND pe.ora_fine=?");
            ps.setDate(1, SwitchDate.toDate(periodo.getData()));
            ps.setInt(1, periodo.getOraInizio());
            ps.setInt(1, periodo.getOraFine());

            Periodo p = null;
            ResultSet rs = ps.executeQuery();

            if(rs.next())
                p = PeriodoExtractor.extract(rs);

            return p;
        }
    }

    public boolean insertPeriodo(Periodo p) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps = conn.prepareStatement("INSERT INTO periodo(data_p,ora_inizio,ora_fine) VALUES(?,?,?)");
            ps.setDate(1,SwitchDate.toDate(p.getData()));
            ps.setInt(2,p.getOraInizio());
            ps.setInt(3,p.getOraFine());

            if(ps.executeUpdate()!=1)
                return true;
            return false;
        }
    }
}
