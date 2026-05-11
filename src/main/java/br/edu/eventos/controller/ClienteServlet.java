package br.edu.eventos.controller;

import br.edu.eventos.model.Cliente;
import br.edu.eventos.model.TipoIngresso;
import br.edu.eventos.repository.MongoSistemaRepository;
import br.edu.eventos.repository.SistemaRepository;
import br.edu.eventos.service.EventoService;
import br.edu.eventos.service.ReservaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ClienteServlet", urlPatterns = {"/cliente"})
public class ClienteServlet extends HttpServlet {
    private EventoService eventoService;
    private ReservaService reservaService;

    @Override
    public void init() {
        SistemaRepository repository = new MongoSistemaRepository();
        eventoService = new EventoService(repository);
        reservaService = new ReservaService(repository);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cliente cliente = exigirCliente(request, response);
        if (cliente == null) {
            return;
        }
        carregarTela(request, response, cliente, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Cliente cliente = exigirCliente(request, response);
        if (cliente == null) {
            return;
        }

        try {
            String acao = request.getParameter("acao");
            if ("confirmar".equals(acao)) {
                reservaService.confirmarReserva(cliente.getId(), request.getParameter("ingressoId"));
                carregarTela(request, response, cliente, "Reserva confirmada com sucesso.");
                return;
            }
            if ("cancelar".equals(acao)) {
                reservaService.cancelarReserva(cliente.getId(), request.getParameter("ingressoId"));
                carregarTela(request, response, cliente, "Reserva cancelada e disponibilidade atualizada.");
                return;
            }

            reservaService.reservar(
                    cliente.getId(),
                    request.getParameter("eventoId"),
                    TipoIngresso.valueOf(request.getParameter("tipoIngresso"))
            );
            carregarTela(request, response, cliente, "Reserva criada com sucesso. QR Code gerado para o ingresso.");
        } catch (RuntimeException ex) {
            carregarTela(request, response, cliente, "Erro: " + ex.getMessage());
        }
    }

    private Cliente exigirCliente(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cliente cliente = (Cliente) request.getSession().getAttribute("usuario");
        if (cliente == null || cliente.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        return cliente;
    }

    private void carregarTela(HttpServletRequest request, HttpServletResponse response, Cliente cliente, String mensagem)
            throws ServletException, IOException {
        request.setAttribute("cliente", cliente);
        request.setAttribute("eventos", eventoService.listarEventos());
        request.setAttribute("ingressos", reservaService.listarIngressosDoCliente(cliente.getId()));
        request.setAttribute("tipos", TipoIngresso.values());
        request.setAttribute("mensagem", mensagem);
        request.getRequestDispatcher("/WEB-INF/views/cliente.jsp").forward(request, response);
    }
}
