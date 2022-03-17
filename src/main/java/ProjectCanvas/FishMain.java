package ProjectCanvas;

public class FishMain {

    public String computeScreenDirection(double mus_x, double mus_y) {
        String dir = "";
        if (mus_y > 270){
            if (mus_x > 280){
                dir = "right";
            }
            if (mus_x < 280){
                dir = "left";
            }
            
        }
        return dir;
    }
    
}
