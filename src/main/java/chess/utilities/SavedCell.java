package chess.utilities;

import chess.mechanics.*;

import java.awt.*;

public class SavedCell {
    private String pieceType;
    private Cell.Colour pieceColour;
    private boolean pieceHasMoved;
    private Point position;

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

        if (!pieceType.equals("")) {
            cell.setPiece(pieceType, pieceColour, pieceHasMoved);
        }

        return cell;
    }
}
