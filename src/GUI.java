import Units.Enemy;
import Units.Player;
import Units.Weapon;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * A GUI for the game
 * Standard output (i.e. System.out) is redirected to a TextArea in the GUI.
 */
public class GUI extends Application implements Observer{
    // Stuff involving interaction with the model
    /** Connection to the game model */
    private HoAModel model;
    /** Used to store unit names to be referenced when combat or heal are called */
    private String unit1, unit2;
    /** For anything that requires more a list of units (e.g. Multi-Shot or Link Heal) */
    private ArrayList<String> multiTargets;
    /** Distance for combat */
    private int distance;

    // Brute force method used to tell the GUI exactly what is happening and how to interpret inputs
    /** Anything that requires more than one easy input should be a different mode (e.g. attacking or healing) */
    private enum Mode {
        ATTACK,
        HEAL,
        MULTISHOT,
        PIERCE,
        BACKSTAB,
        SUPERNOVA,
        LINKHEAL,
        ADAPTABILITY
    }
    /** What is happening right now; used while drawing parts of the GUI that could be used for multiple purposes. */
    private Mode mode;

    // GUI stuff
    /** Top-most GUI component; referenced in several different drawing methods */
    private BorderPane mainBorder;
    /** Tells the user whether it is player or enemy phase and what turn number it currently is */
    private Label turnCount;

    @Override
    public void init() {
        // TODO This needs to be able to change based on user input
        this.model = new HoAModel("data/players.txt", "data/level1.txt", "data/weapons.txt");
        this.model.addObserver(this);
        this.multiTargets = new ArrayList<>();
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

        // Creates a TextArea that echoes standard output
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
        turnCount = new Label("PLAYER Phase\nTurn 1");
        turnCount.setFont(new Font("Arial", 40));
        Button endTurn = new Button("End Turn");
        endTurn.setOnAction(e -> model.nextPhase());
        Button save = new Button("Save");
        save.setOnAction(e -> model.save());
        Button healAll = new Button("Heal All");
        healAll.setOnAction(e -> model.healAll());
        buttons.getChildren().addAll(turnCount, endTurn, save, healAll);
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

        // Ability buttons
        switch (player.getRole()) {
            case "Marksman":
                Button multiShot = new Button("Multi-Shot");
                multiShot.setOnAction(e -> {
                    unit1 = playerName;
                    mode = Mode.MULTISHOT;
                    drawEnemies();
                });

                Button pierce = new Button("Pierce");
                pierce.setOnAction(e -> {
                    unit1 = playerName;
                    mode = Mode.PIERCE;
                    drawEnemies();
                });
                buttons.getChildren().addAll(multiShot, pierce);
                break;
            case "Assassin":
                Button backstab = new Button("Backstab");
                backstab.setOnAction(e -> {
                    unit1 = playerName;
                    mode = Mode.BACKSTAB;
                    drawEnemies();
                });
                break;
            case "Monk":
                break;
            case "Gladiator":
                break;
            case "Paladin":
                break;
            case "Shaman":
                Button empoweredStrike = new Button("Empowered Strike");
                // TODO
                buttons.getChildren().add(empoweredStrike);
                break;
            case "Saint":
                Button linkHeal = new Button("Link Heal");
                linkHeal.setOnAction(e -> {
                    unit1 = playerName;
                    mode = Mode.LINKHEAL;
                    drawPlayers();
                });
                buttons.getChildren().add(linkHeal);
                break;
            case "Sorcerer":
                Button supernova = new Button("Supernova");
                supernova.setOnAction(e -> {
                    unit1 = playerName;
                    mode = Mode.SUPERNOVA;
                    drawEnemies();
                });
                buttons.getChildren().add(supernova);
                break;
            case "Blademaster":
                break;
            case "Strategist":
                Button adaptability = new Button("Adaptability");
                adaptability.setOnAction(e -> {
                    unit1 = playerName;
                    mode = Mode.ADAPTABILITY;
                    drawEnemies();
                });
                buttons.getChildren().add(adaptability);
                break;
        }

        Button back = new Button("Back");
        back.setOnAction(e -> drawMain());

        buttons.getChildren().addAll(attack, heal, back);

        display.getChildren().addAll(name, hp, stats, equipped);

        // Add stuff for actions only if the player is able to take an action
        if (player.hasTurn()) {
            display.getChildren().addAll(inventory, buttons);
        } else {
            display.getChildren().add(back);
        }
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

        display.getChildren().addAll(name, hp, stats, equipped);

        // Only add actions if the enemy can take an action
        if (enemy.hasTurn()) {
            display.getChildren().addAll(buttons);
        } else {
            display.getChildren().add(back);
        }

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
                if (mode.equals(Mode.ATTACK) || mode.equals(Mode.BACKSTAB)) {
                    unit2 = enemy.getName();
                    drawRanges();
                } else if (mode.equals(Mode.ADAPTABILITY)) {
                    unit2 = enemy.getName();
                    drawPlayers();
                } else if (mode.equals(Mode.HEAL)) {
                    model.heal(unit1, enemy.getName());
                } else if (mode.equals(Mode.MULTISHOT) || mode.equals(Mode.PIERCE) || mode.equals(Mode.SUPERNOVA)) {
                    multiTargets.add(enemy.getName());
                }
            });
            button.setMaxWidth(100);
            enemies.getChildren().add(button);
        }
        Button done = new Button("Done");
        done.setOnAction(e -> {
            switch(mode) {
                case MULTISHOT:
                    model.multiShot(unit1, multiTargets);
                    break;
                case PIERCE:
                    model.pierce(unit1, multiTargets);
                    break;
                case SUPERNOVA:
                    model.supernova(unit1, multiTargets);
                    break;
                default:
                    drawMain();
                    break;
            }
        });
        enemies.getChildren().add(done);
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
                } else if (mode.equals(Mode.LINKHEAL) || mode.equals(Mode.ADAPTABILITY)) {
                    multiTargets.add(player.getName());
                }
            });
            button.setMaxWidth(100);
            players.getChildren().add(button);
        }
        Button done = new Button("Done");
        done.setOnAction(e -> {
            switch(mode) {
                case LINKHEAL:
                    model.linkHeal(unit1, multiTargets);
                    break;
                case ADAPTABILITY:
                    drawRanges();
                    break;
                default:
                    drawMain();
                    break;
            }
        });
        players.getChildren().add(done);
        mainBorder.setCenter(players);
    }

    /**
     * Last step before combat
     */
    private void drawRanges() {
        VBox ranges = new VBox(10);

        Button one = new Button("1");
        one.setOnAction(e -> {
            distance = 1;
            enterCombat();
        });
        one.setMinSize(100, 100);
        Button two = new Button("2");
        two.setOnAction(e -> {
            distance = 2;
            enterCombat();
        });
        two.setMinSize(100, 100);
        Button three = new Button("3");
        three.setOnAction(e -> {
            distance = 3;
            enterCombat();
        });
        three.setMinSize(100, 100);

        ranges.getChildren().addAll(one, two, three);
        mainBorder.setCenter(ranges);
    }

    /**
     * Simple utility function to enter into different combat scenarios based on the current mode.
     * For example, backstabbing an opponent is slightly different than simply attacking one.
     */
    private void enterCombat() {
        switch (mode) {
            case ATTACK:
                model.combat(unit1, unit2, distance);
                break;
            case BACKSTAB:
                model.backstab(unit1, unit2, distance);
                break;
            case ADAPTABILITY:
                model.adaptability(unit1, unit2, distance, multiTargets);
                break;
        }
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
        // Whenever anything changes in the model, we can return to the main screen (all units displayed)
        drawMain();
        turnCount.setText(model.getPhase() + " Phase\n Turn " + model.getTurnCount());
    }

    /**
     * Utility method that redirects standard output (i.e. System.out) to a specified text area
     * @param textArea Text Area to display standard output
     */
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

        private Console(TextArea ta) {
            this.output = ta;
        }

        @Override
        public void write(int i) throws IOException {
            output.appendText(String.valueOf((char) i));
        }
    }
}
