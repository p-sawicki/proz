import javax.swing.*;
import java.awt.*;

public class Cell extends JComponent {
    private CellColor color;

    public enum CellColor {white, black}

    public Cell(CellColor color){
        this.color = color;

        setBorder(BorderFactory.createEmptyBorder());
    }

    public void paint(Graphics g){
        super.paintComponent(g);

        if(color == CellColor.white)
            g.setColor(new Color(221, 192, 127));
        else
            g.setColor(new Color(101, 67, 33));

        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }
}
