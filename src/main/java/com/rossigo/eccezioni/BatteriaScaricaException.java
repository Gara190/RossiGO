package com.rossigo.eccezioni;

public class BatteriaScaricaException extends RossiGoException {
    private final int livelloAttuale;

    public BatteriaScaricaException(String codiceVeicolo, int livelloAttuale) {
        super("Il veicolo " + codiceVeicolo + " ha la batteria troppo scarica (" + livelloAttuale + "%).");
        this.livelloAttuale = livelloAttuale;
    }

    public int getLivelloAttuale() {
        return livelloAttuale;
    }
}