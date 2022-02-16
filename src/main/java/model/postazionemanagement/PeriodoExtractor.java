package model.postazionemanagement;

import utility.SwitchDate;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Questa classe si occupa di estrarre si occupa di estrarre dal ResultSet i valori riguardanti Periodo
 * e di creare e restituire un oggetto di tipo Periodo
 */
public class PeriodoExtractor {
    public static Periodo extract(ResultSet rs) throws SQLException {
        Periodo p = new Periodo();

        p.setId(rs.getInt("pe.periodo_id"));
        p.setData(SwitchDate.toGregorianCalendar(rs.getDate("pe.data_p")));
        p.setOraInizio(rs.getInt("pe.ora_inizio"));
        p.setOraFine(rs.getInt("pe.ora_fine"));

        return p;
    }
}
