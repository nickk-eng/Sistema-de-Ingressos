package br.edu.eventos.model;

public enum TipoIngresso {
    NORMAL("Normal"),
    VIP("VIP"),
    MEIA("Meia-entrada");

    private final String descricao;

    TipoIngresso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
