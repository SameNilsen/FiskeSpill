package ProjectCanvas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHandling implements FileHandlingInterface{

    @Override
    public void save(String filename, List<Integer> highscoreList) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(new File(getFilePath(filename)))) {
            System.out.println("Lagrer liste :" + highscoreList);
            for (Integer integer : highscoreList) {
                System.out.println("Tall: " + integer);
                writer.println(integer);
            }
		}        
    }

    @Override
    public List<Integer> load(String filename) throws FileNotFoundException {
        List<Integer> list = new ArrayList<Integer>();
        try (Scanner scanner = new Scanner(new File(getFilePath(filename)))) {
			while(scanner.hasNextInt()){
                list.add(scanner.nextInt());
            }
		}
        return list;
    }

    public static String getFilePath(String filename) {
		return FileHandling.class.getResource("saves/").getFile() + filename + ".txt";
	}
    
}
