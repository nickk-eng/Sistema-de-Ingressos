package br.edu.eventos.listener;

import br.edu.eventos.config.MongoConnection;
import br.edu.eventos.repository.MongoSistemaRepository;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            new MongoSistemaRepository().inicializarDados();
        } catch (RuntimeException ex) {
            sce.getServletContext().log("Nao foi possivel inicializar dados no MongoDB.", ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        MongoConnection.close();
    }
}
