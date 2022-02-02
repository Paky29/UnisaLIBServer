package presenter.libropresenter;

import model.libromanagement.Libro;
import model.posizionemanagement.Posizione;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LibroValidatorTest {

    @Test
    public void LibroValido(){
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(1980).autore("alessandro manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").titolo("Promessi Sposi").isbn("978190445X").nCopie(5).build();
        assertTrue(LibroValidator.validate(l));
    }

    @Test
    public void LibroISBNVuoto(){
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(1980).autore("alessandro manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").titolo("Promessi Sposi").isbn("").nCopie(5).build();
        System.out.println(l.getIsbn());
        assertFalse(LibroValidator.validate(l));
    }

    @Test
    public void LibroISBNNullo(){
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(1980).autore("alessandro manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").titolo("Promessi Sposi").isbn(null).nCopie(5).build();
        assertFalse(LibroValidator.validate(l));
    }

    @Test
    public void LibroISBNLunghezzaEcceduta(){
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(1980).autore("alessandro manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").titolo("Promessi Sposi").isbn("97819045444X").nCopie(5).build();
        assertFalse(LibroValidator.validate(l));
    }

    @Test
    public void LibroAnnoSuperioreAnnoCorrente(){
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(2033).autore("alessandro manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").titolo("Promessi Sposi").isbn("978190445X").nCopie(5).build();
        assertFalse(LibroValidator.validate(l));
    }

    @Test
    public void LibroAnnoFormatoIncorretto(){
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(500).autore("alessandro manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").titolo("Promessi Sposi").isbn("978190445X").nCopie(5).build();
        assertFalse(LibroValidator.validate(l));
    }

    @Test
    public void LibroTitoloVuoto(){
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(1980).autore("alessandro manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").titolo("").isbn("978190445X").nCopie(5).build();
        assertFalse(LibroValidator.validate(l));
    }

    @Test
    public void LibroTitoloNull(){
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(1980).autore("alessandro manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").titolo(null).isbn("978190445X").nCopie(5).build();
        assertFalse(LibroValidator.validate(l));
    }

    @Test
    public void LibroTitoloSuperioreLimite(){
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(1980).autore("alessandro manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").titolo("Artificial intelligence (AI) is intelligence demonstrated by machines, as opposed to natural intelligence displayed by animals including humans. Leading AI textbooks define the field as the study of \"intelligent agents\": any system that perceives its environment and takes actions that maximize its chance of achieving its goals.[a] Some popular accounts use the term \"artificial intelligence\" to describe machines that mimic \"cognitive\" functions that humans associate with the human mind, such as \"learning\" and \"problem solving\", however, this definition is rejected by major AI researchers.").isbn("978190445X").nCopie(5).build();
        assertFalse(LibroValidator.validate(l));
    }

    @Test
    public void LibroAutorePatternIncorretto(){
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(1980).autore("alessandro1 manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").titolo("Promessi Sposi").isbn("978190445Z").nCopie(5).build();
        assertFalse(LibroValidator.validate(l));
    }

    @Test
    public void LibroAutoreNull(){
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(1980).autore(null).categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").titolo("Promessi Sposi").isbn("978190445Z").nCopie(5).build();
        assertFalse(LibroValidator.validate(l));
    }

    @Test
    public void LibroAutoreVuoto(){
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(1980).autore("Artificial intelligence (AI) is intelligence demonstrated by machines, as opposed to natural intelligence displayed by animals including humans. Leading AI textbooks define the field as the study of \"intelligent agents\": any system that perceives its environment and takes actions that maximize its chance of achieving its goals.[a] Some popular accounts use the term \"artificial intelligence\" to describe machines that mimic \"cognitive\" functions that humans associate with the human mind, such as \"learning\" and \"problem solving\", however, this definition is rejected by major AI researchers.").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").titolo("Promessi Sposi").isbn("978190445X").nCopie(5).build();
        assertFalse(LibroValidator.validate(l));
    }

    @Test
    public void LibroAutoreSuperioreLimite(){
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(1980).autore("").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").titolo("Promessi Sposi").isbn("978190445Z").nCopie(5).build();
        assertFalse(LibroValidator.validate(l));
    }

    @Test
    public void LibroEditoreIncorretto(){
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(1980).autore("alessandro manzoni").categoria("lettere").editore("").posizione(p).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").titolo("Promessi Sposi").isbn("978190445Z").nCopie(5).build();
        assertFalse(LibroValidator.validate(l));
    }

    @Test
    public void LibroNCopieIncorretto(){
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(1980).autore("alessandro manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg").titolo("Promessi Sposi").isbn("978190445X").nCopie(-1).build();
        assertFalse(LibroValidator.validate(l));
    }

    @Test
    public void LibroUrlIncorretto(){
        Posizione p=new Posizione(1,"umanistica","piano 1");
        Libro l= new Libro.LibroBuilder().annoPubbl(1980).autore("alessandro manzoni").categoria("lettere").editore("Mondadori").posizione(p).urlCopertina("copertina.jpg").titolo("Promessi Sposi").isbn("978190445X").nCopie(5).build();
        assertFalse(LibroValidator.validate(l));
    }

}
