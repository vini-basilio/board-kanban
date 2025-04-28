package br.com.dio.persistence.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.dio.dto.BoardColumnDTO;
import com.mysql.cj.jdbc.StatementImpl;

import br.com.dio.persistence.entity.BoardColumnEntity;
import br.com.dio.persistence.entity.BoardColumnKindEnum;
import br.com.dio.persistence.entity.CardEntity;
import lombok.RequiredArgsConstructor;
import static java.util.Objects.isNull;

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
            statement.setLong(i, entity.getBoard().getId());
            statement.executeUpdate();

            if (statement instanceof StatementImpl impl) {
                entity.setId(impl.getLastInsertID());
            }
            return entity;
        }
    }

    public Optional<BoardColumnEntity> findById(final Long id) throws SQLException {
        var sql = """
                 SELECT bc.name,
                        bc.kind,
                        c.id,
                        c.title,
                        c.description
                FROM BOARDS_COLUMN bc
                INNER JOIN CARDS c
                    ON c.board_column_id = bc.id
                WHERE bc.id = ?
                """;

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();

            var resultSet = statement.getResultSet();

            if (resultSet.next()) {
                var entity = new BoardColumnEntity();
                entity.setName(resultSet.getString("name"));
                entity.setKind(BoardColumnKindEnum.findByName(resultSet.getString("kind")));

                var card = new CardEntity();
                do {
                    if (isNull(resultSet.getString("c.title"))) {
                        break;
                    }
                    card.setId(resultSet.getLong("c.id"));
                    card.setTitle(resultSet.getString("c.title"));
                    card.setDescription(resultSet.getString("c.description"));
                    entity.getCards().add(card);
                } while (resultSet.next());
                return Optional.of(entity);
            }
            return Optional.empty();
        }
    }

    public List<BoardColumnEntity> findByBoardId(final Long id) throws SQLException {
        var sql = "SELECT id, name, `order`, kind FROM BOARDS_COLUMN WHERE board_id = ? ORDER BY `order`";

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

    public List<BoardColumnDTO> findByBoardIdWithDetails(final Long id) throws SQLException {
        var sql = """
                  SELECT bc.id,
                         bc.name,
                         bc.kind,
                  COUNT(
                         SELECT c.id
                         FROM CARDS c
                         WHERE c.board_column_id = bc.id)
                         cards_amount
                  FROM BOARDS_COLUMNS bc
                  WHERE board_id = ?
                  ORDER BY `order`
                """;

        List<BoardColumnDTO> dtos = new ArrayList<>();

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();

            var resultSet = statement.getResultSet();

            while (resultSet.next()) {
                var dto = new BoardColumnDTO(
                        resultSet.getLong("bc.id"),
                        resultSet.getString("bc.name"),
                        BoardColumnKindEnum.findByName(resultSet.getString("bc.kind")),
                        resultSet.getInt("cards_amount"));

                dtos.add(dto);
            }

            return dtos;
        }
    }
}
