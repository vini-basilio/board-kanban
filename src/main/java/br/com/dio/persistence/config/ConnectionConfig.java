package br.com.dio.persistence.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionConfig {
    public static Connection getConnection() throws SQLException {
        var url = "jdbc:mysql://localhost:3307/board";
        var user = System.getenv("DB_USER");
        var password = System.getenv("DB_PASSWORD");
        ;
        var connection = DriverManager.getConnection(url, user, password);

        connection.setAutoCommit(false);
        return connection;
    }
}
