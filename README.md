# Sistema de Ingressos para Eventos

Aplicação Java Web desenvolvida com Maven e NetBeans para gerenciamento de eventos, login de clientes, reserva de ingressos e validação administrativa por QR Code.

O sistema foi implementado em MVC, com Servlets como controllers, JSP como views e classes Java como model. A persistência é feita com MongoDB Atlas.

## Funcionalidades

- Login de cliente.
- Login de administrador.
- Redirecionamento automático da raiz para a tela de login.
- Catálogo de eventos disponíveis.
- Reserva de ingresso pelo cliente autenticado.
- Confirmação de reserva.
- Cancelamento de reserva.
- Bloqueio de reserva duplicada para o mesmo cliente e evento.
- Controle de quantidade de ingressos disponíveis.
- Geração de identificador único para cada ingresso.
- Geração de QR Code para cada ingresso.
- Área administrativa para visualizar ingressos emitidos.
- Validação de ingresso por QR Code.
- Bloqueio de uso duplicado do mesmo ingresso.
- Relatórios simples de ingressos disponíveis, reservados, confirmados, utilizados e cancelados.

## Tecnologias

- Java 17
- Maven
- NetBeans
- Apache Tomcat 10
- Jakarta Servlet 6
- JSP
- JSTL
- MongoDB Atlas
- MongoDB Java Driver
- ZXing para QR Code
- HTML e CSS
- JUnit 5

## Arquitetura MVC

O projeto está organizado no padrão MVC:

| Camada | Pasta | Responsabilidade |
| --- | --- | --- |
| Model | `src/main/java/br/edu/eventos/model` | Classes de domínio, como Cliente, Evento, Ingresso e Reserva |
| View | `src/main/webapp/WEB-INF/views` | Telas JSP do cliente, login e administrador |
| Controller | `src/main/java/br/edu/eventos/controller` | Servlets que recebem requisições e direcionam o fluxo |

Além do MVC, foram usadas camadas auxiliares:

| Camada | Pasta | Responsabilidade |
| --- | --- | --- |
| Service | `src/main/java/br/edu/eventos/service` | Regras de negócio |
| Repository | `src/main/java/br/edu/eventos/repository` | Comunicação com o MongoDB |
| Config | `src/main/java/br/edu/eventos/config` | Configuração da conexão |

## Estrutura de pastas

```text
src/main/java/br/edu/eventos
  config
  controller
  listener
  model
  repository
  service

src/main/webapp
  assets/css
  WEB-INF/views
  index.jsp

docs
  arquitetura-mvc.md
  diagrama-caso-uso.svg
  diagrama-componentes.svg
  relatorio-abnt.md
```

## Classes principais

| Classe | Função |
| --- | --- |
| `Cliente` | Representa cliente ou administrador |
| `Evento` | Representa evento do catálogo |
| `Ingresso` | Classe abstrata dos ingressos |
| `IngressoNormal` | Ingresso com valor base |
| `IngressoVIP` | Ingresso com acréscimo de 50% |
| `IngressoMeia` | Ingresso com desconto de 50% |
| `Reserva` | Relaciona cliente, evento e ingresso |
| `AuthService` | Autenticação |
| `ReservaService` | Reserva, confirmação e cancelamento |
| `AdminService` | Validação de QR Code e relatórios |
| `MongoSistemaRepository` | Persistência no MongoDB |

## Banco de dados

Banco usado:

```text
sistema_ingressos
```

Collections:

```text
clientes
eventos
ingressos
reservas
```

Na primeira execução, o sistema cria dados iniciais:

```text
Cliente: cliente@teste.com / 123
Admin: admin@teste.com / admin
```

Também são criados eventos de exemplo.

## Configurando o MongoDB Atlas

1. Acesse o MongoDB Atlas.
2. Crie uma conta ou faça login.
3. Crie um projeto.
4. Crie um cluster gratuito.
5. Vá em `Database Access`.
6. Crie um usuário de banco de dados.
7. Vá em `Network Access`.
8. Libere seu IP.
9. Para teste acadêmico, pode liberar `0.0.0.0/0`.
10. Vá em `Database > Connect > Drivers`.
11. Copie a URI de conexão.

A URI fica parecida com:

```text
mongodb+srv://USUARIO:SENHA@cluster0.xxxxx.mongodb.net/sistema_ingressos?retryWrites=true&w=majority
```

## Configurando a variável MONGODB_URI

No PowerShell:

```powershell
setx MONGODB_URI "mongodb+srv://USUARIO:SENHA@cluster0.xxxxx.mongodb.net/sistema_ingressos?retryWrites=true&w=majority"
```

Depois:

1. Feche o NetBeans.
2. Abra o NetBeans novamente.
3. Rode `Clean and Build`.
4. Rode `Run Project`.

Observação: se a senha tiver caracteres especiais como `@`, `#`, `%`, `/` ou espaço, prefira criar uma senha simples no Atlas, por exemplo `Senha12345`.

## Alternativa rápida para teste

Também é possível colocar a URI diretamente em:

```text
src/main/java/br/edu/eventos/config/MongoConnection.java
```

Troque:

```java
private static final String DEFAULT_URI = "mongodb://localhost:27017";
```

por:

```java
private static final String DEFAULT_URI = "mongodb+srv://USUARIO:SENHA@cluster0.xxxxx.mongodb.net/sistema_ingressos?retryWrites=true&w=majority";
```

Para GitHub, o ideal é usar `MONGODB_URI`, para não enviar senha no código.

## Configurando o Tomcat no NetBeans

1. Baixe o Tomcat 10 em `https://tomcat.apache.org/download-10.cgi`.
2. Baixe a versão `.zip`.
3. Extraia para uma pasta simples, por exemplo:

```text
C:\tomcat10
```

4. No NetBeans, vá em `Tools > Servers`.
5. Clique em `Add Server`.
6. Escolha `Apache Tomcat or TomEE`.
7. Em `Server Location`, selecione a pasta do Tomcat.
8. Use usuário e senha:

```text
admin / admin
```

9. Marque `Create user if it does not exist`.
10. Finalize.

## Abrindo o projeto

1. Abra o NetBeans.
2. Vá em `File > Open Project`.
3. Selecione a pasta do projeto.
4. Aguarde o Maven baixar as dependências.
5. Clique com o botão direito no projeto.
6. Vá em `Properties > Run`.
7. Escolha o Tomcat configurado.

## Executando

No NetBeans:

```text
Clean and Build
Run Project
```

URL inicial:

```text
http://localhost:8080/sistema-ingressos-eventos/
```

O sistema redireciona para:

```text
http://localhost:8080/sistema-ingressos-eventos/login
```

## Fluxo para demonstrar no vídeo

1. Abra o sistema.
2. Faça login como cliente:

```text
cliente@teste.com
123
```

3. Mostre o catálogo de eventos.
4. Reserve um ingresso.
5. Confirme a reserva.
6. Mostre o QR Code.
7. Tente reservar novamente o mesmo evento para mostrar o bloqueio.
8. Saia do sistema.
9. Faça login como administrador:

```text
admin@teste.com
admin
```

10. Mostre os relatórios.
11. Valide o ingresso.
12. Tente validar o mesmo ingresso novamente para mostrar o bloqueio de uso duplicado.

## Testes

Com Maven:

```bash
mvn test
```

O teste valida o cálculo polimórfico dos ingressos Normal, VIP e Meia-entrada.

## Documentação

Arquivos principais:

```text
docs/relatorio-abnt.md
docs/arquitetura-mvc.md
docs/diagrama-caso-uso.svg
docs/diagrama-componentes.svg
```

## Problemas comuns

### Erro de conexão com MongoDB

Verifique:

- Se a URI está correta.
- Se o usuário e a senha do Atlas estão corretos.
- Se o IP está liberado em `Network Access`.
- Se o cluster está ativo.
- Se o NetBeans foi reiniciado depois do `setx MONGODB_URI`.

### Aparece tela antiga do sistema

Faça:

```text
Clean and Build
Run Project
```

Se continuar, remova a aplicação antiga em:

```text
Services > Servers > Tomcat > Web Applications
```

### Erro no admin por dados antigos

Se o banco tiver documentos antigos da primeira versão, apague a collection `ingressos` ou o banco `sistema_ingressos` no MongoDB Atlas e rode o projeto novamente.

## Autor

Preencher antes da entrega:

```text
Nome: Nicholas Moura
Curso: Sistemas de Informação
Disciplina: POO
```
