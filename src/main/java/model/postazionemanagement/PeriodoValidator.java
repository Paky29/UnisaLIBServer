package model.postazionemanagement;

import utility.SwitchDate;

import java.util.GregorianCalendar;

public class PeriodoValidator {
    public static boolean validate(Periodo p){
        if (SwitchDate.compareDate(p.getData(), new GregorianCalendar())<0)
            return false;
        if(p.getOraInizio()!=9 && p.getOraInizio()!=11 && p.getOraInizio()!=14 && p.getOraInizio()!=16)
            return false;
        if(p.getOraFine()!=11 && p.getOraFine()!=13 && p.getOraFine()!=16 && p.getOraFine()!=18)
            return false;
        return true;
    }
}
