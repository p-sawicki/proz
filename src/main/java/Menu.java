import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.ImageIO;
import java.io.IOException;


public class Menu implements Runnable{
    private final int windowHeight = 720;
    private final int windowWidth = 720;
    private final Dimension windowSize = new Dimension(windowWidth, windowHeight);

    public void run(){
        // window parameters
        final JFrame menuWindow = new JFrame("Menu");
        menuWindow.setSize(windowSize);
        menuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuWindow.setVisible(true);

        setBackgroundImage(menuWindow);
        addButtons(menuWindow);
    }

    private void setBackgroundImage(JFrame window) {
        try {
            window.setContentPane(
                    new JLabel(new ImageIcon(ImageIO.read(new File("src/main/resources/alpha/alpha/320/menuBackground.png")))));
        } catch (IOException e) {
            System.out.println("Could not open image: " + e.getMessage());
        };
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
            if(openSavedGame()) { // saved game was successfully resumed
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
        buttonPanel.setLayout(new GridLayout(4,1));
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
        final JFrame gameParametersWindow = new JFrame("Game Parameters");
        gameParametersWindow.setSize(new Dimension(400, 100));
        gameParametersWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameParametersWindow.setVisible(true);
        gameParametersWindow.setLayout(new GridLayout(3,2));

        // window objects
        JLabel enterNameLabel = new JLabel("  Please enter your name:");
        JTextField nameField = new JTextField();
        JLabel enterIPLabel = new JLabel("  Please enter opponent IP:");
        JTextField ipField = new JTextField();
        JLabel spacerLabel = new JLabel("");
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
            if(playerName.equals("")) {
                System.out.println("player hasn't entered his name");
                return;
            }
            String opponentIP = ipField.getText();

            //new GameWindow(playerName, opponentIP);
            new GameWindow(playerName);
            gameParametersWindow.dispose();
        });
    }

    private void setGameParametersAndStartGame(String playerName, String opponentIP, Board board) {
        // window parameters
        final JFrame gameParametersWindow = new JFrame("Game Parameters");
        gameParametersWindow.setSize(new Dimension(400, 100));
        gameParametersWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameParametersWindow.setVisible(true);
        gameParametersWindow.setLayout(new GridLayout(3,2));

        // window objects
        JLabel enterNameLabel = new JLabel("  Your name:");
        JLabel nameField = new JLabel(playerName);
        JLabel enterIPLabel = new JLabel("  Opponent IP:");
        JLabel ipField = new JLabel(opponentIP);
        JLabel spacerLabel = new JLabel("");
        JButton startButton = new JButton("Start");

        gameParametersWindow.add(enterNameLabel);
        gameParametersWindow.add(nameField);
        gameParametersWindow.add(enterIPLabel);
        gameParametersWindow.add(ipField);
        gameParametersWindow.add(spacerLabel);
        gameParametersWindow.add(startButton);

        // button listener
        startButton.addActionListener(e -> {

            // add exception cases

            //new GameWindow(playerName, opponentIP, board);
            new GameWindow(playerName);
            gameParametersWindow.dispose();
        });
    }

    private boolean openSavedGame() {
        // fileChooser parameters
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choose a saved game to resume: ");
        //jfc.setFileSelectionMode(JFileChooser.FILES_ONLY); // default setting
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
}
