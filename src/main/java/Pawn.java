import javafx.util.Pair;

public class Pawn extends Piece{
    public Pawn(Cell.Colour colour) {
        super(colour);
    }

    @Override
    public String getName() {
        return "Pawn";
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

        if(super.cell.getPiece().getColourAsString().equals("White")) {
            if (curX == desX) {
                if ( (curY == 1 && desY == 3) && !isOccupied){ // pawn's first move
                    if(super.checkIfPathIsClear(super.cell, destination)) // there is no other piece between current pos and destination
                        return true; //movePiece
                }
                if( (desY-curY == 1) && !isOccupied){
                    return true; //movePiece
                }
            }
            if( ((desX-curX == 1) || (desX-curX == -1)) && (desY-curY == 1) && isOccupied ){ //beats other piece
                if(destination.getPiece().getColourAsString().equals("Black")) {
                    return true; //movePiece to beat
                }
            }
        }
        if(super.cell.getPiece().getColourAsString().equals("Black")) {
            if (curX == desX) {
                if ( (curY == 6 && desY == 4) && !isOccupied) { // pawn's first move
                    if (super.checkIfPathIsClear(super.cell, destination)) { // there is no other piece between current pos and destination
                        return true; //movePiece
                    }
                }
                if( (desY-curY == -1) && !isOccupied) {
                    return true; //movePiece
                }
            }
            if( ((desX-curX == 1) || (desX-curX == -1)) && (desY-curY == -1) && isOccupied ) { //beats other piece
                if(destination.getPiece().getColourAsString().equals("White")) {
                    return true; //movePiece to beat
                }
            }
        }
        return false;
    }
}
