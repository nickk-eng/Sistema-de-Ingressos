package br.edu.eventos.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Reserva {
    private String id;
    private String clienteId;
    private String eventoId;
    private String ingressoId;
    private EstadoIngresso status;
    private LocalDateTime criadaEm;

    public Reserva(String clienteId, String eventoId, String ingressoId, EstadoIngresso status, LocalDateTime criadaEm) {
        this.clienteId = Objects.requireNonNull(clienteId);
        this.eventoId = Objects.requireNonNull(eventoId);
        this.ingressoId = Objects.requireNonNull(ingressoId);
        this.status = Objects.requireNonNull(status);
        this.criadaEm = Objects.requireNonNull(criadaEm);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public String getEventoId() {
        return eventoId;
    }

    public String getIngressoId() {
        return ingressoId;
    }

    public EstadoIngresso getStatus() {
        return status;
    }

    public void setStatus(EstadoIngresso status) {
        this.status = Objects.requireNonNull(status);
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }
}
