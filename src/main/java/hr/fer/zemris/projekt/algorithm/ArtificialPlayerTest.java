package hr.fer.zemris.projekt.algorithm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import hr.fer.zemris.projekt.algorithm.player.ArtificialPlayer;
import hr.fer.zemris.projekt.algorithm.player.ClimbNearestLadderPlayer;
import hr.fer.zemris.projekt.model.ModelToScreenCoordinateConvertor;
import hr.fer.zemris.projekt.model.ModelToScreenCoordinateConvertor.DoublePoint;
import hr.fer.zemris.projekt.model.ModelToScreenCoordinateConvertor.LongPoint;
import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.controller.GameControllerListener;
import hr.fer.zemris.projekt.model.controller.PlayerAction;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl.GameParameters;
import hr.fer.zemris.projekt.model.input.GameInputExtractor;
import hr.fer.zemris.projekt.model.input.impl.RayColliderInputExtractor;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.impl.Barrel;
import hr.fer.zemris.projekt.model.objects.impl.BoundingBox2DImpl;
import hr.fer.zemris.projekt.model.objects.impl.Ladder;
import hr.fer.zemris.projekt.model.objects.impl.Platform;
import hr.fer.zemris.projekt.model.objects.impl.Player;
import hr.fer.zemris.projekt.model.raycollider.RayCollider;
import hr.fer.zemris.projekt.model.raycollider.RayCollider.Collision;
import hr.fer.zemris.projekt.model.scenes.ClimbingScene;

@SuppressWarnings("serial")
public class ArtificialPlayerTest extends JPanel implements GameControllerListener {

    public static void main(String[] args) {
        show(new ClimbNearestLadderPlayer());
    }

    public static void show(ArtificialPlayer artificialPlayer) {
        var gpt = new ArtificialPlayerTest(artificialPlayer);
        JFrame frame = new JFrame();

        frame.add(gpt);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

    private Player p;
    private List<Game2DObject> objects;
    private int playerWidth = 25, playerHeight = 50;
    private GameController gc;
    private GameParameters params;
    private ModelToScreenCoordinateConvertor convertor;
    private ArtificialPlayer artificialPlayer;

    public ArtificialPlayerTest(ArtificialPlayer artificialPlayer) {
        this.artificialPlayer = artificialPlayer;

            Dimension size = new Dimension(WIDTH, HEIGHT);
            this.setMinimumSize(size);
            this.setPreferredSize(size);
            this.setMaximumSize(size);

            init();
            repaint();
        }

        private void init() {
            params = new GameParameters(60, 1000, 0.5, 100, 100, 300, 75, 75);

            p = new Player(new BoundingBox2DImpl(250, 110, playerWidth, playerHeight), 0, 0, "Player1");

            objects = new ArrayList<>();
            objects.add(new Platform(new BoundingBox2DImpl(100, 50, 420, 20)));

            gc = new GameControllerImpl(p, objects, params);
            gc.addGameObject(new Platform(new BoundingBox2DImpl(100, 175, 420, 20)));
            gc.addGameObject(new Platform(new BoundingBox2DImpl(100, 300, 420, 20)));
            gc.addGameObject(new Platform(new BoundingBox2DImpl(100, 425, 420, 20)));
            gc.addGameObject(new Ladder(new BoundingBox2DImpl(150, 155, 35, 105)));
            gc.addGameObject(new Ladder(new BoundingBox2DImpl(400, 155, 35, 105)));
            gc.addGameObject(new Ladder(new BoundingBox2DImpl(220, 280, 35, 105)));
            gc.addGameObject(new Ladder(new BoundingBox2DImpl(245, 405, 35, 105)));

            objects = null;
            gc.addListener(this);
            convertor = new ModelToScreenCoordinateConvertor(0, WIDTH, 0,
                    HEIGHT, 0, WIDTH, 0, HEIGHT);

            GameInputExtractor inputExtractor = new RayColliderInputExtractor(4);

            new Thread(() -> {
                try {
                    Thread.sleep(3_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                PlayerAction previousAction = PlayerAction.UP;
                int tick = 0;

                while (true) {
                    gc.tick();

                    ++tick;
                    tick %= 10;

                    try {
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (tick != 1) continue;

                    gc.unsetPlayerAction(previousAction);
                    previousAction = artificialPlayer.calculateAction(inputExtractor.extractInputs(gc));
                    gc.setPlayerAction(previousAction);

                    System.out.println(previousAction);
                }

            }).start();

            System.out.println("Game started with parameters:");
            System.out.println(params);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, WIDTH, HEIGHT);

            for (Game2DObject obj : gc.getGameObjects()) {
                DoublePoint newPosition = new DoublePoint();
                newPosition.x = obj.getBoundingBox().getX();
                newPosition.y = obj.getBoundingBox().getY();
                LongPoint newScreen = convertor.convert(newPosition);

                Color inner = null, outer = null;
                if (obj instanceof Platform) {
                    inner = Color.CYAN;
                    outer = Color.BLUE;
                } else if (obj instanceof Ladder) {
                    inner = Color.PINK;
                    outer = Color.RED;
                } else if (obj instanceof Barrel) {
                    inner = Color.MAGENTA;
                    outer = Color.RED;
                } else if (obj instanceof Player) {
                    inner = Color.GRAY;
                    outer = Color.DARK_GRAY;
                } else {
                    continue;
                }

                g2d.setColor(inner);
                g2d.fillRect((int) newScreen.x, (int) newScreen.y, (int) convertor.scaleWidth(obj.getBoundingBox().getWidth()),
                        (int) convertor.scaleHeight(obj.getBoundingBox().getHeight()));
                g2d.setColor(outer);
                g2d.drawRect((int) newScreen.x, (int) newScreen.y, (int) convertor.scaleWidth(obj.getBoundingBox().getWidth()),
                        (int) convertor.scaleHeight(obj.getBoundingBox().getHeight()));
            }

            RayColliderInputExtractor inputExtractor = new RayColliderInputExtractor(4);

            List<RayCollider.Collision> allCollisions = inputExtractor.calculateCollisions(gc);

            // Map : object with which any ray collides -> collisionDescriptor
            Map<Game2DObject, Collision> filteredClosestCollisions = new HashMap<>();

            for(var collision : allCollisions) {
                if(collision==null) continue;
                Collision oldC = filteredClosestCollisions.get(collision.getObject());
                if(oldC==null || oldC!=null && collision.getDistance() < oldC.getDistance())
                    filteredClosestCollisions.put(collision.getObject(), collision);
            }

            for (RayCollider.Collision collision : filteredClosestCollisions.values()) {
                if (collision == null) continue;

                Game2DObject obj = collision.getObject();
                DoublePoint newPosition = new DoublePoint();
                newPosition.x = obj.getBoundingBox().getX();
                newPosition.y = obj.getBoundingBox().getY();
                LongPoint newScreen = convertor.convert(newPosition);
                g2d.setColor(Color.BLACK);
                g2d.drawString(String.valueOf((int) collision.getDistance()), (int) newScreen.x, (int) newScreen.y);

                DoublePoint origin = new DoublePoint();
                origin.x = collision.getRayOrigin().getX();
                origin.y = collision.getRayOrigin().getY();
                LongPoint originL = convertor.convert(origin);
                DoublePoint collisionPoint = new DoublePoint();
                collisionPoint.x = collision.getPoint().getX();
                collisionPoint.y = collision.getPoint().getY();
                LongPoint collisionPointL = convertor.convert(collisionPoint);
                g2d.setColor(Color.GREEN);
                g2d.drawLine((int)originL.x, (int)originL.y, (int)collisionPointL.x, (int)collisionPointL.y);

            }
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
            if(object.getBoundingBox().getX() + object.getBoundingBox().getWidth() <= 0 ||
                    object.getBoundingBox().getX() >= WIDTH ||
                    object.getBoundingBox().getY() <= 0 ||
                    object.getBoundingBox().getY() - object.getBoundingBox().getHeight() >= HEIGHT)
                object.destroy();
        }

        @Override
        public void gameObjectDestroyed(Game2DObject object) {
            if(object instanceof Player) {
                System.out.println("Game Over");
                System.exit(0);
            }
        }

        @Override
        public void tickPerformed() {
            SwingUtilities.invokeLater(() -> repaint());
        }

        @Override
        public void playerActionStateChanged(PlayerAction action, boolean isSet) {
            // DO NOTHING
        }

    }
