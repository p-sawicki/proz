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
}
