import java.io.Serializable;

/**
 * Player class for the Chess game.
 */
public class Player implements Serializable {
    /** Name of this Player. For classic Chess, name is either "black" or "white". */
    private String name;

    /**
     * Constructor for the Player.
     * @param name the name of this Player
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Gets the name of this Player
     * @return the name of this Player
     */
    public String getName() {
        return name;
    }
}
