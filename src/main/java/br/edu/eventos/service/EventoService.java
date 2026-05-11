package br.edu.eventos.service;

import br.edu.eventos.model.Evento;
import br.edu.eventos.repository.SistemaRepository;

import java.util.List;

public class EventoService {
    private final SistemaRepository repository;

    public EventoService(SistemaRepository repository) {
        this.repository = repository;
    }

    public List<Evento> listarEventos() {
        return repository.listarEventos();
    }
}
