package model.libromanagement;

import com.google.gson.Gson;
import model.libromanagement.Libro;
import model.libromanagement.LibroExtractor;
import model.utentemanagement.Utente;
import utility.ConPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {
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

    public Libro doRetrieveByTitolo(String titolo) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT l.isbn, l.titolo, l.autore, l.editore, l.n_copie, l.anno_pubblicazione, l.url_copertina, l.rating, l.categoria_fk, p.posizione_id, p.biblioteca, p.zona FROM libro l, posizione p WHERE p.posizione_id = l.posizione_fk AND l.titolo LIKE ?");
            ps.setString(1,"%"+titolo+"%");
            Libro l = null;
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                l = LibroExtractor.extract(rs);

            return l;
        }
    }


    public Libro doRetrieveByCategoria(String categoria) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT l.isbn, l.titolo, l.autore, l.editore, l.n_copie, l.anno_pubblicazione, l.url_copertina, l.rating, l.categoria_fk, p.posizione_id, p.biblioteca, p.zona FROM libro l, posizione p WHERE p.posizione_id = l.posizione_fk AND l.categoria_fk = ?");
            ps.setString(1,categoria);
            Libro l = null;
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                l = LibroExtractor.extract(rs);

            return l;
        }
    }


    public Libro doRetrieveByAutore(String autore) throws SQLException {
        try(Connection conn= ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT l.isbn, l.titolo, l.autore, l.editore, l.n_copie, l.anno_pubblicazione, l.url_copertina, l.rating, l.categoria_fk, p.posizione_id, p.biblioteca, p.zona FROM libro l, posizione p WHERE p.posizione_id = l.posizione_fk AND l.autore = ?");
            ps.setString(1,autore);
            Libro l = null;
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                l = LibroExtractor.extract(rs);

            return l;
        }
    }

    public ArrayList<String> doRetrieveAllCategorie() throws SQLException {
        try(Connection conn=ConPool.getConnection()){
            PreparedStatement ps=conn.prepareStatement("SELECT c.nome FROM Categoria c");
            ResultSet rs=ps.executeQuery();
            ArrayList<String> categorie=new ArrayList<>();
            while(rs.next())
                categorie.add(rs.getString("c.nome"));

            if(categorie.isEmpty())
                return null;

            return categorie;
        }
    }
}
