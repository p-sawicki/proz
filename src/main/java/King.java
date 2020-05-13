import java.awt.Point;

public class King extends Piece{
    public King(Cell.Colour colour) {
        super(colour);
    }

    @Override
    public String getName() {
        return "King";
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

        if( (moveX + moveY == 1) || (moveX == 1 && moveY == 1) ){
            if(!isOccupied) {
                return true; //movePiece
            }
            else{
                if(super.getColourAsString() != destination.getPiece().getColourAsString()) {
                    return true; //movePiece to beat
                }
            }
        }
        return false;
    }
}
