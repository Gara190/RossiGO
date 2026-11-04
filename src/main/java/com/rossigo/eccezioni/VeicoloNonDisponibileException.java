package com.rossigo.eccezioni;

public class VeicoloNonDisponibileException extends RossiGoException {
    public VeicoloNonDisponibileException(String codice) {
        super("Veicolo " + codice + " non disponibile (già in uso o in ricarica).");
    }
}