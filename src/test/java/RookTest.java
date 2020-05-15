import junit.framework.TestCase;

public class RookTest extends TestCase {
    public void testMoves() {
        Board board = BoardSetup.getStartingState();
        Rook rook = (Rook) board.getCells()[0][0].getPiece();
        assert rook.getMoves().isEmpty();
    }
}