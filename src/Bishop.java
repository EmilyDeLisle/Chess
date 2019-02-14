import java.io.Serializable;

/**
 * Bishop Chess Piece
 * @author Emily DeLisle
 * @version 1.0
 */
public class Bishop extends Piece implements Serializable {

    /**
     * Constructor for the Bishop.
     * @param player the Player that owns this Bishop
     * @param square the Square this Bishop is on
     */
    public Bishop(Player player, Square square) {
        super(player, square);
        super.name = "Bishop";
        if (player.getName().equals("Black")) {
            super.icon = "icons/bbishop.png";
        } else {
            super.icon = "icons/wbishop.png";
        }
    }

    /**
     * Determines whether the destination Square is a valid move for the Bishop.
     * @param square the potential Square to move to
     * @return whether the move is valid or not
     */
    @Override
    boolean isValidMove(Square square) {
        int[] currentCoords = super.currentSquare.getCoords();
        int[] destCoords = square.getCoords();
        int distanceX = Math.abs(currentCoords[0] - destCoords[0]);
        int distanceY = Math.abs(currentCoords[1] - destCoords[1]);

        // Move must be in a diagonal line along one axis
        // If a Piece is in the destination Square, it must owned by the opposing Player for the
        // Bishop to capture it
        if (distanceX == distanceY) {
            if (square.isEmpty()) {
                return true;
            } else if (!square.isEmpty()
                    && !square.getPiece().getPlayer().equals(super.getPlayer())) {
                System.out.println("Bishop captures " + square.getPiece().getName());
                return true;
            }
        }
        System.out.println("Invalid Bishop Move");
        return false;
    }
}
