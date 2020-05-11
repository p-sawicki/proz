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

    public ArrayList<Pair<Integer, Integer>> getPossibleMoves() {
        ArrayList<Pair<Integer, Integer>> moves = new ArrayList<>();
        int y = cell.getPosition().getKey();
        int x = cell.getPosition().getValue();
        moves.add(new Pair<>(y - 1, x - 1));
        moves.add(new Pair<>(y - 1, x));
        moves.add(new Pair<>(y - 1, x + 1));
        moves.add(new Pair<>(y, x - 1));
        moves.add(new Pair<>(y, x + 1));
        moves.add(new Pair<>(y + 1, x - 1));
        moves.add(new Pair<>(y + 1, x));
        moves.add(new Pair<>(y + 1, x));
        return trimPossibleMoves(moves);
    }
}
