import junit.framework.TestCase;
import java.awt.*;

public class pieceMovementTest extends TestCase {
    Board testBoard = getEmptyBoard();

    public void cellTest() {
        Cell cell1 = new Cell(Cell.Colour.black, testBoard, new Point(0,0));

        // cell position
        Point p1 = new Point(5,4);
        Point p2 = new Point(0,0);

        assert cell1.getPosition() != p1;
        assert cell1.getPosition() == p2;

        // piece insertion, cell occupation, piece removal
        assert cell1.getOccupation() == false;
        assert cell1.getPiece() == null;

        Piece pieceKnight = new Knight(Cell.Colour.white);
        cell1.setPiece(pieceKnight);

        assert cell1.getOccupation() == true;
        assert cell1.getPiece() == pieceKnight;
        assert cell1.getPieceNameColor() == "White Knight";

        cell1.removePiece();

        assert cell1.getOccupation() == false;
        assert cell1.getPiece() == null;
    }

    public void pieceTest() {
        Piece piece1 = new Pawn(Cell.Colour.black);
        Rook rook1 = new Rook(Cell.Colour.white);

        assert piece1.getColourAsString() == "Black";
        assert rook1.getColourAsString() == "White";
        assert rook1.getName() == "Rook";
    }

    public void pieceMoveTest(){
        Cell[][] boardCells = testBoard.getCells();

        // y=0,x=0 - bottom left corner cell
        // y=7,x=0 - top left corner cell (y increases ^, x increases ->)
        Cell queenCell = boardCells[0][2]; // y=0, x=2
        Cell rookCell = boardCells[2][0]; // y=2, x=0
        Cell bishopCell = boardCells[0][0]; // y=0, x=0
        Cell newQueenCell = boardCells[2][2]; // y=2, x=2
        Cell newBishopCell = boardCells[3][3]; // y=3, x=3
        Cell newBishopCell2 = boardCells[1][3]; // y=1, x=3

        Piece queen = new Queen(Cell.Colour.white);
        Piece rook = new Rook(Cell.Colour.black);
        Piece bishop = new Bishop(Cell.Colour.black);

        queenCell.setPiece(queen);
        assert queenCell.getPieceNameColor() == "White Queen";
        rookCell.setPiece(rook);
        assert rookCell.getPieceNameColor() == "Black Rook";
        bishopCell.setPiece(bishop);
        assert bishopCell.getPieceNameColor() == "Black Bishop";

        // queen moves 2 cells up the board
        assert queen.isAppropriateMove(newQueenCell) == true;
        assert testBoard.moveIfPossible(queenCell, newQueenCell) == true;
        assert queenCell.getPieceNameColor() == "";
        assert newQueenCell.getPieceNameColor() == "White Queen";

        // checkIfPathIsClear test: bishop wants to move over queen
        assert bishop.checkIfPathIsClear(bishopCell, newBishopCell) == false;
        assert bishop.isAppropriateMove(newBishopCell) == false;

        // inappropriate bishop move
        assert bishop.isAppropriateMove(newBishopCell2) == false;
        assert testBoard.moveIfPossible(bishopCell, newBishopCell2) == false;

        //queen beats bishop
        assert testBoard.moveIfPossible(newQueenCell, bishopCell) == true;
        assert newQueenCell.getPieceNameColor() == "";
        assert bishopCell.getPieceNameColor() == "White Queen";

        // rook beats queen
        assert testBoard.moveIfPossible(rookCell, bishopCell) == true;
        assert rookCell.getPieceNameColor() == "";
        assert bishopCell.getPieceNameColor() == "Black Rook";
    }

    public static Board getEmptyBoard(){
        return new Board();
    }
}
