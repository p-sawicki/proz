package chess.utilities;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Utility {
    public static String getResourcePath(){
        return "src" + File.separator + "main" + File.separator + "resources" + File.separator;
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
}
