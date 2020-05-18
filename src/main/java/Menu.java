import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileNameExtensionFilter;


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

        // menu buttons
        final JMenuItem newGameButton = new JMenuItem("New game");
        final JMenuItem openGameButton = new JMenuItem("Load game");

        // buttons' listeners
        newGameButton.addActionListener(e -> {
            setGameParameters();
            menuWindow.dispose();
        });
        openGameButton.addActionListener(e -> { // new window must show up with saved games to choose from
            if(openSavedGame()) { // saved game was successfully resumed
                //menuWindow.dispose();
            }
        });

        // set up menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(newGameButton);
        menuBar.add(openGameButton);
        menuWindow.setJMenuBar(menuBar);
    }

    private void setGameParameters() { // asks for player name, opponent IP
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
            String opponentIP = ipField.getText();

            // add exception cases

            //new GameWindow(playerName, opponentIP);
            new GameWindow(playerName);
            gameParametersWindow.dispose();
        });
    }

    private boolean openSavedGame() {
        // fileChooser parameters
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choose a saved game to resume: ");
        //jfc.setFileSelectionMode(JFileChooser.FILES_ONLY); // default setting
        jfc.setAcceptAllFileFilterUsed(false); // restricts file selection to those declared below
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files", "xml");
        jfc.addChoosableFileFilter(filter);

        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            System.out.println("player has chosen saved game at: " + selectedFile.getAbsolutePath());
        }

        if (returnValue == JFileChooser.CANCEL_OPTION) {
            return false; // player hasn's chosen saved game
        }

        // opens saved game to resume playing
        //new GameWindow(playerName, opponentIP, board); open saved game
        return true;
    }

}
