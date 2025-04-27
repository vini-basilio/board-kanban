package br.com.dio.persistence.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.jdbc.StatementImpl;

import br.com.dio.persistence.entity.BoardColumnEntity;
import br.com.dio.persistence.entity.BoardColumnKindEnum;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoardColumnDAO {
    private final Connection connection;

    public BoardColumnEntity insert(final BoardColumnEntity entity) throws SQLException {
        var sql = "INSERT INTO BOARDS_COLUMNS (name, `order`, kind, board_id) values (?, ?, ?, ?)";

        try (var statement = connection.prepareStatement(sql)) {
            var i = 1;
            statement.setString(i++, entity.getName());
            statement.setInt(i++, entity.getOrder());
            statement.setString(i++, entity.getKind().name());
            statement.setLong(i++, entity.getBoard().getId());
            statement.executeUpdate();

            if (statement instanceof StatementImpl impl) {
                entity.setId(impl.getLastInsertID());
            }
            return entity;
        }
    }

    public List<BoardColumnEntity> findByBoardId(final Long id) throws SQLException {
        var sql = "SELECT id, name, `order` FROM BOARDS_COLUMN WHERE board_id = ? ORDER BY `order`";

        List<BoardColumnEntity> entities = new ArrayList<>();

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();

            var resultSet = statement.getResultSet();

            while (resultSet.next()) {
                var entity = new BoardColumnEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                entity.setOrder(resultSet.getInt("order"));
                entity.setKind(BoardColumnKindEnum.findByName(resultSet.getString("kind")));
                entities.add(entity);
            }

            return entities;
        }

    }
}
