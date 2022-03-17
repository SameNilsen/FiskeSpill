package Project;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

public class ProjectController implements Initializable{
    @FXML
    private TextField firstNumber, secondNumber, operator;

    @FXML
    private Label result;

    @FXML
    private Ellipse arm;

    @FXML
    private Ellipse rightArm;

    @FXML
    private Rectangle leftLeg;

    private Game game = new Game();

    private AnimationTimer timer;

    private MouseEvent mouse;

    @FXML
    private Text status;

    @FXML
    AnchorPane background;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("gallaea"+background.getPrefHeight());
        timer = new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {   
                double change_y = game.gravity();
                if (arm.localToScreen(arm.getBoundsInLocal()).getMinY() < background.getPrefHeight()){
                    // System.out.println(""+ arm.localToScreen(arm.getBoundsInLocal()).getMinY()+" "+background.getPrefHeight());
                    //arm.setCenterY(arm.getCenterY()+change_y);
                    arm.setLayoutY(arm.getLayoutY()+change_y);
                }
                if (rightArm.localToScreen(rightArm.getBoundsInLocal()).getMinY() < background.getPrefHeight()){
                    // System.out.println(""+ arm.localToScreen(arm.getBoundsInLocal()).getMinY()+" "+background.getPrefHeight());
                    //rightArm.setCenterY(rightArm.getCenterY()+change_y);
                    rightArm.setLayoutY(rightArm.getLayoutY()+change_y);
                }
                if (leftLeg.localToScreen(leftLeg.getBoundsInLocal()).getMinY() < background.getPrefHeight()){
                    leftLeg.setY(leftLeg.getY()+change_y);
                }
            }
        };
        timer.start();
    }

    private void initGame() {
        game = new Game();
    }

    @FXML
    private void handleButtonClick() {
        //arm.setCenterX(arm.getCenterX()+100);
        arm.setLayoutY(arm.getLayoutY()-100);
        //rightArm.getTransforms().add(new Rotate(30, rightArm.getTransforms().inverseDeltaTransform(rightArm.getRotationAxis())));
        // rightArm.setRotate(rightArm.getRotate() - 5);
        rightArm.getTransforms().add(new Rotate(rightArm.getRotate() + 30, rightArm.getCenterX(), rightArm.getCenterY()));
        double angle = 80*Math.sin(Math.toRadians(30));
        if ((0 < angle && angle < 90) || (-180 < angle && angle < -90)){
            System.out.println("Lener mot høyre");
        }
        if ((0 < 90 && angle < 180) || (-90 < angle && angle < 0)){
            System.out.println("Lener mot venstre");
        }
        System.out.println("------");
        arm.setRotationAxis(Rotate.Z_AXIS);
        arm.setRotate(arm.getRotate() + 30);
        System.out.println(arm.getRotate());
        System.out.println(arm.getRotationAxis());
        timer.start();
    }


    @FXML
    private void handleEllipseDrag() {
        arm.setOnMouseDragged((MouseEvent e) -> {
            arm.setCenterX(e.getX());
            arm.setCenterY(e.getY());
            timer.stop();
        });
        rightArm.setOnMouseDragged((MouseEvent e) -> {
            rightArm.setCenterX(e.getX());
            rightArm.setCenterY(e.getY());
            timer.stop();
            //System.out.println(1212);
        });
        leftLeg.setOnMouseDragged((MouseEvent e) -> {
            // leftLeg.relocate(e.getX(), e.getY());
            leftLeg.setX(e.getX());
            leftLeg.setY(e.getY());
            // leftLeg.setLayoutX(e.getY());
            // leftLeg.setLayoutY(e.getY());
            timer.stop();
        });
        //timer.start();
        //System.out.println(333);
    }

    @FXML
    private void handleEllipseRelease() {
        System.out.println(343434);
        rightArm.setOnMouseClicked((MouseEvent e) -> {
            timer.start();
        });
        arm.setOnMouseClicked((MouseEvent e) -> {
            timer.start();
        });
        background.setOnMouseClicked((MouseEvent e) -> {
            timer.start();
        });
        
    }

    public void gravity() {
        arm.setCenterX(arm.getCenterX()+1);
    }

    // @FXML
    // private void displayPosition() {
    //     status.setOnMouseMoved((MouseEvent e) -> {
    //         status.setText("X = "+e.getX()+" Y = "+e.getY());
    //     });
    // }

    @FXML
    private void displayPosition(MouseEvent e) {
        status.setText("X = "+e.getX()+" Y = "+e.getY());
    }

}
