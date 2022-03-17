package Project;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ProjectApp extends Application {

    private ProjectController projectcontroller;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Test App");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("ProjectAppF.fxml"))));

        // new AnimationTimer()
        // {
        //     public void handle(long currentNanoTime)
        //     {
        //         projectcontroller.gravity();
        //     }
        // }.start();

        primaryStage.show();
    }

}
