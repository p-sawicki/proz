import javax.swing.*;
import java.awt.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;
//import java.time.LocalDateTime;


public class GameWindow {
    private JFrame window;
    private Board board;
    private String playerName;

    public GameWindow(String playerName){
        window = new JFrame("Chess");
        window.setLayout(new BorderLayout(10, 10));

        board = new Board(Cell.Colour.white);
        setPlayerName(playerName);

        window.add(board, BorderLayout.CENTER);

        window.pack();
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(ignoredWarning("Do you want to exit game?"))
                {
                    System.exit(0);
                }
            }
        });
        window.setVisible(true);

        initializeGameMenuBar(window);
    }

    private void initializeGameMenuBar(JFrame window) {
        // Menu buttons
        final JMenuItem goBackToMenuButton = new JMenuItem("Go to menu");
        final JMenuItem saveGameButton = new JMenuItem("Save game");
        saveGameButton.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        final JMenuItem quitGameButton = new JMenuItem("Quit game");
        quitGameButton.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));

        // buttons' listeners
        goBackToMenuButton.addActionListener(e -> {
            // if at least 1 move was made, ask player if he wants to exit a game without save
            // if he is playing with another player then notify another player
            if(!checkIfBoardWasAltered() || ignoredWarning("Do you want to exit game without saving?"))
            {
                SwingUtilities.invokeLater(new Menu());
                window.dispose();
            }
        });
        saveGameButton.addActionListener(e -> {
            saveGame(); // save current game
        });
        quitGameButton.addActionListener(e -> {
            if(!checkIfBoardWasAltered() || ignoredWarning("Do you want to exit game without saving?"))
            {
                System.exit(0);
            }
        });

        // Menu bar in game
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(goBackToMenuButton);
        menuBar.add(saveGameButton);
        menuBar.add(quitGameButton);
        window.setJMenuBar(menuBar);
    }

    private void saveGame() {
        JFileChooser fileChooser = createFileChooser();
        fileChooser.setSelectedFile(new File("chessGame_" + java.time.LocalDate.now()));
/*
        System.out.println("Project Directory : "+ System.getProperty("user.dir"));
        String gameDirectory = System.getProperty("user.dir");
        jfc.setSelectedFile(new File(gameDirectory + "/" + "chessGame_" + java.time.LocalDate.now())); // sets game folder as default to save file in
*/
        int returnValue = fileChooser.showSaveDialog(null);
        if(returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.exists() || ignoredWarning("Do you want to overwrite data in file?")) { // prevents overwriting existing file without player consent
                String fileName = getFileName(fileChooser);
                //
                // save game state to xml file /////////////////////////////
                // saveGame(fileName);
                //if(successful save)
                    board.setBoardAltered(false);
                //
            }
        }
    }

    public JFileChooser createFileChooser() {
        // fileChooser parameters
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle("Choose a folder and file name to save game: ");
        //jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false); // restricts file formats to those declared below
        FileNameExtensionFilter filterXML = new FileNameExtensionFilter("XML files", "xml");
        fileChooser.addChoosableFileFilter(filterXML);
        return  fileChooser;
    }

    public String getFileName(JFileChooser fileChooser) {
        File selectedFile = fileChooser.getSelectedFile();
        String selectedFileName = selectedFile.getAbsolutePath();

        System.out.println("Player has chosen to save game at: " + selectedFileName);
        FileNameExtensionFilter fileFilter = (FileNameExtensionFilter)fileChooser.getFileFilter();
        System.out.println("Accepted extensions: " + Arrays.toString(fileFilter.getExtensions()));
        if (!fileFilter.accept(selectedFile))
            selectedFileName = selectedFileName + "." + fileFilter.getExtensions()[0];
        System.out.println("Game will be saved at: " + selectedFileName);

        return selectedFileName;
    }

    public JFrame createWarningWindow() {
        final JFrame warningWindow = new JFrame("Warning");
        warningWindow.setSize(new Dimension(300, 100));
        warningWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        warningWindow.setVisible(true);
        return warningWindow;
    }

    private boolean ignoredWarning(String warningMessage) {
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

    public boolean checkIfBoardWasAltered() {
        return board.checkIfBoardAltered();
    }

    public Board getBoard() {
        return board;
    }
}
