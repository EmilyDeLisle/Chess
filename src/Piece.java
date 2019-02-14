import java.io.Serializable;

/**
 * Abstract Piece class from which all other Pieces are derived.
 * @author Emily DeLisle
 * @version 2.0
 */
abstract class Piece implements Serializable {

    /** The name of this Piece */
    String name;

    /** The icon for this piece */
    String icon;

    /** The Player that owns this Piece */
    Player player;

    /** The Square this Piece is currently on */
    Square currentSquare;

    /**
     * Constructor for the Piece.
     * @param player the Player that owns this Piece
     */
    public Piece(Player player, Square square) {
        this.player = player;
        currentSquare = square;
        name = "Piece";
        icon = null;
    }

    /**
     * Gets the icon of this Piece.
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Gets the name of this Piece.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the Player that owns this Piece.
     * @return the Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the Square this piece is currently on
     * @param square the Square this Piece is on
     */
    public void setSquare (Square square) {
        currentSquare = square;
    }

    /**
     * Determines whether a destination Square is one of the valid moves of this Piece.
     * @param square the potential Square to move to
     * @return whether the Piece can move or not
     */
    abstract boolean isValidMove(Square square);

}
