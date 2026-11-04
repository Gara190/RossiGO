package com.rossigo.persistenza;

import com.rossigo.eccezioni.ErroreDatabaseException;
import com.rossigo.modello.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static DatabaseManager instance;
    private static final String URL = "jdbc:sqlite:rossigo.db";

    private DatabaseManager() {
        inizializzaDatabase();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) instance = new DatabaseManager();
        return instance;
    }

    private void inizializzaDatabase() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS veicoli (codice TEXT PRIMARY KEY, tipo TEXT, batteria INT, stato TEXT, extra INT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS utenti (id TEXT PRIMARY KEY, nome TEXT, email TEXT, codice_fiscale TEXT, credito REAL)");
        } catch (SQLException e) {
            throw new RuntimeException("Errore init DB", e);
        }
    }

    public void salvaVeicolo(Veicolo v) throws ErroreDatabaseException {
        String tipo = (v instanceof Bici) ? "BICI" : "MONOPATTINO";
        int extra = (v instanceof Bici) ? ((Bici) v).getNumeroMarce() : ((Monopattino) v).getVelocitaMassima();
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement("INSERT OR REPLACE INTO veicoli VALUES(?, ?, ?, ?, ?)")) {
            ps.setString(1, v.getCodice());
            ps.setString(2, tipo);
            ps.setInt(3, v.getBatteria());
            ps.setString(4, v.getStato().name());
            ps.setInt(5, extra);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new ErroreDatabaseException("Salvataggio veicolo fallito", e);
        }
    }

    public void aggiornaVeicolo(String codice, int batt, StatoVeicolo stato) throws ErroreDatabaseException {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement("UPDATE veicoli SET batteria = ?, stato = ? WHERE codice = ?")) {
            ps.setInt(1, batt);
            ps.setString(2, stato.name());
            ps.setString(3, codice);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new ErroreDatabaseException("Aggiornamento veicolo fallito", e);
        }
    }

    public void salvaUtente(Utente u) throws ErroreDatabaseException {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement("INSERT OR REPLACE INTO utenti VALUES(?, ?, ?, ?, ?)")) {
            ps.setString(1, u.getId());
            ps.setString(2, u.getNome());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getCodiceFiscale());
            ps.setDouble(5, u.getCredito());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new ErroreDatabaseException("Salvataggio utente fallito", e);
        }
    }

    public void aggiornaCredito(String id, double credito) throws ErroreDatabaseException {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement("UPDATE utenti SET credito = ? WHERE id = ?")) {
            ps.setDouble(1, credito);
            ps.setString(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new ErroreDatabaseException("Aggiornamento credito fallito", e);
        }
    }

    public List<Veicolo> caricaVeicoli() throws ErroreDatabaseException {
        List<Veicolo> lista = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM veicoli")) {
            while (rs.next()) {
                if ("BICI".equals(rs.getString("tipo"))) {
                    lista.add(new Bici(rs.getString("codice"), rs.getInt("batteria"), StatoVeicolo.valueOf(rs.getString("stato")), rs.getInt("extra")));
                } else {
                    lista.add(new Monopattino(rs.getString("codice"), rs.getInt("batteria"), StatoVeicolo.valueOf(rs.getString("stato")), rs.getInt("extra")));
                }
            }
        } catch (SQLException e) {
            throw new ErroreDatabaseException("Caricamento veicoli fallito", e);
        }
        return lista;
    }

    public List<Utente> caricaUtenti() throws ErroreDatabaseException {
        List<Utente> lista = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM utenti")) {
            while (rs.next()) {
                lista.add(new Utente(rs.getString("id"), rs.getString("nome"), rs.getString("email"), rs.getString("codice_fiscale"), rs.getDouble("credito")));
            }
        } catch (SQLException e) {
            throw new ErroreDatabaseException("Caricamento utenti fallito", e);
        }
        return lista;
    }
}