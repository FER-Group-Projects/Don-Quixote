package hr.fer.zemris.projekt.model.raycollider;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.objects.BoundingBox2D;
import hr.fer.zemris.projekt.model.objects.Game2DObject;

public class RayCollider {
	
	public static List<Collision> raycastAll(GameController gc, Vector2D origin, Vector2D direction, double maxDistance) {
		return raycastAll(gc, origin, direction, maxDistance, true);
	}
	
	// looking for intersection of two line segments :
	// first one is from p to p+r (where p is origin of the ray, and r is normalized direction of the ray scaled by maxDistance)
	// second one is (for each game object in gc) from q to q+s (where q is origin of the vector and and r 
	// 													is normalized direction scaled by length of that side of the rectangle)
	public static List<Collision> raycastAll(GameController gc, Vector2D origin, Vector2D direction, double maxDistance, boolean ignoreOriginCollider) {
		
		if(gc==null || origin == null || direction == null)
			throw new NullPointerException();
		if(maxDistance == Double.POSITIVE_INFINITY || maxDistance <= 0)
			throw new IllegalArgumentException();
		
		List<Game2DObject> objects = gc.getGameObjects();
		
		Game2DObject originObject = null;
		if(ignoreOriginCollider) {
			originObject = calculateOriginObject(objects, origin);
		}
		
		List<Collision> collisions = new ArrayList<>();
		
		Vector2D p = origin; // point1 of ray
		Vector2D r = direction.normalized().scale(maxDistance); // direction of ray
		
		for(Game2DObject obj : objects) {
			
			if(obj == originObject) continue;
			
			BoundingBox2D bb = obj.getBoundingBox();
			
			double distance = Double.POSITIVE_INFINITY;
			Vector2D point = null;
			
			if(bb.containsPoint(origin.getX(), origin.getY())) {
				
				distance = 0;
				point = origin;
				
			} else {
			
				// left side
				Vector2D q = new Vector2D(bb.getX(), bb.getY()); // point1 of line segment
				Vector2D s = new Vector2D(0, -bb.getHeight()); // point2 of line segment
				double newDistance = distanceFromPToLineSegmentIntersection(p, r, q, s);
				if(newDistance < distance) {
					distance = newDistance;
					point = p.add(r.normalized().scale(distance));
				}
				
				// right side
				q = new Vector2D(bb.getX() + bb.getWidth(), bb.getY()); // point1 of line segment
				s = new Vector2D(0, -bb.getHeight()); // point2 of line segment
				newDistance = distanceFromPToLineSegmentIntersection(p, r, q, s);
				if(newDistance < distance) {
					distance = newDistance;
					point = p.add(r.normalized().scale(distance));
				}
				
				// top side
				q = new Vector2D(bb.getX(), bb.getY()); // point1 of line segment
				s = new Vector2D(bb.getWidth(), 0); // point2 of line segment
				newDistance = distanceFromPToLineSegmentIntersection(p, r, q, s);
				if(newDistance < distance) {
					distance = newDistance;
					point = p.add(r.normalized().scale(distance));
				}
							
				// bottom side
				q = new Vector2D(bb.getX(), bb.getY() - bb.getHeight()); // point1 of line segment
				s = new Vector2D(bb.getWidth(), 0); // point2 of line segment
				newDistance = distanceFromPToLineSegmentIntersection(p, r, q, s);
				if(newDistance < distance) {
					distance = newDistance;
					point = p.add(r.normalized().scale(distance));
				}
			
			}
			
			if(distance != Double.POSITIVE_INFINITY) {
				collisions.add(new Collision(obj, distance, point, origin, direction));
			}
			
		}
		
		return collisions;
		
	}
	
	private static Game2DObject calculateOriginObject(List<Game2DObject> gameObjects, Vector2D origin) {
		
		Game2DObject originObj = null;
		double originObjArea = Double.POSITIVE_INFINITY;
		
		for(var obj : gameObjects) {
			double area = obj.getBoundingBox().getWidth() * obj.getBoundingBox().getHeight();
			if(obj.getBoundingBox().containsPoint(origin.getX(), origin.getY()) && area < originObjArea) {
				originObj = obj;
				originObjArea = area;
			}
		}
		
		return originObj;
	}

	public static Collision raycast(GameController gc, Vector2D origin, Vector2D direction, double maxDistance) {
		return raycast(gc, origin, direction, maxDistance, true);
	}
	
	public static Collision raycast(GameController gc, Vector2D origin, Vector2D direction, double maxDistance, boolean ignoreOriginCollider) {
		
		List<Collision> collisions = raycastAll(gc, origin, direction, maxDistance, ignoreOriginCollider);
		if(collisions.size() == 0) return null;
		
		Collision closest = collisions.get(0);
		
		for(Collision c : collisions) {
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
	
	public static class Collision {
		
		private Game2DObject object;
		private double distance;
		private Vector2D point;
		
		private Vector2D rayOrigin;
		private Vector2D rayDirection;
		
		public Collision(Game2DObject object, double distance, Vector2D point, Vector2D rayOrigin,
				Vector2D rayDirection) {
			this.object = object;
			this.distance = distance;
			this.point = point;
			this.rayOrigin = rayOrigin;
			this.rayDirection = rayDirection;
		}

		public Game2DObject getObject() {
			return object;
		}
		
		public double getDistance() {
			return distance;
		}
		
		public Vector2D getPoint() {
			return point;
		}
		
		public Vector2D getRayOrigin() {
			return rayOrigin;
		}
		
		public Vector2D getRayDirection() {
			return rayDirection;
		}

		@Override
		public String toString() {
			return "Collider [object=" + object + ", distance=" + distance + "]";
		}
		
	}

}
