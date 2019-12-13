package hr.fer.zemris.projekt.model.scenes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import hr.fer.zemris.projekt.model.ModelToScreenCoordinateConvertor;
import hr.fer.zemris.projekt.model.ModelToScreenCoordinateConvertor.DoublePoint;
import hr.fer.zemris.projekt.model.ModelToScreenCoordinateConvertor.LongPoint;
import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.controller.GameControllerListener;
import hr.fer.zemris.projekt.model.controller.PlayerAction;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.impl.Barrel;
import hr.fer.zemris.projekt.model.objects.impl.Ladder;
import hr.fer.zemris.projekt.model.objects.impl.Platform;
import hr.fer.zemris.projekt.model.objects.impl.Player;

@SuppressWarnings("serial")
public class ClimbingSceneTest extends JPanel implements GameControllerListener {
	
	public static void main(String[] args) {

		var gpt = new ClimbingSceneTest();
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

	private int playerWidth = 25, playerHeight = 50;
	private GameController gc;
	private ModelToScreenCoordinateConvertor convertor;

	public ClimbingSceneTest() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);

		init();
		repaint();
	}

	private void init() {
		gc = new ClimbingScene(60, 1000, 0.5, 100, 100, 300, 75, 75, playerWidth, playerHeight, 420, 20, 35).generateScene();
		gc.addListener(this);
		convertor = new ModelToScreenCoordinateConvertor(0, WIDTH, 0,
				HEIGHT, 0, WIDTH, 0, HEIGHT);		
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
