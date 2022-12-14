package ProjectTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ProjectCanvas.Fish;
import ProjectCanvas.YellowFish;
import javafx.geometry.Point2D;

public class FishTest {
    
    private YellowFish fish;

    @BeforeEach
    public void setup() {
        fish = new YellowFish(new Point2D(100, 200));
    }

    @Test
    @DisplayName("Test konstruktør")
    public void testConstructor() {
        
        assertEquals(100, fish.getPosX());
        assertEquals(200, fish.getPosY());
    }

    @Test
    @DisplayName("Test at posisjonen endres om den når kanten")
    public void testCalculate() {
        Fish fish2 = new Fish(new Point2D(1499, 600));
        fish2.setAngle(0);
        fish2.calculateNextX();
        double a = fish2.getAngle();
        if (fish2.getPosX() > 1500){
            assertNotEquals(fish2.getAngle(), a);
        }
    }

    @Test
    @DisplayName("Test riktig poeng")
    public void testPoints() {
        int expectedPoints = (int) ((1 + (fish.getFish().getFitWidth() + fish.getFish().getFitHeight())* 0.01) * 100);
        assertEquals(fish.getPoint(), expectedPoints);
    }
}
