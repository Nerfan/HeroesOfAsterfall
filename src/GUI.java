import Units.Enemy;
import Units.Player;
import Units.Weapon;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Observable;
import java.util.Observer;

/**
 * A GUI for the game
 * Standard output (i.e. System.out) is redirected to a TextArea in the GUI.
 */
public class GUI extends Application implements Observer{
    /** Connection to the game model */
    private HoAModel model;

    /** GUI Components */
    private BorderPane mainBorder;

    @Override
    public void init() {
        this.model = new HoAModel("data/players.txt", "data/level1.txt", "data/weapons.txt");
        this.model.addObserver(this);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(createScene());
        stage.setTitle("Heroes of Asterfall");
        stage.show();
    }

    /**
     * Create the scene to be initially be shown on the gui
     * Anything that involves the way the GUI appears should be in here.
     * This is called by the start method; it's separated for easier to read code.
     * @return A scene to be shown in the start method
     */
    private Scene createScene() {
        // The top-most node; everything else in the GUI should go under here
        mainBorder = new BorderPane();

        // Creates a TextArea that echoes standard output (probably temporary)
        TextArea ta = new TextArea();
        ta.setEditable(false);
        ta.setMaxWidth(500);
        redirectConsoleTo(ta);
        mainBorder.setLeft(ta);

        drawMain();

        return new Scene(mainBorder);
    }

    /**
     * Draws the right hand side of the GUI (intended to show players/enemies, etc)
     */
    private void drawMain() {
        HBox units = new HBox(50);

        VBox players = new VBox(20);
        for (Player player : model.getPlayers().values()) {
            Button button = new Button(player.getName());
            button.setOnAction(e -> this.model.setGameState(player.getName()));
            button.setMaxWidth(100);
            players.getChildren().add(button);
        }
        units.getChildren().add(players);

        VBox enemies = new VBox(20);
        for (Enemy enemy : model.getEnemies().values()) {
            Button button = new Button(enemy.getName());
            button.setOnAction(e -> System.out.println(enemy));
            button.setMaxWidth(100);
            enemies.getChildren().add(button);
        }
        units.getChildren().add(enemies);
        mainBorder.setCenter(units);
    }

    /**
     * Draws the right hand side of the GUI to display player info and commands about that player
     */
    private void drawPlayer(String playerName) {
        Player player = this.model.getPlayer(playerName);
        VBox display = new VBox(20);

        Label name = new Label(player.getName());
        name.setFont(new Font("Arial", 30));

        Label hp = new Label("HP: " + player.getHp());
        hp.setFont(new Font("Arial", 20));

        Label stats = new Label(player.statsToString());

        HBox inventory = new HBox(25);
        for (Weapon weapon : player.getInventory().values()) {
            Button button = new Button(weapon.getName());
            button.setOnAction(e -> this.model.switchWeapon(playerName, weapon.getName()));
            inventory.getChildren().add(button);
        }

        Button back = new Button("Back");
        back.setOnAction(e -> this.model.setGameState("main"));

        display.getChildren().addAll(name, hp, stats, inventory, back);
        mainBorder.setCenter(display);
    }

    /**
     * Point of entry; simply calls Application.launch()
     * That method then calls the init() method, then the start method.
     * @param args Unused
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void update(Observable observable, Object o) {
        if (this.model.getGameState().equals("main")) {
            this.drawMain();
        } else if (this.model.getPlayers().containsKey(this.model.getGameState().toLowerCase())) {
            this.drawPlayer(this.model.getGameState());
        }
    }

    private static void redirectConsoleTo(TextArea textArea) {
        PrintStream ps = new PrintStream(new Console(textArea));
        System.setOut(ps);
        System.setErr(ps);
    }

    /**
     * Utility class used to redirect standard output
     */
    private static class Console extends OutputStream {

        private TextArea output;

        public Console(TextArea ta) {
            this.output = ta;
        }

        @Override
        public void write(int i) throws IOException {
            output.appendText(String.valueOf((char) i));
        }
    }
}
