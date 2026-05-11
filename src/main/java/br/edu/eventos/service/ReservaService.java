package br.edu.eventos.service;

import br.edu.eventos.model.Cliente;
import br.edu.eventos.model.EstadoIngresso;
import br.edu.eventos.model.Evento;
import br.edu.eventos.model.Ingresso;
import br.edu.eventos.model.IngressoDetalhado;
import br.edu.eventos.model.IngressoFactory;
import br.edu.eventos.model.Reserva;
import br.edu.eventos.model.TipoIngresso;
import br.edu.eventos.repository.SistemaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ReservaService {
    private final SistemaRepository repository;

    public ReservaService(SistemaRepository repository) {
        this.repository = repository;
    }

    public Ingresso reservar(String clienteId, String eventoId, TipoIngresso tipoIngresso) {
        Evento evento = repository.buscarEventoPorId(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento nao encontrado."));

        if (!evento.possuiDisponibilidade()) {
            throw new IllegalStateException("Nao ha ingressos disponiveis para este evento.");
        }

        if (repository.existeReservaAtiva(clienteId, eventoId)) {
            throw new IllegalStateException("Cliente ja possui reserva ativa para este evento.");
        }

        String codigoQr = "EVT-" + UUID.randomUUID();
        Ingresso ingresso = IngressoFactory.criar(tipoIngresso, clienteId, eventoId, codigoQr, evento.getValorIngresso());
        ingresso.setEstado(EstadoIngresso.RESERVADO);
        ingresso = repository.salvarIngresso(ingresso);

        Reserva reserva = new Reserva(clienteId, eventoId, ingresso.getId(), EstadoIngresso.RESERVADO, LocalDateTime.now());
        repository.salvarReserva(reserva);

        evento.reservarUnidade();
        repository.salvarEvento(evento);
        return ingresso;
    }

    public List<IngressoDetalhado> listarIngressosDoCliente(String clienteId) {
        return repository.listarReservasPorCliente(clienteId).stream()
                .map(this::montarDetalhe)
                .flatMap(Optional::stream)
                .toList();
    }

    public void confirmarReserva(String clienteId, String ingressoId) {
        Ingresso ingresso = buscarIngressoDoCliente(clienteId, ingressoId);
        if (ingresso.getEstado() != EstadoIngresso.RESERVADO) {
            throw new IllegalStateException("Apenas ingressos reservados podem ser confirmados.");
        }
        ingresso.setEstado(EstadoIngresso.CONFIRMADO);
        repository.atualizarIngresso(ingresso);

        Reserva reserva = repository.buscarReservaPorIngressoId(ingressoId)
                .orElseThrow(() -> new IllegalStateException("Reserva nao encontrada."));
        reserva.setStatus(EstadoIngresso.CONFIRMADO);
        repository.atualizarReserva(reserva);
    }

    public void cancelarReserva(String clienteId, String ingressoId) {
        Ingresso ingresso = buscarIngressoDoCliente(clienteId, ingressoId);
        if (ingresso.getEstado() == EstadoIngresso.UTILIZADO) {
            throw new IllegalStateException("Ingresso utilizado nao pode ser cancelado.");
        }
        if (ingresso.getEstado() == EstadoIngresso.CANCELADO) {
            throw new IllegalStateException("Ingresso ja esta cancelado.");
        }

        ingresso.setEstado(EstadoIngresso.CANCELADO);
        repository.atualizarIngresso(ingresso);

        Reserva reserva = repository.buscarReservaPorIngressoId(ingressoId)
                .orElseThrow(() -> new IllegalStateException("Reserva nao encontrada."));
        reserva.setStatus(EstadoIngresso.CANCELADO);
        repository.atualizarReserva(reserva);

        Evento evento = repository.buscarEventoPorId(ingresso.getEventoId())
                .orElseThrow(() -> new IllegalStateException("Evento nao encontrado."));
        evento.devolverUnidade();
        repository.salvarEvento(evento);
    }

    private Ingresso buscarIngressoDoCliente(String clienteId, String ingressoId) {
        Ingresso ingresso = repository.buscarIngressoPorId(ingressoId)
                .orElseThrow(() -> new IllegalArgumentException("Ingresso nao encontrado."));
        if (!ingresso.getClienteId().equals(clienteId)) {
            throw new IllegalStateException("Ingresso nao pertence ao cliente autenticado.");
        }
        return ingresso;
    }

    private Optional<IngressoDetalhado> montarDetalhe(Reserva reserva) {
        Optional<Ingresso> ingresso = repository.buscarIngressoPorId(reserva.getIngressoId());
        Optional<Evento> evento = repository.buscarEventoPorId(reserva.getEventoId());
        Optional<Cliente> cliente = repository.buscarClientePorId(reserva.getClienteId());
        if (ingresso.isEmpty() || evento.isEmpty() || cliente.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new IngressoDetalhado(ingresso.get(), evento.get(), cliente.get(), reserva));
    }
}
