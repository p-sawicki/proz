import chess.mechanics.*;
import chess.mechanics.pieces.*;

import junit.framework.TestCase;

import java.awt.*;

public class PieceMovementTest extends TestCase {
    Board testBoard = BoardSetup.getEmptyBoard(Cell.Colour.black);

    public void testCell() {
        Cell cell1 = new Cell(Cell.Colour.black, testBoard, new Point(0, 0));

        // cell position
        Point p1 = new Point(5, 4);
        Point p2 = new Point(0, 0);

        assert !cell1.getPosition().equals(p1);
        assert cell1.getPosition().equals(p2);

        // piece insertion, cell occupation, piece removal
        assert !cell1.getOccupation();
        assert cell1.getPiece() == null;

        Piece pieceKnight = new Knight(Cell.Colour.white);
        cell1.setPiece(pieceKnight);

        assert cell1.getOccupation();
        assert cell1.getPiece() == pieceKnight;
        assert cell1.getPieceNameColor().equals("White Knight");

        cell1.removePiece();

        assert !cell1.getOccupation();
        assert cell1.getPiece() == null;
    }

    public void testPiece() {
        Piece piece1 = new Pawn(Cell.Colour.black);
        Rook rook1 = new Rook(Cell.Colour.white);

        assert piece1.getColourAsString().equals("Black");
        assert rook1.getColourAsString().equals("White");
        assert rook1.getName().equals("Rook");
    }

    public void testPieceMove() {
        Board board = new Board(Cell.Colour.white, null);
        Cell[][] boardCells = board.getCells();
        Piece[][] pieces = BoardSetup.getEmptyPieces();

        // y=0,x=0 - bottom left corner cell
        // y=7,x=0 - top left corner cell (y increases ^, x increases ->)
        Cell queenCell = boardCells[0][2]; // y=0, x=2
        Cell rookCell = boardCells[2][0]; // y=2, x=0
        Cell bishopCell = boardCells[0][0]; // y=0, x=0
        Cell newQueenCell = boardCells[2][2]; // y=2, x=2
        Cell newBishopCell2 = boardCells[1][3]; // y=1, x=3

        Piece queen = new Queen(Cell.Colour.white);
        Piece rook = new Rook(Cell.Colour.black);
        Piece bishop = new Bishop(Cell.Colour.black);

        pieces[0][2] = queen;
        pieces[2][0] = rook;
        pieces[0][0] = bishop;
        board.setPieces(pieces);

        queenCell.setPiece(queen);
        assert queenCell.getPieceNameColor().equals("White Queen");
        rookCell.setPiece(rook);
        assert rookCell.getPieceNameColor().equals("Black Rook");
        bishopCell.setPiece(bishop);
        assert bishopCell.getPieceNameColor().equals("Black Bishop");
        assert queen.isLegalMove(newQueenCell);
        assert !bishop.isLegalMove(newBishopCell2);
        assert newQueenCell.getPieceNameColor().equals("");
    }
}
