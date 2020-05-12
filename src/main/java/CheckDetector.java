import java.util.HashSet;

public class CheckDetector {
    enum State {none, check, checkmate}

    public static State isOpponentChecked(Board board, Cell.Colour playerColour){
        Cell[][] cells = board.getCells();
        Cell.Colour opponentColour = playerColour == Cell.Colour.black ? Cell.Colour.white : Cell.Colour.black;

        HashSet<Move> opponentMoves = new HashSet<>();
        for(int y = 0; y < board.getBoardSize(); ++y){
            for(int x = 0; x < board.getBoardSize(); ++x){
                Piece piece = cells[y][x].getPiece();
                if(piece != null && piece.getColour() != playerColour)
                        opponentMoves.addAll(piece.getPossibleMoves());
            }
        }
        if(!isPlayerChecked(cells, opponentColour, board.getBoardSize()))
            return State.none;
        for(Move opponentMove : opponentMoves){
            System.out.println(opponentMove.before.y + " " + opponentMove.before.x + " " + opponentMove.after.y + " " + opponentMove.after.x);
            Cell[][] afterMove = cells.clone();
            Piece piece = cells[opponentMove.before.y][opponentMove.before.x].getPiece();
            Cell.Colour colour = piece.getColour();
            afterMove[opponentMove.after.y][opponentMove.after.x].setPiece(piece);
            afterMove[opponentMove.before.y][opponentMove.before.x].removePiece();
            if(!isPlayerChecked(afterMove, colour, board.getBoardSize()))
                return State.check;
        }
        return State.checkmate;
    }

    public static boolean isPlayerChecked(Cell[][] cells, Cell.Colour colour, int boardSize){
        HashSet<Move> opponentMoves = new HashSet<>();
        for(int y = 0; y < boardSize; ++y){
            for(int x = 0; x < boardSize; ++x){
                Piece piece = cells[y][x].getPiece();
                if(piece != null && piece.getColour() != colour)
                        opponentMoves.addAll(piece.getPossibleMoves());
            }
        }
        for(Move move : opponentMoves){
            int y = move.after.y;
            int x = move.after.x;
            Piece piece = cells[y][x].getPiece();
            if(piece instanceof King && piece.getColour() != colour)
                return true;
        }
        return false;
    }
}
