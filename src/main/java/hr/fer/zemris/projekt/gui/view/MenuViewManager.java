package hr.fer.zemris.projekt.gui.view;

import hr.fer.zemris.projekt.algorithm.player.ArtificialPlayer;
import hr.fer.zemris.projekt.gui.configuration.WindowConfig;
import hr.fer.zemris.projekt.gui.util.ResourceLoader;
import hr.fer.zemris.projekt.model.serialization.JavaArtificialPlayerSerializer;
import hr.fer.zemris.projekt.model.serialization.SerializationException;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.nio.file.Paths;

import static hr.fer.zemris.projekt.gui.configuration.SceneConfig.*;

public class MenuViewManager {

    private Pane menuPane;
    private Scene menuScene;
    private Stage menuStage;

    public MenuViewManager() {
        menuPane = new Pane();
        menuScene = new Scene(menuPane, GAME_SCENE_WIDTH, GAME_SCENE_HEIGHT);
        menuScene.getStylesheets().add(MENU_SCENE_STYLESHEET);
        menuStage = new Stage();
        menuStage.setScene(menuScene);
        menuStage.setResizable(false);
        menuStage.setTitle(WindowConfig.WINDOW_TITLE);

        menuPane.getChildren().addAll(gameTitle(), new GameMenu(this));
    }

    public Stage getMenuStage() {
        return menuStage;
    }

    private static void bindToMidScreenX(Region node, Pane root) {
        node.layoutXProperty()
                .bind(root.widthProperty().subtract(node.widthProperty()).divide(2));
    }

    private Node gameTitle() {
        Label title = new Label("Don Quixote");
        title.setId("title");
        bindToMidScreenX(title, menuPane);
        return title;
    }

    /**
     * Parent component which models a simple game menu.
     */
    private static class GameMenu extends Parent {
        private TitledPane mainMenuPane;
        private TitledPane sceneMenuPane;
        private TitledPane apMenuPane;

        // vertical padding between menu items
        private int menuPadding = 20;

        // current active menu
        private Node activeMenu;

        // main menu items
        private Button btnStartGame = new Button("Start game");
        private Button btnAP = new Button("Artificial player");
        private Button btnExit = new Button("Exit");

        // scene menu items
        private Button btnStart = new Button("Start");
        private RadioButton[] scenes = new RadioButton[]{
                new RadioButton("Normal scene"),
                new RadioButton("Climbing scene"),
                new RadioButton("Barrel scene"),
                new RadioButton("Climbing barrel scene")
        };
        // scene menu toggle group
        private ToggleGroup sceneTg = new ToggleGroup();

        // AI players menu items
        private RadioButton[] aiModels = new RadioButton[]{
                new RadioButton("player.elman"),
                new RadioButton("Model 2"),
                new RadioButton("Model 3"),
                new RadioButton("Model 4")
        };
        // AI menu toggle group
        private ToggleGroup aiTg = new ToggleGroup();

        // return button
        private Button btnReturn1 = new Button("Return");
        private Button btnReturn2 = new Button("Return");

        // null if artificial player item isn't used
        private ArtificialPlayer aiPlayer;

        public GameMenu(MenuViewManager manager) {
            // style class
            getStyleClass().clear();
            getStyleClass().add("game-menu");

            initMainMenuPane(manager.menuPane);
            initSceneMenuPane(manager);
            initAPMenuPane();
            initReturnButton();

            activeMenu = mainMenuPane;
            getChildren().add(mainMenuPane);
        }


        private void initMainMenuPane(Pane root) {
            VBox menu = new VBox(menuPadding);
            mainMenuPane = new TitledPane("Main menu", menu);
            bindToMidScreenX(mainMenuPane, root);
            // add menu items
            menu.getChildren().addAll(btnStartGame, btnAP, btnExit);
            // start game action
            btnStartGame.setOnAction(event -> translateTransition(mainMenuPane, sceneMenuPane));
            // artificial player action
            btnAP.setOnAction(event -> translateTransition(mainMenuPane, apMenuPane));
            // exit action
            btnExit.setOnAction(event -> Platform.exit());
        }

        private void initSceneMenuPane(MenuViewManager manager) {
            VBox menu = new VBox(menuPadding);
            sceneMenuPane = new TitledPane("Scenes", menu);
            // add menu items
            menu.getChildren().add(btnStart);
            for (RadioButton scene : scenes) {
                RadioButtonSelectionHandler handler = new RadioButtonSelectionHandler(scene);
                scene.setOnMousePressed(handler.mousePressed);
                scene.setOnMouseReleased(handler.mouseReleased);
                scene.setToggleGroup(sceneTg);
                menu.getChildren().add(scene);
            }
            menu.getChildren().addAll(btnReturn2);
        }

        private void initAPMenuPane() {
            VBox menu = new VBox(menuPadding);
            apMenuPane = new TitledPane("AI players", menu);
            // add menu items
            for (RadioButton model : aiModels) {
                RadioButtonSelectionHandler handler = new RadioButtonSelectionHandler(model);
                model.setOnMousePressed(handler.mousePressed);
                model.setOnMouseReleased(handler.mouseReleased);
                model.setToggleGroup(aiTg);
                menu.getChildren().add(model);
            }
            menu.getChildren().addAll(btnReturn1);
        }

        private void initReturnButton() {
            EventHandler<ActionEvent> action = event -> {
                RadioButton selectedModel = activeMenu == apMenuPane
                        ? (RadioButton) aiTg.getSelectedToggle()
                        : (RadioButton) sceneTg.getSelectedToggle();
                if (selectedModel != null && activeMenu == apMenuPane) {
                    JavaArtificialPlayerSerializer deserializer = new JavaArtificialPlayerSerializer();
                    try {
                        String path = ResourceLoader.loadResource(
                                getClass(),
                                "/" + selectedModel.getText()
                        );
                        aiPlayer = deserializer.deserialize(Paths.get(path));
                    } catch (SerializationException e) {
                        e.printStackTrace();
                    }
                }
                translateTransition(activeMenu, mainMenuPane);
            };
            btnReturn1.setOnAction(action);
            btnReturn2.setOnAction(action);
            btnReturn1.getStyleClass().add("button-return");
            btnReturn2.getStyleClass().add("button-return");
            btnExit.getStyleClass().add("button-return");
        }

        private void translateTransition(Node out, Node in) {
            final int offsetX = 600;

            getChildren().add(in);

            // node out translate transition
            TranslateTransition outTT = new TranslateTransition(Duration.seconds(0.35), out);
            outTT.setToX(out.getTranslateX() - offsetX);

            // node in translate transition
            TranslateTransition inTT = new TranslateTransition(Duration.seconds(0.75), in);
            inTT.setToX(out.getLayoutX());

            outTT.play();
            inTT.play();

            outTT.setOnFinished(evt -> getChildren().remove(out));
            activeMenu = in;
        }
    }

    /**
     * Handler for radio button selection.
     */
    private static class RadioButtonSelectionHandler {
        private boolean selected;
        private RadioButton button;

        public RadioButtonSelectionHandler(RadioButton button) {
            this.button = button;
        }

        private EventHandler<MouseEvent> mousePressed = event -> {
            if (button.isSelected()) selected = true;
        };

        private EventHandler<MouseEvent> mouseReleased = event -> {
            if (selected) button.setSelected(false);
            selected = false;
        };
    }
}
