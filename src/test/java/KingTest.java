import chess.mechanics.*;
import chess.mechanics.pieces.King;

import junit.framework.TestCase;

public class KingTest extends TestCase {
    public void testMoves() {
        Board board = BoardSetup.getEmptyBoard(Cell.Colour.black);
        King king = new King(Cell.Colour.black);
        board.getCells()[7][7].setPiece(king);
        assert king.getMoves().size() == 3;
    }
}