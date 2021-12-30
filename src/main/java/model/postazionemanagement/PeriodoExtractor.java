package model.postazionemanagement;

import utility.SwitchDate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PeriodoExtractor {
    public static Periodo extract(ResultSet rs) throws SQLException {
        Periodo p = new Periodo();

        p.setId(rs.getInt("p.periodo_id"));
        p.setData(SwitchDate.toGregorianCalendar(rs.getDate("p.data_p")));
        p.setOraInizio(rs.getInt("p.ora_inizio"));
        p.setOraFine(rs.getInt("p.ora_fine"));

        return p;
    }
}
