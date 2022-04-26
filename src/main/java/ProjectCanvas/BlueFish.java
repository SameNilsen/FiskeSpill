package ProjectCanvas;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BlueFish extends Fish{

    private Image blueFish = new Image(getClass().getResourceAsStream("res/blueFish.png"));
    private ImageView imageView = new ImageView();

    public BlueFish(Point2D pos, Point2D size) {
        super(pos, size);
        double posX = pos.getX();
        double posY = pos.getY();
        imageView.setImage(blueFish);
        // System.out.println(":::::::::"+fishImages.indexOf(imageView.getImage()));
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(rand.nextInt(30, 60));
        imageView.setFitWidth(rand.nextInt(30, 80));
        imageView.setY(posY);
        imageView.setX(posX);
    }
    @Override
    public double getPoint(){
        return 200;
    }
}
