/**
 * Generic Piece placeholder class. Currently, all Pieces have the same behaviour. In future
 * versions, the various Piece types will all have differing move sets.
 * @author Emily DeLisle
 * @version 2.0
 */
public class GenericPiece extends Piece {

    /**
     * Constructor for the Piece.
     * @param player the Player that owns this Piece
     * @param square the Square this Piece is on
     */
    public GenericPiece(Player player, Square square) {
        super(player, square);
        super.name = "GenericPiece";
    }

    /**
     * Determines whether a destination Square is a valid move for this Piece
     * @param square the potential Square to move to
     * @return whether the Piece can move or not
     */
    @Override
    boolean isValidMove(Square square) {
        return true;
    }

}
