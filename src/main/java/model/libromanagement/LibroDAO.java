package model.libromanagement;
import utility.ConPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Questa classe si occupa di gestire le varie interazioni tra la classe Libro e la base di dati.
 * Sono implementati i metodi principali relativi alle operazioni CRUD
 */
public class LibroDAO {
    /**
     * Controlla se una categoria esiste nella base dei dati
     * @param categoria il nome della categoria
     * @return l'esito della richiesta
     */
    public boolean existCategoria(String categoria)throws SQLException{
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT count(*) as esiste FROM categoria c WHERE c.nome=?");
            ps.setString(1, categoria);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("esiste") > 0;
            }
            return false;
        }
    }
    /**
     * Inserisce un libro all'interno della base di dati
     * @param libro il libro da inserire
     * @return l'esito della transazione
     */
    public boolean insert(Libro libro) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT into libro (isbn, titolo, autore, editore, n_copie, anno_pubblicazione, url_copertina, categoria_fk, posizione_fk) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, libro.getIsbn());
            ps.setString(2, libro.getTitolo());
            ps.setString(3, libro.getAutore());
            ps.setString(4, libro.getEditore());
            ps.setInt(5, libro.getnCopie());
            ps.setInt(6, libro.getAnnoPubbl());
            ps.setString(7, libro.getUrlCopertina());
            ps.setString(8, libro.getCategoria());
            ps.setInt(9, libro.getPosizione().getId());

            return ps.executeUpdate() == 1;
        }
    }
    /**
     * Recupera tutti i libri dalla base di dati dato un isbn
     * @param isbn l'identificativo del libro in base al quale filtrare la base dati
     * @return libro
     */
    public Libro doRetrieveByCodiceISBN(String isbn) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT l.isbn, l.titolo, l.autore, l.editore, l.n_copie, l.anno_pubblicazione, l.url_copertina, l.rating, l.categoria_fk, p.posizione_id, p.biblioteca, p.zona FROM libro l, posizione p WHERE p.posizione_id = l.posizione_fk AND l.isbn=?");
            ps.setString(1,isbn);
            Libro l = null;
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                l = LibroExtractor.extract(rs);

            return l;
        }
    }
    /**
     * Recupera tutti i libri dalla base di dati ricercando per titolo o autore
     * @param ricerca stringa estratta contenente titolo o autore
     * @return la lista di Libri
     */
    public ArrayList<Libro> doRetrieveByTitoloAutore(String ricerca) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT l.isbn, l.titolo, l.autore, l.editore, l.n_copie, l.anno_pubblicazione, l.url_copertina, l.rating, l.categoria_fk, p.posizione_id, p.biblioteca, p.zona " +
                    "FROM libro l, posizione p WHERE p.posizione_id = l.posizione_fk AND (l.titolo LIKE ? OR l.autore LIKE ?)");
            ps.setString(1,"%"+ricerca+"%");
            ps.setString(2,"%"+ricerca+"%");
            ResultSet rs = ps.executeQuery();
            ArrayList<Libro> libri=new ArrayList<>();
            while(rs.next())
                libri.add(LibroExtractor.extract(rs));

            return libri;
        }
    }

    /**
     * Recupera tutti i libri dalla base di dati ricercando per categoria
     * @param categoria stringa contenente la categoria dei libri ricercati
     * @return la lista di Libri
     */
    public ArrayList<Libro> doRetrieveByCategoria(String categoria) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT l.isbn, l.titolo, l.autore, l.editore, l.n_copie, l.anno_pubblicazione, l.url_copertina, l.rating, l.categoria_fk, p.posizione_id, p.biblioteca, p.zona " +
                    "FROM libro l, posizione p WHERE p.posizione_id = l.posizione_fk AND l.categoria_fk = ?");
            ps.setString(1,categoria);
            ResultSet rs = ps.executeQuery();
            ArrayList<Libro> libri=new ArrayList<>();
            while(rs.next())
                libri.add(LibroExtractor.extract(rs));

            return libri;
        }
    }
    /**
     * Recupera tutti i libri dalla base di dati aggiungi alla lista degli interessi di un utente
     * @param email identificativo dell'utente di cui si vuole visualizzare gli interessi
     * @return la lista di Libri
     */
    public ArrayList<Libro> doRetrieveInteresse(String email) throws SQLException {
        try (Connection conn = ConPool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT l.isbn, l.titolo, l.autore, l.editore, l.n_copie, l.anno_pubblicazione, l.url_copertina, l.rating, l.categoria_fk, p.posizione_id, p.biblioteca, p.zona " +
                    "FROM utente u, interesse i, libro l, posizione p WHERE u.email=i.utente_fk AND l.isbn=i.libro_fk AND p.posizione_id = l.posizione_fk AND u.email=?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            ArrayList<Libro> libri=new ArrayList<>();
            while(rs.next())
                libri.add(LibroExtractor.extract(rs));

            return libri;
        }
    }
    /**
     * Recupera tutte le categorie dalla base di dati
     * @return lista di categorie
     */
    public ArrayList<String> doRetrieveAllCategorie() throws SQLException {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT c.nome FROM Categoria c");
            ResultSet rs=ps.executeQuery();
            ArrayList<String> categorie=new ArrayList<>();
            while(rs.next())
                categorie.add(rs.getString("c.nome"));

            return categorie;
        }
    }
    /**
     * Elimina il libro dalla lista di interessi dell'utente
     * @param isbn identificativo del libro da voler eliminare
     * @param email identificativo dell'utente che vuole eliminare un libro
     * @return l'esito della transazione
     */
    public boolean doDeleteInteresse(String email, String isbn) throws SQLException {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("DELETE FROM Interesse i WHERE i.utente_fk=? AND i.libro_fk=?");
            ps.setString(1, email);
            ps.setString(2, isbn);

            return ps.executeUpdate() == 1;
        }
    }

    /**
     * Aggiuge il libro dalla lista di interessi dell'utente
     * @param isbn identificativo del libro da voler aggiungere
     * @param email email dell'utente che vuole aggiungere un libro
     * @return l'esito della transazione
     */
    public boolean doAddInteresse(String email, String isbn) throws SQLException {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("INSERT INTO Interesse VALUES(?,?)");
            ps.setString(1, email);
            ps.setString(2, isbn);

            return ps.executeUpdate() == 1;
        }
    }

}
