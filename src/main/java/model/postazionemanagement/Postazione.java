package model.postazionemanagement;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.posizionemanagement.Posizione;
import model.utentemanagement.Utente;

public class Postazione {
    private String id;
    private boolean disponibile;
    private Posizione posizione;
    private ArrayList<Periodo> blocchi;

    public Postazione(){
        blocchi=new ArrayList<>();
    }
    public Postazione(String id, boolean disponibile, Posizione posizione) {
        this.id = id;
        this.disponibile = disponibile;
        this.posizione = posizione;
    }

    public Postazione(boolean disponibile, Posizione posizione) {
        this.disponibile = disponibile;
        this.posizione = posizione;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Postazione pos = (Postazione) o;
        boolean bl=true;
        if(blocchi.size()!=pos.blocchi.size())
            return false;
        for(Periodo blocco: blocchi)
            if(!pos.blocchi.contains(blocco))
                return false;
        return disponibile == pos.disponibile && id.equals(pos.id) && posizione.equals(pos.posizione) && bl;
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
