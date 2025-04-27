package br.com.dio.service;

import java.sql.Connection;
import java.sql.SQLException;

import br.com.dio.persistence.dao.BoardColumnDAO;
import br.com.dio.persistence.dao.BoardDAO;
import br.com.dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardService {
    private final Connection connection;

    public BoardEntity insert(final BoardEntity entity) throws SQLException {
        var dao = new BoardDAO(connection);
        var boardColumnDAO = new BoardColumnDAO(connection);

        try {
            dao.insert(entity);
            var colmuns = entity.getBoardColumn().stream().map(c -> {
                c.setBoard(entity);
                return c;
            }).toList();

            for (var c : colmuns) {
                boardColumnDAO.insert(c);
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }

        return entity;
    }

    public boolean delete(final Long id) throws SQLException {

        var dao = new BoardDAO(connection);
        try {

            if (!dao.existsById(id))
                return false;

            dao.delete(id);
            connection.commit();

            return true;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }
}
