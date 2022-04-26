package exampleproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ProjectCanvas.FileHandling;

public class FileHandlingTest {

    FileHandling fileHandling;

    @BeforeEach
    public void setup() {
        fileHandling = new FileHandling();
        List<Integer> list = new ArrayList<Integer>();
    }

    @Test
    @DisplayName("Test loading file")
    public void testLoad() {
        
    }
    
}
