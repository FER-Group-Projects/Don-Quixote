package hr.fer.zemris.projekt.gui;

import hr.fer.zemris.projekt.gui.configuration.SceneConfig;
import hr.fer.zemris.projekt.gui.view.GameViewManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import static hr.fer.zemris.projekt.gui.assets.Sprites.LADDER_SPRITE;
import static hr.fer.zemris.projekt.gui.assets.Sprites.PLAYER_SPRITESHEET;
import static hr.fer.zemris.projekt.gui.configuration.SceneConfig.PLAYER_HEIGHT;
import static hr.fer.zemris.projekt.gui.configuration.SceneConfig.PLAYER_WIDTH;

public class GameLauncher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new AnchorPane();
        Canvas canvas = new Canvas(SceneConfig.GAME_SCENE_WIDTH,
                SceneConfig.GAME_SCENE_HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, SceneConfig.GAME_SCENE_WIDTH,
                SceneConfig.GAME_SCENE_HEIGHT);
        primaryStage.setScene(scene);
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.drawImage(new Image(LADDER_SPRITE), 0, 0);
        g.drawImage(new Image(PLAYER_SPRITESHEET), 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, 0, 0,
                PLAYER_WIDTH, PLAYER_HEIGHT);
        GameViewManager gameViewManager = new GameViewManager();
        primaryStage = gameViewManager.getGameStage();
        primaryStage.show();
        /*Pane root = new AnchorPane();
        Canvas canvas = new Canvas(GAME_SCENE_WIDTH, GAME_SCENE_HEIGHT);
        GraphicsContext g = canvas.getGraphicsContext2D();
        Image ladder = ImageModifier.makeTransparent(new Image(LADDER_SPRITE));
        Image player = ImageModifier.makeTransparent(new Image(PLAYER_SPRITESHEET));
        g.drawImage(ladder, 0, 0);
        g.drawImage(player, 0, 0, 64, 77, 64, 0, -64, 77);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, GAME_SCENE_WIDTH, GAME_SCENE_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();*/
    }

    @Override
    public void stop() throws Exception {
        System.out.println("STOP");
    }
}
