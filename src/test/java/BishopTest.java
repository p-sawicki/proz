import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashSet;

public class BishopTest extends TestCase {
    public void testMovesEmptyBoard() {
        Board board = BoardSetup.getEmptyBoard();
        Piece[][] pieces = BoardSetup.getEmptyPieces();
        Point start = board.getCells()[3][3].getPosition();
        pieces[start.y][start.x] = new Bishop(Cell.Colour.black);
        board.setPieces(pieces);

        HashSet<Move> expectedMoves = new HashSet<>();
        expectedMoves.add(Move.get(start, board.getCells()[2][2].getPosition()));
        expectedMoves.add(Move.get(start, board.getCells()[1][1].getPosition()));
        expectedMoves.add(Move.get(start, board.getCells()[0][0].getPosition()));
        expectedMoves.add(Move.get(start, board.getCells()[4][4].getPosition()));
        expectedMoves.add(Move.get(start, board.getCells()[5][5].getPosition()));
        expectedMoves.add(Move.get(start, board.getCells()[6][6].getPosition()));
        expectedMoves.add(Move.get(start, board.getCells()[7][7].getPosition()));
        expectedMoves.add(Move.get(start, board.getCells()[4][2].getPosition()));
        expectedMoves.add(Move.get(start, board.getCells()[5][1].getPosition()));
        expectedMoves.add(Move.get(start, board.getCells()[6][0].getPosition()));
        expectedMoves.add(Move.get(start, board.getCells()[2][4].getPosition()));
        expectedMoves.add(Move.get(start, board.getCells()[1][5].getPosition()));
        expectedMoves.add(Move.get(start, board.getCells()[0][6].getPosition()));

        ArrayList<Move> moves = pieces[start.y][start.x].getPossibleMoves();
        assert moves.size() == expectedMoves.size();
        for(Move move : moves) {
            assert (expectedMoves.contains(move));
        }
    }
}