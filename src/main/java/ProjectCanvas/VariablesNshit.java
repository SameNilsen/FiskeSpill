package ProjectCanvas;

import java.util.ArrayList;
import java.util.List;

public class VariablesNshit {

    double score = 0;
    Fish caughtFiss = null;
    List<Fish> caughtFishesList = new ArrayList<Fish>();
    List<Fish> fishes = new ArrayList<Fish>();
    List<Double> hichscorelist = new ArrayList<Double>();


    public double getScore() {
        return this.score;
    }
    public void setScore(double score) {
        this.score = score;
    }

    public Fish getCaughtFiss() {
        return this.caughtFiss;
    }
    public void setCaughtFiss(Fish caughtFiss) {
        this.caughtFiss = caughtFiss; 
    }

    public List<Fish> getCaughtFishesList() {
        return this.caughtFishesList;
    }
    public void addToCaughtFishesList(Fish fish) {
        this.caughtFishesList.add(fish);
    }
    public void newCaughtFishesList(ArrayList<Fish> list) {
        this.caughtFishesList = list;
    }

    public List<Fish> getFishesList() {
        return this.fishes;
    }
    public void addToFishesList(Fish fish) {
        this.fishes.add(fish);
    }

    public List<Double> getHighscoreList() {
        return this.hichscorelist;
    }
    public void newHighscoreList(List<Double> newList) {
        this.hichscorelist = newList;
    }
    
}
