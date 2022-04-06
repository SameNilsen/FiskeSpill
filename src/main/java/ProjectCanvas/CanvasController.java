package ProjectCanvas;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.DoubleExpression;
import javafx.fxml.FXML;
import javafx.scene.PerspectiveCamera;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

//        Denne klassen skal være kontrolleren til spillet og skal egentlig inneholde minst mulig logikk
//        og mest mulig f.eks " fish.getFish().setRotate(fish.getAngle()); " der man skal sette 
//        vinkelen på fisken, mens selve utregningen av hva vinkelen skal være burde gjøres i en 
//        annen klasse f.eks Fish.java. 
//        Må ryddes...
//        Det er masse print greier rundt omkring, men det er bare for debugging, alt det skal fjernes 
//        til slutt.

//        Tastene:
//        Bevege båten AWSD
//        Kaste ut snøret: Hold R og så slipp
//        Trykk på kalkuler button for å spawne fiskene
//        Trykk på moveFiss button for å starte animasjonen

public class CanvasController implements Initializable {
    @FXML
    private TextField firstNumber, secondNumber, operator;

    @FXML
    private Label result;

    @FXML
    private Ellipse boat;

    @FXML
    ScrollPane background;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Text status;

    @FXML
    private ImageView image;

    @FXML
    private ImageView imageViewRod;

    @FXML
    Image boatImage = new Image(getClass().getResourceAsStream("boat2.png"));

    private FishMain main;

    private AnimationTimer timer2;

    private AnimationTimer timer3;

    private AnimationTimer timer4;

    ArrayList<Fish> fishes = new ArrayList<Fish>();

    private String boatDir = "right";

    private long startTime;

    private Ellipse dupp;

    int x1 = 0;
    private int y1 = 0;

    private void initMain(double mus_x, double mus_y) {
        main = new FishMain();
    }

    //    Denne metoden er et grensesnitt som er garantert til å kjøre først når denne klassen kalles på.
    //    Her initialiseres ulike ting som f.eks bildet og koordinatene til båten og fiskestangen.

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("gallaea");

        //  Båt
        image.setImage(boatImage);
        image.setY(10);
        
        //  Setter start verdi for hvor langt ned skjermen skal være scrollet
        background.setVvalue(0.8);

        //  Fiskestang
        imageViewRod = new ImageView();
        Image fishinRodImage = new Image(getClass().getResourceAsStream("fishingRod.png"));
        imageViewRod.setImage(fishinRodImage);
        anchorPane.getChildren().add(imageViewRod);
        imageViewRod.setPreserveRatio(true);
        imageViewRod.setFitHeight(75);
        imageViewRod.setFitWidth(75);
        imageViewRod.setY(410);
        imageViewRod.setX(435);
        System.out.println(System.nanoTime());

        dupp = new Ellipse(525, 20, 5, 5);

        //  En foreløpig ubrukt timer.
        //  En slik timer er forøvrig en type bakgrunnsprosess som gjør det mulig at flere ting skjer samtidig.
        timer3 = new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {   
                // status.setY(value);
            }
        };
        timer3.start();
    }

    //  Dette er en timer som startes idet duppen forlater fiskestangen. Duppen følger en slags krumlinjet
    //  bevegelse gitt av x = v*cos(alfa)*t og y = v*sin(alfa)*t - 1/2 * g * t^2
    //  Denne utregningen burde vel egentlig gjøres i en annen klasse f.eks FishMain.java
    //  De etterfølgende if-setningene er for å flytte skjermen mens duppen beveger seg. Når duppen 
    //  når et bestemt sluttpunkt stoppes timeren med timerDupp.stop()
    AnimationTimer timerDupp = new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {   
                if (dupp.getCenterY() < anchorPane.getHeight()-100){
                    double time = (currentNanoTime-startTime)*Math.pow(10, -9)*6;
                    //System.out.println((currentNanoTime-startTime)*Math.pow(10, -9));
                    // System.out.println(dupp.localToScene(dupp.getBoundsInLocal()).getMaxX());
                    dupp.setCenterX(image.getX()+545+(50 * Math.cos(Math.toRadians(30)) * time));
                    dupp.setCenterY(410+((50 * Math.sin(Math.toRadians(30)) * time) - (0.5 * 9.81 * Math.pow(time, 2)))*-1);
                    System.out.println(dupp.getCenterY());
                    if (dupp.localToScene(dupp.getBoundsInLocal()).getMaxX() > 500){
                        // background.setHvalue((dupp.getCenterX()-400)/1500);
                        background.setHvalue(background.getHvalue()+0.004);
                    }
                    if (dupp.localToScene(dupp.getBoundsInLocal()).getMaxY() < 80){
                        // background.setHvalue((dupp.getCenterX()-400)/1500);
                        background.setVvalue(background.getVvalue()-0.004);
                    }
                    if (dupp.localToScene(dupp.getBoundsInLocal()).getMaxY() > 400){
                        // background.setHvalue((dupp.getCenterX()-400)/1500);
                        background.setVvalue(background.getVvalue()+0.007);
                    }
                }
                else{
                    timerDupp.stop();
                    System.out.println("STOPP");
                }
            }
        };

    //  Dette er en timer for båten. Det er ikke strengt tatt nødvendig å legge båtens bevegelse i en
    //  timer, men jaja. Ettersom båten beveger seg må også både fiskestangen og skjermen bevege seg like
    //  mye.
    AnimationTimer timerBoat = new AnimationTimer()
    {
        public void handle(long currentNanoTime)
        {   
            if (boatDir == "right"){
                image.setX(image.getX()+5);
                imageViewRod.setX(imageViewRod.getX()+5);
                dupp.setCenterX(dupp.getCenterX()+5);
                if (image.localToScene(image.getBoundsInLocal()).getMaxX() > 500){
                    background.setHvalue(background.getHvalue()+0.005);
                    // background.setHvalue((image.getX())/1000);
                }
            }
            if (boatDir == "left"){
                image.setX(image.getX()-5);
                imageViewRod.setX(imageViewRod.getX()-5);
                if (image.localToScene(image.getBoundsInLocal()).getMinX() < 100){
                    // background.setHvalue((image.getX()+350)/1500);
                    background.setHvalue(background.getHvalue()-0.005);
                }
            }
        }
    };

    //  Dette er en timer for når duppen skal sveives inn igjen. Har ikke blitt laget enda. Må beregne
    //  bevegelsen avhenging av hvor båten er.
    AnimationTimer timerReelInn = new AnimationTimer()
    {
        public void handle(long currentNanoTime)
        {  
            //  BEREGN AVSTAND
            double boat_x = image.getX()+520;
            double boat_y = 410;
            dupp.setCenterX(dupp.getCenterX() - Math.abs((boat_x-dupp.getCenterX()))/100);
            dupp.setCenterY(dupp.getCenterY() - Math.abs((boat_y-dupp.getCenterY()))/100);
            System.out.println(image.getX()+520);
        }
    };

    //  Denne metoden håndterer hva som skjer når en tast på tastaturet blir trykket. 
    //  Jeg hadde opprinnelig lagt bevegelsen til båten direkte inni her, men valgte å flytte det
    //  til en egen AnimationTimer. Dermed er det veldig rotete her, fordi jeg ikke fjernet
    //  koden, jeg bare kommenterte den ut. 
    @FXML
    private void handleKeyPressed(KeyEvent keyEvent) {
        //  Ignorer de første tre linjene. Skulle teste noe. Det funket ikke.
        Stage stage = (Stage) background.getScene().getWindow();
        PerspectiveCamera camera = new PerspectiveCamera();
        stage.getScene().setCamera(camera);

        background.setOnKeyPressed((KeyEvent e) -> {
            System.out.println(image.localToScene(image.getBoundsInLocal()).getMinX());
            if (e.getCode() == KeyCode.D){
                
                // Flytter båt, fiskestang, dupp til høyre.

                System.out.println((image.getX()+500) + " " + background.getHvalue());
                boatDir = "right";
                timerBoat.start();

                // dupp.setCenterX(dupp.getCenterX()+10);
                // image.setX(image.getX()+1);
                image.setScaleX(1);
                
                imageViewRod.setX(image.getX()+450);
                // imageViewRod.setX(imageViewRod.getX()+10);
                imageViewRod.setScaleX(1);

                //  Flytter skjermen (alt lagt til i timerBoat istedet).

                // if (boat.getCenterX() > camera.getLayoutX())
                // if (image.localToScene(image.getBoundsInLocal()).getMaxX() > 500){
                //     background.setHvalue(background.getHvalue()+0.009);
                //     // background.setHvalue((image.getX())/1500);
                // }
                // // background.setHvalue((image.getX()+500)/1500);
                // camera.setLayoutX(boat.getCenterX());
            }
            if (e.getCode() == KeyCode.A){
                
                //  Flytter båt, fiskestang, dupp til venstre.

                System.out.println((image.getX()+250) + " " + background.getHvalue());
                boatDir = "left";
                timerBoat.start();

                // dupp.setCenterX(dupp.getCenterX()-10);
                // image.setX(image.getX()-10);
                image.setScaleX(-1);
                
                imageViewRod.setX(image.getX()+290);
                // imageViewRod.setX(imageViewRod.getX()-10);
                imageViewRod.setScaleX(-1);
                
                //  Flytter skjermen (alt lagt til i timerBoat istedet).

                // if (image.localToScene(image.getBoundsInLocal()).getMaxX() < 280){
                //     background.setHvalue(background.getHvalue()-0.01);
                // }
                // camera.setLayoutX(boat.getCenterX());
            }

            //  Når man trykker R roteres fiskestangen bakover.
            if (e.getCode() == KeyCode.R){
                if (boatDir == "left"){
                    imageViewRod.setRotate(imageViewRod.getRotate()+1);
                }
                else{
                    imageViewRod.setRotate(imageViewRod.getRotate()-1);
                }
            }

            //  Dette er en hjelpeknapp som printer ut ulike ting som kan brukes til debugging.
            if (e.getCode() == KeyCode.I){
                System.out.println(dupp.localToScene(dupp.getBoundsInLocal()).getMaxY());
                System.out.println(background.getHvalue());
                System.out.println(image.getX());
                System.out.println(anchorPane.getWidth());
                System.out.println(anchorPane.getHeight());
            }

            //  Man sveiver inn duppen med denne knappen. Har enda ikke noe virkning.
            if (e.getCode() == KeyCode.F){
                timerReelInn.start();
            }
            //System.out.println(234234);
        });

        //  Funker ikke / brukes ikke / ignorer.
        if (keyEvent.getCode() == KeyCode.RIGHT) {
            System.out.println(1212);
            boat.setLayoutX(boat.getCenterX()+100);
        }
        // anchorPane.setOnKeyPressed((KeyEvent e) -> {
        //     System.out.println("asd"+anchorPane.getChildren().));
        // });
    }

    //  Når man slipper tastene må de ulike animasjonene avsluttes. Dette skjer her.
    @FXML
    private void handleKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.R){
            anchorPane.getChildren().add(dupp);
            imageViewRod.setRotate(0);
            startTime = System.nanoTime();
            timerDupp.start();
        }
        if (keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.A){
            timerBoat.stop();
        }
        if (keyEvent.getCode() == KeyCode.F){
            timerReelInn.stop();
        }
    }

    //  Kalkuler button

    //  Første halvdel av metoden brukes ikke.
    //  andre halvdel fjerner først horisontal og vertikal scrollbar. Disse linjene burde egentlig
    //  flyttes til initialize metoden. For-løkka lager 10 nye instanser av fiskeklassen med posisjon
    //  og størrelse og sånt. Deretter legges de inn i en ArrayList fishes som skal itereres gjennom
    //  når alle skal bevege på seg. til slutt legges de til i anchorPane for at de i det hele tatt 
    //  skal vises på skjermen.
    @FXML
    private void handleButtonClick() {
        System.out.println(22);
        Stage stage = (Stage) background.getScene().getWindow();
        // PerspectiveCamera camera = new PerspectiveCamera();
        // stage.getScene().setCamera(camera);
        // camera.setLayoutX(1000);
        //stage.set

        System.out.println(background.getHvalue());
        background.setHbarPolicy(ScrollBarPolicy.NEVER);
        background.setVbarPolicy(ScrollBarPolicy.NEVER);
        background.setHvalue(0.01);
        for (int i = 0; i < 11; i++) {
            Fish fish2 = new Fish(100+i*2,170 + i*50, 30, 10);
            fishes.add(fish2);
            anchorPane.getChildren().add(fish2.getFish());   
        }
        // background.getScene().getRoot().getChildrenUnmodifiable().add(circle);
    }

    //  moveFiss button

    //  Starter animasjonen/bevegelsen til alle fiskene. Alt legges i en egen separat timer (med
    //  dårlig navn). En for-løkke itererer gjennom alle fiskene og beveger dem hver for seg. 
    //  Hvor neste posisjon til hver fisk er blir kalkulert i en separat klasse Fish.java.
    @FXML
    private void handleFissButton() {
        timer4 = new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {   
                for (Fish fish : fishes) {
                    // System.out.println("gammel X: "+fish.getPosX());
                    // System.out.println("gammel Y: "+fish.getPosY());
                    // System.out.println("X: "+fish.calculateNextX());
                    // System.out.println("Y: "+fish.calculateNextY());
                    fish.setPos(fish.getPosX()+fish.calculateNextX(), fish.getPosY()+fish.calculateNextY());
                    fish.getFish().setRotate(fish.getAngle());   
                    // System.out.println(fish.getAngle());
                    // System.out.println("Ny X: "+fish.getPosX());
                    // System.out.println("Ny Y: "+fish.getPosY());
                }
            }
        };
        timer4.start();
    }

    //  Hjelpefunksjon.
    //  Viser x og y posisjon til musa når man beveger den.
    @FXML
    private void displayPosition(MouseEvent e) {
        status.setText("X = "+Math.round(e.getX())+" Y = "+Math.round(e.getY()) + ", viewport: " + dupp.localToScene(dupp.getBoundsInLocal()).getMaxY());
        // background.setHvalue((e.getX())/1500);
    }

    //  Når man klikker i nedre høyre/venstre del av skjermen.
    //  Trenger strengt tatt heller ikke å være i en AnimationTimer.
    //  initMain lager en instans av FishMain.java. Her kalkuleres/bestemmes hvor skjermen skal flyttes
    //  når man trykker med musa. 
    @FXML
    private void moveScreen(MouseEvent e) {
        initMain(e.getX(), e.getY());
        timer2 = new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {   
                String dir = main.computeScreenDirection(e.getX(), e.getY());
                if (dir == "right"){
                    background.setHvalue(background.getHvalue()+0.007);
                }
                if (dir == "left"){
                    background.setHvalue(background.getHvalue()-0.007);
                }
            }
        };
        timer2.start();
    }

    @FXML
    private void stopScreen(MouseEvent e) {
        timer2.stop();
    }

    // @FXML
    // private void moveScreenWithBoat(KeyEvent KeyEvent) {
    //     if (e.getCode() == KeyCode.D){

    //     }
    // }

}
