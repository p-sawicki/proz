import java.awt.Point;

public class Bishop extends Piece {
    public Bishop(Cell.Colour colour) {
        super(colour);
    }

    @Override
    public String getName() {
        return "Bishop";
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

        if (moveX == moveY) { // diagonal move
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
}
