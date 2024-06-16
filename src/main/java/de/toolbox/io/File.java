package de.toolbox.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class File {

    private List<String> content;
    private java.io.File file;
    private Thread autoSaveThread;

    private int contentHash;
    private int autoSaveInterval  = 1000;
    private boolean isTemporary   = false;
    private boolean isAutoSave    = false;
    private boolean blockedThread = false;
    


    public File(java.io.File file, boolean autoSave) throws IOException {
        this(file.getAbsolutePath(), autoSave);
    }
    public File(String path, boolean autoSave) throws IOException {
        file    = new java.io.File(path);
        content = new ArrayList<>();
        if (exist() && file.isDirectory()) {
            throw new IOException("File is a directory");
        }
        if (exist()) {
            loadContent();
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if(isTemporary) {
                    delete();
                }
            }
        });
        autoSaveThread = new Thread() {
            public void run() {
                while(isAutoSave) {
                    try {
                        Thread.sleep(autoSaveInterval);
                        while(blockedThread) {
                        }
                        autoSave();
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        if(autoSave) {
            autoSaveThread.start();
        }
    }

    public File(String path) throws IOException {
        this(path, false);
    }

    public File(java.io.File file) throws IOException {
        this(file, false);
    }
    
    public List<String> getContent() throws IOException {
        return content;
    }
    
    public void setContent(List<String> content) throws IOException {
        this.content = content;
    }
    
    public int getLineSize() throws IOException {
        if (getContent() == null){
            return 0;
        }
        return getContent().size();
    }
    
    private boolean exist() {
        return file.exists();
    }
    
    public boolean delete() {
        if(!exist()) {
            return false;
        }
        return file.delete();
    }

    public boolean create() throws IOException {
        return file.createNewFile();
    }

    public boolean rename(String newName) {
        return file.renameTo(new java.io.File(newName));
    }
    
    public boolean move(String newPath) {
        return file.renameTo(new java.io.File(newPath));
    }
    
    public boolean setPermissions(int permissions) {
        return setPermissions((permissions & 4) == 4, (permissions & 2) == 2, (permissions & 1) == 1);
    }

    public boolean setPermissions(boolean readable, boolean writable, boolean executable) {
        return file.setReadable(readable) && file.setWritable(writable) && file.setExecutable(executable);
    }

    public boolean setReadable(boolean readable) {
        return file.setReadable(readable);
    }

    public boolean setWritable(boolean writable) {
        return file.setWritable(writable);
    }

    public boolean setExecutable(boolean executable) {
        return file.setExecutable(executable);
    }

    public boolean isReadable() {
        return file.canRead();
    }

    public boolean isWritable() {
        return file.canWrite();
    }

    public boolean isExecutable() {
        return file.canExecute();
    }
    
    public void setTemporary() {
        setTemporary(true);
    }

    public long getByteSize() {
        return file.length();
    }

    public void setTemporary(boolean isTemporary) {
        this.isTemporary = isTemporary;
    }
    
    public long getLastModification() {
        return file.lastModified();
    }

    public String getPath() {
        return file.getAbsolutePath();
    }

    public String getName() {
        if(file.getName().contains(".")) {
            return file.getName().substring(0, file.getName().lastIndexOf("."));
        }
        return file.getName();
    }

    public String getExtension() {
        if(file.getName().contains(".")) {
            return file.getName().substring(file.getName().lastIndexOf(".") + 1);
        }
        return "";
    }

    public String getParentPath() {
        return file.getParent();
    }

    public java.io.File getParent() {
        return file.getParentFile();
    }

    public void appendLine(String line) throws IOException {
        blockedThread = true;
        content.add(line);
        FileWriter.appendToFile(file.getAbsolutePath(), "\n" + line);
        contentHash = content.hashCode();
        blockedThread = false;
    }

    public void appendLines(List<String> lines) throws IOException {
        blockedThread = true;
        content.addAll(lines);
        FileWriter.appendToFile(file.getAbsolutePath(), "\n" + lines);
        contentHash = content.hashCode();
        blockedThread = false;
    }

    public void appendLines(String[] lines) throws IOException {
        blockedThread = true;
        content.addAll(Arrays.asList(lines));
        FileWriter.appendToFile(file.getAbsolutePath(), "\n" + lines);
        contentHash = content.hashCode();
        blockedThread = false;
    }
    
    public void changeLine(int line, String newLine) throws IOException {
        content.set(line, newLine);
    }

    public void setName(String name) {
        rename(name + "." + getExtension());
    }

    public void setExtension(String extension) {
        rename(getName() + "." + extension);
    }
    
    public boolean isSaved() {
        return content.hashCode() == contentHash;
    }
    
    public List<String> load() throws IOException {
        return getContent();
    }

    private void autoSave() throws IOException {
        if(content.hashCode() == contentHash) {
            return;
        }
        FileWriter.writeToFile(file.getAbsolutePath(), content);
        contentHash = content.hashCode();
    }

    public void save() throws IOException {
        FileWriter.writeToFile(file.getAbsolutePath(), content);
        contentHash = content.hashCode();
    }

    public void setAutoSave(boolean autoSave) {
        isAutoSave = autoSave;
        if(autoSave) {
            autoSaveThread.start();
        } else {
            autoSaveThread.interrupt();
        }
    }

    public void setAutoSaveInterval(int interval) {
        autoSaveInterval = interval;
    }

    public boolean isAutoSave() {
        return isAutoSave;
    }

    public int getAutoSaveInterval() {
        return autoSaveInterval;
    }

    public boolean add(String line) throws IOException {
        blockedThread = true;
        appendLine(line);
        return true;
    }

    public boolean add(int index, String line) throws IOException {
        content.add(index, line);
        return true;
    }

    public boolean addAll(List<String> lines) throws IOException {
        blockedThread = true;
        appendLines(lines);
        return true;
    }

    public boolean addAll(int index, List<String> lines) throws IOException {
        return content.addAll(index, lines);
    }

    public boolean remove(String line) throws IOException {
        return content.remove(line);
    }

    public boolean remove(int index) throws IOException {
        content.remove(index);
        return true;
    }

    public boolean removeAll(List<String> lines) throws IOException {
        return content.removeAll(lines);
    }

    public boolean removeAll(int fromIndex, int toIndex) throws IOException {
        for(int i = fromIndex; i < toIndex; i++) {
            content.remove(i);
        }
        return true;
    }

    public boolean clear() throws IOException {
        content.clear();
        return true;
    }

    public boolean contains(String line) throws IOException {
        return content.contains(line);
    }

    public boolean containsAll(List<String> lines) throws IOException {
        return content.containsAll(lines);
    }

    public String get(int index) throws IOException {
        return content.get(index);
    }

    public int indexOf(String line) throws IOException {
        return content.indexOf(line);
    }

    public int lastIndexOf(String line) throws IOException {
        return content.lastIndexOf(line);
    }

    public boolean isEmpty() throws IOException {
        return content.isEmpty();
    }

    private void loadContent() {
        try {
            content = FileReader.readFileAsList(file.getAbsolutePath());
        } catch (IOException e) {
        }
    }

}
