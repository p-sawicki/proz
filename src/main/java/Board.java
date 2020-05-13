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
    private Point clickedCellPosition = new Point(-1, -1);
    private final Point nullPosition = new Point(-1, -1);

    //starting board state for testing
    private final Piece[][] pieces = {
            {new Rook(Cell.Colour.white), new Knight(Cell.Colour.white), new Bishop(Cell.Colour.white), new Queen(Cell.Colour.white),
                    new King(Cell.Colour.white), new Bishop(Cell.Colour.white), new Knight(Cell.Colour.white), new Rook(Cell.Colour.white)},
            {new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white),
                    new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white)},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black),
                    new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black)},
            {new Rook(Cell.Colour.black), new Knight(Cell.Colour.black), new Bishop(Cell.Colour.black), new Queen(Cell.Colour.black),
                    new King(Cell.Colour.black), new Bishop(Cell.Colour.black), new Knight(Cell.Colour.black), new Rook(Cell.Colour.black)}
    };

    public Board(){
        cells = new Cell[size][size];
        setLayout(new GridLayout(size, size, 0, 0));

        this.addMouseListener(this);

        Cell.Colour color = Cell.Colour.black;
        for(int y = size - 1; y >= 0; --y) {
            for(int x = 0; x < size; ++x) {
                Cell cell = new Cell(color, this, new Point(x, y));
                cells[y][x] = cell;
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
        String currentTurn = whiteTurn ? "White" : "Black";
        System.out.println(currentTurn + " turn");
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
        System.out.println("moveIfPossible function called for element " + start.getPieceNameColor()
                + " from cell X:" + start.getPosition().x + ", Y:" + start.getPosition().y
                + " to cell X:" + destination.getPosition().x + ", Y:" + destination.getPosition().y);
        if(!start.getOccupation()) {
            System.out.println("start cell not occupied, position: " + start.getPosition().toString());
            return false;
        }

        boolean moveable = start.getPiece().isAppropriateMove(destination);
        if(moveable) {
            Piece movedPiece = start.getPiece();
            start.removePiece();
            destination.setPiece(movedPiece);
            repaint();
            return true;
        }
        return false;
    }

    //@Override
    // this function is invoked when the mouse button has been clicked (pressed and released) on a component
    public void mouseClicked(MouseEvent e){
        Cell clicked = (Cell) getComponentAt(new Point(e.getX(), e.getY()));
        Point clickedPosition = clicked.getPosition();
        boolean isMoved = false, isMoved2 = false;
        System.out.println("Mouse clicked on the component " + clicked.getPieceNameColor());
        int posX = clickedPosition.x;
        int posY = clickedPosition.y;
        System.out.println("posX = " + posX + ", posY = " + posY);

        if(clickedCellPosition.equals(nullPosition)){ //player hasn't chosen cell to move yet
            if(clicked.getOccupation()) {
                System.out.println("Mouse clicked to choose component");
                if(clicked.getPiece().getColourAsString().equals("White") && whiteTurn
                    || clicked.getPiece().getColourAsString().equals("Black") && !whiteTurn){
                    this.clickedCellPosition = clickedPosition;
                }
            }
        } else { // player clicked mouse when some cell is chosen
            int prevX = clickedCellPosition.x;
            int prevY = clickedCellPosition.y;

            if(!clicked.getOccupation()) {
                System.out.println("Mouse clicked to move component to vacant cell");

                isMoved = moveIfPossible(cells[prevY][prevX], clicked); //movePiece from clickedCell to clicked
                this.clickedCellPosition = nullPosition;
            }
            if(clicked.getOccupation()){
                if(clicked.getPiece().getColourAsString().equals("White") && !whiteTurn
                        || clicked.getPiece().getColourAsString().equals("Black") && whiteTurn){
                    System.out.println("Mouse clicked to beat component");
                    isMoved2 = moveIfPossible(cells[prevY][prevX], clicked); //beat clicked with clickedCell
                    this.clickedCellPosition = nullPosition;
                }
                if(clicked.getPiece().getColourAsString().equals("White") && whiteTurn
                        || clicked.getPiece().getColourAsString().equals("Black") && !whiteTurn){
                    System.out.println("Mouse clicked to change component to move");
                    this.clickedCellPosition = clickedPosition;
                }
            }
            if(isMoved || isMoved2) {
                System.out.println("One of the components was moved");
                switchTurn();
                // check for checkmate situation
            }
        }
        repaint();
    }

    // this function is invoked when the mouse enters a component
    public void mouseEntered(MouseEvent e) {}

    // this function is invoked when the mouse exits the component
    public void mouseExited(MouseEvent e) {}

    // this function is invoked when the mouse button has been pressed on a component
    public void mousePressed(MouseEvent e) {}

    // this function is invoked when the mouse button has been released on a component
    public void mouseReleased(MouseEvent e) {}
}
