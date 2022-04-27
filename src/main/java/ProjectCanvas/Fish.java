package ProjectCanvas;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
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
    
    Random rand = new Random();
    private double angle;
    private double speed;
    private Point2D pos;

    public Fish(Point2D pos) {
        this.pos = pos;
        // this.imageView.setImage(img);

        // // System.out.println(":::::::::"+fishImages.indexOf(imageView.getImage()));
        // this.imageView.setPreserveRatio(true);
        // this.imageView.setFitHeight(rand.nextInt(30, 60));
        // this.imageView.setY(this.pos.getY());
        // this.imageView.setX(this.pos.getX());
        // fishImages.add(fish1);
        // fishImages.add(blueFish);
        // fishImages.add(pinkFish);
        // points.add(100);
        // points.add(300);
        // points.add(800);

        // this.fish = new Ellipse(pos.getX(), pos.getY(), size.getX(), size.getY());
        
        this.angle = rand.nextInt(0, 360);
        if ((0 < angle && angle < 90) || (270 < angle && angle < 369)){
            this.dir = true;
        }
        else{
            this.dir = false;
        }
        this.speed = rand.nextInt(1, 4);

        // imageView.setImage(fishImages.get(rand.nextInt(fishImages.size())));
        // System.out.println(":::::::::"+fishImages.indexOf(imageView.getImage()));
        // imageView.setPreserveRatio(true);
        // imageView.setFitHeight(rand.nextInt(30, 60));
        // imageView.setFitWidth(rand.nextInt(30, 80));
        // imageView.setY(posY);
        // imageView.setX(posX);

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
                this.dir = false;
            }
            else{
                this.imageView.setScaleY(1);
                this.dir = true;
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
}
