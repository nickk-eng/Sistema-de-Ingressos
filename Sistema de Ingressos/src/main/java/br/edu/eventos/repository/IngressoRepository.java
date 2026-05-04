package br.edu.eventos.repository;

import br.edu.eventos.model.Ingresso;

import java.util.List;
import java.util.Optional;

public interface IngressoRepository {
    Ingresso salvar(Ingresso ingresso);

    List<Ingresso> listar();

    Optional<Ingresso> buscarPorId(String id);

    void atualizar(Ingresso ingresso);
}
