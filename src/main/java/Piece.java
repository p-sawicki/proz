import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

    private String getColourAsString(){
        return colour == Cell.Colour.black ? "Black" : "White";
    }

    public abstract String getName();

    public abstract boolean isAppropriateMove(Cell destination); //checks if piece selected by player can be moved to selected cell

    public boolean checkIfPathIsClear(Cell start, Cell destinaltion){ // checks if cells between start and destionation are not occupied
        Pair<short, short> startPos = start.getPosition();
        startX = startPos.getValue0();
        startY = startPos.getValue1();
        Pair<short, short> desPos = destination.getPosition();
        destX = desPos.getValue0();
        destY = desPos.getValue1();
        Cell[][] cells = start.boardptr.getCells();

        if(startX == destX){ // vertical movement
            destY > startY ? s=startY, d=destY : d=startY, s=destY;
            for(int k = s+1; k < d; k++){
                if(cells[k][destX].getOccupation() == 1)
                    return 0;
            }
        }
        if(startY == destY){ // horizontal movement
            destX > startX ? s=startX, d=destX : d=startX, s=destX;
            for(int k = s+1; k < d; k++){
                if(cells[destY][k].getOccupation() == 1)
                    return 0;
            }
        }
        if(startX != destX && startY != destY) { // diagonal movement
            if( (destX > startX && destY > startY) || (destX < startX && destY < startY) ) {
                destX > startX ? s = startX, d = destX :d = startX, s = destX; // s - lowest X, d - highest X
                destY > startY ? m = startY, t = destY :t = startY, m = destY; // m - lowest Y, t - highest Y
                for (int k = s + 1, int l = m + 1; k<d && l<t; k++, l++){ //increase X, increase Y
                    if (cells[l][k].getOccupation() == 1)
                        return 0;
                }
            }
            else{
                destX > startX ? s = startX, d = destX :d = startX, s = destX; // s - lowest X, d - highest X
                destY > startY ? m = startY, t = destY :t = startY, m = destY; // m - lowest Y, t - highest Y
                for (int k = s + 1, int l = t - 1; k<d && l>m; k++, l--){ //increase X, decrease Y
                    if (cells[l][k].getOccupation() == 1)
                        return 0;
                }
            }
        }
        return 1;
    }
}
