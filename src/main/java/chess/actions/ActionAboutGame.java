package chess.actions;

import chess.utilities.Utility;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionAboutGame implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        openGameDescription();
    }

    public void openGameDescription() {
        final JFrame descriptionWindow = new JFrame("About program");

        ImageIcon icon = new ImageIcon(Utility.getResourcePath() + "gameLogo.png");
        System.out.println("Trying to get game logo at: " + Utility.getResourcePath() + "gameLogo.png");
        JOptionPane.showMessageDialog(descriptionWindow,
                "\n" + "Program title: \"Chess\"" + "\n\n" + "Version: 1.0" + "\n\n" + "Authors: Piotr Sawicki, Vladyslav Kyryk" + "\n",
                "About program",
                JOptionPane.INFORMATION_MESSAGE, icon);
    }
}
