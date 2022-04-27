package ProjectCanvas;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Boat {
    
    // Image boatImage = new Image(getClass().getResourceAsStream("boat2.png"));
    private ImageView image;
    private Point2D pos;

    // True - Right
    // False - Left
    private Boolean direction = true;


    public Boat(Point2D pos, Image boatImage, ImageView image) {
        this.pos = pos;
        this.image = image;
        this.image.setImage(boatImage);
        this.image.setPreserveRatio(true);
        this.image.setFitHeight(350);
        this.image.setFitWidth(350);
        this.image.setY(this.pos.getY());
        this.image.setX(this.pos.getX());
    }

    public ImageView getBoat() {
        return this.image;
    }

    public void moveBoat(Point2D newPos) {
        if (newPos.getX() < this.pos.getX()) {
            this.setDirection(false);
            this.image.setScaleX(-1);
        }
        else {
            this.setDirection(true);
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

    public boolean getDirection() {
        return this.direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }


}
