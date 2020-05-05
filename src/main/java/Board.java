import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    private final Cell[][] cells;
    private final int size = 8;
    private final int windowHeight = 720;
    private final int windowWidth = 720;
    private final Dimension windowSize = new Dimension(windowWidth, windowHeight);

    public Board(){
        cells = new Cell[size][size];
        setLayout(new GridLayout(size, size, 0, 0));

        Cell.CellColor color = Cell.CellColor.black;
        for(int y = 0; y < size; ++y){
            for(int x = 0; x < size; ++x){
                Cell cell = new Cell(color);
                cells[x][y] = cell;
                add(cell);

                if(x != size - 1)
                    color = color == Cell.CellColor.black ? Cell.CellColor.white : Cell.CellColor.black;
            }
        }

        setMinimumSize(windowSize);
        setPreferredSize(windowSize);
        setSize(windowSize);
    }

    @Override
    protected void paintComponent(Graphics g) {
        for(int y = 0; y < size; ++y){
            for(int x = 0; x < size; ++x)
                cells[x][y].paint(g);
        }
    }
}
