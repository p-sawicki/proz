import javax.swing.*;
import java.awt.*;

public class Cell extends JComponent {
    private final Colour colour;
    private Piece piece;
    protected Board* boardptr;
    private Pair<int, int> position;

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

    public void setPosition(int x, int y){
        this.position = new Pair<int, int>(x, y);
    }

    public Pair<int, int> getPosition() {
        return this.position;
    }

    public boolean getOccupation() {
        return isOccupied = this.getPiece() == null ? 0 : 1;
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
