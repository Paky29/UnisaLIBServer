package model.postazionemanagement;

import java.util.ArrayList;

import model.posizionemanagement.Posizione;

public class Postazione {
    private int id;
    private boolean disponibile;
    private Posizione posizione;
    private ArrayList<Periodo> blocchi;

    public Postazione(int id, boolean disponibile, Posizione posizione) {
        this.id = id;
        this.disponibile = disponibile;
        this.posizione = posizione;
    }

    public Postazione(boolean disponibile, Posizione posizione) {
        this.disponibile = disponibile;
        this.posizione = posizione;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
