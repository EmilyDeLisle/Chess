import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.io.*;

/**
 * Board - GUI for Chess game.
 * @author Emily DeLisle
 * @version 2.0
 */
public class Board extends Application implements Serializable {

    /** ClickListener that handles mouse events. */
    class ClickListener implements EventHandler<MouseEvent> {

        /** Rectangle this ClickListener is listening on */
        private Rectangle rect;

        /** Coordinates of the Square this Rectangle represents */
        private int[] coords;

        /**
         * Constructor for ClickListener
         * @param rect Rectangle being listened on
         * @param x X coordinate of Square
         * @param y Y coordinate of Square
         */
        private ClickListener(Rectangle rect, int x, int y) {
            this.rect = rect;
            coords = new int[2];
            coords[0] = x;
            coords[1] = y;
        }

        /**
         * Handler for mouse events. Highlights the currently selected Square. If the selection
         * of the Square results in a move or a capture, display the changes on the GUI.
         * @param event the mouse event
         */
        @Override
        public void handle(MouseEvent event) {

            // Highlights the current Square
            previousRect.setStroke(null);
            rect.setStroke(Color.DEEPSKYBLUE);
            rect.setStrokeWidth(3);
            rect.setStrokeType(StrokeType.INSIDE);
            previousRect = rect;

            // If this is the second Square clicked, do a move or capture if valid
            if (game.selectSquare(coords)) {

                // Removes the highlight after the Piece has moved
                rect.setStroke(null);

                // Remove symbol of captured Piece
                if (!game.getSquare(coords).isEmpty()) {
                    root.getChildren().remove(icons[coords[0]][coords[1]]);
                }

                // Create an icon of the same Piece in new location
                Image img = new Image(currentPiece.getIcon(),
                        86, 86, true, true);
                ImageView icon = new ImageView(img);
                icon.setX(rect.getX() - 3);
                icon.setY(rect.getY() - 2);
                icon.setMouseTransparent(true);
                root.getChildren().add(icon);
                icons[coords[0]][coords[1]] = icon;

                // Remove the icon of the Piece moving
                root.getChildren().remove(icons[currentCoords[0]][currentCoords[1]]);

                // Set the Player info to the other Player
                currentPlayer.setText("Current player: " + game.turn.getName());

            // Else this is the first Square, so get its data
            } else {
                currentCoords = coords;
                if (game.getPiece(coords) != null) {
                    currentPiece = game.getPiece(currentCoords);
                }
            }
        }
    }

    /** Menu stage for user options */
    private class Menu extends Stage {

        /** FileChooser for opening/saving game files */
        private FileChooser fileChooser = new FileChooser();

        /** Position of the menu selector */
        private int select = 1;

        /** Icon for menu selection */
        private Image pawn;

        /** Menu selector */
        private ImageView pointer;

        /** Menu constructor & initial setup */
        private Menu () {

            Group layout = new Group();
            setupLayout(layout);
            Scene scene = new Scene(layout, 300, 300);
            scene.setFill(Color.DIMGREY);

            scene.setOnKeyPressed(this::processKeyEvent);
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Chess File", "*.chess"));

            this.setTitle("Menu");
            this.setScene(scene);
            this.show();
        }

        /**
         * Sets up layout for the menu window.
         * @param layout the root Group
         */
        private void setupLayout(Group layout) {
            Text title = new Text(10, 50, "Chess.");

            Text info = new Text(10, 100, "Select from the following options:");

            Text options = new Text(60, 140, "New game\n\n" +
                                    "Open game\n\n" +
                                    "Save game\n\n" +
                                    "Close menu");

            title.setFont(Font.font ("Verdana", 50));
            title.setFill(Color.WHITE);
            info.setFont(Font.font ("Verdana", 14));
            info.setFill(Color.WHITE);
            options.setFont(Font.font ("Veranda", 14));
            options.setFill(Color.WHITE);

            pawn = new Image("icons/wpawn.png", 24, 24,
                    true, true);
            pointer = new ImageView(pawn);
            pointer.setX(30);
            pointer.setY(120);

            layout.getChildren().addAll(title, info, options, pointer);
        }

        /**
         * Responds to user key input on the Menu.
         * @param event Key event
         */
        private void processKeyEvent(KeyEvent event) {
            final int yTranslate = 33;
            // Arrow key selector controls
            switch (event.getCode()) {
                case DOWN:
                    if (select < 4) {
                        pointer.setY(pointer.getY() + yTranslate);
                        select++;
                    }
                break;
                case UP:
                    if (select > 1) {
                        pointer.setY(pointer.getY() - yTranslate);
                        select--;
                    }
                    break;
                case ENTER:
                    switch (select) {
                        case 1:
                            System.out.println("Starting new game...");

                            // Create a fresh Game and reload Board
                            game = new Game();
                            populateBoard(root);

                            // Close menu
                            this.close();
                            break;
                        case 2:
                            openGame();
                            break;
                        case 3:
                            saveGame();
                            break;
                        case 4:
                            this.close();
                            break;
                        default:
                    }
                    break;
                default:
            }
        }

        /** Opens saved Game from a file chosen by the user. */
        private void openGame() {
            try {
                System.out.println("Opening game...");

                fileChooser.setTitle("Open Game");
                File openFile = fileChooser.showOpenDialog(this);

                // If the open dialog is closed before a file is selected, close the menu and
                // abort the open process
                if (openFile == null) {
                    System.out.println("File open cancelled");
                    this.close();
                    return;
                }
                // Opens Game from file
                FileInputStream fi = new FileInputStream(openFile);
                ObjectInputStream oi = new ObjectInputStream(fi);

                // Read Game object
                game = (Game) oi.readObject();

                // Close streams
                oi.close();
                fi.close();

                // Reload Board
                populateBoard(root);

                // Close menu
                this.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (IOException e) {
                System.out.println("Error initializing stream");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        /** Opens current Game from to a file chosen by the user. */
        private void saveGame() {
            try {
                System.out.println("Saving game...");

                fileChooser.setTitle("Save Game");
                fileChooser.setInitialFileName("game");
                File saveFile = fileChooser.showSaveDialog(this);

                // Creates file to write to
                FileOutputStream f = new FileOutputStream(saveFile);
                ObjectOutputStream o = new ObjectOutputStream(f);

                // Write objects to file
                o.writeObject(game);

                // Close streams
                o.close();
                f.close();

                // Close menu
                this.close();

            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (IOException e) {
                System.out.println("Error initializing stream");
            }
        }
    }

    /** Game logic */
    private Game game;

    /** Coordinates of currently active square */
    private int[] currentCoords = new int[2];

    /** Currently active Piece */
    private Piece currentPiece;

    /** Previously active Rectangle */
    private Rectangle previousRect = new Rectangle(0, 0, 0, 0);

    /** Holds all icons */
    private ImageView[][] icons = new ImageView[8][8];

    /** Current Player display */
    private Text currentPlayer;

    /** Group for objects in the Scene */
    private Group root;

    /**
     * Populates the board with black and white Rectangles and all the Pieces for each player.
     * @param group Group to display objects in the scene
     */
    private void populateBoard(Group group) {

        // Variables used in creating Rectangles
        boolean whiteSquare;
        final int maxSquares = 8;
        final int squareSize = 80;

        // Creates black and white Rectangles on the board
        for (int i = 0; i < maxSquares; i++) {
            if (i % 2 == 0) {
                whiteSquare = true;
            } else {
                whiteSquare = false;
            }
            for (int j = 0; j < maxSquares; j++) {
                Rectangle rect = new Rectangle(j * squareSize, i * squareSize,
                        squareSize, squareSize);
                if (whiteSquare) {
                    rect.setFill(Color.WHITE);
                    whiteSquare = false;
                } else {
                    rect.setFill(Color.BLACK);
                    whiteSquare = true;
                }

                // Adds a ClickListener to the Rectangle.
                int[] coords = new int[2];
                coords[0] = j;
                coords[1] = i;
                ClickListener listener = new ClickListener(rect, j, i);
                rect.setOnMouseClicked(listener);
                group.getChildren().add(rect);

                // If the game has a Piece that has a starting spot that corresponds to these
                // coordinates, create a corresponding Piece icon on the GUI.
                if (game.getPiece(coords) != null) {
                    Piece piece = game.getPiece(coords);
                    Image img = new Image(piece.getIcon(),
                            86, 86, true, true);
                    ImageView icon = new ImageView(img);
                    icon.setX(rect.getX() - 3);
                    icon.setY(rect.getY() - 2);
                    icon.setMouseTransparent(true);
                    icons[j][i] = icon;
                    group.getChildren().add(icon);
                }
            }
        }

        Rectangle infobar = new Rectangle(0,640,640,20);
        infobar.setFill(Color.GREY);

        currentPlayer = new Text(5, 655, "Current player: " + game.turn.getName());
        currentPlayer.setFont(Font.font ("Veranda", 14));
        currentPlayer.setFill(Color.WHITE);

        Text menuInfo = new Text(461, 655, "Press M to open the menu");
        menuInfo.setFont(Font.font ("Veranda", 14));
        menuInfo.setFill(Color.WHITE);

        root.getChildren().addAll(infobar, currentPlayer, menuInfo);
    }

    /**
     * Opens the Menu stage when the user hits the "M" key.
     * @param event Key event
     */
    private void processKeyEvent(KeyEvent event) {
        if (event.getCode().toString().equals("M")) {
            new Menu();
        }
    }

    /**
     * Creates and displays the Scene.
     * @param primaryStage the stage
     */
    @Override
    public void start(Stage primaryStage) {
        root = new Group();
        game = new Game();
        populateBoard(root);
        Scene scene = new Scene(root, 640, 660);
        scene.setOnKeyPressed(this::processKeyEvent);
        primaryStage.setTitle("Chess");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
