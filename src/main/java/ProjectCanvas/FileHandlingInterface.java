package ProjectCanvas;

import java.io.FileNotFoundException;
import java.util.List;

public interface FileHandlingInterface {

	public void save(String filename, List<Double> highscoreList) throws FileNotFoundException;

	public List<Double> load(String filename) throws FileNotFoundException;

}