package hr.fer.zemris.projekt.algorithm.player;

import hr.fer.zemris.projekt.model.controller.PlayerAction;
import hr.fer.zemris.projekt.model.input.impl.RayColliderInputExtractor;
import hr.fer.zemris.projekt.model.objects.impl.Ladder;
import hr.fer.zemris.projekt.model.objects.impl.Platform;

public class ClimbNearestLadderPlayer implements ArtificialPlayer {

    @Override
    public PlayerAction calculateAction(double[] input) {
        if (input.length != 8) {
            throw new IllegalArgumentException("Artificial player expects input to contain exactly 8 numbers.");
        }

        double ladderType = RayColliderInputExtractor.getOrdinalNumberOfGameObject(Ladder.class);
        double platformType = RayColliderInputExtractor.getOrdinalNumberOfGameObject(Platform.class);

        // If it is *completely inside* a ladder, go up
        if (input[0] == ladderType && input[1] > 12.5 && input[1] < 27.5 &&
            input[4] == ladderType && input[5] > 12.5 && input[5] < 27.5) {
            return PlayerAction.UP;
        }

        // If its center has gone out of the ladder and inside a platform, go up since it cannot go left or right yet
        if (input[2] != ladderType && input[6] == ladderType ||
        		input[6] == platformType && input[7] < 1 * 50 / 10) {
            return PlayerAction.UP;
        }

        // If there is a ladder on top of it, it is inside the ladder, so it should center itself so it can go up
        if (input[2] == ladderType) {
            return input[1] < input[5] ? PlayerAction.LEFT : PlayerAction.RIGHT;
        }
        
        if (input[0]==0 && input[4]==0) {
            return PlayerAction.JUMP;
        }

        // Otherwise go to the closest ladder
        if (input[0]==0 || input[4] != 0 && input[1] > input[5]) {
            return PlayerAction.LEFT;
        }

        if (input[4]==0 || input[0] != 0 && input[1] < input[5]) {
            return PlayerAction.RIGHT;
        }

        // If both are equally distant, go right
        if (input[1] == input[5] && input[1] != 0.0 && input[0] == ladderType && input[4] == ladderType) {
            return PlayerAction.RIGHT;
        }
        
        // No more ladders
        return PlayerAction.JUMP;

    }

}