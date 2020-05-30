package chess.gui;

import chess.network.*;
import chess.utilities.*;
import chess.actions.*;
import chess.mechanics.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;


public class Menu implements Runnable {
    private final int windowHeight = 720;
    private final int windowWidth = 720;
    private final Dimension windowSize = new Dimension(windowWidth, windowHeight);
    private final MenuConnectionHandler menuConnectionHandler;
    private JFrame menuWindow;
    private JLabel spacerLabel;
    private GameAttributes gameAttributes;

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

        this.gameAttributes = new GameAttributes();
    }

    private void setBackgroundImage(JFrame window) {
        try {
            window.setContentPane(
                    new JLabel(new ImageIcon(ImageIO.read(new File(Utility.getResourcePath() + "menuBackground.png")))));
        } catch (IOException e) {
            System.out.println("Could not open image: " + e.getMessage());
        }
        ;
    }

    private void addButtons(JFrame window) {
        // initialize buttons
        final JButton newGameButton = createMainMenuButton("New game");
        final JButton loadGameButton = createMainMenuButton("Load game");
        final JButton aboutGameButton = createMainMenuButton("About game");
        final JButton quitGameButton = createMainMenuButton("Quit game");

        // buttons' listeners
        newGameButton.addActionListener(e -> {
            setGameType();
            window.dispose();
        });
        loadGameButton.addActionListener(new ActionOpen(this));
        aboutGameButton.addActionListener(e -> {
            openGameDescription();
        });
        quitGameButton.addActionListener(e -> {
            System.exit(0);
        });

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

    private void setGameType() {
        // window parameters
        JFrame gameParametersWindow = createGameParametersWindow(3,1);

        // window objects
        JLabel enterNameLabel = new JLabel("  Please select game type:");
        JButton singlePlayerButton = new JButton("Single player");
        JButton multiPlayerButton = new JButton("Multiplayer");

        gameParametersWindow.add(enterNameLabel);
        gameParametersWindow.add(singlePlayerButton);
        gameParametersWindow.add(multiPlayerButton);

        // buttons' listeners
        singlePlayerButton.addActionListener(e -> {
            gameAttributes.setSinglePlayer(true);
            startNewGame(true);
            gameParametersWindow.dispose();
        });
        multiPlayerButton.addActionListener(e -> {
            gameAttributes.setSinglePlayer(false);
            startNewGame(false);
            gameParametersWindow.dispose();
        });
    }

    public void startNewGame(boolean singlePlayer) { // asks for player name, opponent IP
        JFrame gameParametersWindow = createGameParametersWindow(3, 2);

        // window objects
        JLabel enterNameLabel = new JLabel("  Please enter your name:");
        JTextField nameField = new JTextField();
        JLabel enterIPLabel = new JLabel("  Please enter opponent IP:");
        JTextField ipField = new JTextField();
        spacerLabel = new JLabel("");
        JButton startButton = new JButton("Start");

        gameParametersWindow.add(enterNameLabel);
        gameParametersWindow.add(nameField);

        if (!singlePlayer) {
            gameParametersWindow.add(enterIPLabel);
            gameParametersWindow.add(ipField);
        }

        gameParametersWindow.add(spacerLabel);
        gameParametersWindow.add(startButton);

        // button listener
        startButton.addActionListener(e -> {
            String playerName = nameField.getText();
            if (playerName.equals("")) {
                System.out.println("player hasn't entered his name");
                return;
            }
            String opponentIP = null;
            if (!singlePlayer) {
                opponentIP = ipField.getText();
            }
            gameAttributes.setPlayerName(playerName);
            gameAttributes.setOpponentIP(opponentIP);
            if (opponentIP == null) {
                menuConnectionHandler.stopReceiving();
                new GameWindow(gameAttributes);
            } else {
                spacerLabel.setText("Waiting...");
                menuConnectionHandler.challenge(playerName, opponentIP);
            }
            gameParametersWindow.dispose();
        });
    }

    public void resumeSavedGame(GameAttributes savedGameAttributes) { // resumes saved game
        JFrame gameParametersWindow = createGameParametersWindow(2, 2);

        // window objects
        JLabel enterNameLabel = new JLabel("  Your name:");
        JLabel nameField = new JLabel(savedGameAttributes.getPlayerName());
        JLabel spacerLabel = new JLabel("");
        JButton startButton = new JButton("Start");

        gameParametersWindow.add(enterNameLabel);
        gameParametersWindow.add(nameField);
        gameParametersWindow.add(spacerLabel);
        gameParametersWindow.add(startButton);

        // button listener
        startButton.addActionListener(e -> {
                menuConnectionHandler.stopReceiving();
                new GameWindow(savedGameAttributes);
        });
    }

    public void openGameDescription() {
        final JFrame descriptionWindow = new JFrame("About program");

        ImageIcon icon = new ImageIcon(Utility.getResourcePath() + "gameLogo.png");
        System.out.println("Trying to get game loga at: " + Utility.getResourcePath() + "gameLogo.png");
        JOptionPane.showMessageDialog(descriptionWindow,
                "\n" + "Program title: \"Chess\"" + "\n\n" + "Version: 1.0" + "\n\n" + "Authors: Piotr Sawicki, Vladyslav Kyryk" + "\n",
                "About program",
                JOptionPane.INFORMATION_MESSAGE, icon);
    }

    public void onOpponentChallenge(String opponentName, String opponentAddress) {
        final JFrame challengeWindow = new JFrame("Challenge Window");
        challengeWindow.setSize(new Dimension(500, 100));
        challengeWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        challengeWindow.setVisible(true);
        challengeWindow.setLayout(new GridLayout(2, 2));

        challengeWindow.add(new JLabel("You've been challenged to a game!"));
        challengeWindow.add(new JLabel("Opponent name: " + opponentName + " IP: " + opponentAddress));
        JButton acceptButton = new JButton("Accept");
        JButton declineButton = new JButton("Decline");
        challengeWindow.add(acceptButton);
        challengeWindow.add(declineButton);

        acceptButton.addActionListener(e -> {
            gameAttributes.setOpponentName(opponentName);
            gameAttributes.setOpponentIP(opponentAddress);

            Cell.Colour myColour = (new Random()).nextInt(2) == 0 ? Cell.Colour.black : Cell.Colour.white;
            gameAttributes.setPlayerColour(myColour);
            enterPlayerName();
            menuConnectionHandler.accept(opponentAddress, myColour == Cell.Colour.black ? Cell.Colour.white : Cell.Colour.black, gameAttributes.getPlayerName());
            gameAttributes.setConnectionHandler(new ConnectionHandler(opponentAddress, null));
            new GameWindow(gameAttributes);
            menuConnectionHandler.stopReceiving(); // stop receiving because player is in game
            challengeWindow.dispose();
            menuWindow.dispose();
        });

        declineButton.addActionListener(e -> {
            menuConnectionHandler.decline(opponentAddress);
            challengeWindow.dispose();
        });
    }

    public void onChallengeAccepted(Cell.Colour colour, String opponentAddress, String opponentName) {
        gameAttributes.setPlayerColour(colour);
        gameAttributes.setConnectionHandler(new ConnectionHandler(opponentAddress, null));
        new GameWindow(this.gameAttributes);
        menuConnectionHandler.stopReceiving();
    }

    public void onChallengeDeclined() {
        spacerLabel.setText("Declined!");
    }

    public void enterPlayerName() {
        JFrame gameParametersWindow = createGameParametersWindow(2, 2);

        // window objects
        JLabel enterNameLabel = new JLabel("  Please enter your name:");
        JTextField nameField = new JTextField();
        spacerLabel = new JLabel("");
        JButton startButton = new JButton("Start");

        gameParametersWindow.add(enterNameLabel);
        gameParametersWindow.add(nameField);
        gameParametersWindow.add(spacerLabel);
        gameParametersWindow.add(startButton);

        // button listener
        startButton.addActionListener(e -> {
            String name = nameField.getText();
            if (name.equals("")) {
                System.out.println("player hasn't entered his name");
                return;
            }
            gameAttributes.setPlayerName(name);
            gameParametersWindow.dispose();
        });
    }

    public JFrame getMenuWindow() {
        return menuWindow;
    }

    public JFrame createGameParametersWindow(int rows, int columns) {
        JFrame gameParametersWindow = new JFrame("Game Parameters");

        // window parameters
        gameParametersWindow.setSize(new Dimension(400, 100));
        gameParametersWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameParametersWindow.setVisible(true);
        gameParametersWindow.setLayout(new GridLayout(rows, columns));
        return gameParametersWindow;
    }
}
