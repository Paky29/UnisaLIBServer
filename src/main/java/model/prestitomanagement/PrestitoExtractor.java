package model.prestitomanagement;

import model.libromanagement.LibroDAO;
import model.utentemanagement.UtenteDAO;
import utility.SwitchDate;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Questa classe si occupa di estrarre si occupa di estrarre dal ResultSet i valori riguardanti Prestito
 * e di creare e restituire un oggetto di tipo Prestito
 */
public class PrestitoExtractor {
    public static Prestito extract(ResultSet rs) throws SQLException {
        Prestito p;
        if(rs.getDate("p.data_consegna")!=null) {
           p = new Prestito.PrestitoBuilder().
                    dataInizio(SwitchDate.toGregorianCalendar(rs.getDate("p.data_inizio"))).
                    dataFine(SwitchDate.toGregorianCalendar(rs.getDate("p.data_fine"))).
                    attivo(rs.getBoolean("p.is_attivo")).
                    dataConsegna(SwitchDate.toGregorianCalendar(rs.getDate("p.data_consegna"))).
                    libro(new LibroDAO().doRetrieveByCodiceISBN(rs.getString("p.libro_fk"))).
                    utente(new UtenteDAO().doRetrieveByEmail(rs.getString("p.utente_fk"))).
                    voto(rs.getInt("p.voto")).
                    commento(rs.getString("p.commento")).
                    build();
        }
        else{
            p = new Prestito.PrestitoBuilder().
                    dataInizio(SwitchDate.toGregorianCalendar(rs.getDate("p.data_inizio"))).
                    dataFine(SwitchDate.toGregorianCalendar(rs.getDate("p.data_fine"))).
                    attivo(rs.getBoolean("p.is_attivo")).
                    libro(new LibroDAO().doRetrieveByCodiceISBN(rs.getString("p.libro_fk"))).
                    utente(new UtenteDAO().doRetrieveByEmail(rs.getString("p.utente_fk"))).
                    voto(rs.getInt("p.voto")).
                    commento(rs.getString("p.commento")).
                    build();
        }

        return p;
    }
}