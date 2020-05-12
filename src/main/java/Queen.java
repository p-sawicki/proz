import javafx.util.Pair;

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
        Pair<Integer, Integer> curPos = super.cell.getPosition();
        int curX = curPos.getValue();
        int curY = curPos.getKey();
        Pair<Integer, Integer> desPos = destination.getPosition();
        int desX = desPos.getValue();
        int desY = desPos.getKey();
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
