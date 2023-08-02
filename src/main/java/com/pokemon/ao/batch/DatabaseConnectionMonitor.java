package com.pokemon.ao.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@EnableScheduling
@Slf4j
public class DatabaseConnectionMonitor {

    private final DataSource dataSource;

    @Autowired
    private DatabaseConnectionMonitor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Scheduled(fixedDelayString = "${batch.databaseConnectionMonitor.fixedDelay}")
    public void checkDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            log.info("La connessione al database è attiva.");
        } catch (SQLException e) {
            handleDatabaseDisconnection();
        }
    }

    public void handleDatabaseDisconnection() {
        log.error("Connessione al database persa!");
        System.exit(1);
    }
}
