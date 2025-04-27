package br.com.dio.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.com.dio.persistence.config.ConnectionConfig;
import br.com.dio.persistence.entity.BoardColumnEntity;
import br.com.dio.persistence.entity.BoardColumnKindEnum;
import br.com.dio.persistence.entity.BoardEntity;
import br.com.dio.service.BoardQueryService;
import br.com.dio.service.BoardService;

public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);

    public void execute() throws SQLException {
        System.out.println("Bem-vindo ao gerenciador de boards\nEscolha a opcao desejada");

        var option = -1;

        while (true) {
            System.out.println("1 - Criar um card");
            System.out.println("2 - Selecionar um board existente");
            System.out.println("3 - Excluir um board");
            System.out.println("4 - Sair");

            option = scanner.nextInt();
            switch (option) {
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Opcao invalida, selecione uma das opcoes do menu");
            }
        }
    }

    private BoardColumnEntity createColumn(final String name, final BoardColumnKindEnum kind, final int order) {
        var boardColumn = new BoardColumnEntity();

        boardColumn.setName(name);
        boardColumn.setKind(kind);
        boardColumn.setOrder(order);

        return boardColumn;
    }

    public void createBoard() throws SQLException {
        var entity = new BoardEntity();
        System.out.println("Informe o nome do seu board");
        entity.setName(scanner.next());

        System.out.println("Seu board tera colunas alem das 3 padroes? Se sim, informe quantas, senão digite 0");

        var additionalColumns = scanner.nextInt();

        List<BoardColumnEntity> columns = new ArrayList<>();

        System.out.println("informe o nome da coluna inicial do board");

        var initialColumnName = scanner.next();
        var initialColumn = createColumn(initialColumnName, BoardColumnKindEnum.INITIAL, 0);
        columns.add(initialColumn);

        for (int i = 0; i < additionalColumns; i++) {
            System.out.println("informe o nome da coluna de tarefa pendente do board");

            var pendingColumnName = scanner.next();
            var pendingColumn = createColumn(pendingColumnName, BoardColumnKindEnum.PEDING, i + 1);
            columns.add(pendingColumn);

        }

        System.out.println("informe o nome da coluna final do board");

        var finalColumnName = scanner.next();
        var finalColumn = createColumn(finalColumnName, BoardColumnKindEnum.FINAL, additionalColumns + 1);
        columns.add(finalColumn);

        System.out.println("informe o nome da coluna final do board");

        var cancelColumnName = scanner.next();
        var cancelColumn = createColumn(cancelColumnName, BoardColumnKindEnum.CANCEL, additionalColumns + 2);
        columns.add(cancelColumn);

        entity.setBoardColumn(columns);
        try (var connection = ConnectionConfig.getConnection()) {
            var service = new BoardService(connection);
            service.insert(entity);
        }
    }

    public void selectBoard() throws SQLException {
        System.out.println("Informe o id do board que deseja selecionar");

        var id = scanner.nextLong();

        try (var connection = ConnectionConfig.getConnection()) {
            var queryService = new BoardQueryService(connection);
            var optional = queryService.findById(id);

            optional.ifPresentOrElse(
                    b -> new BoardMenu(b).execute(),
                    () -> System.out.printf("Não foi encontrado um board com id %s\n", id));
        }
    }

    public void deleteBoard() throws SQLException {
        System.out.println("Informe o id do board que sera excluido");

        var id = scanner.nextLong();
        try (var connection = ConnectionConfig.getConnection()) {

            var service = new BoardService(connection);
            if (service.delete(id)) {
                System.out.printf("O board %s foi excluido\n", id);
            } else {
                System.out.printf("Não foi encontrado um board com id %s\n", id);
            }
        }
    }

}
