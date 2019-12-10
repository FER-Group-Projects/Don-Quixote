package hr.fer.zemris.projekt.model.raycollider;

import java.util.ArrayList;
import java.util.List;
import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.objects.BoundingBox2D;
import hr.fer.zemris.projekt.model.objects.Game2DObject;

public class RayCollider {
	
	public static List<Collider> raycastAll(GameController gc, Vector2D origin, Vector2D direction, double maxDistance) {
		return raycastAll(gc, origin, direction, maxDistance, true);
	}
	
	// looking for intersection of two line segments :
	// first one is from p to p+r (where p is origin of the ray, and r is normalized direction of the ray scaled by maxDistance)
	// second one is (for each game object in gc) from q to q+s (where q is origin of the vector and and r 
	// 													is normalized direction scaled by length of that side of the rectangle)
	public static List<Collider> raycastAll(GameController gc, Vector2D origin, Vector2D direction, double maxDistance, boolean ignoreOriginCollider) {
		
		if(gc==null || origin == null || direction == null)
			throw new NullPointerException();
		if(maxDistance == Double.POSITIVE_INFINITY || maxDistance <= 0)
			throw new IllegalArgumentException();
		
		List<Collider> colliders = new ArrayList<>();
		
		Vector2D p = origin; // point1 of ray
		Vector2D r = direction.normalized().scale(maxDistance); // direction of ray
		
		List<Game2DObject> objects = gc.getGameObjects();
		
		for(Game2DObject obj : objects) {
			
			BoundingBox2D bb = obj.getBoundingBox();
			
			double distance = Double.POSITIVE_INFINITY;
			
			if(bb.isInBoundingBox(origin.getX(), origin.getY())) {
				
				distance = 0;
				
			} else {
			
				// left side
				Vector2D q = new Vector2D(bb.getX(), bb.getY()); // point1 of line segment
				Vector2D s = new Vector2D(0, -bb.getHeight()); // point2 of line segment
				distance = Math.min(distance, distanceFromPToLineSegmentIntersection(p, r, q, s));
				
				// right side
				q = new Vector2D(bb.getX() + bb.getWidth(), bb.getY()); // point1 of line segment
				s = new Vector2D(0, -bb.getHeight()); // point2 of line segment
				distance = Math.min(distance, distanceFromPToLineSegmentIntersection(p, r, q, s));
				
				// top side
				q = new Vector2D(bb.getX(), bb.getY()); // point1 of line segment
				s = new Vector2D(bb.getWidth(), 0); // point2 of line segment
				distance = Math.min(distance, distanceFromPToLineSegmentIntersection(p, r, q, s));
							
				// bottom side
				q = new Vector2D(bb.getX(), bb.getY() - bb.getHeight()); // point1 of line segment
				s = new Vector2D(bb.getWidth(), 0); // point2 of line segment
				distance = Math.min(distance, distanceFromPToLineSegmentIntersection(p, r, q, s));
			
			}
			
			if(distance == 0 && !ignoreOriginCollider || distance != 0 && distance != Double.POSITIVE_INFINITY) {
				colliders.add(new Collider(obj, distance));
			}
			
		}
		
		return colliders;
		
	}
	
	public static Collider raycast(GameController gc, Vector2D origin, Vector2D direction, double maxDistance) {
		return raycast(gc, origin, direction, maxDistance, true);
	}
	
	public static Collider raycast(GameController gc, Vector2D origin, Vector2D direction, double maxDistance, boolean ignoreOriginCollider) {
		
		List<Collider> colliders = raycastAll(gc, origin, direction, maxDistance, ignoreOriginCollider);
		if(colliders.size() == 0) return null;
		
		Collider closest = colliders.get(0);
		
		for(Collider c : colliders) {
			if(c.getDistance() < closest.getDistance())
				closest = c;
		}
		
		return closest;
		
	}
	
	private static double distanceFromPToLineSegmentIntersection(Vector2D p, Vector2D r, Vector2D q, Vector2D s) {
		if(r.exterior(s) == 0 && q.sub(p).exterior(r) == 0) {
			// collinear
			double t0 = q.sub(p).dot(r) / r.dot(r);
			double t1 = q.add(s).sub(p).dot(r) / r.dot(r);
			
			if(t0 >= 0 && t0 <= 1 || t1 >= 0 && t1 <= 1 ||
					t0 <= 0 && t1 >= 1 || t1 <=0 && t0 >= 1) // then they intersect
				return Math.min(t0*r.norm(), t1*r.norm());
			
		} else if(r.exterior(s) == 0 && q.sub(p).exterior(r) != 0) {
			// parallel
			// => no intersection
		} else if(r.exterior(s) != 0) {
			double t = q.sub(p).exterior(s) / r.exterior(s);
			double u = q.sub(p).exterior(r) / r.exterior(s);
			
			if(t >= 0 && t <= 1 && u >= 0 && u <= 1) {
				Vector2D intersection = p.add(r.scale(t));
				return intersection.sub(p).norm();
			}
		} else {
			// no intersection
		}
		return Double.POSITIVE_INFINITY;
	}
	
	public static class Collider {
		
		private Game2DObject object;
		private double distance;
		
		public Collider(Game2DObject object, double distance) {
			this.object = object;
			this.distance = distance;
		}
		
		public Game2DObject getObject() {
			return object;
		}
		
		public double getDistance() {
			return distance;
		}

		@Override
		public String toString() {
			return "Collider [object=" + object + ", distance=" + distance + "]";
		}
		
	}

}
