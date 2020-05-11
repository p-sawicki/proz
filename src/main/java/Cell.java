import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;

public class Cell extends JComponent {
    private final Colour colour;
    private Piece piece;
    protected Board board;
    private final Point position;

    public enum Colour {white, black}

    public Cell(Colour color, Board board, Point position) {
        this.colour = color;
        this.board = board;
        this.position = position;

        setBorder(BorderFactory.createEmptyBorder());
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        piece.setCell(this);
    }

    public void removePiece() {
        piece = null;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public Point getPosition() {
        return position;
    }

    public boolean getOccupation() {
        return this.getPiece() == null ? false : true;
    }

    public void paint(Graphics g) {
        super.paintComponent(g);

        if (colour == Colour.white)
            g.setColor(new Color(221, 192, 127));
        else
            g.setColor(new Color(101, 67, 33));

        g.fillRect(getX(), getY(), getWidth(), getHeight());
        if (piece != null)
            piece.draw(g);
    }

    public String getPieceNameColor() {
        if (this.getPiece() != null)
            return this.getPiece().getColourAsString() + " " + this.getPiece().getName();
        return "";
    }

    public Board getBoard() {
        return board;
    }

    public Piece getPiece() {
        return piece;
    }

    public Pair<Integer, Integer> getPosition() {
        return position;
    }
}
