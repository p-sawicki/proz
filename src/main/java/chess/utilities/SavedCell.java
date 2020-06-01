package chess.utilities;

import chess.mechanics.*;

import java.awt.*;

public class SavedCell {
    private final String pieceType;
    private final Cell.Colour pieceColour;
    private final boolean pieceHasMoved;
    private final Point position;

    SavedCell(Cell cell) {
        Piece piece = cell.getPiece();
        position = cell.getPosition();
        if (piece != null) {
            pieceType = piece.getName();
            pieceColour = piece.getColour();
            pieceHasMoved = piece.getHasMoved();
        } else {
            pieceType = "";
            pieceColour = Cell.Colour.white;
            pieceHasMoved = true;
        }
    }

    public Cell createCell() {
        Cell cell = new Cell(position);
        if (!pieceType.equals(""))
            cell.setPiece(pieceType, pieceColour, pieceHasMoved);
        return cell;
    }
}
