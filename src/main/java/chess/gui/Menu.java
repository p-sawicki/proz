package chess.gui;

import chess.actions.ActionAboutGame;
import chess.actions.ActionNewGame;
import chess.actions.ActionOpen;
import chess.network.MenuConnectionHandler;
import chess.utilities.Utility;

import javax.swing.*;
import java.awt.*;

public class Menu implements Runnable {
    private final int windowHeight = 720;
    private final int windowWidth = 720;
    private final Dimension windowSize = new Dimension(windowWidth, windowHeight);
    private final MenuConnectionHandler menuConnectionHandler;
    private JFrame menuWindow;

    public Menu() {
        menuConnectionHandler = new MenuConnectionHandler(this);
        menuConnectionHandler.start();
    }

    public void run() {
        // window parameters
        menuWindow = new JFrame("Menu");
        menuWindow.setSize(windowSize);
        menuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuWindow.setVisible(true);

        setBackgroundImage(menuWindow);
        addButtons(menuWindow);
    }

    private void setBackgroundImage(JFrame window) {
        window.setContentPane(
                new JLabel(new ImageIcon(Utility.getResourcePath("menuBackground.png"))));
    }

    private void addButtons(JFrame window) {
        // initialize buttons
        final JButton newGameButton = createMainMenuButton("New game");
        final JButton loadGameButton = createMainMenuButton("Load game");
        final JButton aboutGameButton = createMainMenuButton("About game");
        final JButton quitGameButton = createMainMenuButton("Quit game");

        // buttons' listeners
        newGameButton.addActionListener(new ActionNewGame(this));
        loadGameButton.addActionListener(new ActionOpen(this));
        aboutGameButton.addActionListener(new ActionAboutGame());
        quitGameButton.addActionListener(e -> System.exit(0));

        // initialize button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));
        buttonPanel.setSize(new Dimension(200, 200));
        buttonPanel.setLocation(260, 300);
        buttonPanel.setVisible(true);
        buttonPanel.setOpaque(false);

        buttonPanel.add(newGameButton);
        buttonPanel.add(loadGameButton);
        buttonPanel.add(aboutGameButton);
        buttonPanel.add(quitGameButton);
        window.add(buttonPanel);
    }

    private JButton createMainMenuButton(String buttonText) {
        JButton newButton = new JButton(buttonText);
        newButton.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        newButton.setForeground(new Color(85, 76, 76));
        return newButton;
    }

    public JFrame getMenuWindow() {
        return menuWindow;
    }

    public MenuConnectionHandler getMenuConnectionHandler() {
        return menuConnectionHandler;
    }
}
