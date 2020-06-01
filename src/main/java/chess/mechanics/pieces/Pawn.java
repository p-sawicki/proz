package chess.mechanics.pieces;

import chess.mechanics.*;

import java.awt.Point;
import java.util.ArrayList;

public class Pawn extends Piece {

    public Pawn(Cell.Colour colour) {
        super(colour);
    }

    public Pawn(Cell.Colour colour, boolean hasMoved) {
        super(colour, hasMoved);
    }

    public Piece copy() {
        return new Pawn(colour);
    }

    @Override
    public String getName() {
        return "Pawn";
    }

    protected ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        Point start = cell.getPosition();
        int y = start.y;
        int x = start.x;
        Cell.Colour opponentColour = colour == Cell.Colour.black ? Cell.Colour.white : Cell.Colour.black;
        if (colour == Cell.Colour.black) {
            if (y == cell.getBoard().getBoardSize() - 2 && cell.getBoard().getCells()[y - 2][x].getPiece() == null)
                moves.add(new Move(start, new Point(x, y - 2)));
            if (y - 1 >= 0) {
                if (cell.getBoard().getCells()[y - 1][x].getPiece() == null)
                    moves.add(new Move(start, new Point(x, y - 1)));
                if (x - 1 >= 0) {
                    Piece piece = cell.getBoard().getCells()[y - 1][x - 1].getPiece();
                    if (piece != null && piece.getColour() != colour)
                        moves.add(new Move(start, new Point(x - 1, y - 1)));

                    piece = cell.getBoard().getCells()[y][x - 1].getPiece();
                    if(piece != null && piece.getColour() != colour && cell.getBoard().getLastDoubleStep(opponentColour) == piece){
                        Move move = new Move(start, new Point(x - 1, y - 1));
                        move.secondMove = new Move(new Point(x - 1, y), null);
                        moves.add(move);
                    }
                }
                if (x + 1 < cell.getBoard().getBoardSize()) {
                    Piece piece = cell.getBoard().getCells()[y - 1][x + 1].getPiece();
                    if (piece != null && piece.getColour() != colour)
                        moves.add(new Move(start, new Point(x + 1, y - 1)));

                    piece = cell.getBoard().getCells()[y][x + 1].getPiece();
                    if(piece != null && piece.getColour() != colour && cell.getBoard().getLastDoubleStep(opponentColour) == piece){
                        Move move = new Move(start, new Point(x + 1, y - 1));
                        move.secondMove = new Move(new Point(x + 1, y), null);
                        moves.add(move);
                    }
                }
            }
        } else {
            if (y == 1 && cell.getBoard().getCells()[y + 2][x].getPiece() == null)
                moves.add(new Move(start, new Point(x, y + 2)));
            if (y + 1 < cell.getBoard().getBoardSize()) {
                if (cell.getBoard().getCells()[y + 1][x].getPiece() == null)
                    moves.add(new Move(start, new Point(x, y + 1)));
                if (x - 1 >= 0) {
                    Piece piece = cell.getBoard().getCells()[y + 1][x - 1].getPiece();
                    if (piece != null && piece.getColour() != colour)
                        moves.add(new Move(start, new Point(x - 1, y + 1)));

                    piece = cell.getBoard().getCells()[y][x - 1].getPiece();
                    if(piece != null && piece.getColour() != colour && cell.getBoard().getLastDoubleStep(opponentColour) == piece){
                        Move move = new Move(start, new Point(x - 1, y + 1));
                        move.secondMove = new Move(new Point(x - 1, y), null);
                        moves.add(move);
                    }
                }
                if (x + 1 < cell.getBoard().getBoardSize()) {
                    Piece piece = cell.getBoard().getCells()[y + 1][x + 1].getPiece();
                    if (piece != null && piece.getColour() != colour)
                        moves.add(new Move(start, new Point(x + 1, y + 1)));

                    piece = cell.getBoard().getCells()[y][x + 1].getPiece();
                    if(piece != null && piece.getColour() != colour && cell.getBoard().getLastDoubleStep(opponentColour) == piece){
                        Move move = new Move(start, new Point(x + 1, y + 1));
                        move.secondMove = new Move(new Point(x + 1, y), null);
                        moves.add(move);
                    }
                }
            }
        }
        return removeMovesThatLeadToCheck(moves);
    }
}
