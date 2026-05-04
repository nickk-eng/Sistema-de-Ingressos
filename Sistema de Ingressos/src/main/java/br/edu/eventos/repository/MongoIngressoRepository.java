package br.edu.eventos.repository;

import br.edu.eventos.config.MongoConnection;
import br.edu.eventos.model.EstadoIngresso;
import br.edu.eventos.model.Ingresso;
import br.edu.eventos.model.IngressoFactory;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class MongoIngressoRepository implements IngressoRepository {
    private final MongoCollection<Document> collection;

    public MongoIngressoRepository() {
        this.collection = MongoConnection.getDatabase().getCollection("ingressos");
    }

    @Override
    public Ingresso salvar(Ingresso ingresso) {
        Document document = toDocument(ingresso);
        collection.insertOne(document);
        ingresso.setId(document.getObjectId("_id").toHexString());
        return ingresso;
    }

    @Override
    public List<Ingresso> listar() {
        List<Ingresso> ingressos = new ArrayList<>();
        collection.find().sort(new Document("dataEvento", 1)).forEach(document -> ingressos.add(toIngresso(document)));
        return ingressos;
    }

    @Override
    public Optional<Ingresso> buscarPorId(String id) {
        if (!ObjectId.isValid(id)) {
            return Optional.empty();
        }
        Document document = collection.find(eq("_id", new ObjectId(id))).first();
        return Optional.ofNullable(document).map(this::toIngresso);
    }

    @Override
    public void atualizar(Ingresso ingresso) {
        if (ingresso.getId() == null || !ObjectId.isValid(ingresso.getId())) {
            throw new IllegalArgumentException("Ingresso sem identificador válido.");
        }
        collection.replaceOne(eq("_id", new ObjectId(ingresso.getId())), toDocument(ingresso));
    }

    private Document toDocument(Ingresso ingresso) {
        Document document = new Document("tipo", ingresso.getTipo())
                .append("evento", ingresso.getEvento())
                .append("participante", ingresso.getParticipante())
                .append("dataEvento", ingresso.getDataEvento().toString())
                .append("valorBase", ingresso.getValorBase().toPlainString())
                .append("valorCalculado", ingresso.calcularValor().toPlainString())
                .append("estado", ingresso.getEstado().name())
                .append("detalhes", ingresso.imprimirIngresso());

        if (ingresso.getId() != null && ObjectId.isValid(ingresso.getId())) {
            document.append("_id", new ObjectId(ingresso.getId()));
        }
        return document;
    }

    private Ingresso toIngresso(Document document) {
        Ingresso ingresso = IngressoFactory.criar(
                document.getString("tipo"),
                document.getString("evento"),
                document.getString("participante"),
                LocalDate.parse(document.getString("dataEvento")),
                new BigDecimal(document.getString("valorBase"))
        );
        ingresso.setId(document.getObjectId("_id").toHexString());
        ingresso.setEstado(EstadoIngresso.valueOf(document.getString("estado")));
        return ingresso;
    }
}
