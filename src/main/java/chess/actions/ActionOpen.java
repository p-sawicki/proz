package chess.actions;

import chess.gui.GameWindow;
import chess.gui.Menu;
import chess.utilities.GameAttributes;
import chess.utilities.SavedGame;
import chess.utilities.Utility;
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
    private final Menu menu;

    public ActionOpen(Menu menu) {
        this.menu = menu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        openSavedGame();
    }

    private void openSavedGame() {
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

            GameAttributes savedGameAttr = savedGame.createGameAttributes();
            resumeSavedGame(savedGameAttr);
        }
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

    public void resumeSavedGame(GameAttributes savedGameAttributes) { // resumes saved game
        JFrame gameParametersWindow = Utility.createGameParametersWindow(2, 2);

        // window objects
        JLabel enterNameLabel = new JLabel("  Your name:");
        JLabel nameField = new JLabel(savedGameAttributes.getPlayerName());
        JLabel spacerLabel = new JLabel("");
        JButton startButton = new JButton("Start");

        gameParametersWindow.add(enterNameLabel);
        gameParametersWindow.add(nameField);
        gameParametersWindow.add(spacerLabel);
        gameParametersWindow.add(startButton);

        // button listener
        startButton.addActionListener(e -> {
            menu.getMenuConnectionHandler().stopReceiving();
            new GameWindow(savedGameAttributes);
            gameParametersWindow.dispose();
            menu.getMenuWindow().dispose();
        });
    }


}
