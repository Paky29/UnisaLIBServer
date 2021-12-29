package model.prestitomanagement;

import model.libromanagement.LibroDAO;
import model.utentemanagement.UtenteDAO;
import utility.SwitchDate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrestitoExtractor {
    public static Prestito extract(ResultSet rs) throws SQLException {
        Prestito p=new Prestito.PrestitoBuilder().
                dataInizio(SwitchDate.toGregorianCalendar(rs.getDate("p.data_inizio"))).
                dataFine(SwitchDate.toGregorianCalendar(rs.getDate("p.data_fine"))).
                attivo(rs.getBoolean("p.is_attivo")).
                dataConsegna(SwitchDate.toGregorianCalendar(rs.getDate("p.data_consegna"))).
                libro(new LibroDAO().doRetrieveByCodiceISBN("p.libro_fk")).
                utente(new UtenteDAO().doRetrieveByEmail("p.utente_fk")).
                voto(rs.getInt("p.voto")).
                commento(rs.getString("p.commento")).
                build();

        return p;
    }
}