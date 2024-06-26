package de.toolbox.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import de.toolbox.system.ProcessData;

public class Zip {


    // TODO: Schreiben einer Methode mit der man daten aus Zip liest ohne sie zu entpacken



    public static boolean unzip(String source, String destination) {
        try {
            // Eingabestream für die Zip-Datei
            FileInputStream fis = new FileInputStream(source);
            ZipInputStream zis = new ZipInputStream(fis);

            // Methode zum Entpacken aufrufen
            extractFiles(zis, destination);

            // Stream schließen
            zis.close();
            fis.close();

            System.out.println("Entpacken abgeschlossen.");

        } catch (IOException e) {
            return false;
        }
        return true;
    }
    
    public static boolean zip(String source, String destination, String zipName) {
        String folderMarker;
        if(ProcessData.isWindows()) folderMarker = "\\";
        else folderMarker = "/";

        try {
            // Ausgabedatei erstellen
            FileOutputStream fos = new FileOutputStream(destination + folderMarker + zipName + ".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);

            // Methode zum Hinzufügen von Dateien zum Zip-Archiv aufrufen
            addFilesToZip(source, destination, zos);

            // Stream schließen
            zos.close();
            fos.close();

            System.out.println("Zip-Vorgang abgeschlossen.");

        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static void zip(String source, String destination) {
        String sourceFileName = new File(source).getName();
        zip(source, destination, sourceFileName);
    }

    private static void addFilesToZip(String folder, String sourceFolder, ZipOutputStream zos) throws IOException {
        File sourceFile = new File(folder);
        File[] files = sourceFile.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                // Rekursiver Aufruf für Unterverzeichnisse
                addFilesToZip(file.getAbsolutePath(), sourceFolder, zos);
            } else {
                // Datei zum Zip-Archiv hinzufügen
                String relativePath = file.getAbsolutePath().substring(sourceFolder.length() + 1);
                ZipEntry zipEntry = new ZipEntry(relativePath);
                zos.putNextEntry(zipEntry);

                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int length;

                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }

                zos.closeEntry();
                fis.close();
            }
        }
    }

    private static void extractFiles(ZipInputStream zis, String destinationFolder) throws IOException {
        byte[] buffer = new byte[1024];

        ZipEntry zipEntry = zis.getNextEntry();

        while (zipEntry != null) {
            String fileName = zipEntry.getName();
            File newFile = new File(destinationFolder + File.separator + fileName);

            // Erstellen der Verzeichnisse, wenn diese nicht existieren
            new File(newFile.getParent()).mkdirs();

            // Datei schreiben
            FileOutputStream fos = new FileOutputStream(newFile);
            int length;

            while ((length = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            fos.close();
            zis.closeEntry();
            zipEntry = zis.getNextEntry();
        }
    }
}
