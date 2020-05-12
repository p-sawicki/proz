import java.awt.Point;
import java.util.ArrayList;

public class Pawn extends Piece{
    private boolean doneFirstMove;
    public Pawn(Cell.Colour colour) {
        this(colour, false);
    }

    public Pawn(Cell.Colour colour, boolean doneFirstMove){
        super(colour);
        this.doneFirstMove = doneFirstMove;
    }

    public boolean isDoneFirstMove() {
        return doneFirstMove;
    }

    public void setDoneFirstMove(boolean doneFirstMove) {
        this.doneFirstMove = doneFirstMove;
    }

    @Override
    public String getName() {
        return "Pawn";
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

        if (super.cell.getPiece().getColourAsString().equals("White")) {
            if (curX == desX) {
                if ((curY == 1 && desY == 3) && !isOccupied) { // pawn's first move
                    if (super.checkIfPathIsClear(super.cell, destination)) // there is no other piece between current pos and destination
                        return true; //movePiece
                }
                if ((desY - curY == 1) && !isOccupied) {
                    return true; //movePiece
                }
            }
            if (((desX - curX == 1) || (desX - curX == -1)) && (desY - curY == 1) && isOccupied) { //beats other piece
                if (destination.getPiece().getColourAsString().equals("Black")) {
                    return true; //movePiece to beat
                }
            }
        }
        if (super.cell.getPiece().getColourAsString().equals("Black")) {
            if (curX == desX) {
                if ((curY == 6 && desY == 4) && !isOccupied) { // pawn's first move
                    if (super.checkIfPathIsClear(super.cell, destination)) { // there is no other piece between current pos and destination
                        return true; //movePiece
                    }
                }
                if ((desY - curY == -1) && !isOccupied) {
                    return true; //movePiece
                }
            }
            if (((desX - curX == 1) || (desX - curX == -1)) && (desY - curY == -1) && isOccupied) { //beats other piece
                if (destination.getPiece().getColourAsString().equals("White")) {
                    return true; //movePiece to beat
                }
            }
        }
        return false;
    }

    public ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        int y = cell.getPosition().y;
        int x = cell.getPosition().x;
        Point start = cell.getPosition();
        moves.add(Move.get(start, new Point(y - 1, x)));
        if (!doneFirstMove)
            moves.add(Move.get(start, new Point(y - 2, x)));
        if (y - 1 >= 0) {
            if (x - 1 >= 0) {
                Piece piece = cell.getBoard().getCells()[y - 1][x - 1].getPiece();
                if (piece != null && piece.getColour() != colour)
                    moves.add(Move.get(start, new Point(y - 1, x - 1)));
            }
            if(x + 1 < cell.getBoard().getBoardSize()){
                Piece piece = cell.getBoard().getCells()[y - 1][x + 1].getPiece();
                if(piece != null && piece.getColour() != colour)
                    moves.add(Move.get(start, new Point(y - 1, x + 1)));
            }
        }
        return trimPossibleMoves(moves);
    }
}
