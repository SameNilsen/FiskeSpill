package ProjectCanvas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHandling implements FileHandlingInterface{

    @Override
    public void save(String filename, List<Integer> highscoreList) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(new File(getFilePath(filename)))) {
            System.out.println("Lagrer liste :" + highscoreList);
            for (Integer tall : highscoreList) {
                System.out.println("Tall: " + tall);
                writer.println(tall);
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
        System.out.println(list);
        return list;
    }

    public static String getFilePath(String filename) {
        return "src/main/resources/ProjectCanvas/saves/"+filename+".txt";
		// return FileHandling.class.getResource("saves/").getFile() + filename + ".txt";
	}

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(123);
        System.out.println(new File("src/test.txt").getAbsolutePath());
        List<Integer> list = new ArrayList<Integer>();
        // File fil = new File("src/main/resources/ProjectCanvas/saves/HighscoreList.txt");
        File fil = new File(getFilePath("HighscoreList"));
        try (Scanner scanner = new Scanner(fil)) {
			while(scanner.hasNext()){
                String a = scanner.next();
                System.out.println(a +" " +Integer.parseInt(a));
                // list.add(scanner.nextInt());
            }
		} 
        System.out.println(list);
    }
    
}
