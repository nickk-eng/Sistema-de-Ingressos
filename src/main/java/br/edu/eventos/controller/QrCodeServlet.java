package br.edu.eventos.controller;

import br.edu.eventos.service.QrCodeService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "QrCodeServlet", urlPatterns = {"/qr"})
public class QrCodeServlet extends HttpServlet {
    private final QrCodeService qrCodeService = new QrCodeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String codigo = request.getParameter("codigo");
        if (codigo == null || codigo.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Codigo QR nao informado.");
            return;
        }
        response.setContentType("image/png");
        qrCodeService.escreverPng(codigo, response.getOutputStream());
    }
}
