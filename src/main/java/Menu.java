import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Random;


public class Menu implements Runnable {
    private final int windowHeight = 720;
    private final int windowWidth = 720;
    private final Dimension windowSize = new Dimension(windowWidth, windowHeight);
    private MenuConnectionHandler menuConnectionHandler;
    private JFrame menuWindow;
    private JLabel spacerLabel;
    private JFrame gameParametersWindow;

    public void run() {
        // window parameters
        menuWindow = new JFrame("Menu");
        menuWindow.setSize(windowSize);
        menuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuWindow.setVisible(true);

        if (menuConnectionHandler == null) {
            menuConnectionHandler = new MenuConnectionHandler(this);
            menuConnectionHandler.start();
        } else
            menuConnectionHandler.resumeReceiving();

        setBackgroundImage(menuWindow);
        addButtons(menuWindow);
    }

    private void setBackgroundImage(JFrame window) {
        try {
            window.setContentPane(
                    new JLabel(new ImageIcon(ImageIO.read(new File("src/main/resources/alpha/alpha/320/menuBackground.png")))));
        } catch (IOException e) {
            System.out.println("Could not open image: " + e.getMessage());
        }
        ;
    }

    private void addButtons(JFrame window) { // initialize button panel menu

        // initialize buttons
        final JButton newGameButton = createMainMenuButton("New game");
        final JButton openGameButton = createMainMenuButton("Load game");
        final JButton aboutGameButton = createMainMenuButton("About game");
        final JButton quitGameButton = createMainMenuButton("Quit game");

        // buttons' listeners
        newGameButton.addActionListener(e -> {
            setGameParametersAndStartGame();
            window.dispose();
        });
        openGameButton.addActionListener(e -> { // new window must show up with saved games to choose from
            if (openSavedGame()) { // saved game was successfully resumed
                //window.dispose();
            }
        });
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
        buttonPanel.add(openGameButton);
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

    private void setGameParametersAndStartGame() { // asks for player name, opponent IP
        // window parameters
        gameParametersWindow = new JFrame("Game Parameters");
        gameParametersWindow.setSize(new Dimension(400, 100));
        gameParametersWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameParametersWindow.setVisible(true);
        gameParametersWindow.setLayout(new GridLayout(3, 2));

        // window objects
        JLabel enterNameLabel = new JLabel("  Please enter your name:");
        JTextField nameField = new JTextField();
        JLabel enterIPLabel = new JLabel("  Please enter opponent IP:");
        JTextField ipField = new JTextField();
        spacerLabel = new JLabel("");
        JButton startButton = new JButton("Start");

        gameParametersWindow.add(enterNameLabel);
        gameParametersWindow.add(nameField);
        gameParametersWindow.add(enterIPLabel);
        gameParametersWindow.add(ipField);
        gameParametersWindow.add(spacerLabel);
        gameParametersWindow.add(startButton);

        // button listener
        startButton.addActionListener(e -> {
            String playerName = nameField.getText();
            if (playerName.equals("")) {
                System.out.println("player hasn't entered his name");
                return;
            }
            String opponentIP = ipField.getText();
            spacerLabel.setText("Waiting...");
            menuConnectionHandler.challenge(playerName, opponentIP);
        });
    }

    private void setGameParametersAndStartGame(String playerName, String opponentIP, Board board) {
        // window parameters
        final JFrame gameParametersWindow = new JFrame("Game Parameters");
        gameParametersWindow.setSize(new Dimension(400, 100));
        gameParametersWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameParametersWindow.setVisible(true);
        gameParametersWindow.setLayout(new GridLayout(3, 2));

        // window objects
        JLabel enterNameLabel = new JLabel("  Your name:");
        JLabel nameField = new JLabel(playerName);
        JLabel enterIPLabel = new JLabel("  Opponent IP:");
        JLabel ipField = new JLabel(opponentIP);
        JLabel spacerLabel = new JLabel(""); // Here can be opponent status (like: Opponent is active; Opponent is ready)
        JButton startButton = new JButton("Start");

        gameParametersWindow.add(enterNameLabel);
        gameParametersWindow.add(nameField);
        gameParametersWindow.add(enterIPLabel);
        gameParametersWindow.add(ipField);
        gameParametersWindow.add(spacerLabel);
        gameParametersWindow.add(startButton);

        // button listener
        startButton.addActionListener(e -> {
            //new GameWindow(playerName, board);
            new GameWindow(playerName);
            // setConnectionHandler with opponentIP;
            /*if(opponentISNotReady) {
                System.out.println("opponent not ready");
                return;
            }*/
            gameParametersWindow.dispose();
        });
    }

    private boolean openSavedGame() {
        // fileChooser parameters
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choose a saved game to resume: ");
        jfc.setAcceptAllFileFilterUsed(false); // restricts file selection to extensions declared below
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files", "xml");
        jfc.addChoosableFileFilter(filter);

        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            System.out.println("player has chosen saved game at: " + selectedFile.getAbsolutePath());
        }

        if (returnValue == JFileChooser.CANCEL_OPTION) {
            return false; // player hasn't chosen saved game
        }

        //setGameParametersAndStartGame("savedName", "savedIP", savedBoard); // opens saved game to resume playing
        return true;
    }

    private void openGameDescription() {
        final JFrame descriptionWindow = new JFrame("About program");

        ImageIcon icon = new ImageIcon("src/main/resources/alpha/alpha/320/gameLogo.png");
        JOptionPane.showMessageDialog(descriptionWindow,
                "\n" + "Program title: \"Chess\"" + "\n\n" + "Version: 1.0" + "\n\n" + "Authors: Piotr Sawicki, Vladyslav Kyryk" + "\n",
                "About program",
                JOptionPane.INFORMATION_MESSAGE, icon);
    }

    public void onOpponentChallenge(String name, String address) {
        final JFrame challengeWindow = new JFrame("Challenge Window");
        challengeWindow.setSize(new Dimension(720, 100));
        challengeWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        challengeWindow.setVisible(true);
        challengeWindow.setLayout(new GridLayout(2, 2));

        challengeWindow.add(new JLabel("You've been challenged to a game!"));
        challengeWindow.add(new JLabel("Opponent name: " + name + " IP: " + address));
        JButton acceptButton = new JButton("Accept");
        JButton declineButton = new JButton("Decline");
        challengeWindow.add(acceptButton);
        challengeWindow.add(declineButton);

        acceptButton.addActionListener(e -> {
            Cell.Colour colour = (new Random()).nextInt(2) == 0 ? Cell.Colour.black : Cell.Colour.white;
            menuConnectionHandler.accept(address, colour == Cell.Colour.black ? Cell.Colour.white : Cell.Colour.black);
            new GameWindow("test", colour, new ConnectionHandler(address, null), menuConnectionHandler);
            challengeWindow.dispose();
            menuWindow.dispose();
        });

        declineButton.addActionListener(e -> {
            menuConnectionHandler.decline(address);
            challengeWindow.dispose();
        });
    }

    public void onChallengeAccepted(Cell.Colour colour, String address){
        new GameWindow("test", colour, new ConnectionHandler(address, null), menuConnectionHandler);
        gameParametersWindow.dispose();
    }

    public void onChallengeDeclined(){
        spacerLabel.setText("Declined!");
    }
}
