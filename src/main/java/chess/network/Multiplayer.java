package chess.network;

import chess.gui.GameWindow;
import chess.gui.Menu;
import chess.mechanics.Cell;
import chess.utilities.GameAttributes;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Multiplayer {
    private final Menu menu;
    private final GameAttributes gameAttributes;
    private final JLabel spacerLabel;
    private JFrame gameParametersWindow;

    public Multiplayer(Menu menu) {
        this.menu = menu;
        this.gameAttributes = new GameAttributes();
        this.spacerLabel = new JLabel("");
    }

    public Multiplayer(Menu menu, GameAttributes gameAttributes) {
        this.menu = menu;
        this.gameAttributes = gameAttributes;
        this.spacerLabel = gameAttributes.getSpacerLabel();
        this.gameParametersWindow = gameAttributes.getGameParametersWindow();
    }

    public void onOpponentChallenge(String opponentName, String opponentAddress) {
        final JFrame challengeWindow = new JFrame("Challenge Window");
        challengeWindow.setSize(new Dimension(500, 120));
        challengeWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        challengeWindow.setVisible(true);
        challengeWindow.setLayout(new GridLayout(3, 2));

        JTextField nameField = new JTextField();
        JButton acceptButton = new JButton("Accept");
        JButton declineButton = new JButton("Decline");

        challengeWindow.add(new JLabel("  You've been challenged to a game!"));
        challengeWindow.add(new JLabel("Opponent name: " + opponentName + " IP: " + opponentAddress));
        challengeWindow.add(new JLabel("  Enter your name to accept:  "));
        challengeWindow.add(nameField);
        challengeWindow.add(acceptButton);
        challengeWindow.add(declineButton);
        challengeWindow.pack();

        acceptButton.addActionListener(e -> {
            String playerName = nameField.getText();
            if (playerName.equals("")) {
                System.out.println("player hasn't entered his name");
                return;
            }
            gameAttributes.setPlayerName(playerName);
            gameAttributes.setOpponentName(opponentName);
            gameAttributes.setOpponentIP(opponentAddress);
            Cell.Colour myColour = (new Random()).nextInt(2) == 0 ? Cell.Colour.black : Cell.Colour.white;
            gameAttributes.setPlayerColour(myColour);
            menu.getMenuConnectionHandler().accept(opponentAddress, myColour == Cell.Colour.black ? Cell.Colour.white : Cell.Colour.black, gameAttributes.getPlayerName());
            gameAttributes.setConnectionHandler(new ConnectionHandler(opponentAddress, null));
            new GameWindow(gameAttributes);
            menu.getMenuConnectionHandler().stopReceiving(); // stop receiving because player is in game
            challengeWindow.dispose();
            menu.getMenuWindow().dispose();
        });

        declineButton.addActionListener(e -> {
            menu.getMenuConnectionHandler().decline(opponentAddress);
            challengeWindow.dispose();
        });
    }

    public void onChallengeAccepted(Cell.Colour colour, String opponentAddress, String opponentName) {
        gameAttributes.setPlayerColour(colour);
        gameAttributes.setOpponentName(opponentName);
        gameAttributes.setConnectionHandler(new ConnectionHandler(opponentAddress, null));
        gameParametersWindow.dispose();
        new GameWindow(this.gameAttributes);
        menu.getMenuConnectionHandler().stopReceiving();
    }

    public void onChallengeDeclined() {
        spacerLabel.setText("Declined!");
    }
}
