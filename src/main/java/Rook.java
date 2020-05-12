import javafx.util.Pair;

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
        Pair<Integer, Integer> curPos = super.cell.getPosition();
        int curX = curPos.getValue();
        int curY = curPos.getKey();
        Pair<Integer, Integer> desPos = destination.getPosition();
        int desX = desPos.getValue();
        int desY = desPos.getKey();
        boolean isOccupied = destination.getOccupation();
        int moveX = Math.abs(desX - curX);
        int moveY = Math.abs(desY - curY);

        if( (moveX == 0 && moveY > 0) || (moveX > 0 && moveY == 0) ) { // vertical or horizontal move
            if(super.checkIfPathIsClear(super.cell, destination)) {
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
