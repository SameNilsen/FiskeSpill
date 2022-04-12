package ProjectCanvas;

import java.io.Console;
import java.util.Random;

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

    private double speed = 2;
    // private double size;
    private double endring_x;
    private double endring_y;
    // private Ellipse fish;
    private ImageView imageView = new ImageView();
    // private Image fishImage;
    private int timeTillSwitch;
    
    Random rand = new Random();
    private double angle = rand.nextInt(360);

    public Fish(Point2D pos, Point2D size) {
        // this.fish = new Ellipse(pos.getX(), pos.getY(), size.getX(), size.getY());
        double posX = pos.getX();
        double posY = pos.getY();

        imageView.setImage(new Image(getClass().getResourceAsStream("fish1.png")));
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
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

    public double getAngle() {
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
            // if (this.getPosX() > 1500)
            // this.imageView.setScaleX(-1);
            // else
            // this.imageView.setScaleX(1);
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
