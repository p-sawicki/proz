package chess.actions;

import chess.gui.GameWindow;
import chess.utilities.SavedGame;
import chess.utilities.Utility;
import com.thoughtworks.xstream.XStream;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ActionSaveAs implements ActionListener {
    private final GameWindow gameWindow;

    public ActionSaveAs(GameWindow window) {
        this.gameWindow = window;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameWindow.checkIfSaveIsPossible())
            saveGame();
    }

    private void saveGame() {
        JFileChooser fileChooser = createFileChooser();
        fileChooser.setSelectedFile(new File("chessGame_" + java.time.LocalDate.now()));
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.exists() || Utility.ignoredWarning("Do you want to overwrite data in file?")) { // prevents overwriting existing file without player consent
                String fileName = getFileName(fileChooser);
                saveXmlGame(fileName);
                gameWindow.getBoard().setBoardAltered(false);
            }
        }
    }

    public JFileChooser createFileChooser() {
        // fileChooser parameters
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle("Choose a folder and file name to save game: ");
        fileChooser.setAcceptAllFileFilterUsed(false); // restricts file formats to those declared below
        FileNameExtensionFilter filterXML = new FileNameExtensionFilter("XML files", "xml");
        fileChooser.addChoosableFileFilter(filterXML);
        return fileChooser;
    }

    public String getFileName(JFileChooser fileChooser) {
        File selectedFile = fileChooser.getSelectedFile();
        String selectedFileName = selectedFile.getAbsolutePath();
        FileNameExtensionFilter fileFilter = (FileNameExtensionFilter) fileChooser.getFileFilter();
        if (!fileFilter.accept(selectedFile))
            selectedFileName = selectedFileName + "." + fileFilter.getExtensions()[0];
        return selectedFileName;
    }

    public void saveXmlGame(String filename) {
        SavedGame game = new SavedGame(gameWindow);
        XStream xstream = new XStream();
        String xmlGame = xstream.toXML(game);
        // write xml string to the specified file
        try {
            FileWriter fileWriter = new FileWriter(filename);
            fileWriter.write(xmlGame);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
