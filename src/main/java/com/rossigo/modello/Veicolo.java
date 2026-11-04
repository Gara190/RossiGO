package com.rossigo.modello;

public abstract class Veicolo {
    protected String codice;
    protected int batteria;
    protected StatoVeicolo stato;

    public Veicolo(String codice, int batteria, StatoVeicolo stato) {
        this.codice = codice;
        this.batteria = batteria;
        this.stato = stato;
    }

    public abstract double calcolaCosto(int minuti);
    public abstract double consumoBatteriaPerMinuto();

    public String getCodice() { return codice; }
    public int getBatteria() { return batteria; }
    public void setBatteria(int batteria) { this.batteria = Math.max(0, Math.min(100, batteria)); }
    public StatoVeicolo getStato() { return stato; }
    public void setStato(StatoVeicolo stato) { this.stato = stato; }
}