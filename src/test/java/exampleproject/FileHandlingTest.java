package exampleproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ProjectCanvas.FileHandling;

public class FileHandlingTest {

    FileHandling fileHandling;
    List<Integer> list;
    List<Integer> newList;

    @BeforeEach
    public void setup() {
        fileHandling = new FileHandling();
        list = new ArrayList<Integer>();
    }

    @Test
    @DisplayName("Test saving file")
    public void testSave() {
        try {
            fileHandling.save("HighscoreList", list);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } 
        try {
            newList = fileHandling.load("HighscoreList");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(list, newList);

    }

    @Test
    @DisplayName("Test loading file")
    public void testLoad() {
        
    }
    
}
