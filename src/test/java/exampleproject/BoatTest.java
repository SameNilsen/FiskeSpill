package exampleproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ProjectCanvas.Boat;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BoatTest {
    public void testConstructor() {
        Boat boat;
        ImageView boatImage = new ImageView();

        
        boat = new Boat(new Point2D(10, -60), null, boatImage);
        assertEquals(10, boat.getBoat().getX());
        assertEquals(-60, boat.getBoat().getY());

    }

    @Test                                                 
    void testMoveBoat() {
        
        ImageView boatImage = new ImageView();
        Boat boat = new Boat(new Point2D(10, -60), null, boatImage);
        boat.moveBoat(new Point2D(20, -60));
        assertEquals(20, boat.getX());
        assertEquals(true, boat.getDirection());

    }
}
