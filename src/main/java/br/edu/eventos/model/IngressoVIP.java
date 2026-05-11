package br.edu.eventos.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class IngressoVIP extends Ingresso {
    private static final BigDecimal MULTIPLICADOR = new BigDecimal("1.50");

    public IngressoVIP(String clienteId, String eventoId, String codigoQr, BigDecimal valorBase) {
        super(clienteId, eventoId, codigoQr, valorBase);
    }

    @Override
    public BigDecimal calcularValor() {
        return getValorBase().multiply(MULTIPLICADOR).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String imprimirIngresso() {
        return "Ingresso VIP - Entrada prioritaria - Valor: R$ " + calcularValor();
    }

    @Override
    public TipoIngresso getTipo() {
        return TipoIngresso.VIP;
    }
}
