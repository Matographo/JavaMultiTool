package de.multitool;

import javafx.application.Platform;
import javafx.geometry.Pos;
import org.controlsfx.control.Notifications;

public class Main {
    public static void main(String[] args) {
        Platform.runLater(() -> {
            Notifications notificationTest = Notifications.create();
            notificationTest.position(Pos.BASELINE_RIGHT);
            notificationTest.title("Hallo Welt");
            notificationTest.text("Schöner Test");
            notificationTest.show();
        });
    }
}