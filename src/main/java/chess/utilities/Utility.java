package chess.utilities;

import chess.mechanics.Cell;
import chess.mechanics.Piece;
import chess.mechanics.pieces.*;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Utility {
    public static URL getResourcePath(String filename) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResource(filename);
    }

    public static JFrame createGameParametersWindow(int rows, int columns) {
        JFrame gameParametersWindow = new JFrame("Game Parameters");

        // window parameters
        gameParametersWindow.setSize(new Dimension(400, 100));
        gameParametersWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameParametersWindow.setVisible(true);
        gameParametersWindow.setLayout(new GridLayout(rows, columns));
        return gameParametersWindow;
    }

    public static JFrame createWarningWindow() {
        final JFrame warningWindow = new JFrame("Warning");
        warningWindow.setSize(new Dimension(300, 100));
        warningWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        warningWindow.setVisible(true);
        return warningWindow;
    }

    public static boolean ignoredWarning(String warningMessage) {
        JFrame warningWindow = createWarningWindow();
        boolean yesResponse = JOptionPane.showConfirmDialog(warningWindow, warningMessage) == JOptionPane.YES_OPTION;
        warningWindow.setVisible(false);
        warningWindow.dispose();
        return yesResponse;
    }

    public static Piece[][] getStartingState() {
        return new Piece[][]{
                {new Rook(Cell.Colour.white), new Knight(Cell.Colour.white), new Bishop(Cell.Colour.white), new Queen(Cell.Colour.white),
                        new King(Cell.Colour.white), new Bishop(Cell.Colour.white), new Knight(Cell.Colour.white), new Rook(Cell.Colour.white)},
                {new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white),
                        new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white), new Pawn(Cell.Colour.white)},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black),
                        new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black), new Pawn(Cell.Colour.black)},
                {new Rook(Cell.Colour.black), new Knight(Cell.Colour.black), new Bishop(Cell.Colour.black), new Queen(Cell.Colour.black),
                        new King(Cell.Colour.black), new Bishop(Cell.Colour.black), new Knight(Cell.Colour.black), new Rook(Cell.Colour.black)}
        };
    }
}
