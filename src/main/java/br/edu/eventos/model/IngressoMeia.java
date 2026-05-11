package br.edu.eventos.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class IngressoMeia extends Ingresso {
    private static final BigDecimal DESCONTO = new BigDecimal("0.50");

    public IngressoMeia(String clienteId, String eventoId, String codigoQr, BigDecimal valorBase) {
        super(clienteId, eventoId, codigoQr, valorBase);
    }

    @Override
    public BigDecimal calcularValor() {
        return getValorBase().multiply(DESCONTO).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String imprimirIngresso() {
        return "Ingresso Meia-entrada - Necessario comprovante - Valor: R$ " + calcularValor();
    }

    @Override
    public TipoIngresso getTipo() {
        return TipoIngresso.MEIA;
    }
}
