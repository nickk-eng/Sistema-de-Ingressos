package br.edu.eventos.service;

import br.edu.eventos.model.EstadoIngresso;
import br.edu.eventos.model.Ingresso;
import br.edu.eventos.model.IngressoFactory;
import br.edu.eventos.repository.IngressoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class IngressoService {
    private final IngressoRepository repository;

    public IngressoService(IngressoRepository repository) {
        this.repository = repository;
    }

    public Ingresso comprar(String tipo, String evento, String participante, LocalDate dataEvento, BigDecimal valorBase) {
        validarDados(evento, participante, dataEvento, valorBase);
        Ingresso ingresso = IngressoFactory.criar(tipo, evento.trim(), participante.trim(), dataEvento, valorBase);
        ingresso.setEstado(EstadoIngresso.PAGO);
        return repository.salvar(ingresso);
    }

    public List<Ingresso> listar() {
        return repository.listar();
    }

    public void alterarEstado(String id, EstadoIngresso novoEstado) {
        Ingresso ingresso = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Ingresso não encontrado."));

        if (ingresso.getEstado() == EstadoIngresso.CANCELADO || ingresso.getEstado() == EstadoIngresso.USADO) {
            throw new IllegalStateException("Não é possível alterar um ingresso finalizado.");
        }

        ingresso.setEstado(novoEstado);
        repository.atualizar(ingresso);
    }

    private void validarDados(String evento, String participante, LocalDate dataEvento, BigDecimal valorBase) {
        if (evento == null || evento.isBlank()) {
            throw new IllegalArgumentException("Informe o nome do evento.");
        }
        if (participante == null || participante.isBlank()) {
            throw new IllegalArgumentException("Informe o nome do participante.");
        }
        if (dataEvento == null) {
            throw new IllegalArgumentException("Informe a data do evento.");
        }
        if (valorBase == null || valorBase.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor base deve ser maior que zero.");
        }
    }
}
