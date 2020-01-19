package hr.fer.zemris.projekt.gui.util;

import hr.fer.zemris.projekt.gui.configuration.SceneConfig;
import hr.fer.zemris.projekt.gui.view.GameViewManager;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class AlertBox {

    public static void display(String message, GameViewManager manager) {
        Stage window = new Stage(StageStyle.UNDECORATED);
        Stage mainStage = manager.getGameStage();
        Pane gamePane = manager.getGamePane();

        window.setX((mainStage.getWidth() + mainStage.getX()) / 2);
        window.setY(mainStage.getHeight() / 2);
        window.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label(message);

        Button btnClose = new Button("Return to main menu");
        btnClose.setOnAction(e -> {
            window.close();
            FadeTransition ft = new FadeTransition(Duration.seconds(0.35), gamePane);
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.setOnFinished(event -> {
                manager.stop();
                manager.getMenuStage().show();
            });
            ft.play();
        });

        VBox layout = new VBox(10, label, btnClose);
        layout.setId("alert");

        Scene scene = new Scene(layout);
        scene.getStylesheets().add(SceneConfig.MENU_SCENE_STYLESHEET);

        window.setScene(scene);
        window.show();
    }
}
