package br.edu.eventos.model;

import java.util.Objects;

public class Cliente {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private PerfilUsuario perfil;

    public Cliente(String nome, String email, String senha, PerfilUsuario perfil) {
        this.nome = Objects.requireNonNull(nome);
        this.email = Objects.requireNonNull(email);
        this.senha = Objects.requireNonNull(senha);
        this.perfil = Objects.requireNonNull(perfil);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public boolean isAdmin() {
        return perfil == PerfilUsuario.ADMIN;
    }
}
