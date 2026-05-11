<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Administração - Sistema de Ingressos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sistema.css">
</head>
<body>
<div class="app-shell">
    <aside class="sidebar admin-sidebar">
        <div class="brand">
            <span class="brand-mark">AD</span>
            <div>
                <strong>Administração</strong>
                <small>Controle de entrada</small>
            </div>
        </div>
        <nav class="side-nav">
            <a href="#validacao">Validar QR</a>
            <a href="#relatorios">Relatórios</a>
            <a href="${pageContext.request.contextPath}/logout">Sair</a>
        </nav>
    </aside>

    <main class="content">
        <header class="topbar">
            <div>
                <p class="eyebrow">Módulo administrativo</p>
                <h1>Controle de ingressos por QR Code</h1>
            </div>
        </header>

        <c:if test="${not empty mensagem}">
            <div class="notice">${mensagem}</div>
        </c:if>

        <section class="metrics-grid" id="relatorios">
            <article class="metric-card"><span>Disponíveis</span><strong>${disponiveis}</strong></article>
            <article class="metric-card"><span>Reservados</span><strong>${reservados}</strong></article>
            <article class="metric-card"><span>Confirmados</span><strong>${confirmados}</strong></article>
            <article class="metric-card highlight"><span>Utilizados</span><strong>${utilizados}</strong></article>
            <article class="metric-card danger-card"><span>Cancelados</span><strong>${cancelados}</strong></article>
        </section>

        <section class="admin-layout">
            <div class="panel" id="validacao">
                <div class="panel-header">
                    <h2>Validar ingresso</h2>
                </div>
                <form method="post" action="${pageContext.request.contextPath}/admin" class="form-grid">
                    <label>
                        Código do QR Code
                        <input type="text" name="codigoQr" placeholder="Cole ou digite o código do ingresso" required>
                    </label>
                    <button type="submit" class="primary-button">Validar e marcar como utilizado</button>
                </form>
            </div>

            <div class="panel wide">
                <div class="panel-header">
                    <h2>Ingressos emitidos</h2>
                    <span class="badge">${empty ingressos ? 0 : ingressos.size()} registro(s)</span>
                </div>
                <div class="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>Evento</th>
                            <th>Cliente</th>
                            <th>Tipo</th>
                            <th>Status</th>
                            <th>QR Code</th>
                            <th>Ação</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${ingressos}">
                            <tr>
                                <td><strong>${item.evento.nome}</strong><br><span class="muted">${item.evento.local}</span></td>
                                <td>${item.cliente.nome}<br><span class="muted">${item.cliente.email}</span></td>
                                <td><span class="type-chip">${item.ingresso.tipo.descricao}</span></td>
                                <td><span class="status-pill">${item.ingresso.estado.descricao}</span></td>
                                <td>
                                    <img class="qr-thumb" src="${pageContext.request.contextPath}/qr?codigo=${item.ingresso.codigoQr}" alt="QR Code">
                                    <span class="code-text">${item.ingresso.codigoQr}</span>
                                </td>
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/admin">
                                        <input type="hidden" name="codigoQr" value="${item.ingresso.codigoQr}">
                                        <button type="submit" class="small-button">Validar</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty ingressos}">
                            <tr><td colspan="6" class="empty-state">Nenhum ingresso emitido.</td></tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </main>
</div>
</body>
</html>
