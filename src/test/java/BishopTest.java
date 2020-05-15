import junit.framework.TestCase;

import java.awt.*;
import java.util.ArrayList;

public class BishopTest extends TestCase {
    public void testMovesEmptyBoard() {
        Board board = BoardSetup.getEmptyBoard(Cell.Colour.black);
        Piece[][] pieces = BoardSetup.getEmptyPieces();
        Point start = board.getCells()[3][3].getPosition();
        pieces[start.y][start.x] = new Bishop(Cell.Colour.black);
        board.setPieces(pieces);

        ArrayList<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Move(start, board.getCells()[2][2].getPosition()));
        expectedMoves.add(new Move(start, board.getCells()[1][1].getPosition()));
        expectedMoves.add(new Move(start, board.getCells()[0][0].getPosition()));
        expectedMoves.add(new Move(start, board.getCells()[2][4].getPosition()));
        expectedMoves.add(new Move(start, board.getCells()[1][5].getPosition()));
        expectedMoves.add(new Move(start, board.getCells()[0][6].getPosition()));
        expectedMoves.add(new Move(start, board.getCells()[4][2].getPosition()));
        expectedMoves.add(new Move(start, board.getCells()[5][1].getPosition()));
        expectedMoves.add(new Move(start, board.getCells()[6][0].getPosition()));
        expectedMoves.add(new Move(start, board.getCells()[4][4].getPosition()));
        expectedMoves.add(new Move(start, board.getCells()[5][5].getPosition()));
        expectedMoves.add(new Move(start, board.getCells()[6][6].getPosition()));
        expectedMoves.add(new Move(start, board.getCells()[7][7].getPosition()));

        ArrayList<Move> moves = pieces[start.y][start.x].getMoves();
        assert moves.size() == expectedMoves.size();
        for(int i = 0; i < moves.size(); ++i){
            assert moves.get(i).after.x == expectedMoves.get(i).after.x;
            assert moves.get(i).after.y == expectedMoves.get(i).after.y;
        }
    }
}