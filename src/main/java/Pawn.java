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
        Pair<int, int> curPos = super.cell.getPosition();
        int curX = curPos.getValue0();
        int curY = curPos.getValue1();
        Pair<int, int> desPos = destination.getPosition();
        int desX = desPos.getValue0();
        int desY = desPos.getValue1();
        boolean isOccupied = destination.getOccupation();
        boolean isOccupiedByWhite = destination.piece.getColourAsString() == "White" ? 1 : 0;

        if(super.cell.piece.getColourAsString() == "White") {
            if (curX = desX) {
                if ( (curY == 1 && desY == 3) && !isOccupied){ // pawn's first move
                    if(super.checkIfPathIsClear(super.cell, destination)) // there is no other piece between current pos and destination
                        return 1; //movePiece
                }
                if( (desY-curY == 1) && !isOccupied){
                    return 1; //movePiece
                }
            }
            if( ((desX-curX == 1) || (desX-curX == -1)) && (desY-curY == 1) && isOccupied ){ //beats other piece
                if(!isOccupiedByWhite)
                    return 1; //movePiece to beat
            }
        }
        if(super.cell.piece.getColourAsString() == "Black") {
            if (curX = desX) {
                if ( (curY == 6 && desY == 4) && !isOccupied) { // pawn's first move
                    if (super.checkIfPathIsClear(super.cell, destination)) { // there is no other piece between current pos and destination
                        return 1; //movePiece
                    }
                }
                if( (desY-curY == -1) && !isOccupied) {
                    return 1; //movePiece
                }
            }
            if( ((desX-curX == 1) || (desX-curX == -1)) && (desY-curY == -1) && isOccupied ) { //beats other piece
                if(isOccupiedByWhite) {
                    return 1; //movePiece to beat
                }
            }
        }
        return 0;
    }
}
