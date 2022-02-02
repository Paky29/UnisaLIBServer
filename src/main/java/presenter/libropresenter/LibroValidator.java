package presenter.libropresenter;

import model.libromanagement.Libro;

import java.util.Calendar;
import java.util.regex.Pattern;

public class LibroValidator {
    public static boolean validate(Libro l){
        if((l.getIsbn()==null || l.getIsbn().length()!=10) ||(!Pattern.matches("[0-9]{9}[0-9|X]{1}",l.getIsbn())))
            return false;
        if(l.getAnnoPubbl()> Calendar.getInstance().get(Calendar.YEAR) || (!Pattern.matches("^[12][0-9]{3}$",Integer.toString(l.getAnnoPubbl()))))
            return false;
        if(l.getTitolo()==null || (l.getTitolo().length()==0 || l.getTitolo().length()>50))
            return false;
        if(l.getAutore()==null || (l.getAutore().length()==0 || l.getAutore().length()>50) || (!Pattern.matches("^[a-zA-Z ,.'-]+$",l.getAutore())))
            return false;
        if(l.getEditore()==null || (l.getEditore().length()==0 || l.getEditore().length()>50) || (!Pattern.matches("^[a-zA-Z ,.'-]+$",l.getEditore())))
            return false;
        String ncopie=Integer.toString(l.getnCopie());
        if(l.getnCopie()<0 || ncopie.length()>19 || (!Pattern.matches("^(0|[1-9]\\d*)$",ncopie)))
            return false;
        if((!Pattern.matches("(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})",l.getUrlCopertina())) || (l.getUrlCopertina().length()<11 || l.getUrlCopertina().length()>500))
            return false;
        return true;
    }
}
