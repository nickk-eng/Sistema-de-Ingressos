package br.edu.eventos.controller;

import br.edu.eventos.model.Cliente;
import br.edu.eventos.repository.MongoSistemaRepository;
import br.edu.eventos.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private AuthService authService;

    @Override
    public void init() {
        authService = new AuthService(new MongoSistemaRepository());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cliente cliente = (Cliente) request.getSession().getAttribute("usuario");
        if (cliente != null) {
            response.sendRedirect(request.getContextPath() + (cliente.isAdmin() ? "/admin" : "/cliente"));
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            authService.autenticar(request.getParameter("email"), request.getParameter("senha"))
                    .ifPresentOrElse(cliente -> {
                        try {
                            request.getSession().setAttribute("usuario", cliente);
                            response.sendRedirect(request.getContextPath() + (cliente.isAdmin() ? "/admin" : "/cliente"));
                        } catch (IOException ex) {
                            throw new IllegalStateException(ex);
                        }
                    }, () -> {
                        try {
                            request.setAttribute("erro", "E-mail ou senha invalidos.");
                            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                        } catch (ServletException | IOException ex) {
                            throw new IllegalStateException(ex);
                        }
                    });
        } catch (RuntimeException ex) {
            request.setAttribute("erro", "Nao foi possivel conectar ao MongoDB. Configure a variavel MONGODB_URI com a URI do MongoDB Atlas e reinicie o NetBeans.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
