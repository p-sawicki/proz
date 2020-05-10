public class Queen extends Piece{
    public Queen(Cell.Colour colour) {
        super(colour);
    }

    @Override
    public String getName() {
        return "Queen";
    }
}
