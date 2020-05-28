import com.thoughtworks.xstream.XStream;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ActionOpen implements ActionListener {
    private final JFrame menuWindow;

    public ActionOpen(JFrame window) {
        this.menuWindow = window;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (openSavedGame()) { // saved game successfully resumed
            menuWindow.dispose();
        }
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
            String fileName = selectedFile.getAbsolutePath();
            System.out.println("player has chosen saved game at: " + fileName);

            SavedGame savedGame = createSavedGameFromXml(fileName);

            new GameWindow(savedGame.getPlayerName(), savedGame.createBoard());
            //setGameParametersAndStartGame("savedName", "savedIP", savedBoard); // opens saved game to resume playing
        }

        if (returnValue == JFileChooser.CANCEL_OPTION) {
            return false; // player hasn't chosen saved game
        }

        return true;
    }

    public SavedGame createSavedGameFromXml(String fileName) {
        XStream xstream = new XStream();

        String savedXmlGame = "";
        try {
            savedXmlGame = new String (Files.readAllBytes(Paths.get(fileName)));
            //System.out.println( savedXmlGame );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return (SavedGame) xstream.fromXML(savedXmlGame);
    }
}
