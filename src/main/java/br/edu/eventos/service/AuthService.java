package br.edu.eventos.service;

import br.edu.eventos.model.Cliente;
import br.edu.eventos.repository.SistemaRepository;

import java.util.Optional;

public class AuthService {
    private final SistemaRepository repository;

    public AuthService(SistemaRepository repository) {
        this.repository = repository;
    }

    public Optional<Cliente> autenticar(String email, String senha) {
        if (email == null || senha == null) {
            return Optional.empty();
        }
        return repository.buscarClientePorEmail(email.trim().toLowerCase())
                .filter(cliente -> cliente.getSenha().equals(senha));
    }
}
