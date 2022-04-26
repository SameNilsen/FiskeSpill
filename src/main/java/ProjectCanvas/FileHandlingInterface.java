package ProjectCanvas;

import java.io.FileNotFoundException;
import java.util.List;

public interface FileHandlingInterface {

	public void save(String filename, List<Integer> highscoreList) throws FileNotFoundException;

	public List<Integer> load(String filename) throws FileNotFoundException;

}