package com.rossigo.modello;

public class Utente {
    private String id;
    private String nome;
    private String email;
    private String codiceFiscale;
    private double credito;

    public Utente(String id, String nome, String email, String codiceFiscale, double credito) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.codiceFiscale = codiceFiscale;
        this.credito = credito;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getCodiceFiscale() { return codiceFiscale; }
    public double getCredito() { return credito; }
    public void setCredito(double credito) { this.credito = credito; }
}