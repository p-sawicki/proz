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
    protected Point position;

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

    public void clearMoves() {
        possibleMoves.clear();
    }

    protected ArrayList<Move> trimPossibleMoves(ArrayList<Move> moves) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for (Move move : moves) {
            int y = move.after.y;
            int x = move.after.x;
            if (y >= 0 && y < getBoardSize()) {
                if (x >= 0 && x < getBoardSize()) {
                    Piece piece = findPiece(x, y);
                    if (piece == null || piece.getColour() != colour)
                        possibleMoves.add(move);
                }
            }
        }
        return removeMovesThatLeadToCheck(possibleMoves);
    }

    protected ArrayList<Move> removeMovesThatLeadToCheck(ArrayList<Move> moves) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for (Move move : moves) {
            if (!CheckDetector.doesMoveLeadToCheck(cell.getBoard(), move))
                possibleMoves.add(move);
        }
        return possibleMoves;
    }

    private boolean addIfLegal(ArrayList<Move> moves, int x, int y) {
        Piece piece = findPiece(x, y);
        if (piece != null) {
            if (piece.getColour() != colour)
                moves.add(new Move(position, x, y));
            return false;
        }
        moves.add(new Move(position, x, y));
        return true;
    }

    protected ArrayList<Move> getDiagonalMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        int y = position.y - 1;
        int x = position.x - 1;
        while (y >= 0 && x >= 0 && addIfLegal(moves, x--, y--)) {
        }
        y = position.y - 1;
        x = position.x + 1;
        while (y >= 0 && x < getBoardSize() && addIfLegal(moves, x++, y--)) {
        }
        y = position.y + 1;
        x = position.x - 1;
        while (y < getBoardSize() && x >= 0 && addIfLegal(moves, x--, y++)) {
        }
        y = position.y + 1;
        x = position.x + 1;
        while (y < getBoardSize() && x < getBoardSize() && addIfLegal(moves, x++, y++)) {
        }
        return moves;
    }

    protected ArrayList<Move> getVerticalMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        int x = position.x;
        for (int y = position.y - 1; y >= 0 && addIfLegal(moves, x, y); --y) {
        }
        for (int y = position.y + 1; y < getBoardSize() && addIfLegal(moves, x, y); ++y) {
        }
        return moves;
    }

    protected ArrayList<Move> getHorizontalMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        int y = position.y;
        for (int x = position.x - 1; x >= 0 && addIfLegal(moves, x, y); --x) {
        }
        for (int x = position.x + 1; x < getBoardSize() && addIfLegal(moves, x, y); ++x) {
        }
        return moves;
    }

    public boolean isLegalMove(Cell destination) { //checks if piece selected by player can be moved to selected cell
        ArrayList<Move> moves = getMoves();
        for (Move move : moves)
            if (move.after.x == destination.getPosition().x && move.after.y == destination.getPosition().y)
                return true;
        return false;
    }

    protected Piece findPiece(int x, int y) {
        return cell.getBoard().getCells()[y][x].getPiece();
    }

    public Cell getCell() {
        return cell;
    }

    public Point getPosition() {
        return position;
    }

    public int getBoardSize() {
        return cell.getBoard().getBoardSize();
    }

    public boolean getHasMoved() {
        return hasMoved;
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

    public void hasMoved() {
        hasMoved = true;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
        if (cell != null)
            position = cell.getPosition();
    }
}
