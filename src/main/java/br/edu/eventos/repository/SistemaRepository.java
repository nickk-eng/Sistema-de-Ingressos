package br.edu.eventos.repository;

import br.edu.eventos.model.Cliente;
import br.edu.eventos.model.Evento;
import br.edu.eventos.model.Ingresso;
import br.edu.eventos.model.Reserva;

import java.util.List;
import java.util.Optional;

public interface SistemaRepository {
    void inicializarDados();

    Optional<Cliente> buscarClientePorEmail(String email);

    Optional<Cliente> buscarClientePorId(String id);

    List<Evento> listarEventos();

    Optional<Evento> buscarEventoPorId(String id);

    Evento salvarEvento(Evento evento);

    Ingresso salvarIngresso(Ingresso ingresso);

    void atualizarIngresso(Ingresso ingresso);

    Optional<Ingresso> buscarIngressoPorId(String id);

    Optional<Ingresso> buscarIngressoPorCodigoQr(String codigoQr);

    List<Ingresso> listarIngressos();

    Reserva salvarReserva(Reserva reserva);

    void atualizarReserva(Reserva reserva);

    Optional<Reserva> buscarReservaPorIngressoId(String ingressoId);

    boolean existeReservaAtiva(String clienteId, String eventoId);

    List<Reserva> listarReservasPorCliente(String clienteId);

    List<Reserva> listarReservas();
}
