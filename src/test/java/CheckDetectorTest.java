import chess.mechanics.*;
import chess.mechanics.pieces.*;

import junit.framework.TestCase;

public class CheckDetectorTest extends TestCase {
    public void testGameStart() {
        Board board = BoardSetup.getStartingState();

        assert !CheckDetector.isPlayerChecked(board, Cell.Colour.black);
        assert !CheckDetector.isPlayerChecked(board, Cell.Colour.white);
        assert CheckDetector.getOpponentsState(board, Cell.Colour.black) == CheckDetector.State.none;
        assert CheckDetector.getOpponentsState(board, Cell.Colour.white) == CheckDetector.State.none;
    }

    public void testCheck(){
        Piece[][] pieces = BoardSetup.getEmptyPieces();
        pieces[0][0] = new Knight(Cell.Colour.white);
        pieces[1][2] = new King(Cell.Colour.black);
        Board board = new Board(Cell.Colour.black, null);
        board.setPieces(pieces);
        assert CheckDetector.isPlayerChecked(board, Cell.Colour.black);
        assert CheckDetector.getOpponentsState(board, Cell.Colour.white) == CheckDetector.State.check;
    }

    public void testCheckMate(){
        Piece[][] pieces = BoardSetup.getEmptyPieces();
        pieces[0][0] = new King(Cell.Colour.white);
        pieces[1][1] = new Pawn(Cell.Colour.black);
        pieces[6][6] = new King(Cell.Colour.black);
        Board board = new Board(Cell.Colour.white, null);
        board.setPieces(pieces);
        assert CheckDetector.isPlayerChecked(board, Cell.Colour.white);
        assert CheckDetector.getOpponentsState(board, Cell.Colour.black) == CheckDetector.State.check;

        pieces[7][0] = new Rook(Cell.Colour.black);
        pieces[0][7] = new Rook(Cell.Colour.black);
        pieces[7][7] = new Bishop(Cell.Colour.black);
        board = new Board(Cell.Colour.white, null);
        board.setPieces(pieces);
        assert CheckDetector.getOpponentsState(board, Cell.Colour.black) == CheckDetector.State.checkmate;
    }
}