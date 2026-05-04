package br.edu.eventos.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public abstract class Ingresso {
    private String id;
    private String evento;
    private String participante;
    private LocalDate dataEvento;
    private BigDecimal valorBase;
    private EstadoIngresso estado;

    protected Ingresso(String evento, String participante, LocalDate dataEvento, BigDecimal valorBase) {
        this.evento = Objects.requireNonNull(evento);
        this.participante = Objects.requireNonNull(participante);
        this.dataEvento = Objects.requireNonNull(dataEvento);
        this.valorBase = Objects.requireNonNull(valorBase);
        this.estado = EstadoIngresso.RESERVADO;
    }

    public abstract BigDecimal calcularValor();

    public abstract String imprimirIngresso();

    public abstract String getTipo();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvento() {
        return evento;
    }

    public String getParticipante() {
        return participante;
    }

    public LocalDate getDataEvento() {
        return dataEvento;
    }

    public BigDecimal getValorBase() {
        return valorBase;
    }

    public EstadoIngresso getEstado() {
        return estado;
    }

    public void setEstado(EstadoIngresso estado) {
        this.estado = Objects.requireNonNull(estado);
    }
}
