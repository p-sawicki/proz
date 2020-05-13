import java.awt.Point;

public class Queen extends Piece{
    public Queen(Cell.Colour colour) {
        super(colour);
    }

    @Override
    public String getName() {
        return "Queen";
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

        if( (moveX == moveY) || (moveX == 0 && moveY > 0) || (moveX > 0 && moveY == 0) ) { // diagonal, vertical or horizontal move respectively
            if(super.checkIfPathIsClear(super.cell, destination)){
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
