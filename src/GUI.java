import Units.Enemy;
import Units.Player;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    HoAModel model;

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
     * Create the scene to be shown on the gui
     * Anything that involves the way the GUI appears should be in here.
     * This is called by the start method; it's separated for easier to read code.
     * @return A scene to be shown in the start method
     */
    private Scene createScene() {
        // The top-most node; everything else in the GUI should go under here
        BorderPane mainBorder = new BorderPane();

        // Creates a TextArea that echoes standard output
        TextArea ta = new TextArea();
        ta.setEditable(false);
        ta.setMaxWidth(500);
        redirectConsoleTo(ta);
        mainBorder.setLeft(ta);

        HBox units = new HBox(50);
        mainBorder.setRight(units);

        VBox players = new VBox(20);
        for (Player player : model.getPlayers().values()) {
            Button button = new Button(player.getName());
            button.setOnAction(e -> System.out.println(player));
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

        return new Scene(mainBorder);
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
