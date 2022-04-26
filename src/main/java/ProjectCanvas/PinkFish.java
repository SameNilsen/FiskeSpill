package ProjectCanvas;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PinkFish extends Fish{

    private Image pinkFish = new Image(getClass().getResourceAsStream("res/pinkFish.png"));

    public PinkFish(Point2D pos, Point2D size) {
        super(pos, size);
        super.setImageView(pinkFish);
    }
    public double getPoint(){
        return 300;
    }
}
