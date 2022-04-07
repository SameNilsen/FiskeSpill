package ProjectCanvas;

// import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
// import javafx.scene.canvas.Canvas;
// import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import static ProjectCanvas.Constants.WIDTH;
import static ProjectCanvas.Constants.HEIGHT;
import static ProjectCanvas.Constants.TITLE;
import static ProjectCanvas.Constants.PROJECT_CANVAS;

import java.io.IOException;

public class CanvasApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource(PROJECT_CANVAS))));
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.show();
    }

}
