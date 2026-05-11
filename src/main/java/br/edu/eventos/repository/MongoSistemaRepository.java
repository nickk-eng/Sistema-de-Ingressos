package br.edu.eventos.repository;

import br.edu.eventos.config.MongoConnection;
import br.edu.eventos.model.Cliente;
import br.edu.eventos.model.EstadoIngresso;
import br.edu.eventos.model.Evento;
import br.edu.eventos.model.Ingresso;
import br.edu.eventos.model.IngressoFactory;
import br.edu.eventos.model.PerfilUsuario;
import br.edu.eventos.model.Reserva;
import br.edu.eventos.model.TipoIngresso;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Filters.ne;

public class MongoSistemaRepository implements SistemaRepository {
    private final MongoCollection<Document> clientes = MongoConnection.getDatabase().getCollection("clientes");
    private final MongoCollection<Document> eventos = MongoConnection.getDatabase().getCollection("eventos");
    private final MongoCollection<Document> ingressos = MongoConnection.getDatabase().getCollection("ingressos");
    private final MongoCollection<Document> reservas = MongoConnection.getDatabase().getCollection("reservas");

    @Override
    public void inicializarDados() {
        if (clientes.countDocuments() == 0) {
            inserirCliente(new Cliente("Cliente Teste", "cliente@teste.com", "123", PerfilUsuario.CLIENTE));
            inserirCliente(new Cliente("Administrador", "admin@teste.com", "admin", PerfilUsuario.ADMIN));
        }

        if (eventos.countDocuments() == 0) {
            salvarEvento(new Evento("Semana de Tecnologia", "Palestras, oficinas e networking sobre desenvolvimento de software.",
                    LocalDateTime.of(2026, 6, 20, 19, 0), "Auditório Central", 120, 120, new BigDecimal("80.00")));
            salvarEvento(new Evento("Festival Universitário", "Evento cultural com música, apresentações e praça de alimentação.",
                    LocalDateTime.of(2026, 7, 5, 18, 30), "Ginásio da Faculdade", 200, 200, new BigDecimal("60.00")));
            salvarEvento(new Evento("Workshop de Java Web", "Encontro prático sobre Servlets, JSP, Maven e arquitetura em camadas.",
                    LocalDateTime.of(2026, 8, 12, 14, 0), "Laboratório 3", 40, 40, new BigDecimal("100.00")));
        }
    }

    @Override
    public Optional<Cliente> buscarClientePorEmail(String email) {
        return Optional.ofNullable(clientes.find(eq("email", email)).first()).map(this::toCliente);
    }

    @Override
    public Optional<Cliente> buscarClientePorId(String id) {
        return findById(clientes, id).map(this::toCliente);
    }

    @Override
    public List<Evento> listarEventos() {
        List<Evento> lista = new ArrayList<>();
        eventos.find().sort(new Document("dataHora", 1)).forEach(document -> lista.add(toEvento(document)));
        return lista;
    }

    @Override
    public Optional<Evento> buscarEventoPorId(String id) {
        return findById(eventos, id).map(this::toEvento);
    }

    @Override
    public Evento salvarEvento(Evento evento) {
        Document document = toDocument(evento);
        if (evento.getId() == null) {
            eventos.insertOne(document);
            evento.setId(document.getObjectId("_id").toHexString());
            return evento;
        }
        eventos.replaceOne(eq("_id", new ObjectId(evento.getId())), document);
        return evento;
    }

    @Override
    public Ingresso salvarIngresso(Ingresso ingresso) {
        Document document = toDocument(ingresso);
        ingressos.insertOne(document);
        ingresso.setId(document.getObjectId("_id").toHexString());
        return ingresso;
    }

    @Override
    public void atualizarIngresso(Ingresso ingresso) {
        ingressos.replaceOne(eq("_id", new ObjectId(ingresso.getId())), toDocument(ingresso));
    }

    @Override
    public Optional<Ingresso> buscarIngressoPorId(String id) {
        return findById(ingressos, id).map(this::toIngresso);
    }

    @Override
    public Optional<Ingresso> buscarIngressoPorCodigoQr(String codigoQr) {
        return Optional.ofNullable(ingressos.find(eq("codigoQr", codigoQr)).first()).map(this::toIngresso);
    }

    @Override
    public List<Ingresso> listarIngressos() {
        List<Ingresso> lista = new ArrayList<>();
        ingressos.find(exists("codigoQr", true))
                .sort(new Document("emitidoEm", -1))
                .forEach(document -> lista.add(toIngresso(document)));
        return lista;
    }

    @Override
    public Reserva salvarReserva(Reserva reserva) {
        Document document = toDocument(reserva);
        reservas.insertOne(document);
        reserva.setId(document.getObjectId("_id").toHexString());
        return reserva;
    }

    @Override
    public void atualizarReserva(Reserva reserva) {
        reservas.replaceOne(eq("_id", new ObjectId(reserva.getId())), toDocument(reserva));
    }

    @Override
    public Optional<Reserva> buscarReservaPorIngressoId(String ingressoId) {
        return Optional.ofNullable(reservas.find(eq("ingressoId", ingressoId)).first()).map(this::toReserva);
    }

    @Override
    public boolean existeReservaAtiva(String clienteId, String eventoId) {
        return reservas.find(and(
                eq("clienteId", clienteId),
                eq("eventoId", eventoId),
                ne("status", EstadoIngresso.CANCELADO.name())
        )).first() != null;
    }

    @Override
    public List<Reserva> listarReservasPorCliente(String clienteId) {
        List<Reserva> lista = new ArrayList<>();
        reservas.find(eq("clienteId", clienteId)).sort(new Document("criadaEm", -1)).forEach(document -> lista.add(toReserva(document)));
        return lista;
    }

    @Override
    public List<Reserva> listarReservas() {
        List<Reserva> lista = new ArrayList<>();
        reservas.find().sort(new Document("criadaEm", -1)).forEach(document -> lista.add(toReserva(document)));
        return lista;
    }

    private Cliente inserirCliente(Cliente cliente) {
        Document document = new Document("nome", cliente.getNome())
                .append("email", cliente.getEmail())
                .append("senha", cliente.getSenha())
                .append("perfil", cliente.getPerfil().name());
        clientes.insertOne(document);
        cliente.setId(document.getObjectId("_id").toHexString());
        return cliente;
    }

    private Optional<Document> findById(MongoCollection<Document> collection, String id) {
        if (id == null || !ObjectId.isValid(id)) {
            return Optional.empty();
        }
        return Optional.ofNullable(collection.find(eq("_id", new ObjectId(id))).first());
    }

    private Document toDocument(Evento evento) {
        Document document = new Document("nome", evento.getNome())
                .append("descricao", evento.getDescricao())
                .append("dataHora", evento.getDataHora().toString())
                .append("local", evento.getLocal())
                .append("totalIngressos", evento.getTotalIngressos())
                .append("ingressosDisponiveis", evento.getIngressosDisponiveis())
                .append("valorIngresso", evento.getValorIngresso().toPlainString());
        if (evento.getId() != null) {
            document.append("_id", new ObjectId(evento.getId()));
        }
        return document;
    }

    private Document toDocument(Ingresso ingresso) {
        Document document = new Document("clienteId", ingresso.getClienteId())
                .append("eventoId", ingresso.getEventoId())
                .append("codigoQr", ingresso.getCodigoQr())
                .append("estado", ingresso.getEstado().name())
                .append("emitidoEm", ingresso.getEmitidoEm().toString())
                .append("valorBase", ingresso.getValorBase().toPlainString())
                .append("valorFinal", ingresso.calcularValor().toPlainString())
                .append("tipo", ingresso.getTipo().name());
        if (ingresso.getId() != null) {
            document.append("_id", new ObjectId(ingresso.getId()));
        }
        return document;
    }

    private Document toDocument(Reserva reserva) {
        Document document = new Document("clienteId", reserva.getClienteId())
                .append("eventoId", reserva.getEventoId())
                .append("ingressoId", reserva.getIngressoId())
                .append("status", reserva.getStatus().name())
                .append("criadaEm", reserva.getCriadaEm().toString());
        if (reserva.getId() != null) {
            document.append("_id", new ObjectId(reserva.getId()));
        }
        return document;
    }

    private Cliente toCliente(Document document) {
        Cliente cliente = new Cliente(
                document.getString("nome"),
                document.getString("email"),
                document.getString("senha"),
                PerfilUsuario.valueOf(document.getString("perfil"))
        );
        cliente.setId(document.getObjectId("_id").toHexString());
        return cliente;
    }

    private Evento toEvento(Document document) {
        Evento evento = new Evento(
                document.getString("nome"),
                document.getString("descricao"),
                LocalDateTime.parse(document.getString("dataHora")),
                document.getString("local"),
                document.getInteger("totalIngressos"),
                document.getInteger("ingressosDisponiveis"),
                new BigDecimal(document.getString("valorIngresso"))
        );
        evento.setId(document.getObjectId("_id").toHexString());
        return evento;
    }

    private Ingresso toIngresso(Document document) {
        Ingresso ingresso = IngressoFactory.criar(
                TipoIngresso.valueOf(stringOrDefault(document, "tipo", TipoIngresso.NORMAL.name())),
                stringOrDefault(document, "clienteId", ""),
                stringOrDefault(document, "eventoId", ""),
                stringOrDefault(document, "codigoQr", document.getObjectId("_id").toHexString()),
                new BigDecimal(stringOrDefault(document, "valorBase", "0.00"))
        );
        ingresso.setId(document.getObjectId("_id").toHexString());
        ingresso.setEstado(toEstadoIngresso(document.getString("estado")));
        ingresso.setEmitidoEm(LocalDateTime.parse(stringOrDefault(document, "emitidoEm", LocalDateTime.now().toString())));
        return ingresso;
    }

    private String stringOrDefault(Document document, String campo, String valorPadrao) {
        String valor = document.getString(campo);
        return valor == null ? valorPadrao : valor;
    }

    private EstadoIngresso toEstadoIngresso(String estado) {
        if (estado == null) {
            return EstadoIngresso.RESERVADO;
        }
        return switch (estado) {
            case "PAGO" -> EstadoIngresso.CONFIRMADO;
            case "USADO" -> EstadoIngresso.UTILIZADO;
            default -> EstadoIngresso.valueOf(estado);
        };
    }

    private Reserva toReserva(Document document) {
        Reserva reserva = new Reserva(
                document.getString("clienteId"),
                document.getString("eventoId"),
                document.getString("ingressoId"),
                EstadoIngresso.valueOf(document.getString("status")),
                LocalDateTime.parse(document.getString("criadaEm"))
        );
        reserva.setId(document.getObjectId("_id").toHexString());
        return reserva;
    }
}
