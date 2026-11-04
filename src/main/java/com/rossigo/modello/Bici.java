package com.rossigo.modello;

public class Bici extends Veicolo {
    private int numeroMarce;

    public Bici(String codice, int batteria, StatoVeicolo stato, int numeroMarce) {
        super(codice, batteria, stato);
        this.numeroMarce = numeroMarce;
    }

    @Override
    public double calcolaCosto(int minuti) { return minuti * 0.15; }

    @Override
    public double consumoBatteriaPerMinuto() { return 0.5; }

    public int getNumeroMarce() { return numeroMarce; }
}