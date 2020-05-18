import javax.swing.*;
import java.awt.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.time.LocalDateTime;


public class GameWindow {
    private JFrame window;
    private Board board;
    private String playerName;
    //private boolean isGameSaved;

    public GameWindow(String playerName){
        window = new JFrame("Chess");

        window.setLayout(new BorderLayout(10, 10));

        board = new Board(Cell.Colour.white);

        setPlayerName(playerName);

        window.add(board, BorderLayout.CENTER);

        window.pack();
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



        // Menu bar in game
        final JMenuItem goBackToMenuButton = new JMenuItem("Go to menu");
        final JMenuItem saveGameButton = new JMenuItem("Save game");
        //JMenu exitMenu = new JMenu("Menu");
        //JMenu gameMenu = new JMenu("Game");
        JMenuBar menuBar = new JMenuBar();
        //gameMenu.add(goBackToMenuButton);
        menuBar.add(goBackToMenuButton);
        menuBar.add(saveGameButton);
        window.setJMenuBar(menuBar);

        goBackToMenuButton.addActionListener(e -> {
            // if at least 1 move was made, ask player if he wants to exit a game without save
            // if he is playing with another player then notify another player

            SwingUtilities.invokeLater(new Menu());
            window.dispose();
        });

        saveGameButton.addActionListener(e -> {
            saveGame(); // save current game
        });

    }

    private void saveGame() {
        // fileChooser parameters
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choose a folder and file name to save game: ");
        //jfc.setFileSelectionMode(JFileChooser.FILES_ONLY); // default setting
        jfc.setAcceptAllFileFilterUsed(false); // restricts file selection to those declared below
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files", "xml");
        jfc.addChoosableFileFilter(filter);

        /*System.out.println("Project Directory : "+ System.getProperty("user.dir"));
        String gameDirectory = System.getProperty("user.dir");
        jfc.setSelectedFile(new File(gameDirectory + "/" + "chessGame_" + java.time.LocalDate.now() + ".xml"));*/
        jfc.setSelectedFile(new File("chessGame_" + java.time.LocalDate.now() + ".xml"));
        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            System.out.println("player has chosen to save game at: " + selectedFile.getAbsolutePath());

            // check for ".xml" extension
            //String fileExtension = FilenameUtils.getExtension(filename);
            String fileExtension = getfileExtension(jfc.getSelectedFile());
            if(!fileExtension.equals("xml")) {
                System.out.println("player has chosen file with wrong extension");
                return;
            }

            //
            // save game state to xml file /////////////////////////////
            //

        }

        if (returnValue == JFileChooser.CANCEL_OPTION) {
            return;
        }

        // asks player whether he wants to quit or resume game
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    private static String getfileExtension(File file) {
        String filename = file.getName();
        if(filename.lastIndexOf(".") != -1 && filename.lastIndexOf(".") != 0)
            return filename.substring(filename.lastIndexOf(".") + 1);
        else
            return "";
    }

}