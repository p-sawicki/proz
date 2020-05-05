import javax.swing.*;
import java.awt.*;

public class GameWindow {
    private JFrame window;
    private Board board;
    private final int windowHeight = 720;
    private final int windowWidth = 720;
    private final Dimension windowSize = new Dimension(windowWidth, windowHeight);

    public GameWindow(){
        window = new JFrame("Chess");
        window.setSize(windowSize);

        window.setLayout(new BorderLayout(10, 10));

        board = new Board();

        window.add(board, BorderLayout.CENTER);

        window.pack();
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
