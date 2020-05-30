import chess.mechanics.*;
import chess.mechanics.pieces.*;

import junit.framework.TestCase;

public class CheckDetectorTest extends TestCase {
    public void testGameStart() {
        Board board = BoardSetup.getStartingState();

        assert !CheckDetector.isPlayerChecked(board, Cell.Colour.black);
        assert !CheckDetector.isPlayerChecked(board, Cell.Colour.white);
        assert CheckDetector.isOpponentChecked(board, Cell.Colour.black) == CheckDetector.State.none;
        assert CheckDetector.isOpponentChecked(board, Cell.Colour.white) == CheckDetector.State.none;
    }

    public void testCheck(){
        Board board = BoardSetup.getEmptyBoard(Cell.Colour.black);
        board.getCells()[0][0].setPiece(new Knight(Cell.Colour.white));
        board.getCells()[1][2].setPiece(new King(Cell.Colour.black));
        assert CheckDetector.isPlayerChecked(board, Cell.Colour.black);
        assert CheckDetector.isOpponentChecked(board, Cell.Colour.white) == CheckDetector.State.check;
    }

    public void testCheckMate(){
        Board board = BoardSetup.getEmptyBoard(Cell.Colour.white);
        board.getCells()[0][0].setPiece(new King(Cell.Colour.white));
        board.getCells()[1][1].setPiece(new Pawn(Cell.Colour.black));
        assert CheckDetector.isPlayerChecked(board, Cell.Colour.white);
        assert CheckDetector.isOpponentChecked(board, Cell.Colour.black) == CheckDetector.State.check;

        board.getCells()[7][0].setPiece(new Rook(Cell.Colour.black));
        board.getCells()[0][7].setPiece(new Rook(Cell.Colour.black));
        board.getCells()[7][7].setPiece(new Bishop(Cell.Colour.black));
        assert CheckDetector.isOpponentChecked(board, Cell.Colour.black) == CheckDetector.State.checkmate;
    }
}