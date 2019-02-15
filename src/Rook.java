import java.io.Serializable;

/**
 * Rook Chess Piece
 * @author Emily DeLisle
 * @version 1.0
 */
public class Rook extends Piece implements Serializable {

    /**
     * Constructor for the Rook.
     * @param player the Player that owns this Rook
     * @param square the Square this Rook is on
     */
    public Rook(Player player, Square square) {
        super(player, square);
        super.name = "Rook";
        if (player.getName().equals("Black")) {
            super.icon = "icons/brook.png";
        } else {
            super.icon = "icons/wrook.png";
        }
    }

    /**
     * Determines whether the destination Square is a valid move for the Rook.
     * @param square the potential Square to move to
     * @return whether the move is valid or not
     */
    @Override
    boolean isValidMove(Square square) {
        int x1 = super.currentSquare.getCoords()[0];
        int x2 = square.getCoords()[0];
        int y1 = super.currentSquare.getCoords()[1];
        int y2 = square.getCoords()[1];

        // Move must be in a straight line along one axis
        // If a Piece is in the destination Square, it must owned by the opposing Player for the
        // Rook to capture it
        if (x1 == x2 || y1 == y2) {
            if (square.isEmpty()) {
                return true;
            } else if (!square.isEmpty()
                    && !square.getPiece().getPlayer().equals(super.getPlayer())) {
                System.out.println("Rook captures " + square.getPiece().getName());
                return true;
            }
        }
        System.out.println("Invalid Rook move");
        return false;
    }
}
