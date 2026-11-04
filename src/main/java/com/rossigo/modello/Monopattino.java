package com.rossigo.modello;

public class Monopattino extends Veicolo {
    private int velocitaMassima;

    public Monopattino(String codice, int batteria, StatoVeicolo stato, int velocitaMassima) {
        super(codice, batteria, stato);
        this.velocitaMassima = velocitaMassima;
    }

    @Override
    public double calcolaCosto(int minuti) { return minuti * 0.25; }

    @Override
    public double consumoBatteriaPerMinuto() { return 1.2; }

    public int getVelocitaMassima() { return velocitaMassima; }
}