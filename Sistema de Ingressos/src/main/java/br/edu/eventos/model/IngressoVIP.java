package br.edu.eventos.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class IngressoVIP extends Ingresso {
    private static final BigDecimal MULTIPLICADOR_VIP = new BigDecimal("1.50");

    public IngressoVIP(String evento, String participante, LocalDate dataEvento, BigDecimal valorBase) {
        super(evento, participante, dataEvento, valorBase);
    }

    @Override
    public BigDecimal calcularValor() {
        return getValorBase().multiply(MULTIPLICADOR_VIP).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String imprimirIngresso() {
        return "Ingresso VIP - Evento: " + getEvento()
                + " - Participante: " + getParticipante()
                + " - Benefícios: área premium e entrada antecipada"
                + " - Valor: R$ " + calcularValor();
    }

    @Override
    public String getTipo() {
        return "VIP";
    }
}
