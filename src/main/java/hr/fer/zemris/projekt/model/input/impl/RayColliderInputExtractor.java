package hr.fer.zemris.projekt.model.input.impl;

import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.input.GameInputExtractor;
import hr.fer.zemris.projekt.model.objects.BoundingBox2D;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.impl.Barrel;
import hr.fer.zemris.projekt.model.objects.impl.Ladder;
import hr.fer.zemris.projekt.model.objects.impl.Platform;
import hr.fer.zemris.projekt.model.objects.impl.Player;
import hr.fer.zemris.projekt.model.raycollider.RayCollider;
import hr.fer.zemris.projekt.model.raycollider.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class RayColliderInputExtractor implements GameInputExtractor {

    private final double MAX_DISTANCE = 1E6;

    private final int numberOfRays;

    public RayColliderInputExtractor(int numberOfRays) {
        this.numberOfRays = numberOfRays;
    }

    @Override
    public int getNumberOfInputs() {
        return 2 * numberOfRays;
    }

    @Override
    public void extractInputs(GameController controller, double[] extractToThis) {
        if (extractToThis.length != getNumberOfInputs()) {
            throw new IllegalArgumentException("Invalid size of argument array.");
        }

        List<RayCollider.Collision> collisions = calculateCollisions(controller);
        int inputIndex = 0;

        for (RayCollider.Collision collision : collisions) {
            if (collision != null) {
                extractToThis[inputIndex] = getOrdinalNumberOfGameObject(collision.getObject().getClass());
                extractToThis[inputIndex + 1] = collision.getDistance();
            }

            inputIndex += 2;
        }
    }

    public List<RayCollider.Collision> calculateCollisions(GameController controller) {
        Player player = (Player) controller
                .getGameObjects()
                .stream()
                .filter(obj -> obj instanceof Player)
                .findAny()
                .get();

        BoundingBox2D boundingBox = player.getBoundingBox();

        Vector2D playersCenter = new Vector2D(
                boundingBox.getX() + boundingBox.getWidth() / 2,
                boundingBox.getY() - boundingBox.getHeight() / 2
        );

        double angleBetweenRays = 2 * Math.PI / numberOfRays;

        List<RayCollider.Collision> collisions = new ArrayList<>();

        for (int rayIndex = 0; rayIndex < numberOfRays; rayIndex++) {
            Vector2D rayVector = new Vector2D(Math.cos(rayIndex * angleBetweenRays), Math.sin(rayIndex * angleBetweenRays));

            List<RayCollider.Collision> closestCollisions = RayCollider.raycast(controller, playersCenter, rayVector, MAX_DISTANCE);
            if(closestCollisions.size()==0) {
            	collisions.add(null);
            	continue;
            }
            
            RayCollider.Collision collision = closestCollisions.get(0);
            for(var c : closestCollisions) {
            	// => platform has priority over barrel and barrel has priority over ladders
            	if(getOrdinalNumberOfGameObject(collision.getObject().getClass()) > getOrdinalNumberOfGameObject(c.getObject().getClass()))
            		collision = c;
            }
            
            collisions.add(collision);
        }

        return collisions;
    }

    public static double getOrdinalNumberOfGameObject(Class<? extends Game2DObject> clazz) {
        if (clazz == Barrel.class) return 1;
        if (clazz == Ladder.class) return 2;
        if (clazz == Platform.class) return 3;
        if (clazz == Player.class) return 4;

        throw new IllegalArgumentException("Could not recognise game object.");
    }

}
