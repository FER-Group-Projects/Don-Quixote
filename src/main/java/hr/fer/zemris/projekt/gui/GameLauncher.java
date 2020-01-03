package hr.fer.zemris.projekt.gui;

import hr.fer.zemris.projekt.gui.view.MenuViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class GameLauncher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage = new MenuViewManager().getMenuStage();
        //primaryStage = new GameViewManager(null).getGameStage();
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.out.println("STOP");
    }
}
