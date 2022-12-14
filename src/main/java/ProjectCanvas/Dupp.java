package ProjectCanvas;

import javafx.geometry.Point2D;
import javafx.scene.shape.Ellipse;

public class Dupp {

    private Ellipse duppen;
    private Point2D pos;
    private boolean duppMove = true;
    private boolean duppUte = false;

    public Dupp(Point2D pos) {
        this.duppen = new Ellipse(5, 5);
        this.pos = pos;
        this.duppen.setCenterX(pos.getX());
        this.duppen.setCenterY(pos.getY());
        
    }

    public void moveDupp(Point2D newPos) {
        this.pos = newPos;
        this.duppen.setCenterX(this.pos.getX());
        this.duppen.setCenterY(this.pos.getY());
    }
    public double getX() {
        return this.pos.getX();
    }

    public double getY() {
        return this.pos.getY();
    }

    public Ellipse getEllipse() {
        return this.duppen;
        
    }

    public boolean getDuppMove() {
        return this.duppMove;
    }
    public void setDuppMove(boolean duppMove) {
        this.duppMove = duppMove;
    }

    public boolean getDuppUte() {
        return this.duppUte;
    }
    public void setDuppUte(boolean duppUte) {
        this.duppUte = duppUte; 
    }
    
}
