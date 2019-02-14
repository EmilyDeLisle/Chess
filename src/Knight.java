import java.io.Serializable;

/**
 * Knight Chess Piece
 * @author Emily DeLisle
 * @version 1.0
 */
public class Knight extends Piece implements Serializable {

    /**
     * Constructor for the Knight.
     * @param player the Player that owns this Knight
     * @param square the Square this Knight is on
     */
    public Knight(Player player, Square square) {
        super(player, square);
        super.name = "Knight";
        if (player.getName().equals("Black")) {
            super.icon = "icons/bknight.png";
        } else {
            super.icon = "icons/wknight.png";
        }
    }

    /**
     * Determines whether the destination Square is a valid move for the Knight.
     * @param square the potential Square to move to
     * @return whether the move is valid or not
     */
    @Override
    boolean isValidMove(Square square) {
        int[] currentCoords = super.currentSquare.getCoords();
        int[] destCoords = square.getCoords();
        int distanceX = Math.abs(currentCoords[0] - destCoords[0]);
        int distanceY = Math.abs(currentCoords[1] - destCoords[1]);

        // Distance must be 2 or less Squares away on either axis
        if (distanceX <= 2 && distanceY <= 2) {
            // If two Squares away on the X axis, must be 1 Square away on the Y axis
            if (distanceY == 2 && distanceX == 1) {
                if (square.isEmpty()) {
                    return true;
                } else if (!square.isEmpty()
                        && !square.getPiece().getPlayer().equals(super.getPlayer())) {
                    System.out.println("Knight captures " + square.getPiece().getName());
                    return true;
                }
            // If two Squares away on the Y axis, must be 1 Square away on the X axis
            } else if (distanceX == 2 && distanceY == 1) {
                if (square.isEmpty()) {
                    return true;
                } else if (!square.isEmpty()
                        && !square.getPiece().getPlayer().equals(super.getPlayer())) {
                    System.out.println("Knight captures " + square.getPiece().getName());
                    return true;
                }
            }
        }
        System.out.println("Invalid Knight Move");
        return false;
    }


}
