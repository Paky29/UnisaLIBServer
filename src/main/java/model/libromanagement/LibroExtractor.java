package model.libromanagement;

import model.posizionemanagement.Posizione;
import model.posizionemanagement.PosizioneExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LibroExtractor {

    public static Libro extract(ResultSet rs) throws SQLException {

       Posizione p = PosizioneExtractor.extract(rs);

        Libro l= new Libro.LibroBuilder().
                isbn(rs.getString("l.isbn")).
                titolo(rs.getString("l.titolo")).
                autore(rs.getString("l.autore")).
                editore(rs.getString("l.editore")).
                nCopie(rs.getInt("l.n_copie")).
                annoPubbl(rs.getInt("l.anno_pubblicazione")).
                urlCopertina(rs.getString("l.url_copertina")).
                rating(rs.getFloat("l.rating")).
                categoria(rs.getString("l.categoria_fk")).
                posizione(p).
                build();
        return l;
    }
}
