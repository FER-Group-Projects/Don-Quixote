package hr.fer.zemris.projekt.gui;

import hr.fer.zemris.projekt.gui.view.GameViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class GameLauncher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GameViewManager gameViewManager = new GameViewManager();
        primaryStage = gameViewManager.getGameStage();
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.out.println("STOP");
    }
}
