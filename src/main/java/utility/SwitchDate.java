package utility;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SwitchDate {

    public static Date toDate(GregorianCalendar gc){
        Date d = new Date(gc.get(Calendar.YEAR)-1900, gc.get(Calendar.MONTH), gc.get(Calendar.DATE));
        return d;
    }

    public static GregorianCalendar toGregorianCalendar(Date d){
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(d.getYear()+1900, d.getMonth(), d.getDate(), 0 ,0 ,0);
        return gc;
    }

    public static boolean equalsDate(GregorianCalendar d1, GregorianCalendar d2){
        return (d1.get(Calendar.YEAR)==d2.get(Calendar.YEAR) && d1.get(Calendar.MONTH)==d2.get(Calendar.MONTH) && d1.get(Calendar.DATE)==d2.get(Calendar.DATE));
    }

    public static int compareDate(GregorianCalendar d1, GregorianCalendar d2){
        return (new GregorianCalendar(d1.get(Calendar.YEAR), d1.get(Calendar.MONTH), d1.get(Calendar.DATE)).compareTo(new GregorianCalendar(d2.get(Calendar.YEAR), d2.get(Calendar.MONTH), d2.get(Calendar.DATE))));
    }
}
