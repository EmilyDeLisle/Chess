import java.io.*;

/**
 * Game - runs the logic of the game for the GUI Chess Board.
 * @author Emily DeLisle
 * @version 2.0
 */
public class Game implements Serializable {

    /** Black player */
    private Player black = new Player("Black");

    /** White player */
    private Player white = new Player("White");

    /** The Player whose turn it currently is */
    Player turn;

    /** 2D array that holds all the Squares at coordinates corresponding to a Chess board */
    private Square[][] squares;

    /** Click counter */
    private int clickCount = 1;

    /** Origin Square */
    private Square square1;

    /** Destination Square */
    private Square square2;

    /** Constructor for the Game - initializes the Board to its starting state */
    public Game() {
        initializeBoard();
    }

    /**
     * Initial board setup. Creates all the Squares, sets all their coordinates, and creates the
     * Pieces on their starting Squares that belong to each Player.
     */
    private void initializeBoard() {

        squares = new Square[8][8];

        // Sets the initial turn to the White Player
        turn = white;

        final int maxSquares = 8;
        for(int i = 0; i < maxSquares; i ++) {
            for (int j = 0; j < maxSquares; j ++) {

                Square square = new Square(j, i);
                Piece piece;
                Player player;

                if (i < 2) {
                    player = black;
                } else {
                    player = white;
                }

                if (i == 0 || i == 7) {
                    switch(j) {
                        case 3:
                            piece = new Queen(player, square);
                            break;
                        case 4:
                            piece = new King(player, square);
                            break;
                        case 2:
                        case 5:
                            piece = new Bishop(player, square);
                            break;
                        case 1:
                        case 6:
                            piece = new Knight(player, square);
                            break;
                        default:
                            piece = new Rook(player, square);
                    }
                    square.setPiece(piece);
                } else if (i == 1 || i == 6) {
                    piece = new Pawn(player, square);
                    square.setPiece(piece);
                }
                squares[j][i] = square;
            }
        }
    }

    /**
     * A Square is clicked on the GUI. This has several outcomes:
     * 1. If this is the first click, and the Square is empty, nothing happens.
     * 2. If the first Square was not empty, obtain a reference to that Square, and increment the
     *    click counter to 2.
     * 3. If this is the second click, and it is on an empty Square, move the Piece that was on the
     *    origin Square to the destination Square, reset click counter to 1.
     * 4. If there is a Piece on the second Square, and that piece is owned by the Player who is
     *    opposing the current Player, remove that Piece from the game and move the Piece on the
     *    origin Square to the destination Square, reset the click counter to 1.
     * @param coords the coordinates of the Square being clicked on
     * @return true if a Piece will be moved by this click
     */
    public boolean selectSquare(int[] coords) {
        // If it is the first click, get the first Square.
        if (clickCount == 1) {
            square1 = squares[coords[0]][coords[1]];
            // If there is a Piece in the Square, and the current Player is the owner of this Piece,
            // click process continues to the second click.
            if (!square1.isEmpty() && square1.getPiece().getPlayer() == turn) {
                clickCount++;
                return false;
            // If there is no Piece in the Square, click process does not progress
            } else {
                return false;
            }
        // If this is the second click, checks if the path to the destination Square is clear.
        // If it is, move the Piece to the destination Square if the Square is empty.
        // If the destination Square is not empty, the Piece there must be owned by the opposing
        // Player in order to capture it. Otherwise, the move is not valid.
        } else {
            square2 = squares[coords[0]][coords[1]];
            clickCount = 1;
            if (!square2.isEmpty()) {
                // If the Piece is a Knight, don't check for a clear path
                if (square1.getPiece() instanceof Knight) {
                    if (square1.getPiece().isValidMove(square2)) {
                        move(square1, square2);
                        setTurn();
                        return true;
                    }
                } else {
                    // If the Piece is not a Knight, check for a clear path
                    if (clearPath(square1, square2)) {
                        if (square1.getPiece().isValidMove(square2)) {
                            move(square1, square2);
                            setTurn();
                            return true;
                        }
                    }
                }
            } else {
                if (square1.getPiece() instanceof Knight) {
                    if (square1.getPiece().isValidMove(square2)) {
                        move(square1, square2);
                        setTurn();
                        return true;
                    }
                } else {
                    if (clearPath(square1, square2)) {
                        if (square1.getPiece().isValidMove(square2)) {
                            move(square1, square2);
                            setTurn();
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gets the Square matching these coordinates.
     * @param coords the coordinates of the Square
     * @return the Square that matches these coordinates
     */
    public Square getSquare(int[] coords) {
        return squares[coords[0]][coords[1]];
    }

    /**
     * Gets the Piece that is on the Square matching these coordinates.
     * @param coords the coordinates of the Square
     * @return the Piece on the Square
     */
    public Piece getPiece(int[] coords) {
        return squares[coords[0]][coords[1]].getPiece();
    }

    /**
     * Moves a Piece from one Square to another.
     * @param square1 the origin Square
     * @param square2 the destination Square
     */
    public void move(Square square1, Square square2) {
        // Points the destination Square to the Piece now occupying it
        square2.setPiece(square1.getPiece());

        // Points the Piece that has just moved back at its Square
        square2.getPiece().setSquare(square2);

        // Sets the Piece reference in the now empty Square to null
        square1.setPiece(null);
    }

    /**
     * Flips the turn to the other Player.
     */
    private void setTurn() {
        if (turn == black) {
            turn = white;
        } else {
            turn = black;
        }
    }

    /**
     * Determines if the desired path between Squares is clear or not.
     * @param square1 The origin Square
     * @param square2 The destination Square
     * @return whether the path is clear of Pieces or not
     */
    private boolean clearPath(Square square1, Square square2) {
        int x1 = square1.getCoords()[0];
        int x2 = square2.getCoords()[0];
        int y1 = square1.getCoords()[1];
        int y2 = square2.getCoords()[1];

        int targetX;
        int targetY;
        int startX;
        int startY;

        if (y1 > y2) {
            targetY = y1;
            startY = y2 + 1;
        } else {
            targetY = y2;
            startY = y1 + 1;
        }

        if (x1 > x2) {
            targetX = x1;
            startX = x2 + 1;
        } else {
            targetX = x2;
            startX = x1 + 1;
        }

        // Straight path along Y axis
        if (x1 == x2) {

            // Go along y
            while (startY < targetY) {
                if (!squares[x1][startY].isEmpty()) {
                return false;
                } else {
                    startY++;
                }
            }
        // Straight path along X axis
        } else if (y1 == y2) {
            // Go along x
            while (startX < targetX) {
                if (!squares[startX][y1].isEmpty()) {
                return false;
                } else {
                    startX++;
                }
            }
        }

        // Angled path
        if (Math.abs(x1 - x2) == Math.abs(y1 - y2)) {
            int smallerX;
            int largerX;
            if (x1 > x2) {
                largerX = x1;
                smallerX = x2;
            } else {
                smallerX = x1;
                largerX = x2;
            }
            // If the angle is down and to the left
            if ((x1 > x2 && y1 < y2) || (x1 < x2 && y1 > y2)) {
                targetX = smallerX;
                startX = largerX - 1;
                while (startX > targetX && startY < targetY) {
                    if (!squares[startX][startY].isEmpty()) {
                    return false;
                    } else {
                        startX--;
                        startY++;
                    }
                }
            // If the angle is down and to the right
            } else if ((x1 < x2 && y1 < y2) || (x1 > x2 && y1 > y2)) {
                targetX = largerX;
                startX = smallerX + 1;
                // Go to the right and down
                while (startX < targetX && startY < targetY) {
                    if (!squares[startX][startY].isEmpty()) {
                    return false;
                    } else {
                        startX++;
                        startY++;
                    }
                }
            }
        }
        return true;
    }
}
