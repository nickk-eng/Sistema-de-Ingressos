package br.edu.eventos.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class IngressoFactory {
    private IngressoFactory() {
    }

    public static Ingresso criar(String tipo, String evento, String participante, LocalDate dataEvento, BigDecimal valorBase) {
        return switch (tipo.toUpperCase()) {
            case "NORMAL" -> new IngressoNormal(evento, participante, dataEvento, valorBase);
            case "VIP" -> new IngressoVIP(evento, participante, dataEvento, valorBase);
            case "MEIA" -> new IngressoMeia(evento, participante, dataEvento, valorBase);
            default -> throw new IllegalArgumentException("Tipo de ingresso inválido: " + tipo);
        };
    }
}
