package model.postazionemanagement;

import model.posizionemanagement.Posizione;
import model.posizionemanagement.PosizioneExtractor;
import utility.ConPool;
import utility.SwitchDate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;

public class PeriodoDao {

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

    public void insertBlocco(GregorianCalendar date, int oraInizio, int oraFine) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            //non ricordo se dovevano fare un controllo sulla presenza di periodi nel db
            for (int start = oraInizio; start<oraFine; start+=2) {
                PreparedStatement ps = conn.prepareStatement("INSERT into prenotazione (data_p, ora_inizio, ora_fine) " +
                        "VALUES ( ?, ?, ?)");
                ps.setDate(1, SwitchDate.toDate(date));
                ps.setInt(2, start);
                ps.setInt(3, (start+2));

                if (ps.executeUpdate() != 1)
                    throw new RuntimeException("INSERT error");
            }
        }
    }


}
