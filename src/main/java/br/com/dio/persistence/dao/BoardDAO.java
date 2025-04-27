package br.com.dio.persistence.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.mysql.cj.jdbc.StatementImpl;

import br.com.dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardDAO {
    private final Connection connection;

    public BoardEntity insert(final BoardEntity entity) throws SQLException {
        var sql = "INSERT INTO BOARDS (name) values (?)";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getName());
            statement.executeQuery();

            if (statement instanceof StatementImpl impl) {
                entity.setId(impl.getLastInsertID());
            }
        }

        return entity;
    }

    public void delete(final Long id) throws SQLException {
        var sql = "DELETE FROM BOARDS WHERE id = ?";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();
        }
    }

    public Optional<BoardEntity> findById(final Long id) throws SQLException {
        var sql = "SELECT id, name FROM BOARDS WHERE id = ?";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();

            var resultSet = statement.getResultSet();

            if (resultSet.next()) {
                var entity = new BoardEntity();

                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));

                return Optional.of(entity);
            }

            return Optional.empty();
        }
    }

    public boolean existsById(final Long id) throws SQLException {
        var sql = "SELECT 1 FROM BOARDS WHERE id = ?";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();

            return statement.getResultSet().next();
        }

    }
}
