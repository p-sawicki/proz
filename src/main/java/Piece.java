import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.util.Pair;

public abstract class Piece {
    protected final Cell.Colour colour;
    protected Cell cell;
    protected BufferedImage image;

    public Piece(Cell.Colour colour){
        this.colour = colour;
        try {
            image = ImageIO.read(getImageFile());
        }
        catch(IOException e){
            System.out.println("Could not open image: " + e.getMessage());
        }
    }

    public void draw(Graphics g){
        g.drawImage(image, cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight(), null);
    }

    public void setCell(Cell cell){
        this.cell = cell;
    }

    private File getImageFile(){
        return new File("src/main/resources/alpha/alpha/320/" + getColourAsString() + getName() + ".png");
    }

    protected String getColourAsString(){
        return colour == Cell.Colour.black ? "Black" : "White";
    }

    public abstract String getName();

    public abstract boolean isAppropriateMove(Cell destination); //checks if piece selected by player can be moved to selected cell

    public boolean checkIfPathIsClear(Cell start, Cell destination){ // checks if cells between start and destionation are not occupied
        Pair<Integer, Integer> startPos = start.getPosition();
        int startX = startPos.getValue();
        int startY = startPos.getKey();
        Pair<Integer, Integer> desPos = destination.getPosition();
        int destX = desPos.getValue();
        int destY = desPos.getKey();
        Cell[][] cells = start.board.getCells();
        int s,d, m, t;

        if(startX == destX){ // vertical movement
            s = destY > startY ? startY : destY;
            d = destY > startY ? destY : startY;
            for(int k = s+1; k < d; k++){
                if(cells[k][destX].getOccupation() == true)
                    return false;
            }
        }
        if(startY == destY){ // horizontal movement
            s = destX > startX ? startX : destX;
            d = destX > startX ? destX : startX;
            for(int k = s+1; k < d; k++){
                if(cells[destY][k].getOccupation() == true)
                    return false;
            }
        }
        if(startX != destX && startY != destY) { // diagonal movement
            if( (destX > startX && destY > startY) || (destX < startX && destY < startY) ) {
                s = destX > startX ? startX : destX; // s - lowest X, d - highest X
                d = destX > startX ? destX : startX;
                m = destY > startY ? startY : destY; // m - lowest Y, t - highest Y
                t = destY > startY ? destY : startY;
                int l = m + 1;
                for (int k = s + 1; k<d; k++){ //increase X, increase Y
                    if (cells[l][k].getOccupation() == true)
                        return false;
                    l++;
                }
            }
            else{
                s = destX > startX ? startX : destX; // s - lowest X, d - highest X
                d = destX > startX ? destX : startX;
                m = destY > startY ? startY : destY; // m - lowest Y, t - highest Y
                t = destY > startY ? destY : startY;
                int l = t - 1;
                for (int k = s + 1; k<d; k++){ //increase X, decrease Y
                    if (cells[l][k].getOccupation() == true)
                        return false;
                    l--;
                }
            }
        }
        return true;
    }
}
