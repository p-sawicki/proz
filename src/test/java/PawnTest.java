import junit.framework.TestCase;

public class PawnTest extends TestCase {
    public void testStart() {
        Board board = BoardSetup.getStartingState();
        for(int x = 0; x < board.getBoardSize(); ++x){
            assert board.getCells()[1][x].getPiece().getMoves().get(0).after.y == 3;
            board.getCells()[1][x].getPiece().clearMoves();
            assert board.getCells()[1][x].getPiece().getMoves().get(1).after.y == 2;
            board.getCells()[1][x].getPiece().clearMoves();
        }

        for(int x = 0; x < board.getBoardSize(); ++x){
            assert board.getCells()[6][x].getPiece().getMoves().get(0).after.y == 4;
            board.getCells()[6][x].getPiece().clearMoves();
            assert board.getCells()[6][x].getPiece().getMoves().get(1).after.y == 5;
            board.getCells()[6][x].getPiece().clearMoves();
        }
    }
}