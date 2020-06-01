package chess.actions;

import chess.gui.GameWindow;
import chess.gui.Menu;
import chess.utilities.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionNewGame implements ActionListener {
    private final Menu menu;
    private final GameAttributes gameAttributes;

    public ActionNewGame(Menu menu) {
        this.menu = menu;
        this.gameAttributes = new GameAttributes();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setGameType();
    }

    private void setGameType() {
        // window parameters
        JFrame gameParametersWindow = Utility.createGameParametersWindow(3, 1);

        // window objects
        JLabel enterNameLabel = new JLabel("  Please select game type:");
        JButton hotSeatButton = new JButton("Hot seat");
        JButton onlineButton = new JButton("Online");

        gameParametersWindow.add(enterNameLabel);
        gameParametersWindow.add(hotSeatButton);
        gameParametersWindow.add(onlineButton);

        // buttons' listeners
        hotSeatButton.addActionListener(e -> start(gameAttributes, gameParametersWindow, true));
        onlineButton.addActionListener(e -> start(gameAttributes, gameParametersWindow, false));
    }

    private void start(GameAttributes gameAttributes, JFrame gameParametersWindow, boolean singlePlayer) {
        gameAttributes.setSinglePlayer(singlePlayer);
        startNewGame(singlePlayer);
        gameParametersWindow.dispose();
    }

    public void startNewGame(boolean singlePlayer) { // asks for player name, opponent IP
        JFrame gameParametersWindow = Utility.createGameParametersWindow(3, 2);

        // window objects
        JLabel enterNameLabel = new JLabel("  Please enter your name:");
        JTextField nameField = new JTextField();
        JLabel enterIPLabel = new JLabel("  Please enter opponent IP:");
        JTextField ipField = new JTextField();
        JLabel spacerLabel = new JLabel("");
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
            if (playerName.isEmpty())
                return;

            String opponentIP = null;
            if (!singlePlayer)
                opponentIP = ipField.getText();

            gameAttributes.setPlayerName(playerName);
            gameAttributes.setOpponentIP(opponentIP);
            if (opponentIP == null) {
                menu.getMenuConnectionHandler().stopReceiving();
                new GameWindow(gameAttributes);
                menu.getMenuWindow().dispose();
                gameParametersWindow.dispose();
            } else {
                spacerLabel.setText("Waiting...");
                gameAttributes.setGameParametersWindow(gameParametersWindow);
                gameAttributes.setSpacerLabel(spacerLabel);
                menu.getMenuConnectionHandler().challenge(playerName, opponentIP, gameAttributes);
            }
        });
    }
}
