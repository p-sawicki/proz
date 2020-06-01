import chess.mechanics.*;
import chess.utilities.Utility;

public class BoardSetup {
    public static Board getEmptyBoard(Cell.Colour playerColour){
        Board board = new Board(playerColour, null);
        board.setPieces(getEmptyPieces());
        return board;
    }

    public static Board getStartingState(){
        Board board = new Board(Cell.Colour.white, null);
        Piece[][] pieces = Utility.getStartingState();
        board.setPieces(pieces);
        return board;
    }

    public static Piece[][] getEmptyPieces(){
        return new Piece[][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
        };
    }
}
