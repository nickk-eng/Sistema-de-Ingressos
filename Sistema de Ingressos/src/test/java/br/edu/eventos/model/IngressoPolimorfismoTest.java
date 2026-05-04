package br.edu.eventos.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IngressoPolimorfismoTest {
    @Test
    void deveCalcularValoresTratandoTodosComoIngresso() {
        List<Ingresso> ingressos = List.of(
                new IngressoNormal("Festival", "Ana", LocalDate.of(2026, 8, 10), new BigDecimal("100.00")),
                new IngressoVIP("Festival", "Bruno", LocalDate.of(2026, 8, 10), new BigDecimal("100.00")),
                new IngressoMeia("Festival", "Carla", LocalDate.of(2026, 8, 10), new BigDecimal("100.00"))
        );

        assertEquals(new BigDecimal("100.00"), ingressos.get(0).calcularValor());
        assertEquals(new BigDecimal("150.00"), ingressos.get(1).calcularValor());
        assertEquals(new BigDecimal("50.00"), ingressos.get(2).calcularValor());
        assertTrue(ingressos.stream().allMatch(ingresso -> ingresso.imprimirIngresso().contains("Festival")));
    }
}
