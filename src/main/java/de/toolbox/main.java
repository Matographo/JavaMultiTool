package de.toolbox;


import java.io.IOException;


import de.toolbox.io.File;


public class main {
    
    public static void main(String[] args) throws IOException {
        File file = new File("Test.txt", true);

        System.out.println(file.getContent());
        file.add("Hans der Schwanz");
    }
}
