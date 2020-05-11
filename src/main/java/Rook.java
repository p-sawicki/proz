public class Rook extends Piece{
    public Rook(Cell.Colour colour) {
        super(colour);
    }

    @Override
    public String getName() {
        return "Rook";
    }

    @Override
    public abstract boolean isAppropriateMove(Cell destination) {
        Pair<int, int> curPos = super.cell.getPosition();
        int curX = curPos.getValue0();
        int curY = curPos.getValue1();
        Pair<int, int> desPos = destination.getPosition();
        int desX = desPos.getValue0();
        int desY = desPos.getValue1();
        boolean isOccupied = destination.getOccupation();
        int moveX = Math.abs(desX - curX);
        int moveY = Math.abs(desY - curY);

        if( (moveX == 0 && moveY > 0) || (moveX > 0 && moveY == 0) ) { // vertical or horizontal move
            if(super.checkIfPathIsClear(super.cell, destination)) {
                if (!isOccupied) {
                    return 1; //movePiece
                } else {
                    if (super.getColourAsString() != destination.piece.getColourAsString()) {
                        return 1; //movePiece to beat
                    }
                }
            }
        }
        return 0;
    }
}
