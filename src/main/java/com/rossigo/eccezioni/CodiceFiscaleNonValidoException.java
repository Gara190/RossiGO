package com.rossigo.eccezioni;

public class CodiceFiscaleNonValidoException extends RossiGoException {
    public CodiceFiscaleNonValidoException(String cf) {
        super("Codice fiscale non valido: " + cf);
    }
}