/*package model.prestitomanagement;

import utility.SwitchDate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;

public class PrestitoExtractor {
    public static Prestito extract(ResultSet rs) throws SQLException {
        SwitchDate sw = new SwitchDate();

        GregorianCalendar dInizio =  sw.toGregorianCalendar(rs.getDate("p.data_inizio"));
        GregorianCalendar dFine = sw.toGregorianCalendar(rs.getDate("p.data_fine"));
        GregorianCalendar dConsegna = sw.toGregorianCalendar(rs.getDate("p.data_consegna"));
        String libro = rs.getString("p.libro_fk");
        String utente = rs.getString("p.utente_fk");
        int voto = rs.getInt("p.voto");
        boolean attivo = rs.getBoolean("p.is_attivo");

        Prestito p = new Prestito(dInizio, libro, utente, dFine, dConsegna, voto, attivo);

        return p;
    }
}
*/