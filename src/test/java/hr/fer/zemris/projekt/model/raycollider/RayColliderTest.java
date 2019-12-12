package hr.fer.zemris.projekt.model.raycollider;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.controller.impl.GameControllerImpl;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.impl.BoundingBox2DImpl;
import hr.fer.zemris.projekt.model.objects.impl.Ladder;
import hr.fer.zemris.projekt.model.objects.impl.Player;
import hr.fer.zemris.projekt.model.raycollider.RayCollider.Collision;

class RayColliderTest {
	
	static GameController gc;
	
	@BeforeAll
	static void beforeAll() {
		
		List<Game2DObject> objs = new ArrayList<>();
		
		objs.add(new Ladder(new BoundingBox2DImpl(100, 100, 10, 10)));
		objs.add(new Ladder(new BoundingBox2DImpl(200, 100, 10, 10)));
		objs.add(new Ladder(new BoundingBox2DImpl(100, 0, 10, 10)));
		objs.add(new Ladder(new BoundingBox2DImpl(200, 0, 10, 10)));
		
		objs.add(new Ladder(new BoundingBox2DImpl(150, 100, 10, 10)));
		objs.add(new Ladder(new BoundingBox2DImpl(100, 50, 10, 10)));
		objs.add(new Ladder(new BoundingBox2DImpl(200, 50, 10, 10)));
		objs.add(new Ladder(new BoundingBox2DImpl(150, 0, 10, 10)));
		objs.add(new Ladder(new BoundingBox2DImpl(150, 0, 10, 10)));
		
		objs.add(new Ladder(new BoundingBox2DImpl(150, 150, 10, 10)));
		
		objs.add(new Ladder(new BoundingBox2DImpl(145, 55, 20, 20)));
		
		gc = new GameControllerImpl(new Player(new BoundingBox2DImpl(50, 50, 20, 20), 0, 0, "Player1"), 
				objs, new GameControllerImpl.GameParameters(60, 100, 0.5, 10, 10, 10, 10, 10));
	}

	@Test
	void testCollinearDistanceTooShort() {
		assertEquals(0, RayCollider.raycast(gc, new Vector2D(150, 50), new Vector2D(0, 1), 39).size());
	}
	
	@Test
	void testCollinear() {
		Collision c = RayCollider.raycast(gc, new Vector2D(150, 50), new Vector2D(0, 1), 50).get(0);
		assertEquals(40, c.getDistance());
		assertEquals(150, c.getObject().getBoundingBox().getX());
		assertEquals(100, c.getObject().getBoundingBox().getY());
	}
	
	@Test
	void testTwoCollisions() {
		Collision c = RayCollider.raycast(gc, new Vector2D(155, 50), new Vector2D(0, 0.5), 1000).get(0);
		assertEquals(40, c.getDistance());
		assertEquals(150, c.getObject().getBoundingBox().getX());
		assertEquals(100, c.getObject().getBoundingBox().getY());
	}
	
	@Test
	void testTwoClosestCollisions() {
		List<Collision> c = RayCollider.raycast(gc, new Vector2D(155, 50), new Vector2D(0, -1), 1000);
		assertEquals(2, c.size());
		assertEquals(50, c.get(0).getDistance());
		assertEquals(50, c.get(0).getDistance());
	}
	
	@Test
	void testOneCollisionAngle() {
		Collision c = RayCollider.raycast(gc, new Vector2D(155, 50), new Vector2D(0.5, -0.5), 1000).get(0);
		assertEquals(50*Math.sqrt(2), c.getDistance());
		assertEquals(200, c.getObject().getBoundingBox().getX());
		assertEquals(0, c.getObject().getBoundingBox().getY());
	}

}
