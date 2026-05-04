package br.edu.eventos.model;

public enum EstadoIngresso {
    DISPONIVEL("Disponível"),
    RESERVADO("Reservado"),
    PAGO("Pago"),
    CANCELADO("Cancelado"),
    USADO("Usado");

    private final String descricao;

    EstadoIngresso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
