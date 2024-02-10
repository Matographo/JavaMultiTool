package de.multitool;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class RandomFileRenamer {

    public static void main(String[] args) {
        for (int i = 0; i < 500; i++) {
            new RandomFileRenamer().start();
        }
    }

    public void start() {
        ArrayList<File> files = getFiles();
        int length = files.size();

        // Umbenennung mit zufälliger Reihenfolge
        files = renameAll(files);
        Collections.shuffle(files);

        // Dateien umbenennen
        for (int i = 0; i < length; i++) {
            File originalFile = files.get(i);
            String extension = getFileExtension(originalFile);
            File newFile = new File(i + extension);
            originalFile.renameTo(newFile);
        }
    }

    private ArrayList<File> getFiles() {
        File[] allFiles = new File(System.getProperty("user.dir")).listFiles();
        ArrayList<File> files = new ArrayList<>();

        for (File file : allFiles) {
            if (file.isFile()
                    && !file.getName().substring(0, file.getName().indexOf(".")).equals("RandomFileRenamer")) {
                files.add(file);
            }
        }

        return files;
    }

    private ArrayList<File> renameAll(ArrayList<File> files) {
        int i = 0;
        ArrayList<File> renamedFiles = new ArrayList<>();

        for (File originalFile : files) {
            String extension = getFileExtension(originalFile);
            File newFile = new File(i + extension);
            originalFile.renameTo(newFile);
            renamedFiles.add(newFile);
            i++;
        }

        return renamedFiles;
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastDotIndex = name.lastIndexOf(".");
        return lastDotIndex != -1 ? name.substring(lastDotIndex) : "";
    }
}
