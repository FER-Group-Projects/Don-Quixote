package hr.fer.zemris.projekt.gui.view;

import hr.fer.zemris.projekt.algorithm.player.ArtificialPlayer;
import hr.fer.zemris.projekt.gui.configuration.WindowConfig;
import hr.fer.zemris.projekt.gui.util.AlertBox;
import hr.fer.zemris.projekt.gui.view.sprite.*;
import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.controller.GameControllerListener;
import hr.fer.zemris.projekt.model.controller.PlayerAction;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl.GameParameters;
import hr.fer.zemris.projekt.model.input.GameInputExtractor;
import hr.fer.zemris.projekt.model.input.impl.RayColliderInputExtractor;
import hr.fer.zemris.projekt.model.objects.BoundingBox2D;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.impl.*;
import hr.fer.zemris.projekt.model.raycollider.RayCollider;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.event.KeyListener;
import java.util.*;
import java.util.stream.Collectors;

import static hr.fer.zemris.projekt.gui.assets.Audios.WALKING_SOUND;
import static hr.fer.zemris.projekt.gui.assets.Sprites.*;
import static hr.fer.zemris.projekt.gui.configuration.GameParemetersConfig.*;
import static hr.fer.zemris.projekt.gui.configuration.SceneConfig.*;

public class GameViewManager implements GameControllerListener {

    private Pane gamePane;
    private Canvas canvas;
    private Scene gameScene;
    private Stage gameStage;

    private GameController gc;
    private GameParameters params;

    private PlayerSprite playerSprite;
    private List<Sprite> sprites;

    private Random rand = new Random();

    // properties for keys
    private BooleanProperty leftPressed = new SimpleBooleanProperty(false);
    private BooleanProperty rightPressed = new SimpleBooleanProperty(false);
    private BooleanProperty upPressed = new SimpleBooleanProperty(false);
    private BooleanProperty downPressed = new SimpleBooleanProperty(false);

    // binding for key pressed and released
    private BooleanBinding leftOrRightPressed = leftPressed.or(rightPressed);
    private BooleanBinding leftAndRightPressed = leftPressed.and(rightPressed);
    private BooleanBinding upOrDownPressed = upPressed.or(downPressed);
    private BooleanBinding upAndDownPressed = upPressed.and(downPressed);

    // 1 for right and -1 for left
    private int direction = 1;

    private int numberOfPlatforms;

    private volatile boolean stopBarrels;
    private final Thread barrelThread = new Thread(() -> {
        double posX = (PLATFORM_X + PLATFORM_WIDTH) / 2;
        double[] posYs = new double[numberOfPlatforms];
        double offsetY = PLATFORM_START_Y + BARREL_HEIGHT;
        for (int i = 0; i < numberOfPlatforms; i++) {
            posYs[i] = offsetY;
            offsetY += LADDER_HEIGHT;
        }
        while (!stopBarrels) {
            for (double posY : posYs) {
                Barrel barrel = new Barrel(new BoundingBox2DImpl(
                        posX, posY, BARREL_WIDTH, BARREL_HEIGHT),
                        0, 0
                );
                sprites.add(new BarrelSprite(BARREL_SPRITESHEET, barrel));
                gc.addGameObject(barrel);
            }
            try {
                Thread.sleep(5_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    private String gameName;
    private ArtificialPlayer artificialPlayer;
    private volatile boolean stopAIThread;
    private final Thread aiThread = new Thread(() -> {
        GameInputExtractor inputExtractor = new RayColliderInputExtractor(4);
        try {
            Thread.sleep(3_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PlayerAction previousAction = PlayerAction.UP;
        while (!stopAIThread) {
            gc.unsetPlayerAction(previousAction);
            previousAction = artificialPlayer.calculateAction(inputExtractor.extractInputs(gc));
            switch (previousAction) {
                case LEFT:
                    direction = -1;
                    leftPressed.set(true);
                    rightPressed.set(false);
                    upPressed.set(false);
                    downPressed.set(false);
                    break;
                case RIGHT:
                    direction = 1;
                    leftPressed.set(false);
                    rightPressed.set(true);
                    upPressed.set(false);
                    downPressed.set(false);
                    break;
                case UP:
                    leftPressed.set(false);
                    rightPressed.set(false);
                    upPressed.set(true);
                    downPressed.set(false);
                    break;
                case DOWN:
                    leftPressed.set(false);
                    rightPressed.set(false);
                    upPressed.set(false);
                    downPressed.set(true);
                    break;
            }
            gc.setPlayerAction(previousAction);
            System.out.println(previousAction);
        }
    });

    private MediaPlayer walkingSound;
    private MediaPlayer jumpingSound;

    public GameViewManager(ArtificialPlayer artificialPlayer) {
        this.artificialPlayer = artificialPlayer;
        initJavaFXComponents();
        initGameParameters();
        initPlayerSprite();
        initSpritesList();
        if (artificialPlayer == null) {
            initKeyListeners();
            initBindings();
        }
        initGameController();
        startGame();
    }

    private void initJavaFXComponents() {
        gamePane = new AnchorPane();

        canvas = new Canvas(GAME_SCENE_WIDTH, GAME_SCENE_HEIGHT);
        gamePane.getChildren().add(canvas);

        gameScene = new Scene(gamePane, GAME_SCENE_WIDTH, GAME_SCENE_HEIGHT);
        gameScene.getStylesheets().add(GAME_SCENE_STYLESHEET);
        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {

            }
        });

        gameStage = new Stage();
        gameStage.setScene(gameScene);
        gameStage.setResizable(false);
        gameStage.setTitle(WindowConfig.WINDOW_TITLE);
        gameStage.setOnCloseRequest(event -> stop());

        gameName = artificialPlayer == null ?
                "Normal game" :
                artificialPlayer.getClass().getSimpleName();

        walkingSound = new MediaPlayer(new Media(WALKING_SOUND));
        walkingSound.setMute(true);
    }

    private void initGameParameters() {
        params = new GameParameters(
                TICK_RATE_PER_SEC,
                GRAVITATIONAL_ACCELARATION,
                BARREL_LADDER_PROBABILITY,
                PLAYER_DEFAULT_SPEED_GROUND,
                PLAYER_DEFAULT_SPEED_LADDERS,
                PLAYER_DEFAULT_SPEED_JUMP,
                OTHER_DEFAULT_SPEED_GROUND,
                OTHER_DEFAULT_SPEED_LADDERS
        );
    }

    private void initPlayerSprite() {
        Player player = new Player(
                new BoundingBox2DImpl(
                        PLAYER_START_X, PLAYER_START_Y,
                        PLAYER_WIDTH, PLAYER_HEIGHT),
                0, 0,
                "player"
        );
        playerSprite = new PlayerSprite(PLAYER_SPRITESHEET, player);
    }

    private void initSpritesList() {
        sprites = new ArrayList<>();
        initPlatforms();
        initLadders();
    }

    private void initPlatforms() {
        double offsetY = PLATFORM_START_Y;
        while (offsetY <= 0.94 * GAME_SCENE_HEIGHT) {
            Platform platform = new Platform(new BoundingBox2DImpl(
                    PLATFORM_X, offsetY,
                    PLATFORM_WIDTH, PLATFORM_HEIGHT)
            );
            sprites.add(new PlatformSprite(PLATFORM_SPRITE, platform));
            offsetY += LADDER_HEIGHT;
            numberOfPlatforms++;
        }
    }

    private void initLadders() {
        double offsetY = PLATFORM_START_Y + LADDER_HEIGHT;
        for (int i = 0; i < numberOfPlatforms - 1; i++) {
            double lowerBound = PLATFORM_X;
            double boundOffset = (PLATFORM_X + PLATFORM_WIDTH) / LADDERS_PER_PLATFORM;
            double upperBound = boundOffset;
            for (int j = 0; j < LADDERS_PER_PLATFORM; j++) {
                double randomX = lowerBound + rand.nextDouble() * (upperBound - lowerBound);
                if (randomX >= GAME_SCENE_WIDTH - PLATFORM_X
                        || randomX + LADDER_WIDTH >= GAME_SCENE_WIDTH - PLATFORM_X) {
                    randomX = GAME_SCENE_WIDTH - 1.2 * PLATFORM_X - LADDER_WIDTH;
                }
                Ladder ladder = new Ladder(new BoundingBox2DImpl(
                        randomX, offsetY,
                        LADDER_WIDTH, LADDER_HEIGHT)
                );
                sprites.add(new LadderSprite(LADDER_SPRITE, ladder));
                lowerBound = upperBound;
                upperBound += boundOffset;
            }
            offsetY += LADDER_HEIGHT;
        }
    }

    private void initKeyListeners() {
        gameScene.setOnKeyPressed(keyEvent -> {
            KeyCode code = keyEvent.getCode();
            switch (code) {
                case LEFT:
                case A:
                    leftPressed.set(true);
                    if (!rightPressed.get()) direction = -1;
                    gc.setPlayerAction(PlayerAction.LEFT);
                    break;
                case RIGHT:
                case D:
                    rightPressed.set(true);
                    if (!leftPressed.get()) direction = 1;
                    gc.setPlayerAction(PlayerAction.RIGHT);
                    break;
                case UP:
                case W:
                    upPressed.set(true);
                    gc.setPlayerAction(PlayerAction.UP);
                    break;
                case DOWN:
                case S:
                    downPressed.set(true);
                    gc.setPlayerAction(PlayerAction.DOWN);
                    break;
                case SPACE:
                    gc.setPlayerAction(PlayerAction.JUMP);
            }
        });
        gameScene.setOnKeyReleased(keyEvent -> {
            KeyCode code = keyEvent.getCode();
            switch (code) {
                case LEFT:
                case A:
                    leftPressed.set(false);
                    gc.unsetPlayerAction(PlayerAction.LEFT);
                    break;
                case RIGHT:
                case D:
                    rightPressed.set(false);
                    gc.unsetPlayerAction(PlayerAction.RIGHT);
                    break;
                case UP:
                case W:
                    upPressed.set(false);
                    gc.unsetPlayerAction(PlayerAction.UP);
                    break;
                case DOWN:
                case S:
                    downPressed.set(false);
                    gc.unsetPlayerAction(PlayerAction.DOWN);
                    break;
                case SPACE:
                    gc.unsetPlayerAction(PlayerAction.JUMP);
            }
        });
    }

    private void initBindings() {
        leftAndRightPressed.addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                direction = leftPressed.get() ? -1 : 1;
            }
        });
    }

    private void initGameController() {
        gc = new GameControllerImpl(
                (Player) playerSprite.getObject(),
                sprites.stream().map(Sprite::getObject).collect(Collectors.toList()),
                params);
        gc.addListener(this);
        sprites.add(playerSprite);
    }

    private void startGame() {
        gc.start();
        if (artificialPlayer == null) barrelThread.start();
        else aiThread.start();
    }

    private void repaint() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, GAME_SCENE_WIDTH, GAME_SCENE_HEIGHT);
        g.setFont(Font.getDefault());

        for (Game2DObject obj : gc.getGameObjects()) {

            BoundingBox2D bb = obj.getBoundingBox();

            Sprite sprite = sprites.stream()
                    .filter(s -> s.getObject().equals(obj))
                    .findAny().get();

            if (obj instanceof Player) {
                Frame frame;
                if (leftOrRightPressed.get() && !leftAndRightPressed.get() && ((Player) obj).isOnGround() ||
                        upOrDownPressed.get() && !upAndDownPressed.get() && ((Player) obj).isOnLadders()
                ) {
                    frame = ((PlayerSprite) sprite).run();
                } else if (((Player) obj).isJumping()) {
                    frame = ((PlayerSprite) sprite).jump();
                } else {
                    frame = ((PlayerSprite) sprite).idle();
                }

                // mirror image based on direction
                double posX = direction == 1 ? bb.getX() : bb.getX() + PLAYER_WIDTH;
                g.drawImage(
                        sprite.getImage(),
                        frame.x, frame.y,
                        frame.width, frame.height,
                        posX, GAME_SCENE_HEIGHT - bb.getY(),
                        direction * PLAYER_WIDTH, PLAYER_HEIGHT
                );
            }
            if (obj instanceof Barrel) {
                Frame frame = ((BarrelSprite) sprite).roll();
                g.drawImage(
                        sprite.getImage(),
                        frame.x, frame.y,
                        frame.width, frame.height,
                        bb.getX(), GAME_SCENE_HEIGHT - bb.getY(),
                        BARREL_WIDTH, BARREL_HEIGHT
                );
            }
            if (obj instanceof Platform || obj instanceof Ladder) {
                g.drawImage(sprite.getImage(), bb.getX(), GAME_SCENE_HEIGHT - bb.getY());
            }
        }

        RayColliderInputExtractor inputExtractor = new RayColliderInputExtractor(4);

        List<RayCollider.Collision> allCollisions = inputExtractor.calculateCollisions(gc);

        // Map : object with which any ray collides -> collisionDescriptor dd
        Map<Game2DObject, RayCollider.Collision> filteredClosestCollisions = new HashMap<>();

        for (var collision : allCollisions) {
            if (collision == null) continue;
            RayCollider.Collision oldC = filteredClosestCollisions.get(collision.getObject());
            if (oldC == null || collision.getDistance() < oldC.getDistance())
                filteredClosestCollisions.put(collision.getObject(), collision);
        }

        for (RayCollider.Collision collision : filteredClosestCollisions.values()) {
            if (collision == null) continue;

            Game2DObject obj = collision.getObject();
            BoundingBox2D bb = obj.getBoundingBox();
            g.fillText(
                    String.valueOf((int) collision.getDistance()),
                    bb.getX(),
                    GAME_SCENE_HEIGHT - bb.getY()
            );

            g.setStroke(Paint.valueOf("RED"));
            g.strokeLine(
                    collision.getRayOrigin().getX(),
                    GAME_SCENE_HEIGHT - collision.getRayOrigin().getY(),
                    collision.getPoint().getX(),
                    GAME_SCENE_HEIGHT - collision.getPoint().getY()
            );
        }

        double fontSize = 20;
        g.setFont(Font.font(Font.getDefault().getName(), fontSize));
        g.fillText(gameName, 0, fontSize);
    }

    public Stage getGameStage() {
        return gameStage;
    }

    public Pane getGamePane() {
        return gamePane;
    }

    private Stage menuStage;

    public Stage getMenuStage() {
        return menuStage;
    }

    public void createNewGame(Stage menuStage) {
        this.menuStage = menuStage;
        menuStage.hide();
        gameStage.show();
    }

    public void stop() {
        stopBarrels = true;
        stopAIThread = true;
        gc.stop();
        gameStage.close();
    }

    @Override
    public void gameObjectAdded(Game2DObject object) {
        // DO NOTHING
    }

    @Override
    public void gameObjectRemoved(Game2DObject object) {
        // DO NOTHING
    }

    @Override
    public void gameObjectChanged(Game2DObject object) {
        if (object.getBoundingBox().getX() + object.getBoundingBox().getWidth() <= 0 ||
                object.getBoundingBox().getX() >= GAME_SCENE_WIDTH ||
                object.getBoundingBox().getY() <= 0 ||
                object.getBoundingBox().getY() - object.getBoundingBox().getHeight() >= GAME_SCENE_HEIGHT)
            object.destroy();
    }

    @Override
    public void gameObjectDestroyed(Game2DObject object) {
        if (object instanceof Player) {
            javafx.application.Platform.runLater(() -> {
                AlertBox.display("Game over", this);
                System.out.println("Game Over");
            });
        }
    }

    @Override
    public void tickPerformed() {
        javafx.application.Platform.runLater(this::repaint);
    }

    @Override
    public void playerActionStateChanged(PlayerAction action, boolean isSet) {
        // DO NOTNING
    }

}
