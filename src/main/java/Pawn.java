public class Pawn extends Piece{
    public Pawn(Cell.Colour colour) {
        super(colour);
    }

    @Override
    public String getName() {
        return "Pawn";
    }
}
