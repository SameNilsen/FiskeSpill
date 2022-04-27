package ProjectCanvas;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class YellowFish extends Fish {

    private Image yellowFish = new Image(getClass().getResourceAsStream("res/yellowFish.png"));

    public YellowFish(Point2D pos) {
        super(pos);
        super.setImageView(yellowFish);
    }
    public double getPoint(){
        return ((this.getFish().getFitHeight()+this.getFish().getFitWidth()) * 0.01 + 1) * 100;
    }
}
