package chess.gui;

import chess.actions.ActionSaveAs;
import chess.mechanics.Board;
import chess.network.ConnectionHandler;
import chess.network.Message;
import chess.utilities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameWindow {
    private final Board board;
    private String playerName;
    private String opponentName;
    private final ConnectionHandler connectionHandler;

    public GameWindow(GameAttributes gameAttributes) {
        JFrame window = new JFrame("Chess");
        window.setLayout(new BorderLayout(10, 10));

        connectionHandler = gameAttributes.getConnectionHandler();
        if (connectionHandler != null)
            connectionHandler.setGameWindow(window);
        if (gameAttributes.getBoard() != null)
            board = gameAttributes.getBoard();
        else
            board = new Board(gameAttributes.getPlayerColour(), gameAttributes.getConnectionHandler());

        setPlayerName(gameAttributes.getPlayerName());
        setOpponentName(gameAttributes.getOpponentName());

        window.add(board, BorderLayout.CENTER);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (Utility.ignoredWarning("Do you want to exit game?")) {
                    quitProcedure();
                    System.exit(0);
                }
            }
        });
        window.setVisible(true);
        initializeGameMenuBar(window);
        window.pack();
    }

    private void initializeGameMenuBar(JFrame window) {
        // Menu buttons
        final JMenuItem goBackToMenuButton = new JMenuItem("Go to menu");
        final JMenuItem saveGameButton = new JMenuItem("Save game");
        final JMenuItem quitGameButton = new JMenuItem("Quit game");

        saveGameButton.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        quitGameButton.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));

        final JLabel playerNameLabel;
        if (board.checkIfHotSeat())
            playerNameLabel = new JLabel("  |  " + playerName + "  ");
        else
            playerNameLabel = new JLabel("  |  " + playerName + " vs " + opponentName + "  ");

        // buttons' listeners
        goBackToMenuButton.addActionListener(e -> {
            if (!checkIfSaveIsPossible() || Utility.ignoredWarning("Do you want to exit game without saving?")) {
                quitProcedure();
                SwingUtilities.invokeLater(new Menu());
                window.dispose();
            }
        });
        saveGameButton.addActionListener(new ActionSaveAs(this));
        quitGameButton.addActionListener(e -> {
            if (!checkIfSaveIsPossible() || Utility.ignoredWarning("Do you want to exit game without saving?")) {
                quitProcedure();
                System.exit(0);
            }
        });

        // Menu bar in game
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(goBackToMenuButton);
        if (isHotSeat())
            menuBar.add(saveGameButton);
        menuBar.add(quitGameButton);
        menuBar.add(playerNameLabel);
        window.setJMenuBar(menuBar);
    }

    public boolean checkIfSaveIsPossible() {
        return board.checkIfBoardAltered() && isHotSeat();
    }

    public boolean isHotSeat() {
        return board.checkIfHotSeat();
    }

    // getters
    public Board getBoard() {
        return board;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getOpponentName() {
        return opponentName;
    }

    // setters
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public void quitProcedure() {
        if (!isHotSeat() && connectionHandler != null) {
            Message message = new Message(Message.MessageType.quit);
            connectionHandler.send(message);
            connectionHandler.stopReceiving();
        }
    }
}
