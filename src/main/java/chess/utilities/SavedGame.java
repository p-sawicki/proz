package chess.utilities;

import chess.gui.GameWindow;
import chess.mechanics.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SavedGame {
    private final String programVersion = "1.0"; // in case modified program won't support old files
    private final String playerName;
    private final boolean whiteTurn;
    private final List<SavedCell> cellsList;
    private final Point lastBlackPosition;
    private final Point lastWhitePosition;

    public SavedGame(GameWindow gameWindow) {
        Board board = gameWindow.getBoard();
        cellsList = new ArrayList<>();

        // initialize saved Game values
        playerName = gameWindow.getPlayerName();
        whiteTurn = board.getWhiteTurn();

        // initialize list of cells
        Cell[][] cells = board.getCells();
        int size = board.getBoardSize(); // number of rows and columns
        Cell realCell;
        for (int y = size - 1; y >= 0; --y) {
            for (int x = 0; x < size; ++x) {
                realCell = cells[y][x];
                SavedCell savedCell = new SavedCell(realCell);
                cellsList.add(savedCell);
            }
        }

        lastBlackPosition = board.getLastDoubleStepPosition(Cell.Colour.black);
        lastWhitePosition = board.getLastDoubleStepPosition(Cell.Colour.white);
    }

    public Board restoreBoard() {
        int size = 8;
        Cell[][] cells = new Cell[size][size];

        for (int y = size - 1; y >= 0; --y) {
            for (int x = 0; x < size; ++x) {
                int cellListIndex = x + (7 - y) * size;
                SavedCell savedCell = cellsList.get(cellListIndex);
                Cell cell = savedCell.createCell();
                cells[y][x] = cell;
            }
        }

        Board board = new Board(cells, this.whiteTurn, true);
        board.setLastDoubleStep(lastBlackPosition, lastWhitePosition);
        return board;
    }

    public GameAttributes createGameAttributes() {
        GameAttributes savedGameAttr = new GameAttributes();
        savedGameAttr.setBoard(restoreBoard());
        savedGameAttr.setPlayerName(playerName);
        return savedGameAttr;
    }
}
