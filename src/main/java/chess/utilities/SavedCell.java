package chess.utilities;

import chess.mechanics.*;

import java.awt.*;

public class SavedCell {
    private String pieceType;
    private Cell.Colour pieceColour;
    private Point position;

    SavedCell(Cell cell) {
        Piece piece = cell.getPiece();

        position = cell.getPosition();
        if (piece != null) {
            pieceType = piece.getName();
            pieceColour = piece.getColour();
        } else {
            pieceType = "";
            pieceColour = Cell.Colour.white;
        }
    }

    public Cell createCell() {
        Cell cell = new Cell(position);

        if (!pieceType.equals("")) {
            cell.setPiece(pieceType, pieceColour);
        }

        return cell;
    }
}
