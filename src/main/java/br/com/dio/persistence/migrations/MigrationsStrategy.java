package br.com.dio.persistence.migrations;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;

import static br.com.dio.persistence.config.ConnectionConfig.getConnection;

public record MigrationsStrategy(Connection connection) {

    public void executeMigration() {
        var originalOut = System.out;
        var originalErr = System.err;

        try {
            try (var fos = new FileOutputStream("liquibase.log")) {
                System.setOut(new PrintStream(fos));
                System.setErr(new PrintStream(fos));
                try (var connection = getConnection();

                     Database database = DatabaseFactory.getInstance()
                             .findCorrectDatabaseImplementation(new JdbcConnection(connection));

                ) {

                    var liquibase = new Liquibase("/db/changelog/changelog-master.yml",
                            new ClassLoaderResourceAccessor(), database);

                    liquibase.update();
                    liquibase.close();
                } catch (SQLException | LiquibaseException e) {
                    e.printStackTrace();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
    }
}
