package ProjectCanvas;

import java.io.Console;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Ellipse;



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

    // private double size;
    private double endring_x;
    private double endring_y;
    // private Ellipse fish;
    private ImageView imageView = new ImageView();
    // private Image fishImage;
    private int timeTillSwitch;
    private boolean dir;
    private long startTime;
    // AnimationTimer turnTimer;
    
    Random rand = new Random();
    private double angle;
    private double speed;

    public Fish(Point2D pos, Point2D size) {
        // this.fish = new Ellipse(pos.getX(), pos.getY(), size.getX(), size.getY());
        double posX = pos.getX();
        double posY = pos.getY();
        this.angle = rand.nextInt(0, 360);
        if ((0 < angle && angle < 90) || (270 < angle && angle < 369)){
            this.dir = true;
        }
        else{
            this.dir = false;
        }
        this.speed = rand.nextInt(1, 4);

        imageView.setImage(new Image(getClass().getResourceAsStream("fish1.png")));
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(rand.nextInt(30, 60));
        imageView.setFitWidth(rand.nextInt(30, 80));
        imageView.setY(posY);
        imageView.setX(posX);

    }


    public ImageView getFish() {
        // return this.fish;
        return this.imageView;
    }

    public double getSpeed() {
        return this.speed;
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
                    // System.out.println(time + "    "+angle);
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
            
            // for (int i = 0; i < 10000; i++) {
            //     this.angle += 0.01;
            //     this.imageView.setRotate(this.angle);
            // }
            
            // if (this.dir){
            //     System.out.println("SWITCH");
            //     this.angle = rand.nextInt(-90, 90);
            // }
            // else{
            //     System.out.println("SWITCH");
            //     this.angle = rand.nextInt(90, 270);
            // }
        }

        return this.angle;
    }

    public double getPosX() {
        // return this.fish.getCenterX();
        return this.imageView.getX();
    }

    public double getPosY() {
        // return this.fish.getCenterY();
        return this.imageView.getY();
    }

    public double calculateNextX() {
        if (this.getPosX() > 1500 || this.getPosX() < 0){
            this.angle = 180 - this.getAngle();
            if (this.getPosX() > 1500){
                this.imageView.setScaleY(-1);
                this.dir = false;
            }
            else{
                this.imageView.setScaleY(1);
                this.dir = true;
            }
        }
        endring_x = Math.cos(Math.toRadians(this.getAngle()))*speed;
        return endring_x;
    }

    public double calculateNextY() {
        if (this.getPosY() > 1000 || this.getPosY() < 500){
            this.angle = 360 - this.getAngle();
            //System.out.println("angleChange: " +this.getAngle());
        }
        endring_y = Math.sin(Math.toRadians(this.getAngle()))*speed;
        return endring_y;
    }

    public void setPos(Point2D pos) {
        // fish.setCenterX(pos_x);
        // fish.setCenterY(pos_y);
        this.imageView.setX(pos.getX());
        this.imageView.setY(pos.getY());
    }
}
