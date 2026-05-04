<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Ingressos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
</head>
<body>
<main class="layout">
    <header class="app-header">
        <div>
            <p class="eyebrow">Gestão de eventos</p>
            <h1>Sistema de Ingressos</h1>
            <p class="subtitle">Cadastro, cálculo e acompanhamento de ingressos Normal, VIP e Meia-entrada.</p>
        </div>
        <div class="header-actions">
            <span class="tech-pill">Java Web</span>
            <span class="tech-pill">Maven</span>
            <span class="tech-pill">MongoDB Atlas</span>
        </div>
    </header>

    <section class="summary-grid">
        <div class="summary-tile">
            <span>Total cadastrado</span>
            <strong>${empty ingressos ? 0 : ingressos.size()}</strong>
        </div>
        <div class="summary-tile">
            <span>Tipos disponíveis</span>
            <strong>3</strong>
        </div>
        <div class="summary-tile">
            <span>Arquitetura</span>
            <strong>MVC</strong>
        </div>
    </section>

    <section class="workspace">
        <div class="panel purchase-panel">
            <div>
                <p class="eyebrow">Nova compra</p>
                <h2>Emitir ingresso</h2>
            </div>

            <c:if test="${not empty mensagem}">
                <div class="notice">${mensagem}</div>
            </c:if>
            <c:if test="${not empty erroBanco}">
                <div class="notice danger">${erroBanco}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/ingressos" class="form-grid">
                <label class="full">
                    Evento
                    <input type="text" name="evento" placeholder="Ex.: Congresso de Tecnologia" required>
                </label>
                <label>
                    Tipo
                    <select name="tipo" required>
                        <option value="NORMAL">Normal</option>
                        <option value="VIP">VIP</option>
                        <option value="MEIA">Meia-entrada</option>
                    </select>
                </label>
                <label>
                    Participante
                    <input type="text" name="participante" placeholder="Nome do comprador" required>
                </label>
                <label>
                    Data do evento
                    <input type="date" name="dataEvento" required>
                </label>
                <label>
                    Valor base
                    <input type="number" name="valorBase" min="1" step="0.01" placeholder="100.00" required>
                </label>
                <button type="submit" class="primary-button">Comprar ingresso</button>
            </form>
        </div>

        <aside class="panel rules-panel">
            <p class="eyebrow">Regras de valor</p>
            <h2>Cálculo polimórfico</h2>
            <div class="rule-list">
                <div>
                    <span class="type-chip type-NORMAL">Normal</span>
                    <strong>Valor base</strong>
                </div>
                <div>
                    <span class="type-chip type-VIP">VIP</span>
                    <strong>+50%</strong>
                </div>
                <div>
                    <span class="type-chip type-MEIA">Meia</span>
                    <strong>-50%</strong>
                </div>
            </div>

            <c:if test="${not empty ingressoComprado}">
                <div class="last-ticket">
                    <span>Última compra</span>
                    <strong>R$ ${ingressoComprado.calcularValor()}</strong>
                    <p>${ingressoComprado.evento}</p>
                </div>
            </c:if>
        </aside>
    </section>

    <section class="panel">
        <div class="section-title">
            <div>
                <p class="eyebrow">Consulta</p>
                <h2>Ingressos cadastrados</h2>
            </div>
            <span class="badge">${empty ingressos ? 0 : ingressos.size()} registro(s)</span>
        </div>

        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Tipo</th>
                    <th>Evento</th>
                    <th>Participante</th>
                    <th>Data</th>
                    <th>Valor</th>
                    <th>Estado</th>
                    <th>A&ccedil;&atilde;o</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="ingresso" items="${ingressos}">
                    <tr>
                        <td><span class="type-chip type-${ingresso.tipo}">${ingresso.tipo}</span></td>
                        <td>
                            <strong class="event-name">${ingresso.evento}</strong>
                            <span class="ticket-id">ID ${ingresso.id}</span>
                        </td>
                        <td>${ingresso.participante}</td>
                        <td>${ingresso.dataEvento}</td>
                        <td><strong>R$ ${ingresso.calcularValor()}</strong></td>
                        <td><span class="state-pill state-${ingresso.estado.name()}">${ingresso.estado.descricao}</span></td>
                        <td>
                            <form method="post" action="${pageContext.request.contextPath}/ingressos" class="inline-form">
                                <input type="hidden" name="acao" value="alterarEstado">
                                <input type="hidden" name="id" value="${ingresso.id}">
                                <select name="estado">
                                    <c:forEach var="estado" items="${estados}">
                                        <option value="${estado.name()}" ${estado == ingresso.estado ? 'selected' : ''}>
                                            ${estado.descricao}
                                        </option>
                                    </c:forEach>
                                </select>
                                <button type="submit" class="small-button">Salvar</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty ingressos}">
                    <tr>
                        <td colspan="7" class="empty">Nenhum ingresso cadastrado.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </section>
</main>
</body>
</html>
