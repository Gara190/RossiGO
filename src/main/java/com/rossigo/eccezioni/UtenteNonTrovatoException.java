package com.rossigo.eccezioni;

public class UtenteNonTrovatoException extends RossiGoException {
    public UtenteNonTrovatoException(String id) {
        super("Utente con ID '" + id + "' non trovato.");
    }
}