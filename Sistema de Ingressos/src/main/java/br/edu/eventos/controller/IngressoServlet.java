package br.edu.eventos.controller;

import br.edu.eventos.model.EstadoIngresso;
import br.edu.eventos.model.Ingresso;
import br.edu.eventos.repository.MongoIngressoRepository;
import br.edu.eventos.service.IngressoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "IngressoServlet", urlPatterns = {"/ingressos"})
public class IngressoServlet extends HttpServlet {
    private IngressoService service;

    @Override
    public void init() {
        service = new IngressoService(new MongoIngressoRepository());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        carregarTela(request, response, null, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String acao = request.getParameter("acao");

        try {
            if ("alterarEstado".equals(acao)) {
                service.alterarEstado(
                        request.getParameter("id"),
                        EstadoIngresso.valueOf(request.getParameter("estado"))
                );
                carregarTela(request, response, "Estado do ingresso atualizado.", null);
                return;
            }

            Ingresso ingresso = service.comprar(
                    request.getParameter("tipo"),
                    request.getParameter("evento"),
                    request.getParameter("participante"),
                    LocalDate.parse(request.getParameter("dataEvento")),
                    new BigDecimal(request.getParameter("valorBase"))
            );
            carregarTela(request, response, "Ingresso comprado: " + ingresso.imprimirIngresso(), ingresso);
        } catch (RuntimeException ex) {
            carregarTela(request, response, "Erro: " + ex.getMessage(), null);
        }
    }

    private void carregarTela(HttpServletRequest request, HttpServletResponse response, String mensagem, Ingresso ingresso)
            throws ServletException, IOException {
        try {
            List<Ingresso> ingressos = service.listar();
            request.setAttribute("ingressos", ingressos);
        } catch (RuntimeException ex) {
            request.setAttribute("erroBanco", "Não foi possível consultar o MongoDB: " + ex.getMessage());
        }
        request.setAttribute("mensagem", mensagem);
        request.setAttribute("ingressoComprado", ingresso);
        request.setAttribute("estados", EstadoIngresso.values());
        request.getRequestDispatcher("/WEB-INF/views/ingressos.jsp").forward(request, response);
    }
}
