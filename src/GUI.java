import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

/**
 * A GUI for the game
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
        // The top-most node; everything else in the GUI goes under here
        BorderPane mainBorder = new BorderPane();

        return new Scene(mainBorder);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
