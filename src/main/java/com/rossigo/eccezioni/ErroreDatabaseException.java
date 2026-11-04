package com.rossigo.eccezioni;

public class ErroreDatabaseException extends RossiGoException {
    public ErroreDatabaseException(String message, Throwable cause) {
        super("Errore database: " + message, cause);
    }
}