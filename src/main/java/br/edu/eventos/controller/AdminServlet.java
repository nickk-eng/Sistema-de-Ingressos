package br.edu.eventos.controller;

import br.edu.eventos.model.Cliente;
import br.edu.eventos.model.EstadoIngresso;
import br.edu.eventos.repository.MongoSistemaRepository;
import br.edu.eventos.service.AdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {
    private AdminService adminService;

    @Override
    public void init() {
        adminService = new AdminService(new MongoSistemaRepository());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cliente admin = exigirAdmin(request, response);
        if (admin == null) {
            return;
        }
        carregarTela(request, response, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Cliente admin = exigirAdmin(request, response);
        if (admin == null) {
            return;
        }

        try {
            String mensagem = adminService.validarQrCode(request.getParameter("codigoQr"));
            carregarTela(request, response, mensagem);
        } catch (RuntimeException ex) {
            carregarTela(request, response, "Erro: " + ex.getMessage());
        }
    }

    private Cliente exigirAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cliente cliente = (Cliente) request.getSession().getAttribute("usuario");
        if (cliente == null || !cliente.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        return cliente;
    }

    private void carregarTela(HttpServletRequest request, HttpServletResponse response, String mensagem)
            throws ServletException, IOException {
        request.setAttribute("ingressos", adminService.listarIngressosEmitidos());
        request.setAttribute("reservados", adminService.contarPorEstado(EstadoIngresso.RESERVADO));
        request.setAttribute("confirmados", adminService.contarPorEstado(EstadoIngresso.CONFIRMADO));
        request.setAttribute("utilizados", adminService.contarPorEstado(EstadoIngresso.UTILIZADO));
        request.setAttribute("cancelados", adminService.contarPorEstado(EstadoIngresso.CANCELADO));
        request.setAttribute("disponiveis", adminService.contarDisponiveis());
        request.setAttribute("mensagem", mensagem);
        request.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(request, response);
    }
}
