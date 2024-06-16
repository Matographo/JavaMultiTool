package de.toolbox.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.nio.file.StandardOpenOption;

public class FileWriter {
    
    public static void writeToFile(String path, String content) throws IOException {
        Files.write(Paths.get(path), content.getBytes());
    }
    
    public static void writeToFile(String path, String[] content) throws IOException {
        Files.write(Paths.get(path), Arrays.asList(content));
    }
    
    public static void writeToFile(String path, Iterable<String> content) throws IOException {
        Files.write(Paths.get(path), (Iterable<String>) content);
    }

    

    
    public static void appendToFile(String path, String content) throws IOException {
        Files.write(Paths.get(path), content.getBytes(), StandardOpenOption.APPEND);
    }

    public static void appendToFile(String path, String[] content) throws IOException {
        Files.write(Paths.get(path), Arrays.asList(content), StandardOpenOption.APPEND);
    }

    public static void appendToFile(String path, Iterable<String> content) throws IOException {
        Files.write(Paths.get(path), (Iterable<String>) content, StandardOpenOption.APPEND);
    }
}
