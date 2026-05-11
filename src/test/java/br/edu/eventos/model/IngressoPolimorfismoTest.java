package br.edu.eventos.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IngressoPolimorfismoTest {
    @Test
    void deveCalcularValoresPorPolimorfismo() {
        List<Ingresso> ingressos = List.of(
                new IngressoNormal("cliente", "evento", "QR-1", new BigDecimal("100.00")),
                new IngressoVIP("cliente", "evento", "QR-2", new BigDecimal("100.00")),
                new IngressoMeia("cliente", "evento", "QR-3", new BigDecimal("100.00"))
        );

        assertEquals(new BigDecimal("100.00"), ingressos.get(0).calcularValor());
        assertEquals(new BigDecimal("150.00"), ingressos.get(1).calcularValor());
        assertEquals(new BigDecimal("50.00"), ingressos.get(2).calcularValor());
    }
}
