package br.com.dio;

import br.com.dio.persistence.migrations.MigrationsStrategy;

import java.sql.SQLException;

import static br.com.dio.persistence.config.ConnectionConfig.getConnection;

public class Main {
    public static void main(String[] args) throws SQLException {
        try (var connection = getConnection()) {
            new MigrationsStrategy(connection).executeMigration();
        }
    }
}
