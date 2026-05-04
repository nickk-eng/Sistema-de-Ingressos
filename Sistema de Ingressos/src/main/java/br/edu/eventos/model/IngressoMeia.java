package br.edu.eventos.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class IngressoMeia extends Ingresso {
    private static final BigDecimal DESCONTO_MEIA = new BigDecimal("0.50");

    public IngressoMeia(String evento, String participante, LocalDate dataEvento, BigDecimal valorBase) {
        super(evento, participante, dataEvento, valorBase);
    }

    @Override
    public BigDecimal calcularValor() {
        return getValorBase().multiply(DESCONTO_MEIA).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String imprimirIngresso() {
        return "Ingresso Meia-entrada - Evento: " + getEvento()
                + " - Participante: " + getParticipante()
                + " - Necessário comprovar direito à meia-entrada"
                + " - Valor: R$ " + calcularValor();
    }

    @Override
    public String getTipo() {
        return "MEIA";
    }
}
