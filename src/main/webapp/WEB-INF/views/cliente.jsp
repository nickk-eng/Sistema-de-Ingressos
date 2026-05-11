<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Área do Cliente</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sistema.css">
</head>
<body>
<div class="app-shell">
    <aside class="sidebar">
        <div class="brand">
            <span class="brand-mark">EV</span>
            <div>
                <strong>Eventos Web</strong>
                <small>Área do cliente</small>
            </div>
        </div>
        <nav class="side-nav">
            <a href="#catalogo">Catálogo</a>
            <a href="#meus-ingressos">Meus ingressos</a>
            <a href="${pageContext.request.contextPath}/logout">Sair</a>
        </nav>
    </aside>

    <main class="content">
        <header class="topbar">
            <div>
                <p class="eyebrow">Bem-vindo(a), ${cliente.nome}</p>
                <h1>Catálogo de eventos</h1>
            </div>
        </header>

        <c:if test="${not empty mensagem}">
            <div class="notice">${mensagem}</div>
        </c:if>

        <section id="catalogo" class="events-grid">
            <c:forEach var="evento" items="${eventos}">
                <article class="event-card">
                    <div class="event-card-main">
                        <span class="status-pill">${evento.ingressosDisponiveis} disponíveis</span>
                        <h2>${evento.nome}</h2>
                        <p>${evento.descricao}</p>
                        <dl class="event-meta">
                            <div><dt>Data e horário</dt><dd>${evento.dataHora}</dd></div>
                            <div><dt>Local</dt><dd>${evento.local}</dd></div>
                            <div><dt>Valor base</dt><dd>R$ ${evento.valorIngresso}</dd></div>
                        </dl>
                    </div>

                    <form method="post" action="${pageContext.request.contextPath}/cliente" class="reserve-form">
                        <input type="hidden" name="acao" value="reservar">
                        <input type="hidden" name="eventoId" value="${evento.id}">
                        <label>
                            Tipo de ingresso
                            <select name="tipoIngresso">
                                <c:forEach var="tipo" items="${tipos}">
                                    <option value="${tipo.name()}">${tipo.descricao}</option>
                                </c:forEach>
                            </select>
                        </label>
                        <button type="submit" class="primary-button" ${evento.ingressosDisponiveis <= 0 ? 'disabled' : ''}>
                            Reservar ingresso
                        </button>
                    </form>
                </article>
            </c:forEach>
        </section>

        <section id="meus-ingressos" class="panel">
            <div class="panel-header">
                <div>
                    <p class="eyebrow">Área do cliente</p>
                    <h2>Meus ingressos e QR Codes</h2>
                </div>
                <span class="badge">${empty ingressos ? 0 : ingressos.size()} registro(s)</span>
            </div>

            <div class="ticket-grid">
                <c:forEach var="item" items="${ingressos}">
                    <article class="ticket-card">
                        <div>
                            <span class="type-chip">${item.ingresso.tipo.descricao}</span>
                            <h3>${item.evento.nome}</h3>
                            <p>${item.evento.local} - ${item.evento.dataHora}</p>
                            <dl class="ticket-data">
                                <div><dt>Status</dt><dd>${item.ingresso.estado.descricao}</dd></div>
                                <div><dt>Valor</dt><dd>R$ ${item.ingresso.calcularValor()}</dd></div>
                                <div><dt>Código</dt><dd>${item.ingresso.codigoQr}</dd></div>
                            </dl>
                            <div class="ticket-actions">
                                <c:if test="${item.ingresso.estado.name() == 'RESERVADO'}">
                                    <form method="post" action="${pageContext.request.contextPath}/cliente">
                                        <input type="hidden" name="acao" value="confirmar">
                                        <input type="hidden" name="ingressoId" value="${item.ingresso.id}">
                                        <button type="submit" class="small-button">Confirmar</button>
                                    </form>
                                </c:if>
                                <c:if test="${item.ingresso.estado.name() != 'UTILIZADO' && item.ingresso.estado.name() != 'CANCELADO'}">
                                    <form method="post" action="${pageContext.request.contextPath}/cliente">
                                        <input type="hidden" name="acao" value="cancelar">
                                        <input type="hidden" name="ingressoId" value="${item.ingresso.id}">
                                        <button type="submit" class="small-button secondary-button">Cancelar</button>
                                    </form>
                                </c:if>
                            </div>
                        </div>
                        <img class="qr-image" src="${pageContext.request.contextPath}/qr?codigo=${item.ingresso.codigoQr}" alt="QR Code do ingresso">
                    </article>
                </c:forEach>

                <c:if test="${empty ingressos}">
                    <p class="empty-state">Você ainda não possui ingressos reservados.</p>
                </c:if>
            </div>
        </section>
    </main>
</div>
</body>
</html>
