package br.com.dio.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import br.com.dio.persistence.dao.BoardColumnDAO;
import br.com.dio.persistence.dao.BoardDAO;
import br.com.dio.persistence.entity.BoardEntity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoardQueryService {
    private final Connection connection;

    public Optional<BoardEntity> findById(final Long id) throws SQLException {
        var dao = new BoardDAO(connection);

        var optional = dao.findById(id);
        var boardColumnDAO = new BoardColumnDAO(connection);

        if (optional.isPresent()) {
            var entity = optional.get();

            entity.setBoardColumn(boardColumnDAO.findByBoardId(entity.getId()));

            return Optional.of(entity);
        }

        return Optional.empty();
    }
}
