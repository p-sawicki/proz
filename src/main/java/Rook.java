import java.awt.Point;
import java.util.ArrayList;

public class Rook extends Piece{
    public Rook(Cell.Colour colour) {
        super(colour);
    }

    @Override
    public String getName() {
        return "Rook";
    }

    @Override
    public boolean isAppropriateMove(Cell destination) {
        Point curPos = super.cell.getPosition();
        int curX = curPos.x;
        int curY = curPos.y;
        Point desPos = destination.getPosition();
        int desX = desPos.x;
        int desY = desPos.y;
        boolean isOccupied = destination.getOccupation();
        int moveX = Math.abs(desX - curX);
        int moveY = Math.abs(desY - curY);

        if ((moveX == 0 && moveY > 0) || (moveX > 0 && moveY == 0)) { // vertical or horizontal move
            if (super.checkIfPathIsClear(super.cell, destination)) {
                if (!isOccupied) {
                    return true; //movePiece
                } else {
                    if (super.getColourAsString() != destination.getPiece().getColourAsString()) {
                        return true; //movePiece to beat
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        moves.addAll(getVerticalMoves());
        moves.addAll(getHorizontalMoves());
        return moves;
    }
}
