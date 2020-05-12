import javax.swing.*;
import java.awt.*;
import javafx.util.Pair;

public class Cell extends JComponent {
    private final Colour colour;
    private Piece piece;
    protected Board board;
    private Pair<Integer, Integer> position;

    public enum Colour {white, black}

    public Cell(Colour color) {
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
        this.position = new Pair<Integer, Integer>(x, y);
    }

    public Pair<Integer, Integer> getPosition() {
        return position;
    }

    public boolean getOccupation() {
        return this.getPiece() == null ? false : true;
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
