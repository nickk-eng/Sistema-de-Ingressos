package br.edu.eventos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Evento {
    private String id;
    private String nome;
    private String descricao;
    private LocalDateTime dataHora;
    private String local;
    private int totalIngressos;
    private int ingressosDisponiveis;
    private BigDecimal valorIngresso;

    public Evento(String nome, String descricao, LocalDateTime dataHora, String local,
                  int totalIngressos, int ingressosDisponiveis, BigDecimal valorIngresso) {
        this.nome = Objects.requireNonNull(nome);
        this.descricao = Objects.requireNonNull(descricao);
        this.dataHora = Objects.requireNonNull(dataHora);
        this.local = Objects.requireNonNull(local);
        this.totalIngressos = totalIngressos;
        this.ingressosDisponiveis = ingressosDisponiveis;
        this.valorIngresso = Objects.requireNonNull(valorIngresso);
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

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getLocal() {
        return local;
    }

    public int getTotalIngressos() {
        return totalIngressos;
    }

    public int getIngressosDisponiveis() {
        return ingressosDisponiveis;
    }

    public BigDecimal getValorIngresso() {
        return valorIngresso;
    }

    public boolean possuiDisponibilidade() {
        return ingressosDisponiveis > 0;
    }

    public void reservarUnidade() {
        if (!possuiDisponibilidade()) {
            throw new IllegalStateException("Nao ha ingressos disponiveis para este evento.");
        }
        ingressosDisponiveis--;
    }

    public void devolverUnidade() {
        if (ingressosDisponiveis < totalIngressos) {
            ingressosDisponiveis++;
        }
    }
}
