import javax.swing.*;
import java.awt.*;

public class GameWindow {
    private JFrame window;
    private Board board;

    public GameWindow(){
        window = new JFrame("Chess");

        window.setLayout(new BorderLayout(10, 10));

        board = new Board(Cell.Colour.white);

        window.add(board, BorderLayout.CENTER);

        window.pack();
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
