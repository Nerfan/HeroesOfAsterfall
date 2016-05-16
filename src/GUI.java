import Units.Enemy;
import Units.Player;
import Units.Weapon;
import javafx.application.Application;
import javafx.geometry.Insets;
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

    /** Anything that requires more than one easy input should be a different mode (e.g. attacking or healing) */
    private enum Mode {
        ATTACK,
        HEAL,
        MULTITARGET
    }

    /** Used to store units to be referenced when combat or heal are called */
    private String unit1, unit2;
    /** Distance for combat; currently unused */
    private int distance;
    /** What is happening right now; used while drawing parts of the GUI that could be used for multiple purposes. */
    private Mode mode;

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
        ta.setWrapText(true);
        ta.setMaxWidth(500);
        redirectConsoleTo(ta);
        HBox temp = new HBox(ta); // Just to get proper padding on the right hand side
        temp.setPadding(new Insets(0, 20, 0, 0));
        mainBorder.setLeft(temp);

        // Center
        drawMain();

         // Right side
        VBox buttons = new VBox(25);
        Button save = new Button("Save");
        save.setOnAction(e -> model.save());
        Button healAll = new Button("Heal All");
        healAll.setOnAction(e -> model.healAll());
        buttons.getChildren().addAll(save, healAll);
        mainBorder.setRight(buttons);

        return new Scene(mainBorder);
    }

    /**
     * Draws to the GUI a list of all players and enemies (clicking on one opens their info)
     */
    private void drawMain() {
        HBox units = new HBox(50);

        VBox players = new VBox(20);
        for (Player player : model.getPlayers().values()) {
            HBox info = new HBox(10);
            Button button = new Button(player.getName());
            button.setOnAction(e -> drawPlayer(player.getName()));
            button.setPrefWidth(100);
            Label hp = new Label(player.getHp() + "/" + player.getMaxhp());
            info.getChildren().addAll(button, hp);
            players.getChildren().add(info);
        }
        units.getChildren().add(players);

        VBox enemies = new VBox(20);
        for (Enemy enemy : model.getEnemies().values()) {
            HBox info = new HBox(10);
            Button button = new Button(enemy.getName());
            button.setOnAction(e -> drawEnemy(enemy.getName()));
            button.setPrefWidth(100);
            Label hp = new Label(enemy.getHp() + "/" + enemy.getMaxhp());
            info.getChildren().addAll(button, hp);
            enemies.getChildren().add(info);
        }
        units.getChildren().add(enemies);

        mainBorder.setCenter(units);
    }

    /**
     * Draws to the GUI information about a player
     * @param playerName Name of the player to get information about
     */
    private void drawPlayer(String playerName) {
        // Lots of code, but all pretty self-explanatory
        Player player = model.getPlayer(playerName);
        VBox display = new VBox(20);

        Label name = new Label(player.getName());
        name.setFont(new Font("Arial", 30));

        Label hp = new Label("HP: " + player.getHp() + "/" + player.getMaxhp());
        hp.setFont(new Font("Arial", 20));

        Label stats = new Label(player.statsToString());

        Label equipped = new Label("Equipped: " + player.getEquipped());

        HBox inventory = new HBox(25);
        for (Weapon weapon : player.getInventory().values()) {
            Button button = new Button(weapon.getName());
            button.setOnAction(e -> this.model.switchWeapon(playerName, weapon.getName()));
            inventory.getChildren().add(button);
        }

        HBox buttons = new HBox(25);

        Button attack = new Button("Attack");
        attack.setOnAction(e -> {
            unit1 = playerName;
            mode = Mode.ATTACK;
            drawEnemies();
        });

        Button heal = new Button("Heal");
        heal.setOnAction(e -> {
            unit1 = playerName;
            mode = Mode.HEAL;
            drawPlayers();
        });

        Button back = new Button("Back");
        back.setOnAction(e -> drawMain());

        buttons.getChildren().addAll(attack, heal, back);

        display.getChildren().addAll(name, hp, stats, equipped, inventory, buttons);
        mainBorder.setCenter(display);
    }

    /**
     * Draws to the GUI information about an enemy
     * @param enemyName Name of the enemy to get info from
     */
    private void drawEnemy(String enemyName) {
        // Lots of code, but all pretty self-explanatory
        Enemy enemy = model.getEnemy(enemyName);
        VBox display = new VBox(20);

        Label name = new Label(enemy.getName());
        name.setFont(new Font("Arial", 30));

        Label hp = new Label("HP: " + enemy.getHp() + "/" + enemy.getMaxhp());
        hp.setFont(new Font("Arial", 20));

        Label stats = new Label(enemy.statsToString());

        Label equipped = new Label("Equipped: " + enemy.getEquipped());

        HBox buttons = new HBox(25);

        Button attack = new Button("Attack");
        attack.setOnAction(e -> {
            unit1 = enemyName;
            mode = Mode.ATTACK;
            drawPlayers();
        });

        Button heal = new Button("Heal");
        heal.setOnAction(e -> {
            unit1 = enemyName;
            mode = Mode.HEAL;
            drawEnemies();
        });

        Button back = new Button("Back");
        back.setOnAction(e -> drawMain());

        buttons.getChildren().addAll(attack, heal, back);

        display.getChildren().addAll(name, hp, stats, equipped, buttons);
        mainBorder.setCenter(display);
    }

    /**
     * Displays all enemies for target selection
     */
    private void drawEnemies() {
        VBox enemies = new VBox(20);
        for (Enemy enemy : model.getEnemies().values()) {
            Button button = new Button(enemy.getName());
            button.setOnAction(e -> {
                // Need to see if we're attacking or healing the target
                if (mode.equals(Mode.ATTACK)) {
                    unit2 = enemy.getName();
                    drawRanges();
                } else if (mode.equals(Mode.HEAL)) {
                    model.heal(unit1, enemy.getName());
                }
            });
            button.setMaxWidth(100);
            enemies.getChildren().add(button);
        }
        mainBorder.setCenter(enemies);
    }

    /**
     * Displays all players for target selection
     */
    private void drawPlayers() {
        VBox players = new VBox(20);
        for (Player player : model.getPlayers().values()) {
            Button button = new Button(player.getName());
            button.setOnAction(e -> {
                // Need to see if we're attacking or healing the target
                if (mode.equals(Mode.ATTACK)) {
                    unit2 = player.getName();
                    drawRanges();
                } else if (mode.equals(Mode.HEAL)) {
                    model.heal(unit1, player.getName());
                }
            });
            button.setMaxWidth(100);
            players.getChildren().add(button);
        }
        mainBorder.setCenter(players);
    }

    /**
     * Last step before combat
     */
    private void drawRanges() {
        VBox ranges = new VBox(10);

        Button one = new Button("1");
        one.setOnAction(e -> model.combat(unit1, unit2, 1));
        one.setMinSize(100, 100);
        Button two = new Button("2");
        two.setOnAction(e -> model.combat(unit1, unit2, 2));
        two.setMinSize(100, 100);
        Button three = new Button("3");
        three.setOnAction(e -> model.combat(unit1, unit2, 3));
        three.setMinSize(100, 100);

        ranges.getChildren().addAll(one, two, three);
        mainBorder.setCenter(ranges);
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
        // Whenever anything chenges in the model, we can return to the main screen (all units displayed)
        drawMain();
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
