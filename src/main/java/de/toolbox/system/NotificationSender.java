package de.toolbox.system;

import java.io.IOException;

public class NotificationSender {
    
    private String title;
    private String message;
    private String icon;
    private String sound;

    public NotificationSender(String title, String message, String icon, String sound) {
        this.title = title;
        this.message = message;
        this.icon = icon;
        this.sound = sound;
    }

    public NotificationSender(String title, String message) {
        this(title, message, null, null);
    }

    public NotificationSender(String message) {
        this(null, message, null, null);
    }

    public void send() throws IOException, InterruptedException {
        if(ProcessData.isWindows()) sendWindows();
        else if(ProcessData.isLinux()) sendLinux();
    }

    private void sendWindows() throws IOException, InterruptedException {
        ProcessDialog processDialog = new ProcessDialog("cmd.exe", "/c", "$notify.showballoontip(10,\"Script Completed!\",\"Your script ran succesfully!\",[system.windows.forms.tooltipicon]::None)");
        processDialog.start();
    }

    private void sendLinux() throws IOException, InterruptedException {
        ProcessDialog processDialog = new ProcessDialog("notify-send", message);
        processDialog.start();
    }

}
