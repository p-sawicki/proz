import java.awt.Point;
import java.util.ArrayList;

public class Knight extends Piece{
    public Knight(Cell.Colour colour) {
        super(colour);
    }

    @Override
    public String getName() {
        return "Knight";
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

        if( (moveX == 1 && moveY == 2) || (moveX == 2 && moveY == 1) ){
            if(!isOccupied){
                return true; //movePiece
            }
            else{
                if(super.getColourAsString() != destination.getPiece().getColourAsString()){
                    return true; //movePiece to beat
                }
            }
        }
        return false;

    public ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        int y = cell.getPosition().y;
        int x = cell.getPosition().x;
        Point start = cell.getPosition();
        moves.add(Move.get(start, new Point(y - 2, x - 1)));
        moves.add(Move.get(start, new Point(y - 2, x + 1)));
        moves.add(Move.get(start, new Point(y - 1, x - 2)));
        moves.add(Move.get(start, new Point(y - 1, x + 2)));
        moves.add(Move.get(start, new Point(y + 1, x - 2)));
        moves.add(Move.get(start, new Point(y + 1, x + 2)));
        moves.add(Move.get(start, new Point(y + 2, x - 1)));
        moves.add(Move.get(start, new Point(y + 2, x + 1)));
        return trimPossibleMoves(moves);
    }
}
