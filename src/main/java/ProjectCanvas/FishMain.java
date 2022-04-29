package ProjectCanvas;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class FishMain {

    AnchorPane anchorPane;
    VBox highscorePane;
    AnchorPane helpPane;
    Dupp dupp;
    long startTime;
    Boat boat;
    ImageView image;
    ScrollPane background;
    Line line;
    FishingRod fishingrod;
    double dest_x;
    double dest_y;
    boolean duppUte;
    AnchorPane displayFisses;
    long startTime1;
    Text timerText;
    Text scoreText;
    FishMain main;
    MouseEvent e;
    Variables variables;

    public String computeScreenDirection(double mus_x, double mus_y) {
        String dir = "";
        if (mus_y > 270){
            if (mus_x > 280){
                dir = "right";
            }
            if (mus_x < 280){
                dir = "left";
            }
            
        }
        return dir;
    }
    
    
    //  -------------------------------------------------------- Focus Timer --------------------------------------------------------
    //  FOCUS TIMER BESKRIVELSE:
    //  Når man trykker på en av knappene i menyenså skiftes fokuset til den containeren.
    //  For å unngå å måtte klikke på skjermen når man skal trykke på tastaturet igjen, har vi
    //  en timer som kontinuerlig gir fokus til anchorPane som tar imot info fra tastaturet.
    //  I tillegg lar vi Higscore og help vinduene alltid ligge i fronten av lagene.
    AnimationTimer focusTimer = new AnimationTimer()
    {
        public void handle(long currentNanoTime)
        {   
            anchorPane.requestFocus();
            highscorePane.toFront();
            helpPane.toFront();
        }
    };
    public void startFocusTimer(AnchorPane anchorPane, VBox highscorePane, AnchorPane helpPane) {
        this.helpPane = helpPane;
        this.highscorePane = highscorePane;
        this.anchorPane = anchorPane;
        focusTimer.start();
    }
    public void stopFocusTimer() {
        focusTimer.stop();
    }

    //  -------------------------------------------------------- Dupp Timer --------------------------------------------------------
    //  DUPP TIMER BESKRIVELSE:
    //  Dette er en timer som startes idet duppen forlater fiskestangen. Duppen følger en slags krumlinjet
    //  bevegelse gitt av x = v*cos(alfa)*t og y = v*sin(alfa)*t - 1/2 * g * t^2
    //  De etterfølgende if-setningene er for å flytte skjermen mens duppen beveger seg. Når duppen 
    //  når et bestemt sluttpunkt stoppes timeren med timerDupp.stop()
    AnimationTimer timerDupp = new AnimationTimer()
    {
        public void handle(long currentNanoTime)
        {   
            if (dupp.getDuppMove()){
                if (dupp.getY() < anchorPane.getHeight()-100){
                    double time = (currentNanoTime-startTime)*Math.pow(10, -9)*6;
                    if (boat.getDirection()){
                        dupp.moveDupp(new Point2D(image.getX()+465+(50 * Math.cos(Math.toRadians(30)) * time), 390+((50 * Math.sin(Math.toRadians(30)) * time) - (0.5 * 9.81 * Math.pow(time, 2)))*-1));
                    }
                    else{
                        dupp.moveDupp(new Point2D(image.getX()+465+(50 * Math.cos(Math.toRadians(30)) * time * -1), 390+((50 * Math.sin(Math.toRadians(30)) * time) - (0.5 * 9.81 * Math.pow(time, 2)))*-1));
                    }
                    if (dupp.getEllipse().localToScene(dupp.getEllipse().getBoundsInLocal()).getMaxX() > 500){
                        background.setHvalue(background.getHvalue()+0.004);
                    }
                    if (dupp.getEllipse().localToScene(dupp.getEllipse().getBoundsInLocal()).getMaxX() < 100){
                        background.setHvalue(background.getHvalue()-0.004);
                    }
                    if (dupp.getEllipse().localToScene(dupp.getEllipse().getBoundsInLocal()).getMaxY() < 80){
                        background.setVvalue(background.getVvalue()-0.004);
                    }
                    if (dupp.getEllipse().localToScene(dupp.getEllipse().getBoundsInLocal()).getMaxY() > 400){
                        background.setVvalue(background.getVvalue()+0.007);
                    }
                }
                else{
                    System.out.println("STOPP");
                    dupp.setDuppMove(false);
                }
            }
            else{
                if (dupp.getY() < anchorPane.getHeight()-100){
                    dupp.moveDupp(new Point2D(dupp.getX(), dupp.getY()+1));
                }
            }
            if (boat.getDirection()){
                line.setStartX(fishingrod.getX()+80);
                line.setStartY(fishingrod.getY()-50);
            }
            else{
                line.setStartX(fishingrod.getX());
                line.setStartY(fishingrod.getY()-50);
            }
            line.setEndX(dupp.getX());
            line.setEndY(dupp.getY());

            if (variables.getCaughtFiss() != null){
                variables.getCaughtFiss().setPos(new Point2D(dupp.getX()-(variables.getCaughtFiss().getFish().getFitWidth()/2), dupp.getY()));
                variables.getCaughtFiss().getFish().toFront();
            }
        }
    };

    public void startDuppTimer(AnchorPane anchorPane , Dupp dupp, boolean duppMove, long startTime, Boat boat, ImageView image, ScrollPane background, Line line, FishingRod fishingrod, Fish caughtFiss, Variables variables) {
        this.anchorPane = anchorPane;
        this.dupp = dupp;
        this.startTime = startTime;
        this.boat = boat;
        this.image = image;
        this.background = background;
        this.line = line;
        this.fishingrod = fishingrod;
        this.variables = variables;
        timerDupp.start();
    }
    public void stopDuppTimer() {
        timerDupp.stop();
    }

    //  -------------------------------------------------------- Boat Timer --------------------------------------------------------
    //  BOAT TIMER BESKRIVELSE:
    //  Når man trykker A eller D startes denne timeren. Den flytter båt noden, og 
    //  andre noder som bør flyttes likt, litt av gangen mange ganger. Dette gjør
    //  at nodene får en mer naturlig og myk bevegelse.
    AnimationTimer timerBoat = new AnimationTimer()
    {
        public void handle(long currentNanoTime)
        {   
            if (fishingrod.getX() < 1500 && fishingrod.getX() > 0){
                if (boat.getDirection() == true){
                    boat.moveBoat(new Point2D(boat.getX()+5, boat.getY()));
                    fishingrod.moveRod(new Point2D(fishingrod.getX()+5, fishingrod.getY()));
                    dupp.moveDupp(new Point2D(dupp.getX()+5, dupp.getY()));

                    if (image.localToScene(image.getBoundsInLocal()).getMaxX() > 500){
                        background.setHvalue(background.getHvalue()+0.005);
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
        }
    };

    public void startBoatTimer(Dupp dupp, Boat boat, ImageView image, ScrollPane background, FishingRod fishingrod, VBox highscorePane) {
        this.dupp = dupp;
        this.boat = boat;
        this.image = image;
        this.background = background;
        this.fishingrod = fishingrod;
        this.highscorePane = highscorePane;
        timerBoat.start();
    }
    public void stopBoatTimer() {
        timerBoat.stop();
    }

    //  -------------------------------------------------------- Reel Inn Timer --------------------------------------------------------
    //  REEL INN TIMER BESKRIVELSE:
    //  Når man trykker F knappen startes denne timeren. Her beregnes avstanden fra duppen til fiskestangen
    //  og flytter duppen gradvis mot målet. Dersom en fisk er festet på kroken skal denne også
    //  flyttes samt at den skal roteres riktig vei.
    AnimationTimer timerReelInn = new AnimationTimer()
    {
        public void handle(long currentNanoTime)
        {  
            //  BEREGN AVSTAND
            if (boat.getDirection()){
                dest_x = fishingrod.getX()+80;
                dest_y = (fishingrod.getY()-25);
            }
            else{
                dest_x = fishingrod.getX();
                dest_y = (fishingrod.getY()-25);
            }
            dupp.moveDupp(new Point2D(dupp.getX() + (dest_x-dupp.getX())/100, dupp.getY() - Math.abs((dest_y-dupp.getY()))/100));

            if (variables.getCaughtFiss() != null){
                variables.getCaughtFiss().setPos(new Point2D(dupp.getX()-(variables.getCaughtFiss().getFish().getFitWidth()/2), dupp.getY()));
                variables.getCaughtFiss().getFish().toFront();
                double dx = 42;
                if (boat.getDirection()){
                    dx = Math.abs((fishingrod.getX()+80)-dupp.getX());
                }
                else{
                    dx = Math.abs((fishingrod.getX())-dupp.getX());
                }
                double dy = Math.abs(dupp.getY()-(fishingrod.getY()-25));
                
                double alpha = 90 - Math.abs(Math.toDegrees(Math.atan(dy/dx)));
                if (dupp.getX() < fishingrod.getX()+80){
                    alpha *= -1;
                }

                // System.out.println(String.format("Vinkel: %f", alpha));
                variables.getCaughtFiss().setAngle(-90-(alpha));
                variables.getCaughtFiss().getFish().setRotate(variables.getCaughtFiss().getAngle());

            }

            if ((Math.abs(dupp.getX()-dest_x) <= 50) && (Math.abs(dupp.getY()-dest_y) <= 50)){
                anchorPane.getChildren().remove(dupp.getEllipse());
                dupp.moveDupp(new Point2D(dest_x, dest_y));
                timerDupp.stop();

                dupp.setDuppMove(true);

                duppUte = false;
                dupp.setDuppUte(false);

                anchorPane.getChildren().remove(line);
                if (variables.getCaughtFiss() != null){
                    variables.setScore(variables.getScore() + variables.getCaughtFiss().getPoint());
                    variables.getCaughtFiss().setPos(new Point2D(10, 10));
                    variables.addToCaughtFishesList(variables.getCaughtFiss());
                    variables.setCaughtFiss(null);
                }
            }
            if (boat.getDirection()){
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

    public void startReelInnTimer(AnchorPane anchorPane , Dupp dupp, Boat boat, Line line, FishingRod fishingrod, double dest_x, double dest_y, Variables variables) {
        this.anchorPane = anchorPane;
        this.dupp = dupp;
        this.boat = boat;
        this.line = line;
        this.fishingrod = fishingrod;
        this.dest_x = dest_x;
        this.dest_y = dest_y;
        this.variables = variables;
        timerReelInn.start();
    }
    public void stopReelInnTimer() {
        timerReelInn.stop();
    }

    //  -------------------------------------------------------- Move Fish Timer --------------------------------------------------------
    //  MOVE FISH TIMER BESKRIVELSE:
    //  Her beveges hver enkelt fisk. Hvor den skal flyttes utregnes med fish.calculateNext(). 
    //  Dersom fisken i fokus er 100 pixler eller nærmere duppen skal den forsøke å 
    //  bevege seg mot duppen. Dersom fisken er 10 pixler eller nærmere duppen skal 
    //  den anses som fanget (caughtFish()).
    AnimationTimer timer4 = new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {   
                if (variables.getFishesList().size() < 15){
                    System.out.println("NEW FISH COMING");

                    Fish fish = new Fish(null);
                    fish.generateFish(1, variables, anchorPane);
                }
                for (Fish fish : variables.getFishesList()) {
                    if ((Math.abs(dupp.getX()-fish.getPosX()) <= 100) && (Math.abs(dupp.getY()-fish.getPosY()) <= 100) && dupp.getDuppMove() == false && variables.getCaughtFiss() == null && fish.getPosY() > 550){
                        double dx = Math.abs((fish.getPosX())-dupp.getX());
                        double dy = Math.abs(dupp.getY()-(fish.getPosY()));
                        
                        double alpha = 90 - Math.abs(Math.toDegrees(Math.atan(dy/dx)));

                        
                        if (dupp.getX() < fishingrod.getX()+80)
                        alpha *= -1;
                        
                        if (dupp.getY() > fish.getPosY()){
                            alpha += 180;
                        }
                        
                        // System.out.println(String.format("Vinkel: %f", alpha));
                        fish.setAngle(-90-(alpha));
                        fish.getFish().setRotate(fish.getAngle());
                        
                        fish.setPos(new Point2D(fish.getPosX() + Math.cos(Math.toRadians(-90-alpha))*2, fish.getPosY() + Math.sin(Math.toRadians(-90-alpha))*2));
                        
                    }
                    else{
                        fish.setPos(new Point2D(fish.getPosX()+fish.calculateNextX(), fish.getPosY()+fish.calculateNextY()));
                    }
                    // if (fish.getPosY() < 550)
                    // System.out.println(fish.getPosY());
                    fish.getFish().setRotate(fish.getAngle());   
                    if ((Math.abs(dupp.getX()-fish.getPosX()) <= 10) && (Math.abs(dupp.getY()-fish.getPosY()) <= 10) && dupp.getDuppMove() == false && variables.getCaughtFiss() == null){
                        System.out.println("FISK");
                        fish.setPos(new Point2D(dupp.getX(), dupp.getY()));
                        variables.getFishesList().remove(fish);
                        variables.setCaughtFiss(fish);
                        break;
                    }
                }
            }
        };

        public void startFishMoveTimer(AnchorPane anchorPane , Dupp dupp, FishingRod fishingrod, Variables variables) {
            this.anchorPane = anchorPane;
            this.dupp = dupp;
            this.fishingrod = fishingrod;
            this.variables = variables;
            timer4.start();
        }
        public void stopFishMoveTimer() {
            timer4.stop();
        }

        //  -------------------------------------------------------- Display Fish Timer --------------------------------------------------------
        //  DISPLAY FISH TIMER BESKRIVELSE:
        //  Denne timeren oppdaterer konstant hvilke fisker som vises i DisplayFish displayet. 
        //  Denne funksjonen er satt i en timer fordi den skal kunne oppdateres kontinuerlig. 
        //  Dette kunne blitt erstattet med en Observatør-Observert teknikk, men vi 
        //  ble så glad i AnimationTimer at det bare ble sånn.
        AnimationTimer timerDisplayFiss = new AnimationTimer()
            {
                public void handle(long currentNanoTime)
                { 
                    for (Fish fish : variables.getCaughtFishesList()) {
                        if (!displayFisses.getChildren().contains(fish.getFish())){
                            displayFisses.getChildren().add(fish.getFish());
                            fish.setPos(new Point2D(variables.getCaughtFishesList().indexOf(fish)*50, fish.getPosY()));
                        }
                    }
                }
            };
        
        public void startDisplayFissTimer(AnchorPane displayFisses, Variables variables) {
            this.displayFisses = displayFisses;
            this.variables = variables;
            timerDisplayFiss.start();
        }
        public void stopDisplayFissTimer() {
            timerDisplayFiss.stop();
        }

        //  -------------------------------------------------------- Timer For Timer --------------------------------------------------------
        //  TIMER FOR TIMER BESKRIVELSE:
        //  Her oppdateres timeren som viser hvor lang tid man har igjen på runden.
        //  Når tiden er omme sjekkes det om den oppnådde poengsummen er 
        //  god nok til å havne på Highscore lista.
        AnimationTimer timerforTimer = new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            { 
                double time = 60-(currentNanoTime-startTime1)*Math.pow(10, -9);
                timerText.setText("Time: "+ Math.round(time));
                scoreText.setText("| Score: "+variables.getScore());
                if (time < 0){
                    timerText.setText("FERDIG!!!");
                    for (int i = 0; i < variables.getHighscoreList().size()+1; i++) {
                        if (i == variables.getHighscoreList().size()){
                            if (variables.getHighscoreList().size() < 10){
                                variables.getHighscoreList().add(variables.getScore());
                                break;
                            }
                        }
                        else{
                            if (variables.getScore() > variables.getHighscoreList().get(i)){
                                variables.getHighscoreList().add(i, variables.getScore());
                                if (variables.getHighscoreList().size() > 10){
                                    variables.newHighscoreList(variables.getHighscoreList().subList(0, 10));
                                }
                                break;
                            }
                        }
                    }
                    this.stop();
                }
            }
        };

        public void startTimerForTimer(long startTime1, Variables variables, Text timerText, Text scoreText) {
            this.scoreText = scoreText;
            this.startTime1 = startTime1;
            this.timerText = timerText;
            this.variables = variables;
            timerforTimer.start();
        }
        public void stopTimerForTimer() {
            timerforTimer.stop();
        }

        //  -------------------------------------------------------- Move Screen Timer --------------------------------------------------------
        //  MOVE SCREEN TIMER BESKRIVELSE:
        //  Når man trykker på nedre høyre/venstre del av skjermen skal den flytte seg
        //  litt. Dette skjer her.
        AnimationTimer timer2 = new AnimationTimer()
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

        public void startMoveScreenTimer(FishMain main, MouseEvent e, ScrollPane background) {
            this.background = background;
            this.e = e;
            this.main = main;
            timer2.start();
        }
        public void stopMoveScreenTimer() {
            timer2.stop();
            
        }
    
}
