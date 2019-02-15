import java.io.Serializable;

/**
 * King Chess Piece
 * @author Emily DeLisle
 * @version 1.0
 */
public class King extends Piece implements Serializable {

    /**
     * Constructor for the King.
     * @param player the Player that owns this King
     * @param square the Square this King is on
     */
    public King(Player player, Square square) {
        super(player, square);
        super.name = "King";
        if (player.getName().equals("Black")) {
            super.icon = "icons/bking.png";
        } else {
            super.icon = "icons/wking.png";
        }
    }

    /**
     * Determines whether the destination Square is a valid move for the King.
     * @param square the potential Square to move to
     * @return whether the move is valid or not
     */
    @Override
    boolean isValidMove(Square square) {
        int x1 = super.currentSquare.getCoords()[0];
        int x2 = square.getCoords()[0];
        int y1 = super.currentSquare.getCoords()[1];
        int y2 = square.getCoords()[1];
        int distanceX = Math.abs(x1 - x2);
        int distanceY = Math.abs(y1 - y2);

        // Can only move one space in any direction
        final int moveCount = 1;

        // Move can be in a straight line or diagonal, but must be only one Square
        // If a Piece is in the destination Square, it must owned by the opposing Player for the
        // King to capture it
        if (distanceX == moveCount || distanceY == moveCount) {
            if ((x1 == x2 || y1 == y2) || (distanceX == distanceY)) {
                if (square.isEmpty()) {
                    return true;
                } else if (!square.isEmpty()
                        && !square.getPiece().getPlayer().equals(super.getPlayer())) {
                    System.out.println("King captures " + square.getPiece().getName());
                    return true;
                }
            }
        }
        System.out.println("Invalid King move");
        return false;
    }
}
