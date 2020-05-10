import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    private final Cell[][] cells;
    private final int size = 8;
    private final int windowHeight = 720;
    private final int windowWidth = 720;
    private final Dimension windowSize = new Dimension(windowWidth, windowHeight);

    //starting board state for testing
    private final Piece[][] pieces = {
            {new Rook(Cell.Colour.black), new Knight(Cell.Colour.black), new Bishop(Cell.Colour.black), new Queen(Cell.Colour.black),
                    new King(Cell.Colour.black), new Bishop(Cell.Colour.black), new Knight(Cell.Colour.black), new Rook(Cell.Colour.black)},
            {new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black),
                    new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black)},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white),
                    new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white)},
            {new Rook(Cell.Colour.white), new Knight(Cell.Colour.white), new Bishop(Cell.Colour.white), new Queen(Cell.Colour.white),
                    new King(Cell.Colour.white), new Bishop(Cell.Colour.white), new Knight(Cell.Colour.white), new Rook(Cell.Colour.white)}
    };

    public Board(){
        cells = new Cell[size][size];
        setLayout(new GridLayout(size, size, 0, 0));

        Cell.Colour color = Cell.Colour.black;
        for(int y = 0; y < size; ++y){
            for(int x = 0; x < size; ++x){
                Cell cell = new Cell(color);
                cells[y][x] = cell;
                add(cell);

                if(pieces[y][x] != null)
                    cell.setPiece(pieces[y][x]);

                if(x != size - 1)
                    color = color == Cell.Colour.black ? Cell.Colour.white : Cell.Colour.black;
            }
        }

        setMinimumSize(windowSize);
        setPreferredSize(windowSize);
        setSize(windowSize);
    }

    @Override
    protected void paintComponent(Graphics g) {
        for(int y = 0; y < size; ++y){
            for(int x = 0; x < size; ++x) {
                cells[y][x].paint(g);
            }
        }
    }
}
