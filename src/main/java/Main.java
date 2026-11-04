package com.rossigo.app;

import com.rossigo.eccezioni.BatteriaScaricaException;
import com.rossigo.eccezioni.RossiGoException;
import com.rossigo.modello.*;
import com.rossigo.servizio.FlottaService;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ROSSIGO SYSTEM (Versione Essenziale) ===\n");
        FlottaService service = new FlottaService();

        // Setup iniziale flotta
        try {
            service.registraVeicolo(new Bici("BK-0042", 80, StatoVeicolo.LIBERO, 6));
            service.registraVeicolo(new Monopattino("SC-0107", 10, StatoVeicolo.LIBERO, 25));
        } catch (Exception ignored) {}

        // 1. CF non valido
        System.out.println("[SCENARIO 1] Registrazione CF errato:");
        try {
            service.registraUtente(new Utente("U1", "Mario", "m@m.it", "ERRATO", 10.0));
        } catch (RossiGoException e) {
            System.out.println("-> OK: " + e.getMessage());
        }

        // Registrazione utente valido
        Utente uValido = new Utente("U2", "Giulia", "g@b.it", "BNKGLI90A41Z100Y", 5.0);
        try { service.registraUtente(uValido); } catch (Exception ignored) {}

        // 2. Credito insufficiente (< 1€)
        System.out.println("\n[SCENARIO 2] Sblocco con credito basso:");
        try {
            service.registraUtente(new Utente("U3", "Luca", "l@l.it", "VRDLCU85M01Z100W", 0.50));
            service.sbloccaVeicolo("BK-0042", "U3");
        } catch (RossiGoException e) {
            System.out.println("-> OK: " + e.getMessage());
        }

        // 3. Batteria scarica (< 15%)
        System.out.println("\n[SCENARIO 3] Sblocco veicolo con batteria scarica:");
        try {
            service.sbloccaVeicolo("SC-0107", "U2");
        } catch (BatteriaScaricaException e) {
            System.out.println("-> OK (Extra livello batt: " + e.getLivelloAttuale() + "%): " + e.getMessage());
        } catch (RossiGoException e) {
            System.out.println("-> " + e.getMessage());
        }

        // 4. Mezzo già in uso
        System.out.println("\n[SCENARIO 4] Sblocco mezzo già in uso:");
        try {
            service.sbloccaVeicolo("BK-0042", "U2"); // 1° sblocco OK
            service.sbloccaVeicolo("BK-0042", "U2"); // 2° sblocco fallisce
        } catch (RossiGoException e) {
            System.out.println("-> OK: " + e.getMessage());
        }

        // 5. Termine noleggio regolare
        System.out.println("\n[SCENARIO 5] Termine noleggio corretto (20 min):");
        try {
            service.terminaNoleggio("BK-0042", 20, "U2");
            System.out.println("-> Noleggio terminato. Credito residuo: " + service.getUtente("U2").getCredito() + "€");
        } catch (RossiGoException e) {
            System.out.println("-> Errore: " + e.getMessage());
        }

        // 6. Persistenza (nuova istanza del service)
        System.out.println("\n[SCENARIO 6] Verifica persistenza SQLite (Riavvio service):");
        FlottaService serviceRiavviato = new FlottaService();
        Utente uRicaricato = serviceRiavviato.getUtente("U2");
        if (uRicaricato != null) {
            System.out.println("-> OK: Utente " + uRicaricato.getNome() + " trovato nel DB. Credito: " + uRicaricato.getCredito() + "€");
        }
    }
}