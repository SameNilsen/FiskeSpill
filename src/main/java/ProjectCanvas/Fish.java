package ProjectCanvas;


import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;



public class Fish {

    //  I denne klassen lages fiskene. I første utkast hadde jeg Ellipses istedet for Image som
    //  fiskene (for å teste ut funksjonalitet). 
    //  Denne linjen i konstruktøren:
    //     this.fish = new Ellipse(pos_x, pos_y, size_x, size_y);
    //  var da den opprinnelige "fisken", før den ble byttet ut med Image.

    //  Forøvrig, ImageView er bilderammen mens Image er selve bildet. Bilde/Image må legges inni 
    //  bilderammen/ImageView. Deretter er det bilderammen/ImageView som blir flyttet på og som får
    //  en vinkel osv. 

    //  Selve bevegelsen til en fisk er veldig primitiv inntil videre. Den får utdelt en vinkel når
    //  den opprettes, og så følger den en lineær bane ut ifra den vinkelen. Senere bær vi legge på 
    //  individuelle hastigheter, bilder til fiskene, bevegelser osv.

    private double endring_x;
    private double endring_y;
    private ImageView imageView = new ImageView();
    private long startTime;
    private double angle;
    private double speed;
    private Point2D pos;
    private boolean dir;
    Random rand = new Random();

    public Fish(Point2D pos) {
        
        this.pos = pos;
        this.angle = rand.nextInt(0, 360);
        if ((0 < angle && angle < 90) || (270 < angle && angle < 369)){
            this.setDir(true);
        }
        else{
            this.setDir(false);
        }
        this.speed = rand.nextInt(1, 4);
    }

    public void setImageView(Image img) {
        this.imageView.setImage(img);
        this.imageView.setPreserveRatio(true);
        imageView.setFitHeight(rand.nextInt(30, 60));
        imageView.setFitWidth(rand.nextInt(30, 80));
        this.imageView.setY(this.pos.getY());
        this.imageView.setX(this.pos.getX());
    }

    public ImageView getFish() {
        // return this.fish;
        return imageView;
    }
    public boolean getDir() {
        return this.dir;
    }
    public void setDir(boolean dir){
        this.dir = dir;
    }

    public double getSpeed() {
        return speed;
    }

    public double getSize() {
        return 1 + (this.getFish().getFitWidth() + this.getFish().getFitHeight())*0.01;
    }

    public double getPoint() {
        return 0;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        int randomChange = rand.nextInt(1000);
        if (randomChange < 5){
            int randomDir = rand.nextInt(-1, 2);
            startTime = System.nanoTime();
            AnimationTimer turnTimer = new AnimationTimer() {

                @Override
                public void handle(long currentNanoTime) {
                    double time = (currentNanoTime-startTime)*Math.pow(10, -9);
                    if (time > 1){
                        this.stop();
                    }
                    else{
                        angle += randomDir;
                        imageView.setRotate(angle);
                    }
                }
            };
            turnTimer.start();
        }

        return angle;
    }

    public double getPosX() {
        return imageView.getX();
    }

    public double getPosY() {
        return imageView.getY();
    }

    public double calculateNextX() {
        if (this.getPosX() > 1500 || this.getPosX() < 0){
            this.angle = 180 - this.getAngle();
            if (this.getPosX() > 1500){
                this.imageView.setScaleY(-1);
                this.setDir(false);
            }
            else{
                this.imageView.setScaleY(1);
                this.setDir(true);
            }
            if (this.getPosX() < -3){
                System.out.println("GÅ TIL HØYRE");
                System.out.println(this.getFish().getX() + "Speed:"+ this.getSpeed());
                // this.getFish().setX(100);
                return 20;
            }
            if (this.getPosX() > 1503){
                System.out.println("GÅ TIL VENSTRE");
                System.out.println(this.getFish().getX() + "Speed:"+ this.getSpeed());
                // this.getFish().setX(1400);
                return -20;
            }
        }
        endring_x = Math.cos(Math.toRadians(this.getAngle()))*speed;
        return endring_x;
    }

    public double calculateNextY() {
        if (this.getPosY() > 1000 || this.getPosY() < 500){
            this.angle = 360 - this.getAngle();
        }
        if (this.getPosY() < 497){
            System.out.println("GÅ NED");
            System.out.println(this.getFish().getY() + "Speed:"+ this.getSpeed());
            // this.getFish().setY(600);
            return 20;
        }
        if (this.getPosY() > 1003){
            System.out.println("GÅ OPP");
            System.out.println(this.getFish().getY() + "Speed:"+ this.getSpeed());
            // this.getFish().setY(900);
            return -20;
        }
        endring_y = Math.sin(Math.toRadians(this.getAngle()))*speed;
        return endring_y;
    }

    public void setPos(Point2D pos) {
        this.imageView.setX(pos.getX());
        this.imageView.setY(pos.getY());
    }
    public void generateFish(int numbersOfFish, VariablesNshit variables, AnchorPane anchorPane) {
        for (int i = 0; i < 1; i++) {

            // Fish fish2 = new Fish(new Point2D(100+i*2,510 + i*50), new Point2D(30, 10));
            // variables.addToFishesList(fish2);
            // anchorPane.getChildren().add(fish2.getFish());   
            int j = new Random().nextInt(3) + 1;

            YellowFish yellowFish = null;
            BlueFish blueFish = null;
            PinkFish pinkFish = null;
            if (j == 1) {
                yellowFish = new YellowFish(new Point2D(100+i*2,510 + i*50));
                variables.addToFishesList(yellowFish);
                anchorPane.getChildren().add(yellowFish.getFish());   
            }
            else if (j == 2) {
                blueFish = new BlueFish(new Point2D(100+i*2,510 + i*50));
                variables.addToFishesList(blueFish);
                anchorPane.getChildren().add(blueFish.getFish());   
            }
            else{
                pinkFish = new PinkFish(new Point2D(100+i*2,510 + i*50));
                variables.addToFishesList(pinkFish);
                anchorPane.getChildren().add(pinkFish.getFish());   
            }
        }
    }
}
