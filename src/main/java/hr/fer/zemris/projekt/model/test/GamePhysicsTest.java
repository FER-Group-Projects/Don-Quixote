package hr.fer.zemris.projekt.model.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import hr.fer.zemris.projekt.model.ModelToScreenCoordinateConvertor;
import hr.fer.zemris.projekt.model.ModelToScreenCoordinateConvertor.DoublePoint;
import hr.fer.zemris.projekt.model.ModelToScreenCoordinateConvertor.LongPoint;
import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.controller.GameController.PlayerAction;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl.GameParameters;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.Game2DObjectListener;
import hr.fer.zemris.projekt.model.objects.impl.Barrel;
import hr.fer.zemris.projekt.model.objects.impl.BoundingBox2DImpl;
import hr.fer.zemris.projekt.model.objects.impl.Ladder;
import hr.fer.zemris.projekt.model.objects.impl.Platform;
import hr.fer.zemris.projekt.model.objects.impl.Player;

@SuppressWarnings("serial")
public class GamePhysicsTest extends JFrame implements Game2DObjectListener {

	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;

	private Player p;
	private List<Game2DObject> objects;
	private int playerWidth = 25, playerHeight = 50;
	private GameController gc;
	private GameParameters params;
	private ModelToScreenCoordinateConvertor convertor;

	public GamePhysicsTest() {
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);

		init();
		repaint();
	}

	private void init() {
		params = new GameParameters(60, 1000, 0.7);

		p = new Player(new BoundingBox2DImpl(320, 200, playerWidth, playerHeight), 0, 0, 1.5 * 50, 1.5 * 50, "Player1", 5 * 50);
		System.out.println(p.getBoundingBox());

		objects = new ArrayList<>();
		objects.add(new Platform(new BoundingBox2DImpl(100, 50, 420, 20)));
		objects.add(new Platform(new BoundingBox2DImpl(100, 175, 420, 20)));
		objects.add(new Platform(new BoundingBox2DImpl(100, 300, 420, 20)));
		objects.add(new Ladder(new BoundingBox2DImpl(150, 155, 35, 105)));
		objects.add(new Ladder(new BoundingBox2DImpl(220, 280, 35, 105)));
		objects.add(new Ladder(new BoundingBox2DImpl(400, 155, 35, 105)));
		
		gc = new GameControllerImpl(p, objects, params);
		p.addListener(this);
		convertor = new ModelToScreenCoordinateConvertor(0, WIDTH, 0,
				HEIGHT, 0, WIDTH, 0, HEIGHT);

		this.addKeyListener(new KeyListener() {

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

		new Thread(() -> {
			while (true) {
				gc.tick();
				try {
					SwingUtilities.invokeAndWait(() -> repaint());
					Thread.sleep((long) (1000.0/params.getFps()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void boundingBoxChanged(Game2DObject source) {
//		repaint();
	}

	@Override
	public void objectDestroyed(Game2DObject source) {
		this.dispose();
	}

	boolean first = true;
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);

		DoublePoint newPosition = new DoublePoint();
		newPosition.x = p.getBoundingBox().getX();
		newPosition.y = p.getBoundingBox().getY();
		LongPoint newScreen = convertor.convert(newPosition);
		g2d.setColor(Color.BLACK);
		g2d.fillRect((int) newScreen.x, (int) newScreen.y, (int) Math.round(p.getBoundingBox().getWidth()),
				(int) Math.round(p.getBoundingBox().getHeight()));

		for (Game2DObject obj : objects) {
			newPosition = new DoublePoint();
			newPosition.x = obj.getBoundingBox().getX();
			newPosition.y = obj.getBoundingBox().getY();
			newScreen = convertor.convert(newPosition);
			if (obj instanceof Platform) {
				g2d.setColor(Color.BLACK);
				g2d.drawRect((int) newScreen.x, (int) newScreen.y, (int) obj.getBoundingBox().getWidth(),
						(int) obj.getBoundingBox().getHeight());
			} else if (obj instanceof Ladder) {
				g2d.setColor(Color.RED);
				g2d.drawRect((int) newScreen.x, (int) newScreen.y, (int) obj.getBoundingBox().getWidth(),
						(int) obj.getBoundingBox().getHeight());
			} else if (obj instanceof Barrel) {
				g2d.setColor(Color.RED);
				g2d.fillRect((int) newScreen.x, (int) newScreen.y, (int) obj.getBoundingBox().getWidth(),
						(int) obj.getBoundingBox().getHeight());
			}
		}

	}

	public static void main(String[] args) {

		GamePhysicsTest frame = new GamePhysicsTest();
		frame.setVisible(true);

	}

}
