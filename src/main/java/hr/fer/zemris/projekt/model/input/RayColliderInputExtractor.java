package hr.fer.zemris.projekt.model.input;

import hr.fer.zemris.projekt.model.controller.GameController;
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

        List<RayCollider.Collider> colliders = calculateColliders(controller);
        int inputIndex = 0;

        for (RayCollider.Collider collider : colliders) {
            if (collider != null) {
                extractToThis[inputIndex] = getOrdinalNumberOfGameObject(collider.getObject());
                extractToThis[inputIndex] = collider.getDistance();
            }

            inputIndex += 2;
        }
    }

    public List<RayCollider.Collider> calculateColliders(GameController controller) {
        Player player = (Player) controller
                .getGameObjects()
                .stream()
                .filter(obj -> obj instanceof Player)
                .findAny()
                .get();

        BoundingBox2D boundingBox = player.getBoundingBox();

        Vector2D playersCenter = new Vector2D(
                boundingBox.getX() + (double) boundingBox.getWidth() / 2,
                boundingBox.getY() - (double) boundingBox.getHeight() / 2
        );

        double angleBetweenRays = 2 * Math.PI / numberOfRays;

        List<RayCollider.Collider> colliders = new ArrayList<>();

        for (int rayIndex = 0; rayIndex < numberOfRays; rayIndex++) {
            Vector2D rayVector = new Vector2D(Math.cos(rayIndex * angleBetweenRays), Math.sin(rayIndex * angleBetweenRays));

            colliders.add(RayCollider.raycast(controller, playersCenter, rayVector, MAX_DISTANCE, true));
        }

        return colliders;
    }

    private double getOrdinalNumberOfGameObject(Game2DObject object) {
        if (object instanceof Barrel) return 1;
        if (object instanceof Ladder) return 2;
        if (object instanceof Platform) return 3;
        if (object instanceof Player) return 4;

        throw new IllegalArgumentException("Could not recognise game object.");
    }

}
