package ProjectCanvas;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
// import javafx.beans.binding.DoubleExpression;
import javafx.fxml.FXML;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
// import javafx.scene.canvas.Canvas;
// import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
// import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
// import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
// import javafx.scene.layout.Background;
// import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
// import javafx.scene.paint.Color;
// import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
// import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;

import static ProjectCanvas.Constants.RIGHT_KEY;
import static ProjectCanvas.Constants.LEFT_KEY;


//        Tastene:
//        Bevege båten AWSD
//        Kaste ut snøret: Hold R og så slipp
//        Trykk F for å sveive inn
//        Trykk på kalkuler button for å spawne fiskene
//        Trykk på moveFiss button for å starte animasjonen

public class CanvasController implements Initializable {
    @FXML
    private TextField firstNumber, secondNumber, operator;

    @FXML
    private Label result;

    // @FXML
    // private Ellipse bat;

    @FXML
    ScrollPane background;

    @FXML
    HBox topBox;

    @FXML
    Button displayFissButton;

    @FXML
    Button spawnFissButton;

    @FXML
    Button movfissButton;

    @FXML
    Button startButton;

    @FXML
    AnchorPane anchorPane;

    @FXML
    AnchorPane displayFisses;

    @FXML
    private Text status;

    @FXML
    private Text timerText;

    @FXML
    private Text scoreText;

    @FXML
    private ImageView image;

    @FXML
    private ImageView imageViewRod = new ImageView();

    @FXML
    Image boatImage = new Image(getClass().getResourceAsStream("fishman.png"));

    private Image fishinRodImage = new Image(getClass().getResourceAsStream("fishingRod.png"));
    private FishMain main;
    private AnimationTimer timer2;

    private AnimationTimer focusTimer;

    private AnimationTimer timer4;

    // AnimationTimer timerDisplayFiss;

    ArrayList<Fish> fishes = new ArrayList<Fish>();

    ArrayList<Fish> caughtFishesList = new ArrayList<Fish>();

    private String boatDir = "right";

    private long startTime;
    private long startTime1;

    private Ellipse duppen;

    private Boat boat;

    private FishingRod fishingrod;

    private Dupp dupp;

    int x1 = 0;
    private int y1 = 0;

    private boolean duppMove = true;
    private boolean duppUte = false;

    private Line line;

    private double dest_x;
    private double dest_y; 

    private Fish caughtFiss = null;

    private int score;
    private List<Integer> hichscorelist = new ArrayList<Integer>();

    AnchorPane displayFishPane;

    Stage stage;
    Scene primaryScene;

    FileHandlingInterface files = new FileHandling();

    private void initMain(Point2D mousePos) {
        main = new FishMain();
    }

    //    Denne metoden er et grensesnitt som er garantert til å kjøre først når denne klassen kalles på.
    //    Her initialiseres ulike ting som f.eks bildet og koordinatene til båten og fiskestangen.

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        try {
            hichscorelist =  files.load("HighscoreList");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Highscore:" + hichscorelist);

        Image img = new Image(getClass().getResourceAsStream("bckgrnd.png"));
        BackgroundImage bImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background bGround = new Background(bImg);
        anchorPane.setBackground(bGround);

        topBox.setStyle("-fx-background-color: #4FD1EB;");
        displayFisses.setStyle("-fx-background-color: #4FD1EB;");
        displayFissButton.setStyle("-fx-background-color: #4FD1EB;-fx-border-color:black;");
        spawnFissButton.setStyle("-fx-background-color: #4FD1EB;-fx-border-color:black;");
        movfissButton.setStyle("-fx-background-color: #4FD1EB;-fx-border-color:black;");
        startButton.setStyle("-fx-background-color: #4FD1EB;-fx-border-color:black;"); //  SetDisable...

        //  Båt
        this.boat = new Boat(new Point2D(0, 10), boatImage, image);

        this.fishingrod = new FishingRod(new Point2D(410, 435), fishinRodImage, imageViewRod);
        anchorPane.getChildren().add(imageViewRod);

        this.dupp = new Dupp(new Point2D(525, 20));
        
        // dupp = new Ellipse(525, 20, 5, 5);

        line = new Line(fishingrod.getX()+35, fishingrod.getY()-25, dupp.getX(), dupp.getY());
        
        // KOMMENTERT UT FOR TESTING
        // image.setImage(boatImage);
        // image.setY(10);
        // 
        
        //  Setter start verdi for hvor langt ned skjermen skal være scrollet
        background.setVvalue(0.8);

        displayFishPane = new AnchorPane();
        displayFishPane.setLayoutX(displayFishPane.sceneToLocal(100, 100).getX());
        displayFishPane.setLayoutY(displayFishPane.sceneToLocal(100, 100).getY());

        background.setHbarPolicy(ScrollBarPolicy.NEVER);
        background.setVbarPolicy(ScrollBarPolicy.NEVER);

        // stage = (Stage) anchorPane.getScene().getWindow();
        // primaryScene = stage.getScene();

        //  Fiskestang
        // imageViewRod = 
        
        // imageViewRod.setImage(fishinRodImage);
        // anchorPane.getChildren().add(imageViewRod);
        // imageViewRod.setPreserveRatio(true);
        // imageViewRod.setFitHeight(75);
        // imageViewRod.setFitWidth(75);
        // imageViewRod.setY(410);
        // imageViewRod.setX(435);
        System.out.println(System.nanoTime());


        //  En foreløpig ubrukt timer.
        //  En slik timer er forøvrig en type bakgrunnsprosess som gjør det mulig at flere ting skjer samtidig.
        focusTimer = new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {   
                anchorPane.requestFocus();
            }
        };
        focusTimer.start();
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
                if (duppMove){
                    if (dupp.getY() < anchorPane.getHeight()-100){
                        double time = (currentNanoTime-startTime)*Math.pow(10, -9)*6;
                        //System.out.println((currentNanoTime-startTime)*Math.pow(10, -9));
                        // System.out.println(dupp.localToScene(dupp.getBoundsInLocal()).getMaxX());
                        if (boat.direction){
                            dupp.moveDupp(new Point2D(image.getX()+465+(50 * Math.cos(Math.toRadians(30)) * time), 390+((50 * Math.sin(Math.toRadians(30)) * time) - (0.5 * 9.81 * Math.pow(time, 2)))*-1));
                        }
                        else{
                            dupp.moveDupp(new Point2D(image.getX()+465+(50 * Math.cos(Math.toRadians(30)) * time * -1), 390+((50 * Math.sin(Math.toRadians(30)) * time) - (0.5 * 9.81 * Math.pow(time, 2)))*-1));
                        }
                        // dupp.setCenterX(image.getX()+545+(50 * Math.cos(Math.toRadians(30)) * time));
                        // dupp.setCenterY(410+((50 * Math.sin(Math.toRadians(30)) * time) - (0.5 * 9.81 * Math.pow(time, 2)))*-1);
                        // System.out.println(dupp.getCenterY());
                        if (dupp.getEllipse().localToScene(dupp.getEllipse().getBoundsInLocal()).getMaxX() > 500){
                            // background.setHvalue((dupp.getCenterX()-400)/1500);
                            background.setHvalue(background.getHvalue()+0.004);
                        }
                        if (dupp.getEllipse().localToScene(dupp.getEllipse().getBoundsInLocal()).getMaxX() < 100){
                            // background.setHvalue((dupp.getCenterX()-400)/1500);
                            background.setHvalue(background.getHvalue()-0.004);
                        }
                        if (dupp.getEllipse().localToScene(dupp.getEllipse().getBoundsInLocal()).getMaxY() < 80){
                            // background.setHvalue((dupp.getCenterX()-400)/1500);
                            background.setVvalue(background.getVvalue()-0.004);
                        }
                        if (dupp.getEllipse().localToScene(dupp.getEllipse().getBoundsInLocal()).getMaxY() > 400){
                            // background.setHvalue((dupp.getCenterX()-400)/1500);
                            background.setVvalue(background.getVvalue()+0.007);
                        }
                    }
                    else{
                        // timerDupp.stop();
                        System.out.println("STOPP");
                        duppMove = false;
                    }
                }
                else{
                    if (dupp.getY() < anchorPane.getHeight()-100){
                        dupp.moveDupp(new Point2D(dupp.getX(), dupp.getY()+1));
                    }
                }
                if (boat.direction){
                    line.setStartX(fishingrod.getX()+80);
                    line.setStartY(fishingrod.getY()-50);
                }
                else{
                    line.setStartX(fishingrod.getX());
                    line.setStartY(fishingrod.getY()-50);
                }
                line.setEndX(dupp.getX());
                line.setEndY(dupp.getY());

                if (caughtFiss != null){
                    caughtFiss.setPos(new Point2D(dupp.getX(), dupp.getY()));
                }
            }
        };

    //  Dette er en timer for båten. Det er ikke strengt tatt nødvendig å legge båtens bevegelse i en
    //  timer, men jaja. Ettersom båten beveger seg må også både fiskestangen og skjermen bevege seg like
    //  mye.

    // Kommentert ut for å teste ting. Du kan uncommente det når du koder.
    
    // AnimationTimer timerBoat = new AnimationTimer()
    // {
    //     public void handle(long currentNanoTime)
    //     {   
    //         if (boatDir == "right"){
    //             image.setX(image.getX()+5);
    //             imageViewRod.setX(imageViewRod.getX()+5);
    //             dupp.setCenterX(dupp.getCenterX()+5);
    //             if (image.localToScene(image.getBoundsInLocal()).getMaxX() > 500){
    //                 background.setHvalue(background.getHvalue()+0.005);
    //                 // background.setHvalue((image.getX())/1000);
    //             }
    //         }
    //         if (boatDir == "left"){
    //             image.setX(image.getX()-5);
    //             imageViewRod.setX(imageViewRod.getX()-5);
    //             if (image.localToScene(image.getBoundsInLocal()).getMinX() < 100){
    //                 // background.setHvalue((image.getX()+350)/1500);
    //                 background.setHvalue(background.getHvalue()-0.005);
    //             }
    //         }
    //     }
    // };

    AnimationTimer timerBoat = new AnimationTimer()
    {
        public void handle(long currentNanoTime)
        {   
            if (fishingrod.getX() < 1500 && fishingrod.getX() > 0){
                if (boat.direction == true){
                    boat.moveBoat(new Point2D(boat.getX()+5, boat.getY()));
                    fishingrod.moveRod(new Point2D(fishingrod.getX()+5, fishingrod.getY()));
                    dupp.moveDupp(new Point2D(dupp.getX()+5, dupp.getY()));
                    // imageViewRod.setX(imageViewRod.getX()+5);
                    // dupp.setCenterX(dupp.getCenterX()+5);
                    if (image.localToScene(image.getBoundsInLocal()).getMaxX() > 500){
                        background.setHvalue(background.getHvalue()+0.005);
                        // background.setHvalue((image.getX())/1000);
                    }
                }
                else {
                    boat.moveBoat(new Point2D(boat.getX()-5, boat.getY()));
                    fishingrod.moveRod(new Point2D(fishingrod.getX()-5, fishingrod.getY()));
                    dupp.moveDupp(new Point2D(dupp.getX()-5, dupp.getY()));
                    if (image.localToScene(image.getBoundsInLocal()).getMinX() < 100){
                        background.setHvalue(background.getHvalue()-0.005);
                    }
                }    
            }
            

            if (fishingrod.getX() >= 1500){
                boat.moveBoat(new Point2D(boat.getX()-15, boat.getY()));
                fishingrod.moveRod(new Point2D(fishingrod.getX()-15, fishingrod.getY()));
                dupp.moveDupp(new Point2D(dupp.getX()-15, dupp.getY()));
            }
            else if (fishingrod.getX() <= 0){
                boat.moveBoat(new Point2D(boat.getX()+15, boat.getY()));
                fishingrod.moveRod(new Point2D(fishingrod.getX()+15, fishingrod.getY()));
                dupp.moveDupp(new Point2D(dupp.getX()+15, dupp.getY()));
            }
            // if (boatDir == "left"){
            //     image.setX(image.getX()-5);
            //     imageViewRod.setX(imageViewRod.getX()-5);
            //     if (image.localToScene(image.getBoundsInLocal()).getMinX() < 100){
            //         // background.setHvalue((image.getX()+350)/1500);
            //         background.setHvalue(background.getHvalue()-0.005);
            //     }
            // }
        }
    };

    //  Dette er en timer for når duppen skal sveives inn igjen. Har ikke blitt laget enda. Må beregne
    //  bevegelsen avhenging av hvor båten er.
    AnimationTimer timerReelInn = new AnimationTimer()
    {
        public void handle(long currentNanoTime)
        {  
            //  BEREGN AVSTAND
            if (boat.direction){
                dest_x = fishingrod.getX()+80;
                dest_y = (fishingrod.getY()-25);
            }
            else{
                dest_x = fishingrod.getX();
                dest_y = (fishingrod.getY()-25);
            }
            dupp.moveDupp(new Point2D(dupp.getX() + (dest_x-dupp.getX())/100, dupp.getY() - Math.abs((dest_y-dupp.getY()))/100));
            // dupp.setCenterX(dupp.getCenterX() + (boat_x-dupp.getCenterX())/100);
            // dupp.setCenterY(dupp.getCenterY() - Math.abs((boat_y-dupp.getCenterY()))/100);

            if (caughtFiss != null){
                caughtFiss.setPos(new Point2D(dupp.getX(), dupp.getY()));
            }

            if ((Math.abs(dupp.getX()-dest_x) <= 50) && (Math.abs(dupp.getY()-dest_y) <= 50)){
                anchorPane.getChildren().remove(dupp.getEllipse());
                dupp.moveDupp(new Point2D(dest_x, dest_y));
                timerDupp.stop();
                duppMove = true;
                duppUte = false;
                anchorPane.getChildren().remove(line);
                if (caughtFiss != null){
                    score += caughtFiss.getPoint();
                    caughtFiss.setPos(new Point2D(10, 10));
                    caughtFishesList.add(caughtFiss);
                    // displayFisses.getChildren().add(caughtFiss.getFish());
                    caughtFiss = null;
                }
                
                // dupp.setCenterX(boat_x);
                // dupp.setCenterY(boat_y);
                // anchorPane.getChildren().add(dupp.getEllipse());
            }
            if (boat.direction){
                line.setStartX(fishingrod.getX()+80);
                line.setStartY(fishingrod.getY()-50);
            }
            else{
                line.setStartX(fishingrod.getX());
                line.setStartY(fishingrod.getY()-50);
            }
            line.setEndX(dupp.getX());
            line.setEndY(dupp.getY());
        }
    };

    //  Denne metoden håndterer hva som skjer når en tast på tastaturet blir trykket. 
    //  Jeg hadde opprinnelig lagt bevegelsen til båten direkte inni her, men valgte å flytte det
    //  til en egen AnimationTimer. Dermed er det veldig rotete her, fordi jeg ikke fjernet
    //  koden, jeg bare kommenterte den ut. 
    @FXML
    private void handleKeyPressed(KeyEvent keyEvent) {
        //  Ignorer de første tre linjene. Skulle teste noe. Det funket ikke.
        // Stage stage = (Stage) background.getScene().getWindow();
        // PerspectiveCamera camera = new PerspectiveCamera();
        // stage.getScene().setCamera(camera);

        background.setOnKeyPressed((KeyEvent e) -> {
            // System.out.println(image.localToScene(image.getBoundsInLocal()).getMinX());
            if (e.getCode() == RIGHT_KEY){
                // this.boat.moveBoat(new Point2D(this.boat.getX()+5, this.boat.getY()));
                // Flytter båt, fiskestang, dupp til høyre.
                
                // KOMMENTERT UT FOR TESTING
                // System.out.println((image.getX()+500) + " " + background.getHvalue());
                // boatDir = "right";
                if (!duppUte){

                    boat.direction = true;
                    // boat.image.setScaleX(1);
                    fishingrod.moveRod(new Point2D(boat.getX()+440, fishingrod.getY()));
                    timerBoat.start(); 
                }
                
                // image.setScaleX(1);
                // imageViewRod.setX(image.getX()+450);
                // imageViewRod.setScaleX(1);
                //


                // dupp.setCenterX(dupp.getCenterX()+10);
                // image.setX(image.getX()+1);
              
                
                // imageViewRod.setX(imageViewRod.getX()+10);

                //  Flytter skjermen (alt lagt til i timerBoat istedet).

                // if (boat.getCenterX() > camera.getLayoutX())
                // if (image.localToScene(image.getBoundsInLocal()).getMaxX() > 500){
                //     background.setHvalue(background.getHvalue()+0.009);
                //     // background.setHvalue((image.getX())/1500);
                // }
                // // background.setHvalue((image.getX()+500)/1500);
                // camera.setLayoutX(boat.getCenterX());
            }
            else if (e.getCode() == LEFT_KEY){
                if (!duppUte){
                    boat.direction = false;
                    // boat.image.setScaleX(-1);
                    fishingrod.moveRod(new Point2D(boat.getX()+465, fishingrod.getY()));
                    timerBoat.start();
                }
                
                //  Flytter båt, fiskestang, dupp til venstre.
                // KOMMENTERT UT FOR TESTING
                // System.out.println((image.getX()+250) + " " + background.getHvalue());
                // boatDir = "left";
                // timerBoat.start();
                // image.setScaleX(-1);
                // imageViewRod.setX(image.getX()+290);
                // imageViewRod.setScaleX(-1);
                //



                // dupp.setCenterX(dupp.getCenterX()-10);
                // image.setX(image.getX()-10);
                
                // imageViewRod.setX(imageViewRod.getX()-10);
                
                //  Flytter skjermen (alt lagt til i timerBoat istedet).

                // if (image.localToScene(image.getBoundsInLocal()).getMaxX() < 280){
                //     background.setHvalue(background.getHvalue()-0.01);
                // }
                // camera.setLayoutX(boat.getCenterX());
            }
            //  Når man trykker R roteres fiskestangen bakover.
            if (e.getCode() == KeyCode.R){
                if (!duppUte){
                    if (boat.direction){
                        imageViewRod.setRotate(imageViewRod.getRotate()-1);
                    }
                    else{
                        imageViewRod.setRotate(imageViewRod.getRotate()+1);
                    }
                }
                
            }

            //  Dette er en hjelpeknapp som printer ut ulike ting som kan brukes til debugging.
            if (e.getCode() == KeyCode.I){
                // System.out.println(dupp.localToScene(dupp.getBoundsInLocal()).getMaxY());
                System.out.println("BoatX: "+boat.getX());
                System.out.println("RodX: "+fishingrod.getX());
                System.out.println(hichscorelist);
            }

            if (e.getCode() == KeyCode.O){
               try {
                    files.save("HighscoreList", hichscorelist);
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } 
            }
            
            //  Man sveiver inn duppen med denne knappen. 
            if (e.getCode() == KeyCode.F){
                if (duppMove == false){
                    timerDupp.stop();
                    timerReelInn.start(); 
                }
            }
            //System.out.println(234234);
        });

        //  Funker ikke / brukes ikke / ignorer.
        // if (keyEvent.getCode() == KeyCode.RIGHT) {
        //     System.out.println(1212);
        //     // boat.setLayoutX(boat.getCenterX()+100);
        // }
        // anchorPane.setOnKeyPressed((KeyEvent e) -> {
        //     System.out.println("asd"+anchorPane.getChildren().));
        // });
    }

    //  Når man slipper tastene må de ulike animasjonene avsluttes. Dette skjer her.
    @FXML
    private void handleKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.R){
            if (!duppUte){
                anchorPane.getChildren().add(dupp.getEllipse());
                imageViewRod.setRotate(0);
                startTime = System.nanoTime();
                duppMove = true;
                timerDupp.start();
                duppUte = true;
                // line = new Line(fishingrod.getX()+35, fishingrod.getY()-25, dupp.getX(), dupp.getY());
                anchorPane.getChildren().add(line);

                Fish caughtFiss = null;
            }
            
        }
        if (keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.A){
            timerBoat.stop();
        }
        if (keyEvent.getCode() == KeyCode.F){
            if (duppMove == false){
                timerDupp.start();
            }
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
        for (int i = 0; i < 10; i++) {

            Fish fish2 = new Fish(new Point2D(100+i*2,510 + i*50), new Point2D(30, 10));
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
                if (fishes.size() < 10){
                    Fish fish2 = new Fish(new Point2D(100, 700), new Point2D(30, 10));
                    fishes.add(fish2);
                    anchorPane.getChildren().add(fish2.getFish()); 
                }
                for (Fish fish : fishes) {
                    // System.out.println("gammel X: "+fish.getPosX());
                    // System.out.println("gammel Y: "+fish.getPosY());
                    // System.out.println("X: "+fish.calculateNextX());
                    // System.out.println("Y: "+fish.calculateNextY());
                    if ((Math.abs(dupp.getX()-fish.getPosX()) <= 50) && (Math.abs(dupp.getY()-fish.getPosY()) <= 50) && duppMove == false && caughtFiss == null && fish.getPosY() > 550){
                        fish.setPos(new Point2D(fish.getPosX() + (dupp.getX()-fish.getPosX())/50, fish.getPosY() - Math.abs((dupp.getY()-fish.getPosY()))/50));

                        //  MÅ ENDRES MELLOM HER ...

                        if (dupp.getY() < fish.getPosY()){
                            // fish.setAngle(180 + Math.toDegrees(Math.atan( (Math.abs(dupp.getY()-fish.getPosY()))/ (Math.abs(dupp.getX()-fish.getPosX())) )));
                            double dx = Math.abs((fishingrod.getX()+80)-dupp.getX());
                            double dy = Math.abs(dupp.getY()-(fishingrod.getY()-25));
                            
                            double alpha = 90 - Math.abs(Math.toDegrees(Math.atan(dy/dx)));
                            if (dupp.getX() < fishingrod.getX()+80)
                                alpha *= -1;

                            System.out.println(String.format("Vinkel: %f", alpha));
                            fish.setAngle(-90-(alpha));
                        }
                        else{
                            // fish.setAngle(Math.toDegreefs(Math.atan( (Math.abs(dupp.getY()-fish.getPosY()))/ (Math.abs(dupp.getX()-fish.getPosX())) )));
                            double dx = Math.abs((fishingrod.getX()+80)-dupp.getX());
                            double dy = Math.abs(dupp.getY()-(fishingrod.getY()-25));
                            
                            double alpha = 90 - Math.abs(Math.toDegrees(Math.atan(dy/dx)));
                            if (dupp.getX() < fishingrod.getX()+80)
                                alpha *= -1;
                                
                            System.out.println(String.format("Vinkel: %f", alpha));
                            fish.setAngle(-90-(alpha));
                        }
                        fish.setPos(new Point2D(dupp.getX(), dupp.getY()));

                        //  ... OG HER
                    }
                    else{
                        fish.setPos(new Point2D(fish.getPosX()+fish.calculateNextX(), fish.getPosY()+fish.calculateNextY()));
                    }
                    fish.getFish().setRotate(fish.getAngle());   
                    // System.out.println(fish.getAngle());
                    // System.out.println("Ny X: "+fish.getPosX());
                    // System.out.println("Ny Y: "+fish.getPosY());
                    if ((Math.abs(dupp.getX()-fish.getPosX()) <= 10) && (Math.abs(dupp.getY()-fish.getPosY()) <= 10) && duppMove == false && caughtFiss == null){
                        System.out.println("FISK");
                        fishes.remove(fish);
                        caughtFiss = fish;
                        break;
                    }
                }
            }
        };
        timer4.start();
    }

    AnimationTimer timerDisplayFiss = new AnimationTimer()
            {
                public void handle(long currentNanoTime)
                { 
                    for (Fish fish : caughtFishesList) {
                        if (!displayFisses.getChildren().contains(fish.getFish())){
                            displayFisses.getChildren().add(fish.getFish());
                            fish.setPos(new Point2D(caughtFishesList.indexOf(fish)*50, fish.getPosY()));
                        }
                    }
                }
            };

    //  DISPLAY FISHES
    @FXML
    private void handleDisplayFissClick() {
        //  ÅPNE NYTT VINDU
        // Stage newStageWindow = new Stage();
        // StackPane newLayout = new StackPane();
        // Scene newScene = new Scene(newLayout, 230, 100);
        // newStageWindow.setScene(newScene);
        // newStageWindow.show();

        // Scene s = new Scene(new StackPane());
        // anchorPane.getChildren().add(displayFishPane);
        // stage = (Stage) anchorPane.getScene().getWindow();
        // primaryScene = stage.getScene();
        // stage.setScene(new Scene(displayFishPane));
        // displayFishPane.getChildren().add(new Button());
        // if (displayFisses.getChildren().isEmpty()){
        //     for (Fish fish : caughtFishesList) {
        //         displayFisses.getChildren().add(fish.getFish());
        //         fish.setPos(new Point2D(caughtFishesList.indexOf(fish)*50, fish.getPosY()));
        //     }
        // }
        // else{
        //     for (Fish fish : caughtFishesList) {
        //         displayFisses.getChildren().remove(fish.getFish());
        //     }
        // }
        // displayFisses.setVisible(false);
        if (displayFisses.getChildren().isEmpty()){
            timerDisplayFiss.start();
        }
        else{
            for (Fish fish : caughtFishesList) {
                displayFisses.getChildren().remove(fish.getFish());
            }
            timerDisplayFiss.stop();
        }
    }

    @FXML
    private void handleStartButton() {
        startTime1 = System.nanoTime();
        score = 0;
        for (Fish fish : caughtFishesList) {
            displayFisses.getChildren().remove(fish.getFish());
        }
        caughtFishesList = new ArrayList<Fish>();
        AnimationTimer timerforTimer = new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            { 
                double time = 60-(currentNanoTime-startTime1)*Math.pow(10, -9);
                timerText.setText("Time: "+ Math.round(time));
                scoreText.setText("| Score: "+score);
                if (time < 0){
                    timerText.setText("FERDIG!!!");
                    for (int i = 0; i < hichscorelist.size()+1; i++) {
                        if (i == hichscorelist.size()){
                            if (hichscorelist.size() < 10){
                                hichscorelist.add(score);
                                break;
                            }
                        }
                        else{
                            if (score > hichscorelist.get(i)){
                                hichscorelist.add(i, score);
                                if (hichscorelist.size() > 10){
                                    hichscorelist = hichscorelist.subList(0, 10);
                                }
                                break;
                            }
                        }
                    }
                    this.stop();
                }
            }
        };
        timerforTimer.start();
    }

    //  Hjelpefunksjon.
    //  Viser x og y posisjon til musa når man beveger den.
    @FXML
    private void displayPosition(MouseEvent e) {
        // status.setText("  -  X = "+Math.round(e.getX())+" Y = "+Math.round(e.getY()) + ", viewport: " + dupp.getEllipse().localToScene(dupp.getEllipse().getBoundsInLocal()).getMaxY());
        // background.setHvalue((e.getX())/1500);
    }

    //  Når man klikker i nedre høyre/venstre del av skjermen.
    //  Trenger strengt tatt heller ikke å være i en AnimationTimer.
    //  initMain lager en instans av FishMain.java. Her kalkuleres/bestemmes hvor skjermen skal flyttes
    //  når man trykker med musa. 
    @FXML
    private void moveScreen(MouseEvent e) {
        initMain(new Point2D(e.getX(), e.getY()));
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
