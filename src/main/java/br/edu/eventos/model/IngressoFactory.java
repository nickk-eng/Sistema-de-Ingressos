package br.edu.eventos.model;

import java.math.BigDecimal;

public final class IngressoFactory {
    private IngressoFactory() {
    }

    public static Ingresso criar(TipoIngresso tipo, String clienteId, String eventoId, String codigoQr, BigDecimal valorBase) {
        return switch (tipo) {
            case NORMAL -> new IngressoNormal(clienteId, eventoId, codigoQr, valorBase);
            case VIP -> new IngressoVIP(clienteId, eventoId, codigoQr, valorBase);
            case MEIA -> new IngressoMeia(clienteId, eventoId, codigoQr, valorBase);
        };
    }
}
