package ProjectCanvas;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class CanvasApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("FISHES");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("ProjectAppCanvas.fxml"))));
        primaryStage.setWidth(600);
        primaryStage.setHeight(600);
        primaryStage.show();
    }

}
