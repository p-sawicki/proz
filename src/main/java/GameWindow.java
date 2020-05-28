import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//import java.time.LocalDateTime;


public class GameWindow {
    private JFrame window;
    private Board board;
    private String playerName;
    private String opponentName;
    private final ConnectionHandler connectionHandler;

    public GameWindow(String playerName) {
        this(playerName, null, Cell.Colour.white, null, null, "");
    }

    public GameWindow(String playerName, Board board) { this(playerName, board, Cell.Colour.white, null, null, ""); }

    public GameWindow(String playerName, Board board, Cell.Colour playerColour, ConnectionHandler connectionHandler, MenuConnectionHandler menuConnectionHandler, String opponentName) {
        window = new JFrame("Chess");
        window.setLayout(new BorderLayout(10, 10));

        this.connectionHandler = connectionHandler;

        if (menuConnectionHandler != null)
            menuConnectionHandler.stopReceiving();

        if (board != null) {
            this.board = board;
        } else {
            this.board = new Board(playerColour, connectionHandler);
        }
        setPlayerName(playerName);
        setOpponentName(opponentName);

        window.add(this.board, BorderLayout.CENTER);

        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (ignoredWarning("Do you want to exit game?")) {
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
        saveGameButton.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        final JMenuItem quitGameButton = new JMenuItem("Quit game");
        quitGameButton.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        final JLabel playerNameLabel;
        if (board.checkIfSinglePlayer()) {
            playerNameLabel = new JLabel("  |  " + playerName + "  ");
        } else {
            playerNameLabel = new JLabel("  |  " + playerName + " vs " + opponentName + "  ");
        }

        // buttons' listeners
        goBackToMenuButton.addActionListener(e -> {
            // if at least 1 move was made, ask player if he wants to exit a game without save
            // if he is playing with another player then notify another player
            if (!checkIfBoardWasAltered() || ignoredWarning("Do you want to exit game without saving?")) {
                SwingUtilities.invokeLater(new Menu());
                window.dispose();
                if (connectionHandler != null)
                    connectionHandler.stopReceiving();
            }
        });
       /* saveGameButton.addActionListener(e -> {
            if (checkIfBoardWasAltered())
                saveGame(); // save current game
        });*/
       saveGameButton.addActionListener(new ActionSaveAs(this));
        quitGameButton.addActionListener(e -> {
            if (!checkIfBoardWasAltered() || ignoredWarning("Do you want to exit game without saving?")) {
                System.exit(0);
            }
        });

        // Menu bar in game
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(goBackToMenuButton);
        menuBar.add(saveGameButton);
        menuBar.add(quitGameButton);
        menuBar.add(playerNameLabel);
        window.setJMenuBar(menuBar);
    }

    public JFrame createWarningWindow() {
        final JFrame warningWindow = new JFrame("Warning");
        warningWindow.setSize(new Dimension(300, 100));
        warningWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        warningWindow.setVisible(true);
        return warningWindow;
    }

    public boolean ignoredWarning(String warningMessage) {
        JFrame warningWindow = createWarningWindow();
        boolean yesResponse = JOptionPane.showConfirmDialog(warningWindow, warningMessage) == JOptionPane.YES_OPTION;
        warningWindow.setVisible(false);
        warningWindow.dispose();
        return yesResponse;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public Board getBoard() {
        return board;
    }

    public boolean checkIfBoardWasAltered() {
        return board.checkIfBoardAltered();
    }
}
