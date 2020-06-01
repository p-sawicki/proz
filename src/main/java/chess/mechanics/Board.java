package chess.mechanics;

import chess.network.*;
import chess.mechanics.pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Board extends JPanel implements MouseListener {
    private final int size = 8;
    private final int windowHeight = 720;
    private final int windowWidth = 720;
    private final Dimension windowSize = new Dimension(windowWidth, windowHeight);

    private final Cell[][] cells;
    private boolean isBoardAltered; // true if current board state isn't saved
    private boolean singlePlayer;
    private Cell.Colour playerColour;
    private boolean whiteTurn;
    private boolean enableGame;
    private ConnectionHandler connectionHandler;
    private Point clickedCellPosition = new Point(-1, -1);
    private final Point nullPosition = new Point(-1, -1);
    private King whiteKing;
    private King blackKing;
    private Pawn lastDoubleStepBlack;
    private Pawn lastDoubleStepWhite;
    private volatile boolean waitOnPromotion;
    CheckDetector.State state;

    //starting board state
    private final Piece[][] pieces = {
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

    public Board() {
        this(Cell.Colour.white);
    }

    public Board(Cell.Colour playerColour) {
        this(playerColour, null);
    }

    public Board(Cell.Colour playerColour, ConnectionHandler connectionHandler) {
        setBoardAltered(false);
        this.playerColour = playerColour;
        this.connectionHandler = connectionHandler;

        cells = new Cell[size][size];
        setLayout(new GridLayout(size, size, 0, 0));

        this.addMouseListener(this);
        Cell.Colour color = Cell.Colour.black;
        if (playerColour == Cell.Colour.black) {
            for (int y = 0; y < size; ++y) {
                for (int x = size - 1; x >= 0; --x) {
                    Cell cell = new Cell(color, this, new Point(x, y));
                    cells[y][x] = cell;
                    cell.board = this;
                    add(cell);

                    cell.setPiece(pieces[y][x]);
                    if (cell.getPiece() instanceof King) {
                        if (cell.getPiece().colour == Cell.Colour.white)
                            whiteKing = (King) cell.getPiece();
                        else
                            blackKing = (King) cell.getPiece();
                    }

                    if (x != 0)
                        color = color == Cell.Colour.black ? Cell.Colour.white : Cell.Colour.black;
                }
            }
        } else {
            for (int y = size - 1; y >= 0; --y) {
                for (int x = 0; x < size; ++x) {
                    Cell cell = new Cell(color, this, new Point(x, y));
                    cells[y][x] = cell;
                    cell.board = this;
                    add(cell);

                    cell.setPiece(pieces[y][x]);
                    if (cell.getPiece() instanceof King) {
                        if (cell.getPiece().colour == Cell.Colour.white)
                            whiteKing = (King) cell.getPiece();
                        else
                            blackKing = (King) cell.getPiece();
                    }

                    if (x != size - 1)
                        color = color == Cell.Colour.black ? Cell.Colour.white : Cell.Colour.black;
                }
            }
        }

        setMinimumSize(windowSize);
        setPreferredSize(windowSize);
        setSize(windowSize);

        if (connectionHandler != null) {
            singlePlayer = false;
            connectionHandler.setBoard(this);
            if (connectionHandler.isAlive())
                connectionHandler.resumeReceiving();
            else
                connectionHandler.start();
        } else {
            singlePlayer = true;
        }

        whiteTurn = true;
        waitOnPromotion = false;
        enableGame = connectionHandler == null || playerColour != Cell.Colour.black;
    }

    public Board(Board board) {
        this.cells = new Cell[size][size];
        this.playerColour = board.playerColour;
        this.whiteTurn = board.whiteTurn;
        this.enableGame = board.enableGame;
        this.clickedCellPosition = board.clickedCellPosition;
        setBoardAltered(false);

        for (int y = 0; y < size; ++y) {
            for (int x = 0; x < size; ++x) {
                this.cells[y][x] = new Cell(board.getCells()[y][x].getColour(), this, new Point(x, y));
                Piece piece = board.getCells()[y][x].getPiece();
                if (piece != null) {
                    this.cells[y][x].setPiece(piece.copy());
                    if (piece instanceof King) {
                        if (piece.colour == Cell.Colour.white)
                            whiteKing = (King) piece;
                        else
                            blackKing = (King) piece;
                    }
                }
            }
        }
    }

    public Board(Cell[][] cells, boolean whiteTurn, boolean enableGame) {
        this.cells = new Cell[size][size];
        this.whiteTurn = whiteTurn;
        this.enableGame = enableGame;
        this.singlePlayer = true;
        this.connectionHandler = null;
        setBoardAltered(false);

        this.addMouseListener(this);
        setLayout(new GridLayout(size, size, 0, 0));
        Cell.Colour color = Cell.Colour.black;
        for (int y = size - 1; y >= 0; --y) {
            for (int x = 0; x < size; ++x) {
                Cell cell = new Cell(color, this, new Point(x, y));
                this.cells[y][x] = cell;
                add(cell);

                Piece piece = cells[y][x].getPiece();
                if (piece != null) {
                    this.cells[y][x].setPiece(piece.copy());
                    if (this.cells[y][x].getPiece() instanceof King) {
                        if (this.cells[y][x].getPiece().getColour() == Cell.Colour.white)
                            whiteKing = (King) this.cells[y][x].getPiece();
                        else
                            blackKing = (King) this.cells[y][x].getPiece();
                    }
                }

                if (x != size - 1)
                    color = color == Cell.Colour.black ? Cell.Colour.white : Cell.Colour.black;
            }
        }

        setMinimumSize(windowSize);
        setPreferredSize(windowSize);
        setSize(windowSize);
    }

    public Cell.Colour getPlayerColour() {
        return playerColour;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public boolean getWhiteTurn() {
        return whiteTurn;
    }

    public King getKing(Cell.Colour colour) {
        return colour == Cell.Colour.white ? whiteKing : blackKing;
    }

    public void switchTurn() {
        this.whiteTurn = !this.whiteTurn;
        String currentTurn = whiteTurn ? "White" : "Black";
        System.out.println(currentTurn + " turn");
    }

    private void disableGame() {
        enableGame = false;
    }

    public int getBoardSize() {
        return size;
    }

    public boolean checkIfSinglePlayer() {
        return singlePlayer;
    }

    public boolean checkIfBoardAltered() {
        return isBoardAltered;
    }

    public void setBoardAltered(boolean isAltered) {
        isBoardAltered = isAltered;
    }

    public void setPieces(Piece[][] pieces) {
        for (int y = 0; y < size; ++y) {
            for (int x = 0; x < size; ++x)
                cells[y][x].setPiece(pieces[y][x]);
        }
    }

    public void clearMoves() {
        for (int y = 0; y < size; ++y) {
            for (int x = 0; x < size; ++x) {
                Piece piece = cells[y][x].getPiece();
                if (piece != null)
                    piece.clearMoves();
            }
        }
    }

    public Pawn getLastDoubleStep(Cell.Colour colour) {
        return colour == Cell.Colour.white ? lastDoubleStepWhite : lastDoubleStepBlack;
    }

    public void setLastDoubleStep(Pawn pawn){
        if(pawn.getColour() == Cell.Colour.white)
            lastDoubleStepWhite = pawn;
        else
            lastDoubleStepBlack = pawn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        for (int y = 0; y < size; ++y) {
            for (int x = 0; x < size; ++x) {
                cells[y][x].paint(g);
            }
        }
    }

    public void setConnectionHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    public boolean moveIfPossible(Move move, Move.Promotion promotion) { // moves piece if not contradicted by rules
        if (move == null)
            return false;
        Piece movedPiece = cells[move.before.y][move.before.x].getPiece();
        cells[move.before.y][move.before.x].removePiece();
        cells[move.after.y][move.after.x].setPiece(movedPiece);
        lastDoubleStepBlack = null;
        lastDoubleStepWhite = null;
        if (movedPiece instanceof Pawn) {
            if(Math.abs(move.after.y - move.before.y) == 2)
                setLastDoubleStep((Pawn) movedPiece);
            if(promotion != null){
                Piece newPiece = null;
                switch (promotion) {
                    case Rook:
                        newPiece = new Rook(movedPiece.getColour());
                        break;
                    case Queen:
                        newPiece = new Queen(movedPiece.getColour());
                        break;
                    case Knight:
                        newPiece = new Knight(movedPiece.getColour());
                        break;
                    case Bishop:
                        newPiece = new Bishop(movedPiece.getColour());
                }
                cells[move.after.y][move.after.x].setPiece(newPiece);
            }
            else if (move.after.y == 0 || move.after.y == size - 1) {
                waitOnPromotion = true;
                disableGame();
                final JFrame promotionWindow = new JFrame("Promotion Window");
                promotionWindow.setSize(new Dimension(500, 120));
                promotionWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                promotionWindow.setVisible(true);
                promotionWindow.setLayout(new GridLayout(1, 4));

                JButton queenButton = new JButton("Queen");
                JButton rookButton = new JButton("Rook");
                JButton knightButton = new JButton("Knight");
                JButton bishopButton = new JButton("Bishop");

                promotionWindow.add(queenButton);
                promotionWindow.add(rookButton);
                promotionWindow.add(knightButton);
                promotionWindow.add(bishopButton);
                queenButton.addActionListener(e -> {
                    cells[move.after.y][move.after.x].setPiece(new Queen(movedPiece.getColour()));
                    promotionWindow.dispose();
                    enableGame = true;
                    repaint();
                    move.promotion = Move.Promotion.Queen;
                    connectionHandler.send(new Message(move, state, "M"));
                    waitOnPromotion = false;
                });
                rookButton.addActionListener(e -> {
                    cells[move.after.y][move.after.x].setPiece(new Rook(movedPiece.getColour()));
                    promotionWindow.dispose();
                    enableGame = true;
                    repaint();
                    move.promotion = Move.Promotion.Rook;
                    connectionHandler.send(new Message(move, state, "M"));
                    waitOnPromotion = false;
                });
                knightButton.addActionListener(e -> {
                    cells[move.after.y][move.after.x].setPiece(new Knight(movedPiece.getColour()));
                    promotionWindow.dispose();
                    enableGame = true;
                    repaint();
                    move.promotion = Move.Promotion.Knight;
                    connectionHandler.send(new Message(move, state, "M"));
                    waitOnPromotion = false;
                });
                bishopButton.addActionListener(e -> {
                    cells[move.after.y][move.after.x].setPiece(new Bishop(movedPiece.getColour()));
                    promotionWindow.dispose();
                    enableGame = true;
                    repaint();
                    move.promotion = Move.Promotion.Bishop;
                    connectionHandler.send(new Message(move, state, "M"));
                    waitOnPromotion = false;
                });
            }
        }
        if (move.secondMove != null) {
            Piece secondMovedPiece = cells[move.secondMove.before.y][move.secondMove.before.x].getPiece();
            cells[move.secondMove.before.y][move.secondMove.before.x].removePiece();
            if (move.secondMove.after != null)
                cells[move.secondMove.after.y][move.secondMove.after.x].setPiece(secondMovedPiece);
        }
        repaint();
        movedPiece.hasMoved();
        return true;
    }

    //@Override
    // this function is invoked when the mouse button has been clicked (pressed and released) on a component
    public void mouseClicked(MouseEvent e) {
        if (!enableGame) // disables moves if there was a checkmate
            return;
        Cell clicked = (Cell) getComponentAt(new Point(e.getX(), e.getY()));
        Move move = null;
        Point clickedPosition = clicked.getPosition();
        boolean isMoved = false, isMoved2 = false;
        int posX = clickedPosition.x;
        int posY = clickedPosition.y;
        System.out.println("Mouse clicked on the component " + clicked.getPieceNameColor() + " | posX = " + posX + ", posY = " + posY);

        Point previousClickedCell = this.clickedCellPosition;

        if (clickedCellPosition.equals(nullPosition)) { //player hasn't chosen cell to move yet
            if (clicked.getOccupation()) {
                System.out.println("Mouse clicked to choose component");
                if (clicked.getPiece().getColourAsString().equals("White") && whiteTurn
                        || clicked.getPiece().getColourAsString().equals("Black") && !whiteTurn) {
                    this.clickedCellPosition = clickedPosition;
                    highlight(clicked.getPiece().getPossibleMoves());
                    repaint();
                }
            }
        } else { // player clicked mouse when some cell chosen
            int prevX = clickedCellPosition.x;
            int prevY = clickedCellPosition.y;
            Piece piece = cells[prevY][prevX].getPiece();
            if(piece == null)
                return;
            for (Move possibleMove : piece.getPossibleMoves()) {
                if (possibleMove.after.y == clicked.getPosition().y && possibleMove.after.x == clicked.getPosition().x) {
                    move = possibleMove;
                    break;
                }
            }

            if (!clicked.getOccupation()) {
                System.out.println("Mouse clicked to move component to vacant cell");

                isMoved = moveIfPossible(move, null); //movePiece from clickedCell to clicked
                this.clickedCellPosition = previousClickedCell;
            }
            if (clicked.getOccupation()) {
                if (clicked.getPiece().getColourAsString().equals("White") && !whiteTurn
                        || clicked.getPiece().getColourAsString().equals("Black") && whiteTurn) {
                    System.out.println("Mouse clicked to beat component");
                    isMoved2 = moveIfPossible(move, null); //beat clicked with clickedCell
                    this.clickedCellPosition = previousClickedCell;
                }
                if (clicked.getPiece().getColourAsString().equals("White") && whiteTurn
                        || clicked.getPiece().getColourAsString().equals("Black") && !whiteTurn) {
                    System.out.println("Mouse clicked to change component to move");
                    this.clickedCellPosition = clickedPosition;
                    highlight(clicked.getPiece().getPossibleMoves());
                    repaint();
                }
            }
            if (isMoved || isMoved2) {
                resetHighlight();
                repaint();
                System.out.println("One of the components was moved");
                setBoardAltered(true); // sets board status as altered
                clearMoves();
                Cell.Colour playerColour = whiteTurn ? Cell.Colour.white : Cell.Colour.black;
                state = CheckDetector.isOpponentChecked(this, playerColour);
                Message message = new Message(move, state, "M");
                switchTurn();
                if (connectionHandler != null) {
                    if(!waitOnPromotion)
                        connectionHandler.send(message);
                    disableGame();
                }
                if (state == CheckDetector.State.checkmate) {
                    System.out.println("GAME OVER");
                    disableGame();
                    final JFrame warningWindow = new JFrame();
                    if (!singlePlayer) {
                        JOptionPane.showMessageDialog(warningWindow,
                                "You won!",
                                "End of game",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        String winnerColor = whiteTurn ? "White" : "Black";
                        JOptionPane.showMessageDialog(warningWindow,
                                winnerColor + " won!",
                                "End of game",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                }
            }
        }
        repaint();
    }

    private void highlight(ArrayList<Move> movesOfSelectedPiece){
        resetHighlight();
        for(Move move : movesOfSelectedPiece){
            cells[move.after.y][move.after.x].highlight(true);
        }
    }
    private void resetHighlight(){
        for(Cell[] cellRow : cells){
            for(Cell cell : cellRow)
                cell.highlight(false);
        }
    }

    // this function is invoked when the mouse enters a component
    public void mouseEntered(MouseEvent e) {
    }

    // this function is invoked when the mouse exits the component
    public void mouseExited(MouseEvent e) {
    }

    // this function is invoked when the mouse button has been pressed on a component
    public void mousePressed(MouseEvent e) {
    }

    // this function is invoked when the mouse button has been released on a component
    public void mouseReleased(MouseEvent e) {
    }

    public void onMessageReceived(Message message) {
        moveIfPossible(message.move, message.move.promotion);
        enableGame = true;
        whiteTurn = playerColour == Cell.Colour.white;
        if (message.state == CheckDetector.State.check)
            System.out.println("CHECKED");
        else if (message.state == CheckDetector.State.checkmate) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "You lost!",
                    "End of game",
                    JOptionPane.INFORMATION_MESSAGE);
            enableGame = false;
        }
        clickedCellPosition = nullPosition;
    }
}
