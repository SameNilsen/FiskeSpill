package ProjectCanvas;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class BlueFish extends Fish{

    private Image blueFish = new Image(getClass().getResourceAsStream("res/blueFish.png"));

    public BlueFish(Point2D pos) {
        super(pos);
        super.setImageView(blueFish);
    }
    public double getPoint(){
        return ((this.getFish().getFitHeight()+this.getFish().getFitWidth()) * 0.01 + 1) * 300;
    }
}
