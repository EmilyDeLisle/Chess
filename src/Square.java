import java.io.Serializable;

/**
 * Square - Chess board Square that holds a Piece.
 */
public class Square implements Serializable {

    /** Coordinates of this Square */
    private int[] coords = new int[2];

    /** Piece currently on this Square */
    private Piece piece;

    /**
     * Constructor for the Square
     * @param x X coordinate of this Square
     * @param y Y coordinate of this Square
     */
    public Square(int x, int y) {
        coords[0] = x;
        coords[1] = y;
        piece = null;
    }

    /**
     * Gets the coordinatates of this Square
     * @return the coordinates
     */
    public int[] getCoords() {
        return coords;
    }

    /**
     * Gets the Piece on this Square
     * @return the Piece on this Square
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Places a Piece on this Square
     * @param piece the Piece to placex
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * Determines whether this Square is empty or not
     * @return boolean empty or not
     */
    public boolean isEmpty () {
        return piece == null;
    }
}
