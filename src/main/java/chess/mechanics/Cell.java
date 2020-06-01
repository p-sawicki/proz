package chess.mechanics;

import chess.mechanics.pieces.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class Cell extends JComponent {
    private final Colour colour;
    private Piece piece;
    protected Board board;
    private final Point position;
    private boolean highlighted;

    public enum Colour {white, black}

    public Cell() {
        this.colour = Cell.Colour.black;
        this.position = new Point(-1, -1);
    }

    public Cell(Point position) {
        this.colour = Cell.Colour.black;
        this.position = position;
    }

    public Cell(Colour color, Board board, Point position) {
        this.colour = color;
        this.board = board;
        this.position = position;

        setBorder(BorderFactory.createEmptyBorder());
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        if (piece != null)
            piece.setCell(this);
    }

    public void setPiece(String pieceType, Cell.Colour pieceColour, boolean hasMoved) {
        switch (pieceType) {
            case "Pawn":
                this.piece = new Pawn(pieceColour, hasMoved);
                break;
            case "Knight":
                this.piece = new Knight(pieceColour, hasMoved);
                break;
            case "King":
                this.piece = new King(pieceColour, hasMoved);
                break;
            case "Bishop":
                this.piece = new Bishop(pieceColour, hasMoved);
                break;
            case "Queen":
                this.piece = new Queen(pieceColour, hasMoved);
                break;
            case "Rook":
                this.piece = new Rook(pieceColour, hasMoved);
                break;
        }
    }

    public void removePiece() {
        piece = null;
    }

    public Piece getPiece() {
        return piece;
    }

    public Colour getColour() {
        return colour;
    }

    public boolean getOccupation() {
        return this.getPiece() != null;
    }

    public String getPieceNameColor() {
        if (this.getPiece() != null)
            return this.getPiece().getColourAsString() + " " + this.getPiece().getName();
        return "";
    }

    public Board getBoard() {
        return board;
    }

    public Point getPosition() {
        return position;
    }

    public void paint(Graphics g) {
        super.paintComponent(g);
        super.paintBorder(g);
        if(highlighted)
            g.setColor(new Color(109, 241, 82));
        else if (colour == Colour.white)
            g.setColor(new Color(221, 192, 127));
        else
            g.setColor(new Color(85, 76, 76));

        g.fillRect(getX(), getY(), getWidth(), getHeight());
        if (piece != null)
            piece.draw(g);
    }

    public void highlight(boolean highlight){
        highlighted = highlight;
    }
}
