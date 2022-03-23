package ProjectCanvas;

public class FishMain {

    //  Inntil videre er denne klassen fullstendig unødvendig, men den kan/skal fylles med mange 
    //  kalkulasjoner. Per nå er det bare hvor skjermen skal flyttes som blir bestemt her.

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
