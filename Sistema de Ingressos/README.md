# Sistema de Ingressos para Eventos

Projeto Java Web desenvolvido com Maven para gerenciar ingressos de eventos. O sistema permite cadastrar, consultar, calcular valores e alterar estados de ingressos dos tipos Normal, VIP e Meia-entrada, aplicando os conceitos de herança, classe abstrata e polimorfismo.

## Objetivo do projeto

O objetivo é demonstrar, em uma aplicação Java Web, como diferentes tipos de ingresso podem compartilhar uma estrutura comum e, ao mesmo tempo, possuir comportamentos específicos. Para isso, foi criada a classe abstrata `Ingresso`, que é herdada pelas classes `IngressoNormal`, `IngressoVIP` e `IngressoMeia`.

## Tecnologias utilizadas

- Java 17 ou superior
- Maven
- NetBeans
- Jakarta Servlet
- JSP
- JSTL
- MongoDB Atlas
- Apache Tomcat 10 ou superior
- JUnit 5
- PlantUML

## Funcionalidades

- Cadastro de ingresso para evento.
- Seleção do tipo de ingresso: Normal, VIP ou Meia-entrada.
- Cálculo automático do valor final conforme o tipo escolhido.
- Listagem dos ingressos cadastrados.
- Alteração do estado do ingresso.
- Persistência dos dados no MongoDB.
- Interface Web com JSP e CSS.
- Organização em arquitetura multicamadas.

## Regras de negócio

O sistema possui três tipos de ingresso:

| Tipo | Regra de cálculo |
| --- | --- |
| Normal | Mantém o valor base informado |
| VIP | Aplica acréscimo de 50% sobre o valor base |
| Meia-entrada | Aplica desconto de 50% sobre o valor base |

Exemplo:

```text
Valor base: R$ 100,00
Ingresso Normal: R$ 100,00
Ingresso VIP: R$ 150,00
Ingresso Meia-entrada: R$ 50,00
```

## Estrutura do projeto

```text
sistema-ingressos-eventos/
├── docs/
│   ├── diagrama-classes.puml
│   ├── diagrama-classes.svg
│   ├── diagrama-sequencia-comprar-ingresso.puml
│   ├── diagrama-sequencia.svg
│   ├── diagrama-estados-ingresso.puml
│   ├── diagrama-estados.svg
│   └── relatorio-abnt.md
├── src/
│   ├── main/
│   │   ├── java/br/edu/eventos/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── listener/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── webapp/
│   │       ├── assets/css/
│   │       ├── WEB-INF/views/
│   │       └── index.jsp
│   └── test/
├── pom.xml
└── README.md
```

## Arquitetura utilizada

O projeto foi organizado em camadas:

| Camada | Responsabilidade |
| --- | --- |
| `model` | Classes de domínio, herança, polimorfismo e estados do ingresso |
| `repository` | Comunicação com o MongoDB |
| `service` | Regras de negócio da compra e alteração de estado |
| `controller` | Servlet responsável por receber requisições HTTP |
| `webapp` | Páginas JSP e arquivos CSS |
| `config` | Configuração da conexão com o banco |

## Classes principais

### `Ingresso`

Classe abstrata que representa os dados comuns dos ingressos:

- id
- evento
- participante
- data do evento
- valor base
- estado

Ela possui métodos abstratos:

```java
public abstract BigDecimal calcularValor();
public abstract String imprimirIngresso();
public abstract String getTipo();
```

### `IngressoNormal`

Classe concreta que mantém o valor base do ingresso.

### `IngressoVIP`

Classe concreta que aplica acréscimo de 50% ao valor base.

### `IngressoMeia`

Classe concreta que aplica desconto de 50% ao valor base.

### `IngressoFactory`

Classe responsável por criar o objeto correto conforme o tipo selecionado pelo usuário.

### `IngressoService`

Camada que centraliza as regras de negócio.

### `MongoIngressoRepository`

Classe responsável por salvar, listar, buscar e atualizar ingressos no MongoDB.

## Diagramas UML

Os diagramas estão disponíveis na pasta `docs`.

### Diagrama de Classes

Arquivo:

```text
docs/diagrama-classes.svg
```

Também existe a versão editável em PlantUML:

```text
docs/diagrama-classes.puml
```

### Diagrama de Sequência

Arquivo:

```text
docs/diagrama-sequencia.svg
```

Também existe a versão editável em PlantUML:

```text
docs/diagrama-sequencia-comprar-ingresso.puml
```

### Diagrama de Estados

Arquivo:

```text
docs/diagrama-estados.svg
```

Também existe a versão editável em PlantUML:

```text
docs/diagrama-estados-ingresso.puml
```

## Pré-requisitos

Antes de rodar o projeto, instale:

1. JDK 17 ou superior.
2. NetBeans.
3. Maven.
4. Apache Tomcat 10 ou superior.
5. Conta no MongoDB Atlas.

## Como criar o banco no MongoDB Atlas

1. Acesse o site do MongoDB Atlas.
2. Crie uma conta ou faça login.
3. Crie um novo projeto.
4. Crie um cluster gratuito.
5. Vá em `Database Access`.
6. Clique em `Add New Database User`.
7. Crie um usuário e uma senha.
8. Vá em `Network Access`.
9. Clique em `Add IP Address`.
10. Para testes acadêmicos, pode usar `Allow Access from Anywhere`.
11. Vá em `Database`.
12. Clique em `Connect`.
13. Escolha `Drivers`.
14. Copie a URI de conexão.

O sistema usa:

```text
Banco: sistema_ingressos
Collection: ingressos
```

Não é obrigatório criar a collection manualmente. O MongoDB cria automaticamente quando o primeiro ingresso for salvo.

## Como configurar a URI do MongoDB

A URI deve ficar parecida com esta:

```text
mongodb+srv://USUARIO:SENHA@cluster0.xxxxx.mongodb.net/sistema_ingressos?retryWrites=true&w=majority
```

Substitua:

- `USUARIO` pelo usuário criado no MongoDB Atlas.
- `SENHA` pela senha desse usuário.
- `cluster0.xxxxx.mongodb.net` pelo endereço do seu cluster.

No Windows, configure a variável de ambiente pelo PowerShell:

```powershell
setx MONGODB_URI "mongodb+srv://USUARIO:SENHA@cluster0.xxxxx.mongodb.net/sistema_ingressos?retryWrites=true&w=majority"
```

Depois de executar o comando, feche e abra o NetBeans novamente.

Se a variável `MONGODB_URI` não estiver configurada, o sistema tenta usar:

```text
mongodb://localhost:27017
```

## Como configurar o Tomcat no NetBeans

1. Baixe o Apache Tomcat 10 em:

```text
https://tomcat.apache.org/download-10.cgi
```

2. Baixe a versão `.zip`.
3. Extraia o arquivo para uma pasta simples, por exemplo:

```text
C:\tomcat10
```

4. Abra o NetBeans.
5. Vá em `Tools > Servers`.
6. Clique em `Add Server`.
7. Escolha `Apache Tomcat or TomEE`.
8. Em `Server Location`, selecione a pasta do Tomcat.
9. Informe um usuário e senha, por exemplo:

```text
Username: admin
Password: admin
```

10. Marque `Create user if it does not exist`.
11. Clique em `Finish`.

## Como abrir o projeto no NetBeans

1. Abra o NetBeans.
2. Clique em `File > Open Project`.
3. Selecione a pasta do projeto.
4. Aguarde o NetBeans carregar as dependências Maven.
5. Clique com o botão direito no projeto.
6. Vá em `Properties`.
7. Em `Run`, selecione o servidor Tomcat configurado.
8. Clique em `OK`.

## Como executar o sistema

No NetBeans:

1. Clique com o botão direito no projeto.
2. Selecione `Run`.

Ou pressione:

```text
F6
```

O navegador deve abrir uma URL parecida com:

```text
http://localhost:8080/sistema-ingressos-eventos/
```

## Como usar o sistema

1. Acesse a página inicial.
2. Preencha o nome do evento.
3. Escolha o tipo de ingresso.
4. Informe o nome do participante.
5. Informe a data do evento.
6. Informe o valor base.
7. Clique em `Comprar ingresso`.
8. Veja o ingresso cadastrado na tabela.
9. Se desejar, altere o estado do ingresso e clique em `Salvar`.

## Estados do ingresso

O ingresso pode possuir os seguintes estados:

| Estado | Significado |
| --- | --- |
| DISPONIVEL | Ingresso disponível para compra |
| RESERVADO | Ingresso reservado |
| PAGO | Compra confirmada |
| CANCELADO | Compra ou reserva cancelada |
| USADO | Ingresso validado no evento |

## Como rodar os testes

Com Maven instalado, execute:

```bash
mvn test
```

O teste principal valida o comportamento polimórfico dos três tipos de ingresso.

## Possíveis erros e soluções

### Erro: nenhum servidor selecionado

Configure o Tomcat no NetBeans em:

```text
Tools > Servers > Add Server
```

### Erro de conexão com MongoDB

Verifique:

- Se a URI está correta.
- Se o usuário e a senha do banco estão corretos.
- Se o IP foi liberado no MongoDB Atlas.
- Se o cluster está ativo.
- Se o NetBeans foi reiniciado depois do `setx MONGODB_URI`.

### Erro de dependências Maven

Verifique:

- Se o Maven está instalado.
- Se o NetBeans conseguiu baixar as dependências.
- Se existe conexão com a internet.

## Como entregar no GitHub

1. Crie um repositório no GitHub.
2. No terminal, dentro da pasta do projeto, execute:

```bash
git init
git add .
git commit -m "Sistema de ingressos para eventos"
git branch -M main
git remote add origin URL_DO_REPOSITORIO
git push -u origin main
```

Substitua `URL_DO_REPOSITORIO` pela URL do seu repositório.

## Autor

Preencha com seus dados:

```text
Nome: Seu nome
Curso: Seu curso
Disciplina: Nome da disciplina
Instituição: Nome da instituição
```
