package br.edu.eventos.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public final class MongoConnection {
    private static final String DEFAULT_URI = "mongodb+srv://nicholas_db:200604@cluster0.p737yt3.mongodb.net/sistema_ingressos?appName=Cluster0";
    private static final String DATABASE_NAME = "sistema_ingressos";
    private static MongoClient client;

    private MongoConnection() {
    }

    public static synchronized MongoDatabase getDatabase() {
        if (client == null) {
            String uri = System.getenv().getOrDefault("MONGODB_URI", DEFAULT_URI);
            client = MongoClients.create(uri);
        }
        return client.getDatabase(DATABASE_NAME);
    }

    public static synchronized void close() {
        if (client != null) {
            client.close();
            client = null;
        }
    }
}
