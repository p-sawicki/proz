import javax.swing.*;
import java.awt.*;

public class Menu implements Runnable{
    private final int windowHeight = 720;
    private final int windowWidth = 720;
    private final Dimension windowSize = new Dimension(windowWidth, windowHeight);
    public void run(){
        final JFrame window = new JFrame("Menu");
        window.setSize(windowSize);

        final JButton startButton = new JButton("Start");

        startButton.addActionListener(e -> {
            new GameWindow();
            window.dispose();
        });

        window.add(startButton);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }
}
