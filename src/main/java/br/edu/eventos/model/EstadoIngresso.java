package br.edu.eventos.model;

public enum EstadoIngresso {
    RESERVADO("Reservado"),
    CONFIRMADO("Confirmado"),
    UTILIZADO("Utilizado"),
    CANCELADO("Cancelado");

    private final String descricao;

    EstadoIngresso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
