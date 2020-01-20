package hr.fer.zemris.projekt.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import hr.fer.zemris.projekt.model.ModelToScreenCoordinateConvertor;
import hr.fer.zemris.projekt.model.ModelToScreenCoordinateConvertor.DoublePoint;
import hr.fer.zemris.projekt.model.ModelToScreenCoordinateConvertor.LongPoint;
import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.controller.GameControllerListener;
import hr.fer.zemris.projekt.model.controller.PlayerAction;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl.GameParameters;
import hr.fer.zemris.projekt.model.input.impl.RayColliderInputExtractor;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.impl.Barrel;
import hr.fer.zemris.projekt.model.objects.impl.BoundingBox2DImpl;
import hr.fer.zemris.projekt.model.objects.impl.Ladder;
import hr.fer.zemris.projekt.model.objects.impl.Platform;
import hr.fer.zemris.projekt.model.objects.impl.Player;
import hr.fer.zemris.projekt.model.raycollider.RayCollider;
import hr.fer.zemris.projekt.model.raycollider.RayCollider.Collision;

@SuppressWarnings("serial")
public class GamePhysicsTest extends JPanel implements GameControllerListener {
	
	public static void main(String[] args) {

		var gpt = new GamePhysicsTest();
		JFrame frame = new JFrame();		
		GameController gc = gpt.gc;
		frame.addKeyListener(new KeyListener() { 

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();

				if (key == KeyEvent.VK_A) {
					gc.unsetPlayerAction(PlayerAction.LEFT);
				} else if (key == KeyEvent.VK_D) {
					gc.unsetPlayerAction(PlayerAction.RIGHT);
				} else if (key == KeyEvent.VK_SPACE) {
					gc.unsetPlayerAction(PlayerAction.JUMP);
				} else if (key == KeyEvent.VK_S) {
					gc.unsetPlayerAction(PlayerAction.DOWN);
				} else if (key == KeyEvent.VK_W) {
					gc.unsetPlayerAction(PlayerAction.UP);
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();

				if (key == KeyEvent.VK_A) {
					gc.setPlayerAction(PlayerAction.LEFT);
				} else if (key == KeyEvent.VK_D) {
					gc.setPlayerAction(PlayerAction.RIGHT);
				} else if (key == KeyEvent.VK_SPACE) {
					gc.setPlayerAction(PlayerAction.JUMP);
				} else if (key == KeyEvent.VK_S) {
					gc.setPlayerAction(PlayerAction.DOWN);
				} else if (key == KeyEvent.VK_W) {
					gc.setPlayerAction(PlayerAction.UP);
				}
			}

		});
		
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

	public GamePhysicsTest() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);

		init();
		repaint();
	}

	private void init() {
		params = new GameParameters(60, 1000, 0.5, 100, 100, 300, 75, 75);

		p = new Player(new BoundingBox2DImpl(250, 480, playerWidth, playerHeight), 0, 0, "Player1");

		objects = new ArrayList<>();
		objects.add(new Platform(new BoundingBox2DImpl(100, 50, 420, 20)));		
		
		gc = new GameControllerImpl(p, objects, params);
		objects = null;
		gc.addListener(this);
		convertor = new ModelToScreenCoordinateConvertor(0, WIDTH, 0,
				HEIGHT, 0, WIDTH, 0, HEIGHT);
		
		new Thread(() -> {
			try {
			Thread.sleep((long) (1_000.0));
			gc.addGameObject(new Platform(new BoundingBox2DImpl(100, 175, 420, 20)));
			Thread.sleep((long) (1_000.0));
			gc.addGameObject(new Platform(new BoundingBox2DImpl(100, 300, 420, 20)));
			Thread.sleep((long) (1_000.0));
			gc.addGameObject(new Platform(new BoundingBox2DImpl(100, 425, 420, 20)));
			Thread.sleep((long) (1_000.0));
			gc.addGameObject(new Ladder(new BoundingBox2DImpl(150, 175, 35, 125)));
			Thread.sleep((long) (1_000.0));
			gc.addGameObject(new Ladder(new BoundingBox2DImpl(400, 175, 35, 125)));
			Thread.sleep((long) (1_000.0));
			gc.addGameObject(new Ladder(new BoundingBox2DImpl(220, 300, 35, 125)));
			Thread.sleep((long) (1_000.0));
			gc.addGameObject(new Ladder(new BoundingBox2DImpl(245, 425, 35, 125)));
			Thread.sleep((long) (1_000.0));
			gc.addGameObject(new Barrel(new BoundingBox2DImpl(320, 320, 20, 20), -75, 0));
			gc.addGameObject(new Barrel(new BoundingBox2DImpl(250, 320, 20, 20), 75, 0));
			gc.addGameObject(new Barrel(new BoundingBox2DImpl(320, 320, 20, 20), -75, 0));
			gc.addGameObject(new Barrel(new BoundingBox2DImpl(250, 320, 20, 20), 75, 0));
//			p.setY(480);
			} catch(InterruptedException ex) {
			}

			while (true) {
				try {
					Thread.sleep((long) (3_000.0));
				} catch (InterruptedException ex) {
				}
				gc.addGameObject(new Barrel(new BoundingBox2DImpl(350, 380, 20, 20), 0, 0));
				gc.addGameObject(new Barrel(new BoundingBox2DImpl(350, 270, 20, 20), 0, 0));
				gc.addGameObject(new Barrel(new BoundingBox2DImpl(250, 380, 20, 20), 0, 0));
				gc.addGameObject(new Barrel(new BoundingBox2DImpl(250, 270, 20, 20), 0, 0));
				try {
					Thread.sleep((long) (0_500.0));
				} catch (InterruptedException ex) {
				}
				gc.addGameObject(new Barrel(new BoundingBox2DImpl(450, 280, 20, 20), 0, 0));
				gc.addGameObject(new Barrel(new BoundingBox2DImpl(450, 170, 20, 20), 0, 0));
				gc.addGameObject(new Barrel(new BoundingBox2DImpl(350, 280, 20, 20), 0, 0));
				gc.addGameObject(new Barrel(new BoundingBox2DImpl(350, 170, 20, 20), 0, 0));

			}

		}).start();
		
		System.out.println("Game started with parameters: ");
		System.out.println(params);
		
		gc.start();  
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
