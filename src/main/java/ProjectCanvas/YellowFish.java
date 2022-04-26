package ProjectCanvas;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class YellowFish extends Fish {

    private Image yellowFish = new Image(getClass().getResourceAsStream("res/yellowFish.png"));

    public YellowFish(Point2D pos, Point2D size) {
        super(pos, size);
        super.setImageView(yellowFish);
    }
    public double getPoint(){
        return 50;
    }
}
