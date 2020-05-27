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

    public Cell(Point position) {
        this.colour = Cell.Colour.black;
        this.position = position;
    }

    public Cell() {
        this.colour = Cell.Colour.black;
        this.position = new Point(-1, -1);
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        if (piece != null)
            piece.setCell(this);
    }

    public void setPiece(String pieceType, Cell.Colour pieceColour) {
        switch(pieceType) {
            case "Pawn":
                this.piece = new Pawn(pieceColour);
                break;
            case "Knight":
                this.piece = new Knight(pieceColour);
                break;
            case "King":
                this.piece = new King(pieceColour);
                break;
            case "Bishop":
                this.piece = new Bishop(pieceColour);
                break;
            case "Queen":
                this.piece = new Queen(pieceColour);
                break;
            case "Rook":
                this.piece = new Rook(pieceColour);
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

    public void paint(Graphics g) {
        super.paintComponent(g);

        if (colour == Colour.white)
            g.setColor(new Color(221, 192, 127));
        else
            g.setColor(new Color(85, 76, 76));

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

    public Point getPosition() {
        return position;
    }
}
