package hr.fer.zemris.projekt.gui.view;

import hr.fer.zemris.projekt.algorithm.player.ArtificialPlayer;
import hr.fer.zemris.projekt.algorithm.player.ClimbNearestLadderPlayer;
import hr.fer.zemris.projekt.gui.configuration.WindowConfig;
import hr.fer.zemris.projekt.gui.util.ResourceLoader;
import hr.fer.zemris.projekt.model.serialization.JavaArtificialPlayerSerializer;
import hr.fer.zemris.projekt.model.serialization.SerializationException;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
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
        private TitledPane apMenuPane;
        private TitledPane settingsMenuPane;

        // vertical padding between menu items
        private int menuPadding = 20;

        // current active menu
        private Node activeMenu;

        // main menu items
        private Button btnStartGame = new Button("Start game");
        private Button btnAP = new Button("Artificial player");
        private Button btnSettings = new Button("Settings");
        private Button btnExit = new Button("Exit");

        // AI players menu items
        private RadioButton[] aiModels = new RadioButton[]{
                new RadioButton("Model 1"),
                new RadioButton("Model 2"),
                new RadioButton("Model 3")
        };
        private ToggleGroup tg = new ToggleGroup();

        // return button
        private Button btnReturn = new Button("Return");

        // blank button
        private Button btnBlank = new Button();

        // null if artificial player item isn't used
        private ArtificialPlayer aiPlayer;

        public GameMenu(MenuViewManager manager) {
            // style class
            getStyleClass().clear();
            getStyleClass().add("game-menu");

            initMainMenuPane(manager);
            initAPMenuPane();
            initBlankButton();
            initReturnButton();

            activeMenu = mainMenuPane;
            getChildren().add(mainMenuPane);
        }

        private void initMainMenuPane(MenuViewManager manager) {
            VBox menu = new VBox(menuPadding);
            mainMenuPane = new TitledPane("Main menu", menu);
            bindToMidScreenX(mainMenuPane, manager.menuPane);
            // add menu items
            menu.getChildren().addAll(btnStartGame, btnAP, btnSettings, btnExit);
            // start game action
            btnStartGame.setOnAction(event ->
                    new GameViewManager(aiPlayer).createNewGame(manager.menuStage)
            );
            // artificial player action
            btnAP.setOnAction(event -> translateTransition(mainMenuPane, apMenuPane));
            // exit action
            btnExit.setOnAction(event -> Platform.exit());
        }

        private void initAPMenuPane() {
            VBox menu = new VBox(menuPadding);
            apMenuPane = new TitledPane("AI players", menu);
            // add menu items
            for (RadioButton model : aiModels) {
                RadioButtonSelectionHandler handler = new RadioButtonSelectionHandler(model);
                model.setOnMousePressed(handler.mousePressed);
                model.setOnMouseReleased(handler.mouseReleased);
                model.setToggleGroup(tg);
                menu.getChildren().add(model);
            }
            menu.getChildren().addAll(btnBlank, btnReturn);
        }

        private void initBlankButton() {
            btnBlank.setId("blankBtn");
        }

        private void initReturnButton() {
            btnReturn.setOnAction(event -> {
                RadioButton selectedModel = (RadioButton) tg.getSelectedToggle();
                if (selectedModel != null) {
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
            });
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
