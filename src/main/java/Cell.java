import javax.swing.*;
import java.awt.*;

public class Cell extends JComponent {
    private final Colour colour;
    private Piece piece;
    protected Board* boardptr;
    Pair<short, short> position;

    public enum Colour {white, black}

    public Cell(Colour color){
        this.colour = color;

        setBorder(BorderFactory.createEmptyBorder());
    }

    public void setPiece(Piece piece){
        this.piece = piece;
        piece.setCell(this);
    }

    public Piece getPiece() {
        return this.piece;
    }

    public boolean getOccupation() {
        return isOccupied = this.getPiece() == null ? 0 : 1;
    }

    public void setPosition(short x, short y){
        this.position = new Pair<short, short>(x, y);
    }

    public Pair<short, short> getPosition() {
        return this.position;
    }


    public void paint(Graphics g){
        super.paintComponent(g);

        if(colour == Colour.white)
            g.setColor(new Color(221, 192, 127));
        else
            g.setColor(new Color(101, 67, 33));

        g.fillRect(getX(), getY(), getWidth(), getHeight());
        if(piece != null)
            piece.draw(g);
    }
}
