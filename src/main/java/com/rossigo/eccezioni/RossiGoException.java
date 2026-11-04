package com.rossigo.eccezioni;

public class RossiGoException extends Exception {
    public RossiGoException(String message) {
        super(message);
    }

    public RossiGoException(String message, Throwable cause) {
    }
}