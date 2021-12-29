package utility;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SwitchDate {

    public static Date toDate(GregorianCalendar gc){
        Date d = new Date(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE));
        return d;
    }

    public static GregorianCalendar toGregorianCalendar(Date d){
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(d.getYear(), d.getMonth(), d.getDate());
        return gc;
    }
}
