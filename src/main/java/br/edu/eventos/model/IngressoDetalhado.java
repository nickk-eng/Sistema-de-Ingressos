package br.edu.eventos.model;

public class IngressoDetalhado {
    private final Ingresso ingresso;
    private final Evento evento;
    private final Cliente cliente;
    private final Reserva reserva;

    public IngressoDetalhado(Ingresso ingresso, Evento evento, Cliente cliente, Reserva reserva) {
        this.ingresso = ingresso;
        this.evento = evento;
        this.cliente = cliente;
        this.reserva = reserva;
    }

    public Ingresso getIngresso() {
        return ingresso;
    }

    public Evento getEvento() {
        return evento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Reserva getReserva() {
        return reserva;
    }
}
