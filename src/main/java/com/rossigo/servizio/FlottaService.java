package com.rossigo.servizio;

import com.rossigo.eccezioni.*;
import com.rossigo.modello.*;
import com.rossigo.persistenza.DatabaseManager;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class FlottaService {
    private final Map<String, Veicolo> veicoli = new HashMap<>();
    private final Map<String, Utente> utenti = new HashMap<>();
    private final DatabaseManager db = DatabaseManager.getInstance();

    public FlottaService() {
        caricaDaDb();
    }

    private void caricaDaDb() {
        try {
            db.caricaVeicoli().forEach(v -> veicoli.put(v.getCodice(), v));
            db.caricaUtenti().forEach(u -> utenti.put(u.getId(), u));
        } catch (Exception e) {
            System.err.println("Errore caricamento dati iniziali: " + e.getMessage());
        }
    }

    public void registraVeicolo(Veicolo v) throws RossiGoException {
        veicoli.put(v.getCodice(), v);
        db.salvaVeicolo(v);
    }

    public void registraUtente(Utente u) throws RossiGoException {
        if (StringUtils.isBlank(u.getNome()) || !u.getCodiceFiscale().matches("^[A-Z0-9]{16}$")) {
            throw new CodiceFiscaleNonValidoException(u.getCodiceFiscale());
        }
        utenti.put(u.getId(), u);
        db.salvaUtente(u);
    }

    public void sbloccaVeicolo(String codice, String idUtente) throws RossiGoException {
        Veicolo v = veicoli.get(codice);
        Utente u = utenti.get(idUtente);

        if (v == null || v.getStato() != StatoVeicolo.LIBERO) throw new VeicoloNonDisponibileException(codice);
        if (u == null) throw new UtenteNonTrovatoException(idUtente);
        if (v.getBatteria() < 15) throw new BatteriaScaricaException(v.getCodice(), v.getBatteria());
        if (u.getCredito() < 1.0) throw new CreditoInsufficienteException(u.getCredito());

        v.setStato(StatoVeicolo.IN_USO);
        db.aggiornaVeicolo(v.getCodice(), v.getBatteria(), v.getStato());
    }

    public void terminaNoleggio(String codice, int minuti, String idUtente) throws RossiGoException {
        Veicolo v = veicoli.get(codice);
        Utente u = utenti.get(idUtente);

        double costo = 1.0 + v.calcolaCosto(minuti); // Sblocco + Polimorfismo costo
        if (u.getCredito() < costo) throw new CreditoInsufficienteException(u.getCredito());

        int consumo = (int) Math.round(minuti * v.consumoBatteriaPerMinuto()); // Polimorfismo consumo
        v.setBatteria(v.getBatteria() - consumo);
        v.setStato(StatoVeicolo.LIBERO);
        u.setCredito(u.getCredito() - costo);

        db.aggiornaVeicolo(v.getCodice(), v.getBatteria(), v.getStato());
        db.aggiornaCredito(u.getId(), u.getCredito());
    }

    public Utente getUtente(String id) { return utenti.get(id); }
}