package ProjectCanvas;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;


import static ProjectCanvas.Constants.*;


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

    @FXML
    ScrollPane background;

    @FXML
    HBox topBox;

    @FXML
    VBox highscorePane;

    @FXML
    Button displayFissButton;

    @FXML
    Button spawnFissButton;

    @FXML
    Button movfissButton;

    @FXML
    Button startButton;

    @FXML
    Button highscoreButton;

    @FXML
    Button saveButton;

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
    Image boatImage = new Image(getClass().getResourceAsStream("res/fishman.png"));

    private Image fishinRodImage = new Image(getClass().getResourceAsStream("res/fishingRod.png"));
    private FishMain main;

    ArrayList<Fish> fishes = new ArrayList<Fish>();

    ArrayList<Fish> caughtFishesList = new ArrayList<Fish>();

    private long startTime;
    private long startTime1;

    private Boat boat;

    private FishingRod fishingrod;

    private Dupp dupp;

    int x1 = 0;

    boolean duppMove = true;

    private Line line;

    private double dest_x;
    private double dest_y; 

    private Fish caughtFiss = null;

    private List<Double> hichscorelist = new ArrayList<Double>();

    AnchorPane displayFishPane;

    Stage stage;
    Scene primaryScene;

    FileHandlingInterface files = new FileHandling();

    FishMain timerMain = new FishMain();

    VariablesNshit variables = new VariablesNshit();

    private void initMain(Point2D mousePos) {
        main = new FishMain();
    }

    //    Denne metoden er et grensesnitt som er garantert til å kjøre først når denne klassen kalles på.
    //    Her initialiseres ulike ting som f.eks bildet og koordinatene til båten og fiskestangen.

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        try {
            variables.newHighscoreList(files.load("HighscoreList"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Highscore:" + variables.getHighscoreList());
        
        Image img = new Image(getClass().getResourceAsStream("res/bckgrnd.png"));
        BackgroundImage bImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background bGround = new Background(bImg);
        anchorPane.setBackground(bGround);

        topBox.setStyle("-fx-background-color: #4FD1EB;");
        displayFisses.setStyle("-fx-background-color: #4FD1EB;");
        displayFissButton.setStyle("-fx-background-color: #4FD1EB;-fx-border-color:black;");
        spawnFissButton.setStyle("-fx-background-color: #4FD1EB;-fx-border-color:black;");
        movfissButton.setStyle("-fx-background-color: #4FD1EB;-fx-border-color:black;");
        startButton.setStyle("-fx-background-color: #4FD1EB;-fx-border-color:black;"); //  SetDisable...
        highscoreButton.setStyle("-fx-background-color: #4FD1EB;-fx-border-color:black;");
        saveButton.setStyle("-fx-background-color: #4FD1EB;-fx-border-color:black;");
        highscorePane.setStyle("-fx-background-color: #FAE494;-fx-border-color:black;");

        highscorePane.setVisible(false);

        //  Båt
        this.boat = new Boat(new Point2D(0, -60), boatImage, image);

        this.fishingrod = new FishingRod(new Point2D(410, 435), fishinRodImage, imageViewRod);
        anchorPane.getChildren().add(imageViewRod);

        this.dupp = new Dupp(new Point2D(525, 20));
        
        line = new Line(fishingrod.getX()+35, fishingrod.getY()-25, dupp.getX(), dupp.getY());
        
        //  Setter start verdi for hvor langt ned skjermen skal være scrollet
        background.setVvalue(0.8);

        displayFishPane = new AnchorPane();
        displayFishPane.setLayoutX(displayFishPane.sceneToLocal(100, 100).getX());
        displayFishPane.setLayoutY(displayFishPane.sceneToLocal(100, 100).getY());

        background.setHbarPolicy(ScrollBarPolicy.NEVER);
        background.setVbarPolicy(ScrollBarPolicy.NEVER);

        System.out.println(System.nanoTime());
        

        //  En slik timer er forøvrig en type bakgrunnsprosess som gjør det mulig at flere ting skjer samtidig.
        timerMain.startFocusTimer(anchorPane, highscorePane);
        // System.out.println(t.test().getPoint());
        Fish fish = new Fish(null);
        fish.generateFish(10, variables, anchorPane);
        timerMain.startFishMoveTimer(anchorPane, dupp, fishingrod, variables);

        spawnFissButton.setDisable(true);
        movfissButton.setDisable(true);
        topBox.getChildren().remove(spawnFissButton);
        topBox.getChildren().remove(movfissButton);
    }

    //  DUPP TIMER BESKRIVELSE:
    //  Dette er en timer som startes idet duppen forlater fiskestangen. Duppen følger en slags krumlinjet
    //  bevegelse gitt av x = v*cos(alfa)*t og y = v*sin(alfa)*t - 1/2 * g * t^2
    //  Denne utregningen burde vel egentlig gjøres i en annen klasse f.eks FishMain.java
    //  De etterfølgende if-setningene er for å flytte skjermen mens duppen beveger seg. Når duppen 
    //  når et bestemt sluttpunkt stoppes timeren med timerDupp.stop()


    
    //  Denne metoden håndterer hva som skjer når en tast på tastaturet blir trykket. 
    //  Jeg hadde opprinnelig lagt bevegelsen til båten direkte inni her, men valgte å flytte det
    //  til en egen AnimationTimer. Dermed er det veldig rotete her, fordi jeg ikke fjernet
    //  koden, jeg bare kommenterte den ut. 
    @FXML
    private void handleKeyPressed(KeyEvent keyEvent) {
        background.setOnKeyPressed((KeyEvent e) -> {
            if (!dupp.getDuppUte()){
                switch (e.getCode()) {
                    case D:
                        boat.setDirection(true);;
                        fishingrod.moveRod(new Point2D(boat.getX()+440, fishingrod.getY()));
                        timerMain.startBoatTimer(dupp, boat, image, background, fishingrod, highscorePane);
                        break;
                    
                    case A:
                        boat.setDirection(false);
                        fishingrod.moveRod(new Point2D(boat.getX()+465, fishingrod.getY()));
                        timerMain.startBoatTimer(dupp, boat, image, background, fishingrod, highscorePane); 
                        break;

                    case R:
                        if (boat.getDirection()){
                            imageViewRod.setRotate(imageViewRod.getRotate()-1);
                        }
                        else{
                            imageViewRod.setRotate(imageViewRod.getRotate()+1);
                        }
                    default:
                        break;
                }
            }
 

            //  Dette er en hjelpeknapp som printer ut ulike ting som kan brukes til debugging.
            // else if (e.getCode() == KeyCode.I){
            //     // System.out.println(dupp.localToScene(dupp.getBoundsInLocal()).getMaxY());
            //     System.out.println("BoatX: "+boat.getX());
            //     System.out.println("RodX: "+fishingrod.getX());
            //     System.out.println(variables.getHighscoreList());
            //     System.out.println(duppMove + " " + dupp.getDuppMove());
            // }

            if (e.getCode() == KeyCode.O){
               try {
                    files.save("HighscoreList", variables.getHighscoreList());
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } 
            }
            
            //  Man sveiver inn duppen med denne knappen. 
            if (e.getCode() == REEL_IN){
                if (dupp.getDuppMove() == false){
                    timerMain.stopDuppTimer();
                    timerMain.startReelInnTimer(anchorPane, dupp, boat, line, fishingrod, dest_x, dest_y, variables);
                }
            }
        });
    }

    //  Når man slipper tastene må de ulike animasjonene avsluttes. Dette skjer her.
    @FXML
    private void handleKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == THROW){
            if (!dupp.getDuppUte()){
                anchorPane.getChildren().add(dupp.getEllipse());
                imageViewRod.setRotate(0);
                startTime = System.nanoTime();
                dupp.setDuppMove(true);
                timerMain.startDuppTimer(anchorPane, dupp, duppMove, startTime, boat, image, background, line, fishingrod, caughtFiss, variables);
                
                dupp.setDuppUte(true);

                anchorPane.getChildren().add(line);

                variables.setCaughtFiss(null);
            }
            
        }
        if (keyEvent.getCode() == RIGHT_KEY || keyEvent.getCode() == LEFT_KEY){
            timerMain.stopBoatTimer();
        }
        if (keyEvent.getCode() == REEL_IN){
            if (dupp.getDuppMove() == false){
                timerMain.startDuppTimer(anchorPane, dupp, duppMove, startTime, boat, image, background, line, fishingrod, caughtFiss, variables);
            }
            timerMain.stopReelInnTimer();
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
        // System.out.println(22);
        // Stage stage = (Stage) background.getScene().getWindow();

        // System.out.println(background.getHvalue());
        // background.setHbarPolicy(ScrollBarPolicy.NEVER);
        // background.setVbarPolicy(ScrollBarPolicy.NEVER);
        // background.setHvalue(0.01); 
        
    }


    //  moveFiss button

    //  Starter animasjonen/bevegelsen til alle fiskene. Alt legges i en egen separat timer (med
    //  dårlig navn). En for-løkke itererer gjennom alle fiskene og beveger dem hver for seg. 
    //  Hvor neste posisjon til hver fisk er blir kalkulert i en separat klasse Fish.java.
    @FXML
    private void handleFissButton() {
        timerMain.startFishMoveTimer(anchorPane, dupp, fishingrod, variables);
    }

    
    //  DISPLAY FISHES
    @FXML
    private void handleDisplayFissClick() {
        if (displayFisses.getChildren().isEmpty()){
            timerMain.startDisplayFissTimer(displayFisses, variables);
        }
        else{
            for (Fish fish : variables.getCaughtFishesList()) {
                displayFisses.getChildren().remove(fish.getFish());
            }
            timerMain.stopDisplayFissTimer();
        }
    }

    @FXML
    private void handleStartButton() {
        startTime1 = System.nanoTime();
        variables.setScore(0);
        for (Fish fish : variables.getCaughtFishesList()) {
            displayFisses.getChildren().remove(fish.getFish());
        }
        variables.newCaughtFishesList(new ArrayList<Fish>());
        
        timerMain.startTimerForTimer(startTime1, variables, timerText, scoreText);
    }

    @FXML
    private void handleHigscoreButton() {
        if (highscorePane.isVisible()){
            highscorePane.setVisible(false);
            Node title = highscorePane.getChildren().get(0);
            highscorePane.getChildren().clear();
            highscorePane.getChildren().add(title);
        }
        else{
            background.setHvalue(0.5);
            background.setVvalue(0.5);
            highscorePane.setVisible(true);
            for (Integer tall : variables.getHighscoreList()) {
                highscorePane.getChildren().add(new Text(variables.getHighscoreList().indexOf(tall)+1+": "+tall));
            }
        }
    }

    @FXML
    private void handleSaveButton() {
        try {
            files.save("HighscoreList", variables.getHighscoreList());
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } 
    }

    //  Når man klikker i nedre høyre/venstre del av skjermen.
    //  Trenger strengt tatt heller ikke å være i en AnimationTimer.
    //  initMain lager en instans av FishMain.java. Her kalkuleres/bestemmes hvor skjermen skal flyttes
    //  når man trykker med musa. 
    @FXML
    private void moveScreen(MouseEvent e) {
        initMain(new Point2D(e.getX(), e.getY()));
        timerMain.startMoveScreenTimer(main, e, background);
    }

    @FXML
    private void stopScreen(MouseEvent e) {
        timerMain.stopMoveScreenTimer();
        hichscorelist.clear();
    }

}
