<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Sistema de Ingressos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sistema.css">
</head>
<body class="login-page">
<main class="login-shell">
    <section class="login-intro">
        <p class="eyebrow login-eyebrow">Sistema de eventos</p>
        <p>Área restrita para clientes acompanharem ingressos e módulo administrativo para controle de entrada nos eventos.</p>
        <div class="demo-box">
            <strong>Acessos de demonstração</strong>
            <span>Cliente: cliente@teste.com / 123</span>
            <span>Admin: admin@teste.com / admin</span>
        </div>
    </section>

    <section class="panel login-panel">
        <h2>Entrar no sistema</h2>
        <c:if test="${not empty erro}">
            <div class="notice danger">${erro}</div>
        </c:if>
        <form method="post" action="${pageContext.request.contextPath}/login" class="form-grid">
            <label>
                E-mail
                <input type="email" name="email" placeholder="cliente@teste.com" required>
            </label>
            <label>
                Senha
                <input type="password" name="senha" placeholder="Digite sua senha" required>
            </label>
            <button type="submit" class="primary-button">Acessar</button>
        </form>
    </section>
</main>
</body>
</html>
