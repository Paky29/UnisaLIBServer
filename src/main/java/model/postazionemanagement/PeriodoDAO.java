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
            PreparedStatement ps=conn.prepareStatement("SELECT p.periodo_id, p.data_p, p.ora_inizio, p.ora_fine " +
                    "FROM periodo p WHERE p.periodo_id=?");
            ps.setInt(1, id);
            Periodo p = null;
            ResultSet rs = ps.executeQuery();

            if(rs.next())
                p = PeriodoExtractor.extract(rs);

            return p;
        }
    }

    private static Periodo doRetrieveByInfo(GregorianCalendar date, int oraInizio, int oraFine) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT p.periodo_id, p.data_p, p.ora_inizio, p.ora_fine " +
                    "FROM periodo p WHERE p.data=? AND p.ora_inizio=? AND p.ora_fine=?");
            ps.setDate(1, SwitchDate.toDate(date));
            ps.setInt(1, oraInizio);
            ps.setInt(1, oraFine);

            Periodo p = null;
            ResultSet rs = ps.executeQuery();

            if(rs.next())
                p = PeriodoExtractor.extract(rs);

            return p;
        }
    }

    public ArrayList<Periodo> doRetrieveByPostazione(String idPostazione) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT p.periodo_id, p.data_p, p.ora_inizio, p.ora_fine " +
                    "FROM periodo p AND blocco b WHERE b.postazione_fk=?");
            ps.setString(1, idPostazione);
            ArrayList<Periodo> p = new ArrayList<>();
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                p.add(PeriodoExtractor.extract(rs));

            if (p.isEmpty())
                return null;

            return p;
        }
    }

    public ArrayList<Periodo> doRetrieveAll() throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT p.periodo_id, p.data_p, p.ora_inizio, p.ora_fine " +
                    "FROM periodo p");

            ArrayList<Periodo> p = new ArrayList<>();
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                p.add(PeriodoExtractor.extract(rs));

            if (p.isEmpty())
                return null;

            return p;
        }
    }

    public void insertBlocco(GregorianCalendar date, int oraInizio, int oraFine, Postazione p) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            for (int start = oraInizio; start < oraFine; start+=2) {
                PreparedStatement ps = conn.prepareStatement("INSERT into blocco (postazione_fk, periodo_fk) VALUES ( ?, ?)");
                ps.setString(1, p.getId());
                ps.setInt(2, PeriodoDAO.doRetrieveByInfo(date, start, (start+2)).getId());

                if (ps.executeUpdate() != 1)
                    throw new RuntimeException("INSERT error");
            }
        }
    }

    // da inserire in PostazioneDAO
    public void insertBloccoIndeterminato(Postazione p) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps = conn.prepareStatement("UPDATE postazione p SET is_disponibile=? WHERE p.postazione_id=?");
            ps.setBoolean(1, p.isDisponibile());
            ps.setString(2, p.getId());

            if (ps.executeUpdate() != 1)
                throw new RuntimeException("INSERT error");
        }
    }


}
