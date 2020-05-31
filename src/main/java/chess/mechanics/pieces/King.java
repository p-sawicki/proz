package chess.mechanics.pieces;

import chess.mechanics.*;

import java.awt.Point;
import java.util.ArrayList;

public class King extends Piece {
    public King(Cell.Colour colour) {
        super(colour);
    }

    public Piece copy() {
        return new King(colour);
    }

    @Override
    public String getName() {
        return "King";
    }

    protected ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        int y = cell.getPosition().y;
        int x = cell.getPosition().x;
        Point start = cell.getPosition();
        moves.add(new Move(start, new Point(x - 1, y - 1)));
        moves.add(new Move(start, new Point(x, y - 1)));
        moves.add(new Move(start, new Point(x + 1, y - 1)));
        moves.add(new Move(start, new Point(x - 1, y)));
        moves.add(new Move(start, new Point(x + 1, y)));
        moves.add(new Move(start, new Point(x - 1, y + 1)));
        moves.add(new Move(start, new Point(x, y + 1)));
        moves.add(new Move(start, new Point(x + 1, y + 1)));

        Piece piece = cell.getBoard().getCells()[y][0].getPiece();
        if (!hasMoved && piece instanceof Rook && piece.getColour() == colour && !piece.getHasMoved()
                && !CheckDetector.isPlayerChecked(cell.getBoard(), new Move(start, start))
                && !CheckDetector.isPlayerChecked(cell.getBoard(), new Move(start, new Point(x - 1, y)))) {
            Move castle = new Move(start, new Point(x - 2, y));
            castle.secondMove = new Move(piece.getCell().getPosition(), new Point(x - 1, y));
            moves.add(castle);
        }
        piece = cell.getBoard().getCells()[y][cell.getBoard().getBoardSize() - 1].getPiece();
        if (!hasMoved && piece instanceof Rook && piece.getColour() == Cell.Colour.white && !piece.getHasMoved()
                && !CheckDetector.isPlayerChecked(cell.getBoard(), new Move(start, start))
                && !CheckDetector.isPlayerChecked(cell.getBoard(), new Move(start, new Point(x + 1, y)))) {
            Move castle = new Move(start, new Point(x + 2, y));
            castle.secondMove = new Move(piece.getCell().getPosition(), new Point(x + 1, y));
            moves.add(castle);
        }
        return trimPossibleMoves(moves);
    }
}
