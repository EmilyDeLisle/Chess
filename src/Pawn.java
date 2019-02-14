import java.io.Serializable;

/**
 * Pawn chess Piece
 * @author Emily DeLisle
 * @version 1.0
 */
public class Pawn extends Piece implements Serializable {

    // Pawns can move two Squares on their first move
    private int moveCount = 2;

    /**
     * Constructor for the Pawn.
     * @param player the Player that owns this Pawn
     * @param square the Square this Pawn is on
     */
    public Pawn(Player player, Square square) {
        super(player, square);
        super.name = "Pawn";
        if (player.getName().equals("Black")) {
            super.icon = "icons/bpawn.png";
        } else {
            super.icon = "icons/wpawn.png";
        }
    }

    /**
     * Determines whether the destination Square is a valid move for the Pawn.
     * @param square the potential Square to move to
     * @return whether the move is valid or not
     */
    @Override
    boolean isValidMove(Square square) {
        int[] currentCoords = super.currentSquare.getCoords();
        int[] destCoords = square.getCoords();
        int distanceX = Math.abs(currentCoords[0] - destCoords[0]);
        int distanceY = Math.abs(currentCoords[1] - destCoords[1]);

        // Must be in a straight line
        boolean straight = false;
        if (currentCoords[0] == destCoords[0]) {
            straight = true;
        }

        // Must be down if Black
        // Must be up if White
        boolean direction = false;
        if ((super.player.getName().equals("Black") && currentCoords[1] < destCoords[1])
            || (super.player.getName().equals("White") && currentCoords[1] > destCoords[1])) {
            direction = true;
        }

        // Can only move two Squares on first move, otherwise can only move one
        // Destination Square must be empty
        if (straight && direction && distanceY <= moveCount
            && square.isEmpty()) {
            moveCount = 1;
            return true;
        }

        // There must be a piece of the opposite Player in the destination Square for the Piece to
        // move diagonally. Can only move one Square if moving diagonally.
        if (distanceX == distanceY && distanceX == 1
                && !square.isEmpty()) {
            if (!square.getPiece().getPlayer().equals(super.getPlayer())) {
                moveCount = 1;
                System.out.println("Pawn captures " + square.getPiece().getName());
                return true;
            }
        }
        System.out.println("Invalid Pawn move");
        return false;
    }
}
