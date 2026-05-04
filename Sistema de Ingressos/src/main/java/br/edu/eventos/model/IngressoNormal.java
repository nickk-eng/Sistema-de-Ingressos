package br.edu.eventos.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class IngressoNormal extends Ingresso {
    public IngressoNormal(String evento, String participante, LocalDate dataEvento, BigDecimal valorBase) {
        super(evento, participante, dataEvento, valorBase);
    }

    @Override
    public BigDecimal calcularValor() {
        return getValorBase();
    }

    @Override
    public String imprimirIngresso() {
        return "Ingresso Normal - Evento: " + getEvento()
                + " - Participante: " + getParticipante()
                + " - Valor: R$ " + calcularValor();
    }

    @Override
    public String getTipo() {
        return "NORMAL";
    }
}
