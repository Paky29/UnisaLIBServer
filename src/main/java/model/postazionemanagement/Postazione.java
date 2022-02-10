package model.postazionemanagement;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.posizionemanagement.Posizione;
import model.utentemanagement.Utente;
/**
 * Questa classe definisce una Postazione. Una Postazione ha un identificativo, un valore che ne specifica la disponibilità,
 * una posizione e una lista di blocchi inizialmente vuota
 */
public class Postazione {
    private String id;
    private boolean disponibile;
    private Posizione posizione;
    private ArrayList<Periodo> blocchi;
    /**
     * Crea una nuova Postazione
     */
    public Postazione(){
        blocchi=new ArrayList<>();
    }
    /**
     * Crea una nuova Postazione settando gli opportuni parametri
     @param id identificativo della postazione
     @param disponibile valore che indica la disponbilità della postazione
     @param posizione indica la posizione a cui è situata la postazione
     */
    public Postazione(String id, boolean disponibile, Posizione posizione) {
        this.id = id;
        this.disponibile = disponibile;
        this.posizione = posizione;
        this.blocchi=new ArrayList<>();
    }
    /**
     * Crea una nuova Postazione settando gli opportuni parametri
     @param disponibile valore che indica la disponbilità della postazione
     @param posizione indica la posizione a cui è situata la postazione
     */
    public Postazione(boolean disponibile, Posizione posizione) {
        this.disponibile = disponibile;
        this.posizione = posizione;
        this.blocchi=new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Postazione pos = (Postazione) o;
        if(blocchi.size()!=pos.blocchi.size())
            return false;
        for(Periodo blocco: blocchi)
            if(!pos.blocchi.contains(blocco))
                return false;
        return disponibile == pos.disponibile && id.equals(pos.id) && posizione.equals(pos.posizione);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public Posizione getPosizione() {
        return posizione;
    }

    public void setPosizione(Posizione posizione) {
        this.posizione = posizione;
    }

    public ArrayList<Periodo> getBlocchi() {
        return blocchi;
    }

    public void setBlocchi(ArrayList<Periodo> blocchi) {
        this.blocchi = blocchi;
    }

    public static String toJson(List<Postazione> pos){
        Gson gson = new Gson();
        return gson.toJson(pos);
    }

    public static String toJson(Postazione p){
        Gson gson = new Gson();
        Type fooType = new TypeToken<Postazione>() {}.getType();
        String json = gson.toJson(p,fooType);
        return json;
    }
}
