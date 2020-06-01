package chess.mechanics;

import chess.utilities.Utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Piece {
    protected final Cell.Colour colour;
    protected Cell cell;
    protected BufferedImage image;
    protected ArrayList<Move> possibleMoves;
    protected boolean hasMoved;

    public Piece(Cell.Colour colour) {
        this(colour, false);
    }

    public Piece(Cell.Colour colour, boolean hasMoved) {
        this.colour = colour;
        try {
            image = ImageIO.read(getImageFile());
        } catch (IOException e) {
            System.out.println("Could not open image: " + e.getMessage());
        }
        possibleMoves = new ArrayList<>();
        this.hasMoved = hasMoved;
    }

    public abstract Piece copy();

    public void draw(Graphics g) {
        g.drawImage(image, cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight(), null);
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }

    public boolean getHasMoved(){
        return hasMoved;
    }

    public void clearMoves() {
        possibleMoves.clear();
    }

    protected ArrayList<Move> trimPossibleMoves(ArrayList<Move> moves) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for (Move move : moves) {
            int y = move.after.y;
            int x = move.after.x;
            if (y >= 0 && y < cell.getBoard().getBoardSize()) {
                if (x >= 0 && x < cell.getBoard().getBoardSize()) {
                    Piece piece = cell.getBoard().getCells()[y][x].getPiece();
                    if (piece == null || piece.getColour() != colour) {
                        possibleMoves.add(move);
                    }
                }
            }
        }
        return removeMovesThatLeadToCheck(possibleMoves);
    }

    protected ArrayList<Move> removeMovesThatLeadToCheck(ArrayList<Move> moves){
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for(Move move : moves){
            if(!CheckDetector.isPlayerChecked(cell.getBoard(), move))
                possibleMoves.add(move);
        }
        return possibleMoves;
    }

    private boolean addIfLegal(ArrayList<Move> moves, Point start, int x, int y) {
        Piece piece = cell.getBoard().getCells()[y][x].getPiece();
        if (piece != null) {
            if (piece.getColour() != colour)
                moves.add(new Move(start, piece.getCell().getPosition()));
            return false;
        }
        moves.add(new Move(start, new Point(x, y)));
        return true;
    }

    protected ArrayList<Move> getDiagonalMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        Point start = cell.getPosition();
        int y = start.y - 1;
        int x = start.x - 1;
        while (y >= 0 && x >= 0 && addIfLegal(moves, start, x--, y--)) {
        }
        y = start.y - 1;
        x = start.x + 1;
        while (y >= 0 && x < cell.getBoard().getBoardSize() && addIfLegal(moves, start, x++, y--)) {
        }
        y = start.y + 1;
        x = start.x - 1;
        while (y < cell.getBoard().getBoardSize() && x >= 0 && addIfLegal(moves, start, x--, y++)) {
        }
        y = start.y + 1;
        x = start.x + 1;
        while (y < cell.getBoard().getBoardSize() && x < cell.getBoard().getBoardSize() && addIfLegal(moves, start, x++, y++)) {
        }
        return moves;
    }

    protected ArrayList<Move> getVerticalMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        Point start = cell.getPosition();
        int x = start.x;
        for (int y = cell.getPosition().y - 1; y >= 0 && addIfLegal(moves, start, x, y); --y) {
        }
        for (int y = cell.getPosition().y + 1; y < cell.getBoard().getBoardSize() && addIfLegal(moves, start, x, y); ++y) {
        }
        return moves;
    }

    protected ArrayList<Move> getHorizontalMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        Point start = cell.getPosition();
        int y = start.y;
        for (int x = cell.getPosition().x - 1; x >= 0 && addIfLegal(moves, start, x, y); --x) {
        }
        for (int x = cell.getPosition().x + 1; x < cell.getBoard().getBoardSize() && addIfLegal(moves, start, x, y); ++x) {
        }
        return moves;
    }

    public Cell.Colour getColour() {
        return colour;
    }

    protected abstract ArrayList<Move> getPossibleMoves();

    public ArrayList<Move> getMoves() {
        if (possibleMoves.isEmpty())
            possibleMoves = getPossibleMoves();
        return possibleMoves;
    }

    public File getImageFile() {
        return new File(Utility.getResourcePath() + getColourAsString() + getName() + ".png");
    }

    public String getColourAsString() {
        return colour == Cell.Colour.black ? "Black" : "White";
    }

    public abstract String getName();

    public void hasMoved(){
        hasMoved = true;
    }

    public void hasNotMoved() {
        hasMoved = false;
    }

    public boolean isAppropriateMove(Cell destination) { //checks if piece selected by player can be moved to selected cell
        for (Move move : getMoves())
            if (move.after.x == destination.getPosition().x && move.after.y == destination.getPosition().y)
                return true;
        return false;
    }
}
