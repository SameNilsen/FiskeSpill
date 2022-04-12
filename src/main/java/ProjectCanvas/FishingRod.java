package ProjectCanvas;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FishingRod {

    ImageView image;
    Point2D pos;
    Boolean direction = true;

    public FishingRod(Point2D pos, Image rodImage, ImageView image) {
        this.pos = pos;
        this.image = image;
        this.image.setImage(rodImage);
        this.image.setPreserveRatio(true);
        this.image.setFitHeight(75);
        this.image.setFitWidth(75);
        this.image.setY(410);
        this.image.setX(435);
    }

    public void moveRod(Point2D newPos) {
        if (newPos.getX() < this.pos.getX()) {
            this.direction = false;
            this.image.setScaleX(-1);
        }
        else {
            this.direction = true;  
            this.image.setScaleX(1);
        }
        this.pos = newPos;
        this.image.setX(this.pos.getX());
    }
    public double getX() {
        return this.pos.getX();
    }

    public double getY() {
        return this.pos.getY();
    }
    
}
