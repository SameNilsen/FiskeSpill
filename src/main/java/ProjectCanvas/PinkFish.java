package ProjectCanvas;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class PinkFish extends Fish{

    private Image pinkFish = new Image(getClass().getResourceAsStream("res/pinkFish.png"));

    public PinkFish(Point2D pos) {
        super(pos);
        super.setImageView(pinkFish);
    }
    public double getPoint(){
        return ((this.getFish().getFitHeight()+this.getFish().getFitWidth()) * 0.01 + 1) * 800;
    }
}
