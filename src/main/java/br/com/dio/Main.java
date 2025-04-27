package br.com.dio;

import br.com.dio.persistence.migrations.MigrationsStrategy;
import br.com.dio.ui.MainMenu;

import java.sql.SQLException;

import static br.com.dio.persistence.config.ConnectionConfig.getConnection;

public class Main {
    public static void main(String[] args) throws SQLException {
        try (var connection = getConnection()) {
            new MigrationsStrategy(connection).executeMigration();
        }
         new MainMenu().execute();
    }
}
