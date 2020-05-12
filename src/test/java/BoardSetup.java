public class BoardSetup {
    public static Board getEmptyBoard(){
        return new Board();
    }

    public static Board getStartingState(){
        Board board = new Board();
        Piece[][] pieces = getStartingPieces();
        board.setPieces(pieces);
        return board;
    }

    public static Piece[][] getStartingPieces(){
        return new Piece[][]{
                {new Rook(Cell.Colour.black), new Knight(Cell.Colour.black), new Bishop(Cell.Colour.black), new Queen(Cell.Colour.black),
                        new King(Cell.Colour.black), new Bishop(Cell.Colour.black), new Knight(Cell.Colour.black), new Rook(Cell.Colour.black)},
                {new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black),
                        new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black)},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white),
                        new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white)},
                {new Rook(Cell.Colour.white), new Knight(Cell.Colour.white), new Bishop(Cell.Colour.white), new Queen(Cell.Colour.white),
                        new King(Cell.Colour.white), new Bishop(Cell.Colour.white), new Knight(Cell.Colour.white), new Rook(Cell.Colour.white)}
        };
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
