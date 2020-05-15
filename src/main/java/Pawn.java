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

    public Piece copy(){
        return new Pawn(colour);
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

    protected ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        Point start = cell.getPosition();
        int y = start.y;
        int x = start.x;
        if(colour != cell.getBoard().getBottomPlayerColour()) {
            if (y == 6 && cell.getBoard().getCells()[y - 2][x].getPiece() == null)
                moves.add(new Move(start, new Point(x, y - 2)));
            if (y - 1 >= 0) {
                if(cell.getBoard().getCells()[y - 1][x].getPiece() == null)
                    moves.add(new Move(start, new Point(x, y - 1)));
                if (x - 1 >= 0) {
                    Piece piece = cell.getBoard().getCells()[y - 1][x - 1].getPiece();
                    if (piece != null && piece.getColour() != colour)
                        moves.add(new Move(start, new Point(x - 1, y - 1)));
                }
                if (x + 1 < cell.getBoard().getBoardSize()) {
                    Piece piece = cell.getBoard().getCells()[y - 1][x + 1].getPiece();
                    if (piece != null && piece.getColour() != colour)
                        moves.add(new Move(start, new Point(x + 1, y - 1)));
                }
            }
        }
        else{
            if(y == 1 && cell.getBoard().getCells()[y + 2][x].getPiece() == null)
                moves.add(new Move(start, new Point(x, y + 2)));
            if(y + 1 < cell.getBoard().getBoardSize()){
                if(cell.getBoard().getCells()[y + 1][x].getPiece() == null)
                    moves.add(new Move(start, new Point(x, y + 1)));
                if(x - 1 >= 0){
                    Piece piece = cell.getBoard().getCells()[y + 1][x - 1].getPiece();
                    if(piece != null && piece.getColour() != colour)
                        moves.add(new Move(start, new Point(x - 1, y + 1)));
                }
                if(x + 1 < cell.getBoard().getBoardSize()){
                    Piece piece = cell.getBoard().getCells()[y + 1][x + 1].getPiece();
                    if(piece != null && piece.getColour() != colour)
                        moves.add(new Move(start, new Point(x + 1, y + 1)));
                }
            }
        }
        return moves;
    }
}
