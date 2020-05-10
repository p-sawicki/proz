import javax.swing.*;
import java.awt.*;

public class Cell extends JComponent {
    private final Colour colour;
    private Piece piece;

    public enum Colour {white, black}

    public Cell(Colour color){
        this.colour = color;

        setBorder(BorderFactory.createEmptyBorder());
    }

    public void setPiece(Piece piece){
        this.piece = piece;
        piece.setCell(this);
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
