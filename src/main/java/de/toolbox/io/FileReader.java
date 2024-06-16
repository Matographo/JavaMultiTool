package de.toolbox.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FileReader {
    

    public static List<String> readFileAsList(String path) throws IOException {
        return Files.lines(Paths.get(path)).collect(Collectors.toList());
    }
    
    public static String[] readFileAsArray(String path) throws IOException {
        return Files.lines(Paths.get(path)).toArray(String[]::new);
    }
    
    public static Set<String> readFileAsSet(String path) throws IOException {
        return Files.lines(Paths.get(path)).collect(HashSet::new, HashSet::add, HashSet::addAll);
    }
}
