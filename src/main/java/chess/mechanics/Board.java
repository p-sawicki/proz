package chess.mechanics;

import chess.network.*;
import chess.mechanics.pieces.*;
import chess.utilities.Utility;

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

    private final boolean hotSeat;
    private boolean isBoardAltered; // true if current board state isn't saved
    private boolean whiteTurn;
    private boolean enableGame;
    private boolean waitOnPromotion;

    private final Cell[][] cells;
    private final Point nullPosition = new Point(-1, -1);

    private Cell.Colour playerColour;
    private ConnectionHandler connectionHandler;
    private Point clickedCellPosition = new Point(-1, -1);

    private King whiteKing;
    private King blackKing;
    private Pawn lastDoubleStepBlack;
    private Pawn lastDoubleStepWhite;
    CheckDetector.State state;

    public Board(Cell.Colour playerColour, ConnectionHandler connectionHandler) {
        setBoardAltered(false);
        this.playerColour = playerColour;
        this.connectionHandler = connectionHandler;

        cells = new Cell[size][size];
        Piece[][] pieces = Utility.getStartingState();
        setLayout(new GridLayout(size, size, 0, 0));
        this.addMouseListener(this);

        Cell.Colour color = Cell.Colour.black;
        if (playerColour == Cell.Colour.black) {
            for (int y = 0; y < size; ++y) {
                for (int x = size - 1; x >= 0; --x) {
                    initCell(color, x, y, pieces[y][x]);
                    if (x != 0)
                        color = color == Cell.Colour.black ? Cell.Colour.white : Cell.Colour.black;
                }
            }
        } else {
            for (int y = size - 1; y >= 0; --y) {
                for (int x = 0; x < size; ++x) {
                    initCell(color, x, y, pieces[y][x]);
                    if (x != size - 1)
                        color = color == Cell.Colour.black ? Cell.Colour.white : Cell.Colour.black;
                }
            }
        }

        setMinimumSize(windowSize);
        setPreferredSize(windowSize);
        setSize(windowSize);

        if (connectionHandler != null) {
            hotSeat = false;
            connectionHandler.setBoard(this);
            if (connectionHandler.isAlive())
                connectionHandler.resumeReceiving();
            else
                connectionHandler.start();
        } else
            hotSeat = true;
        whiteTurn = true;
        waitOnPromotion = false;
        enableGame = connectionHandler == null || playerColour != Cell.Colour.black;
    }

    private void initCell(Cell.Colour color, int x, int y, Piece piece) {
        Cell cell = new Cell(color, this, new Point(x, y));
        cells[y][x] = cell;
        cell.board = this;
        add(cell);

        if (piece != null) {
            cell.setPiece(piece.copy());
            if (piece instanceof King) {
                setKing((King) cell.getPiece());
            }
        }
    }

    public Board(Cell[][] cells, boolean whiteTurn, boolean enableGame) {
        this.cells = new Cell[size][size];
        this.whiteTurn = whiteTurn;
        this.enableGame = enableGame;
        this.hotSeat = true;
        this.connectionHandler = null;
        setBoardAltered(false);

        this.addMouseListener(this);
        setLayout(new GridLayout(size, size, 0, 0));

        Cell.Colour color = Cell.Colour.black;
        for (int y = size - 1; y >= 0; --y) {
            for (int x = 0; x < size; ++x) {
                initCell(color, x, y, cells[y][x].getPiece());
                if (x != size - 1)
                    color = color == Cell.Colour.black ? Cell.Colour.white : Cell.Colour.black;
            }
        }

        setMinimumSize(windowSize);
        setPreferredSize(windowSize);
        setSize(windowSize);
    }

    public void switchTurn() {
        this.whiteTurn = !this.whiteTurn;
    }

    private void disableGame() {
        enableGame = false;
    }

    public int getBoardSize() {
        return size;
    }

    public boolean checkIfHotSeat() {
        return hotSeat;
    }

    public boolean checkIfBoardAltered() {
        return isBoardAltered;
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

    @Override
    protected void paintComponent(Graphics g) {
        for (int y = 0; y < size; ++y) {
            for (int x = 0; x < size; ++x)
                cells[y][x].paint(g);
        }
    }

    public boolean moveIfPossible(Move move) { // moves piece if not contradicted by rules
        if (move == null)
            return false;
        Piece movedPiece = cells[move.before.y][move.before.x].getPiece();
        cells[move.before.y][move.before.x].removePiece();
        cells[move.after.y][move.after.x].setPiece(movedPiece);
        lastDoubleStepBlack = null;
        lastDoubleStepWhite = null;
        if (movedPiece instanceof Pawn) {
            if (Math.abs(move.after.y - move.before.y) == 2)
                setLastDoubleStep((Pawn) movedPiece);
            if (move.promotion != null) {
                Piece newPiece = null;
                switch (move.promotion) {
                    case rook:
                        newPiece = new Rook(movedPiece.getColour());
                        break;
                    case queen:
                        newPiece = new Queen(movedPiece.getColour());
                        break;
                    case knight:
                        newPiece = new Knight(movedPiece.getColour());
                        break;
                    case bishop:
                        newPiece = new Bishop(movedPiece.getColour());
                        break;
                }
                cells[move.after.y][move.after.x].setPiece(newPiece);
            } else if (move.after.y == 0 || move.after.y == size - 1) {
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

                queenButton.addActionListener(e -> handlePromotion(move, promotionWindow, new Queen(movedPiece.getColour())));
                rookButton.addActionListener(e -> handlePromotion(move, promotionWindow, new Rook(movedPiece.getColour())));
                knightButton.addActionListener(e -> handlePromotion(move, promotionWindow, new Knight(movedPiece.getColour())));
                bishopButton.addActionListener(e -> handlePromotion(move, promotionWindow, new Bishop(movedPiece.getColour())));
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

    private void handlePromotion(Move move, JFrame promotionWindow, Piece newPiece) {
        cells[move.after.y][move.after.x].setPiece(newPiece);
        promotionWindow.dispose();
        enableGame = true;
        repaint();
        if (newPiece instanceof Queen)
            move.promotion = Move.Promotion.queen;
        else if (newPiece instanceof Knight)
            move.promotion = Move.Promotion.knight;
        else if (newPiece instanceof Rook)
            move.promotion = Move.Promotion.rook;
        else
            move.promotion = Move.Promotion.bishop;
        if (connectionHandler != null)
            connectionHandler.send(new Message(move, state, Message.MessageType.move));
        waitOnPromotion = false;
    }

    @Override // this function is invoked when the mouse button has been clicked (pressed and released) on a component
    public void mouseClicked(MouseEvent e) {
        if (!enableGame) // disables moves if there was a checkmate
            return;
        Cell clicked = (Cell) getComponentAt(new Point(e.getX(), e.getY()));
        Move move = null;
        Point clickedPosition = clicked.getPosition();
        boolean isMoved = false;
        Point previousClickedCell = this.clickedCellPosition;

        if (clickedCellPosition.equals(nullPosition)) { //player hasn't chosen cell to move yet
            if (clicked.getOccupation()) {
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
            if (piece == null)
                return;
            for (Move possibleMove : piece.getPossibleMoves()) {
                if (possibleMove.after.y == clicked.getPosition().y && possibleMove.after.x == clicked.getPosition().x) {
                    move = possibleMove;
                    break;
                }
            }
            if (!clicked.getOccupation()) {
                isMoved = moveIfPossible(move); //movePiece from clickedCell to clicked
                this.clickedCellPosition = previousClickedCell;
            } else {
                if (clicked.getPiece().getColourAsString().equals("White") && !whiteTurn
                        || clicked.getPiece().getColourAsString().equals("Black") && whiteTurn) {
                    isMoved = moveIfPossible(move); //beat clicked with clickedCell
                    this.clickedCellPosition = previousClickedCell;
                }
                if (clicked.getPiece().getColourAsString().equals("White") && whiteTurn
                        || clicked.getPiece().getColourAsString().equals("Black") && !whiteTurn) {
                    this.clickedCellPosition = clickedPosition;
                    highlight(clicked.getPiece().getPossibleMoves());
                    repaint();
                }
            }
            if (isMoved) {
                resetHighlight();
                repaint();
                setBoardAltered(true); // sets board status as altered
                clearMoves();
                Cell.Colour playerColour = whiteTurn ? Cell.Colour.white : Cell.Colour.black;
                state = CheckDetector.getOpponentsState(this, playerColour);
                Message message = new Message(move, state, Message.MessageType.move);
                if (connectionHandler != null) {
                    if (!waitOnPromotion)
                        connectionHandler.send(message);
                    disableGame();
                }
                if (state == CheckDetector.State.checkmate) {
                    disableGame();
                    final JFrame warningWindow = new JFrame();
                    if (!hotSeat) {
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
                switchTurn();
                clickedCellPosition = nullPosition;
            }
        }
        repaint();
    }

    private void highlight(ArrayList<Move> movesOfSelectedPiece) {
        resetHighlight();
        for (Move move : movesOfSelectedPiece) {
            cells[move.after.y][move.after.x].highlight(true);
        }
    }

    private void resetHighlight() {
        for (Cell[] cellRow : cells) {
            for (Cell cell : cellRow)
                cell.highlight(false);
        }
    }

    // this function is invoked when the mouse enters a component
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    // this function is invoked when the mouse exits the component
    @Override
    public void mouseExited(MouseEvent e) {
    }

    // this function is invoked when the mouse button has been pressed on a component
    @Override
    public void mousePressed(MouseEvent e) {
    }

    // this function is invoked when the mouse button has been released on a component
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    public void onMessageReceived(Message message) {
        moveIfPossible(message.move);
        enableGame = true;
        whiteTurn = playerColour == Cell.Colour.white;
        if (message.state == CheckDetector.State.checkmate) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "You lost!",
                    "End of game",
                    JOptionPane.INFORMATION_MESSAGE);
            disableGame();
        }
        clickedCellPosition = nullPosition;
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

    public Pawn getLastDoubleStep(Cell.Colour colour) {
        return colour == Cell.Colour.white ? lastDoubleStepWhite : lastDoubleStepBlack;
    }

    public Piece getPiece(int x, int y) {
        return cells[y][x].getPiece();
    }

    public Point getLastDoubleStepPosition(Cell.Colour colour) {
        if (colour == Cell.Colour.white)
            return lastDoubleStepWhite == null ? nullPosition : lastDoubleStepWhite.getCell().getPosition();
        else
            return lastDoubleStepBlack == null ? nullPosition : lastDoubleStepBlack.getCell().getPosition();
    }

    public void setLastDoubleStep(Point blackPosition, Point whitePosition) {
        if (!blackPosition.equals(nullPosition))
            lastDoubleStepBlack = (Pawn) cells[blackPosition.y][blackPosition.x].getPiece();
        if (!whitePosition.equals(nullPosition))
            lastDoubleStepWhite = (Pawn) cells[whitePosition.y][whitePosition.x].getPiece();
    }

    public void setBoardAltered(boolean isAltered) {
        isBoardAltered = isAltered;
    }

    public void setPieces(Piece[][] pieces) {
        for (int y = 0; y < size; ++y) {
            for (int x = 0; x < size; ++x) {
                cells[y][x].setPiece(pieces[y][x]);
                if (pieces[y][x] instanceof King) {
                    setKing((King) pieces[y][x]);
                }
            }
        }
    }

    public void setKing(King king) {
        if (king.getColour() == Cell.Colour.white)
            whiteKing = king;
        else
            blackKing = king;
    }

    public void setLastDoubleStep(Pawn pawn) {
        if (pawn.getColour() == Cell.Colour.white)
            lastDoubleStepWhite = pawn;
        else
            lastDoubleStepBlack = pawn;
    }
}