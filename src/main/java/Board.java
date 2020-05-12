import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements MouseListener {
    private final Cell[][] cells;
    private final int size = 8;
    private final int windowHeight = 720;
    private final int windowWidth = 720;
    private final Dimension windowSize = new Dimension(windowWidth, windowHeight);

    private boolean whiteTurn;
    private Cell clickedCell;

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
                cell.setPosition(x, y);
                cell.board = this;
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

        whiteTurn = true;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void switchTurn(){
        this.whiteTurn = !this.whiteTurn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        for(int y = 0; y < size; ++y){
            for(int x = 0; x < size; ++x) {
                cells[y][x].paint(g);
            }
        }
    }

    public boolean moveIfPossible(Cell start, Cell destination){ // moves piece if not contradicted by rules
        boolean moveable = start.getPiece().isAppropriateMove(destination);
        if(moveable){
            Piece movedPiece = start.getPiece();
            start.setPiece(null);
            //remove piece image from cell
            destination.setPiece(movedPiece);
            //add piece image to cell
            return true;
        }
        return false;
    }

    //@Override
    // this function is invoked when the mouse button has been clicked (pressed and released) on a component
    public void mouseClicked(MouseEvent e){
        Cell clicked = (Cell) getComponentAt(new Point(e.getX(), e.getY()));
        boolean isMoved = false, isMoved2 = false;

        if(clickedCell == null){ //player hasn't chosen cell to move yet
            if(clicked.getOccupation() == true){
                if(clicked.getPiece().getColourAsString() == "White" && whiteTurn
                    || clicked.getPiece().getColourAsString() == "Black" && !whiteTurn){
                    this.clickedCell = clicked;
                }
            }
        } else { // player clicked mouse when some cell is chosen
            if(clicked.getOccupation() == false){
                isMoved = moveIfPossible(clickedCell, clicked); //movePiece from clickedCell to clicked
                this.clickedCell = null;
            }
            if(clicked.getOccupation() == true){
                if(clicked.getPiece().getColourAsString() == "White" && !whiteTurn
                        || clicked.getPiece().getColourAsString() == "Black" && whiteTurn){
                    isMoved2 = moveIfPossible(clickedCell, clicked); //beat clicked with clickedCell
                    this.clickedCell = null;
                }
                if(clicked.getPiece().getColourAsString() == "White" && whiteTurn
                        || clicked.getPiece().getColourAsString() == "Black" && !whiteTurn){
                    this.clickedCell = clicked;
                }
            }
            if(isMoved || isMoved2) {
                switchTurn();
                // check for checkmate situation
            }
        }
    }

    // this function is invoked when the mouse enters a component
    public void mouseEntered(MouseEvent e) {}

    // this function is invoked when the mouse exits the component
    public void mouseExited(MouseEvent e) {}

    // this function is invoked when the mouse button has been pressed on a component
    public void mousePressed(MouseEvent e) {}

    // this function is invoked when the mouse button has been released on a component
    public void mouseReleased(MouseEvent e) {}





  /*  public static void main(String s[])
    {
        System.out.println("Creating new board...");
        new Board("Test chess board");
        System.out.println("New board created");
    }*/
}
