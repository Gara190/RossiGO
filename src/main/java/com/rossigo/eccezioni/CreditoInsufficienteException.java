package com.rossigo.eccezioni;

public class CreditoInsufficienteException extends RossiGoException {
    public CreditoInsufficienteException(double credito) {
        super("Credito insufficiente: " + credito + "€ (minimo 1.00€ richiesto)");
    }
}