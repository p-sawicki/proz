package chess.actions;

import chess.gui.GameWindow;
import chess.gui.Menu;
import chess.utilities.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionNewGame implements ActionListener {
    private Menu menu;
    private GameAttributes gameAttributes;

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
