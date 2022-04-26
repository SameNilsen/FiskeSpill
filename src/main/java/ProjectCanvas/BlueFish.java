package ProjectCanvas;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BlueFish extends Fish{

    private Image blueFish = new Image(getClass().getResourceAsStream("res/blueFish.png"));

    public BlueFish(Point2D pos, Point2D size) {
        super(pos, size);
        super.setImageView(blueFish);
    }

    public double getPoint(){
        return 200;
    }
}
