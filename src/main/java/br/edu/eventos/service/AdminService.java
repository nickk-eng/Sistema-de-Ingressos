package br.edu.eventos.service;

import br.edu.eventos.model.EstadoIngresso;
import br.edu.eventos.model.Evento;
import br.edu.eventos.model.Ingresso;
import br.edu.eventos.model.IngressoDetalhado;
import br.edu.eventos.model.Reserva;
import br.edu.eventos.repository.SistemaRepository;

import java.util.List;
import java.util.Optional;

public class AdminService {
    private final SistemaRepository repository;

    public AdminService(SistemaRepository repository) {
        this.repository = repository;
    }

    public List<IngressoDetalhado> listarIngressosEmitidos() {
        return repository.listarReservas().stream()
                .map(this::montarDetalhe)
                .flatMap(Optional::stream)
                .toList();
    }

    public String validarQrCode(String codigoQr) {
        Ingresso ingresso = repository.buscarIngressoPorCodigoQr(codigoQr)
                .orElseThrow(() -> new IllegalArgumentException("Ingresso nao encontrado para o QR Code informado."));

        if (ingresso.getEstado() == EstadoIngresso.CANCELADO) {
            throw new IllegalStateException("Ingresso cancelado. Entrada nao autorizada.");
        }

        if (ingresso.getEstado() == EstadoIngresso.UTILIZADO) {
            throw new IllegalStateException("Ingresso ja utilizado. Uso duplicado impedido.");
        }

        ingresso.setEstado(EstadoIngresso.UTILIZADO);
        repository.atualizarIngresso(ingresso);

        Reserva reserva = repository.buscarReservaPorIngressoId(ingresso.getId())
                .orElseThrow(() -> new IllegalStateException("Reserva vinculada nao encontrada."));
        reserva.setStatus(EstadoIngresso.UTILIZADO);
        repository.atualizarReserva(reserva);

        return "Ingresso valido. Entrada liberada e ingresso marcado como utilizado.";
    }

    public long contarPorEstado(EstadoIngresso estado) {
        return repository.listarIngressos().stream()
                .filter(ingresso -> ingresso.getEstado() == estado)
                .count();
    }

    public int contarDisponiveis() {
        return repository.listarEventos().stream()
                .mapToInt(Evento::getIngressosDisponiveis)
                .sum();
    }

    private Optional<IngressoDetalhado> montarDetalhe(Reserva reserva) {
        return repository.buscarIngressoPorId(reserva.getIngressoId())
                .flatMap(ingresso -> repository.buscarEventoPorId(reserva.getEventoId())
                        .flatMap(evento -> repository.buscarClientePorId(reserva.getClienteId())
                                .map(cliente -> new IngressoDetalhado(ingresso, evento, cliente, reserva))));
    }
}
