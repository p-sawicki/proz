public class Knight extends Piece{
    public Knight(Cell.Colour colour) {
        super(colour);
    }

    @Override
    public String getName() {
        return "Knight";
    }

    @Override
    public abstract boolean isAppropriateMove(Cell destination) {
        Pair<short, short> curPos = super.cell.getPosition();
        curX = curPos.getValue0();
        curY = curPos.getValue1();
        Pair<short, short> desPos = destination.getPosition();
        desX = desPos.getValue0();
        desY = desPos.getValue1();
        boolean isOccupied = destination.getOccupation();
        moveX = Math.abs(desX - curX);
        moveY = Math.abs(desY - curY);

        if( (moveX == 1 && moveY == 2) || (moveX == 2 && moveY == 1) ){
            if(!isOccupied){
                //movePiece
                return 1;
            }
            else{
                if(super.getColourAsString() != destination.piece.getColourAsString()){
                    //movePiece to beat
                    return 1;
                }
            }
        }
        return 0;
    }
}
