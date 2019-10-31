package hr.fer.zemris.projekt.model.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import hr.fer.zemris.projekt.model.GameController;
import hr.fer.zemris.projekt.model.ModelToScreenCoordinateConvertor;
import hr.fer.zemris.projekt.model.ModelToScreenCoordinateConvertor.DoublePoint;
import hr.fer.zemris.projekt.model.ModelToScreenCoordinateConvertor.LongPoint;
import hr.fer.zemris.projekt.model.GameController.GameParameters;
import hr.fer.zemris.projekt.model.GameController.PlayerAction;
import hr.fer.zemris.projekt.model.objects.BoundingBox;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.Game2DObjectListener;
import hr.fer.zemris.projekt.model.objects.Player;

@SuppressWarnings("serial")
public class GamePhysicsTest extends JFrame implements Game2DObjectListener {
	
	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;
	
	private Player p;
	private int playerWidth = 25, playerHeight = 50;
	private GameController gc;
	private GameParameters params;
	private ModelToScreenCoordinateConvertor convertor;
	
	private DoublePoint old;	
	
	public GamePhysicsTest() {
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		init();
		repaint();
	}
	
	private void init() {
		params = new GameParameters(10, 10, 30, 10, 1.5, 0, 5, 0, 0, 0);
		
		old = new DoublePoint();
		old.x = 2.5;
		old.y = 2.5;
		p = new Player(new BoundingBox(old.x, old.y, playerWidth, playerHeight), 0, 0, "Player1");
		p.setOnGround(true);
		System.out.println(p.getPosition());
		
		gc = new GameController(p, new ArrayList<>(), params);
		p.addListener(this);
		convertor = new ModelToScreenCoordinateConvertor(0, params.getVisibleScreenWidth(), 
				0, params.getVisibleScreenHeight(), 
				0, WIDTH, 0, HEIGHT);
		
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				
				if(key == KeyEvent.VK_A) {
					gc.setPlayerAction(PlayerAction.LEFT, false);
				} else if(key == KeyEvent.VK_D) {
					gc.setPlayerAction(PlayerAction.RIGHT, false);
				} else if(key == KeyEvent.VK_SPACE) {
					gc.setPlayerAction(PlayerAction.JUMP, false);
				} else if(key == KeyEvent.VK_D) {
					gc.setPlayerAction(PlayerAction.DOWN, false);
				} else if(key == KeyEvent.VK_W) {
					gc.setPlayerAction(PlayerAction.UP, false);
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				
				if(key == KeyEvent.VK_A) {
					gc.setPlayerAction(PlayerAction.LEFT, true);
				} else if(key == KeyEvent.VK_D) {
					gc.setPlayerAction(PlayerAction.RIGHT, true);
				} else if(key == KeyEvent.VK_SPACE) {
					gc.setPlayerAction(PlayerAction.JUMP, true);
				} else if(key == KeyEvent.VK_D) {
					gc.setPlayerAction(PlayerAction.DOWN, true);
				} else if(key == KeyEvent.VK_W) {
					gc.setPlayerAction(PlayerAction.UP, true);
				}
			}
			
		});
		
		new Thread(() -> {
			while(true) {
				gc.tick();
				try {
					Thread.sleep((int) (1000.0/params.getFps()));
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void positionChanged(Game2DObject source) {
		repaint();
		if(p.getPosition().getY()<=2.5) p.setOnGround(true);
		System.out.println(p.getPosition());
	}

	@Override
	public void objectDestroyed(Game2DObject source) {
		this.dispose();
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		LongPoint oldScreen = convertor.convert(old);
		g2d.setColor(Color.WHITE);
		g2d.fillRect((int)oldScreen.x, (int)oldScreen.y, 25, 50);
		
		DoublePoint newPosition = new DoublePoint();
		newPosition.x = p.getPosition().getX();
		newPosition.y = p.getPosition().getY();
		LongPoint newScreen = convertor.convert(newPosition);
		g2d.setColor(Color.BLACK);
		g2d.fillRect((int)newScreen.x, (int)newScreen.y, 
				(int)Math.round(p.getPosition().getWidth()), (int)Math.round(p.getPosition().getHeight()));
		old = newPosition;
	}
	
	public static void main(String[] args) {
		
		GamePhysicsTest frame = new GamePhysicsTest();
		frame.setVisible(true);
		
	}

}
