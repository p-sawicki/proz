import java.util.ArrayList;
import java.util.List;

public class SavedGame {
    private String programVersion = "1.0"; // in case modified program won't support old files
    private boolean singlePlayer;
    private String playerName;
    private Cell.Colour playerColour;
    private String opponentName;
    private String opponentIP;
    private boolean whiteTurn;
    private List<SavedCell> cellsList;
    //private GameStatus status;

    //public enum GameStatus {x, checkmate}

    SavedGame(GameWindow gameWindow) {
        Board board = gameWindow.getBoard();
        this.cellsList = new ArrayList<SavedCell>();

        // initialize saved Game values
        this.singlePlayer = board.checkIfSinglePlayer();
        this.playerName = gameWindow.getPlayerName();
        this.playerColour = board.getPlayerColour();
        this.opponentName = gameWindow.getOpponentName();
        //this.opponentIP =
        this.whiteTurn = board.getWhiteTurn();

        // initialize list of cells
        Cell[][] cells = board.getCells();
        int size = 8; // number of rows and columns
        Cell realCell;
        for (int y = size - 1; y >= 0; --y) {
            for (int x = 0; x < size; ++x) {
                realCell = cells[y][x];
                SavedCell savedCell = new SavedCell(realCell);
                cellsList.add(savedCell);
            }
        }
    }

    public Board restoreBoard() {
        int size = 8;
        Cell[][] cells = new Cell[size][size];

        for (int y = size - 1; y >= 0; --y) {
            for (int x = 0; x < size; ++x) {
                int cellListIndex = x + (7 - y)*8;
                SavedCell savedCell = cellsList.get(cellListIndex);
                Cell cell = savedCell.createCell();
                cells[y][x] = cell;
            }
        }

        return new Board(cells, this.singlePlayer, this.playerColour, this.whiteTurn, true);
    }

    public GameAttributes createGameAttributes() {
        GameAttributes savedGameAttr = new GameAttributes();
        savedGameAttr.setBoard(restoreBoard());
        savedGameAttr.setSinglePlayer(singlePlayer);
        savedGameAttr.setPlayerName(playerName);
        savedGameAttr.setPlayerColour(playerColour);
        savedGameAttr.setOpponentName(opponentName);
        savedGameAttr.setOpponentIP(opponentIP);
        return savedGameAttr;
    }
}
