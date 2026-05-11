package br.edu.eventos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Ingresso {
    private String id;
    private String clienteId;
    private String eventoId;
    private String codigoQr;
    private EstadoIngresso estado;
    private LocalDateTime emitidoEm;
    private BigDecimal valorBase;

    protected Ingresso(String clienteId, String eventoId, String codigoQr, BigDecimal valorBase) {
        this.clienteId = Objects.requireNonNull(clienteId);
        this.eventoId = Objects.requireNonNull(eventoId);
        this.codigoQr = Objects.requireNonNull(codigoQr);
        this.valorBase = Objects.requireNonNull(valorBase);
        this.estado = EstadoIngresso.RESERVADO;
        this.emitidoEm = LocalDateTime.now();
    }

    public abstract BigDecimal calcularValor();

    public abstract String imprimirIngresso();

    public abstract TipoIngresso getTipo();

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

    public String getCodigoQr() {
        return codigoQr;
    }

    public EstadoIngresso getEstado() {
        return estado;
    }

    public void setEstado(EstadoIngresso estado) {
        this.estado = Objects.requireNonNull(estado);
    }

    public LocalDateTime getEmitidoEm() {
        return emitidoEm;
    }

    public void setEmitidoEm(LocalDateTime emitidoEm) {
        this.emitidoEm = Objects.requireNonNull(emitidoEm);
    }

    public BigDecimal getValorBase() {
        return valorBase;
    }
}
