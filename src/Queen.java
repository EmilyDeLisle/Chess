import java.io.Serializable;

/**
 * Queen Chess Piece
 * @author Emily DeLisle
 * @version 1.0
 */
public class Queen extends Piece implements Serializable {

    /**
     * Constructor for the Queen.
     * @param player the Player that owns this Queen
     * @param square the Square this Queen is on
     */
    public Queen(Player player, Square square) {
        super(player, square);
        super.name = "Queen";
        if (player.getName().equals("Black")) {
            super.icon = "icons/bqueen.png";
        } else {
            super.icon = "icons/wqueen.png";
        }
    }

    /**
     * Determines whether the destination Square is a valid move for the Queen.
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

        // Move can be in a straight line along one axis
        // If a Piece is in the destination Square, it must owned by the opposing Player for the
        // Queen to capture it
        if (x1 == x2 || y1 == y2) {
            if (square.isEmpty()) {
                return true;
            } else if (!square.isEmpty()
                    && !square.getPiece().getPlayer().equals(super.getPlayer())) {
                System.out.println("Queen captures " + square.getPiece().getName());
                return true;
            }
        }

        // Move can be in a diagonal line along one axis
        // If a Piece is in the destination Square, it must owned by the opposing Player for the
        // Queen to capture it
        if (distanceX == distanceY) {
            if (square.isEmpty()) {
                return true;
            } else if (!square.isEmpty()
                    && !square.getPiece().getPlayer().equals(super.getPlayer())) {
                System.out.println("Queen captures " + square.getPiece().getName());
                return true;
            }
        }

        System.out.println("Invalid Queen move");
        return false;
    }
}
