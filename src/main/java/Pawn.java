public class Pawn extends Piece{
    public Pawn(Cell.Colour colour) {
        super(colour);
    }

    @Override
    public String getName() {
        return "Pawn";
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
        boolean isOccupiedByWhite = destination.piece.getColourAsString() == "White" ? 1 : 0;

        if(super.cell.piece.getColourAsString() == "White") {
            if (curX = desX) {
                if ( (curY == 2 && desY == 4) && !isOccupied){ // pawn's first move
                    if(super.checkIfPathIsClear(super.cell, destination)) // there is no other piece between current pos and destination
                        //movePiece
                        return 1;
                }
                if( (desY-curY == 1) && !isOccupied){
                    //movePiece
                    return 1;
                }
            }
            if( ((desX-curX == 1) || (desX-curX == -1)) && (desY-curY == 1) && isOccupied ){ //beats other piece
                if(!isOccupiedByWhite)
                    //movePiece to beat
                    return 1;
            }
        }
        if(super.cell.piece.getColourAsString() == "Black") {
            if (curX = desX) {
                if ( (curY == 7 && desY == 5) && !isOccupied){ // pawn's first move
                    if(super.checkIfPathIsClear(super.cell, destination)) // there is no other piece between current pos and destination
                    //movePiece
                        return 1;
                }
                if( (desY-curY == -1) && !isOccupied){
                    //movePiece
                    return 1;
                }
            }
            if( ((desX-curX == 1) || (desX-curX == -1)) && (desY-curY == -1) && isOccupied ){ //beats other piece
                if(isOccupiedByWhite)
                    //movePiece to beat
                    return 1;
            }
        }
        return 0;
    }
}
