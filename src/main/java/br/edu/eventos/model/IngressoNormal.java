package br.edu.eventos.model;

import java.math.BigDecimal;

public class IngressoNormal extends Ingresso {
    public IngressoNormal(String clienteId, String eventoId, String codigoQr, BigDecimal valorBase) {
        super(clienteId, eventoId, codigoQr, valorBase);
    }

    @Override
    public BigDecimal calcularValor() {
        return getValorBase();
    }

    @Override
    public String imprimirIngresso() {
        return "Ingresso Normal - Valor: R$ " + calcularValor();
    }

    @Override
    public TipoIngresso getTipo() {
        return TipoIngresso.NORMAL;
    }
}
