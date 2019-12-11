package hr.fer.zemris.projekt.algorithm.player;

import hr.fer.zemris.projekt.model.controller.PlayerAction;
import hr.fer.zemris.projekt.model.input.RayColliderInputExtractor;
import hr.fer.zemris.projekt.model.objects.impl.Ladder;
import hr.fer.zemris.projekt.model.objects.impl.Platform;

public class ClimbNearestLadderPlayer implements ArtificialPlayer {

    private final static double LADDER_WIDTH = 40;

    @Override
    public PlayerAction calculateAction(double[] input) {
        if (input.length != 8) {
            throw new IllegalArgumentException("Artificial player expects input to contain exactly 8 numbers.");
        }

        // 0, 1, 2, 3 => right, up, left, down
        double minimalDistanceFromLadder = Double.MAX_VALUE;
        int indexOfMinimum = -1;

        double ladderType = RayColliderInputExtractor.getOrdinalNumberOfGameObject(Ladder.class);
        double platformType = RayColliderInputExtractor.getOrdinalNumberOfGameObject(Platform.class);

        for (int typeIndex = 0; typeIndex < input.length; typeIndex += 2) {
            if (input[typeIndex] == ladderType && input[typeIndex + 1] < minimalDistanceFromLadder) {
                minimalDistanceFromLadder = input[typeIndex + 1];
                indexOfMinimum = typeIndex / 2;
            }
        }

        // If it is *completely inside* a ladder, go up
        if (input[0] == ladderType && input[1] < 22.5 &&
            input[4] == ladderType && input[5] < 22.5) {
            return PlayerAction.UP;
        }

        // If its center has gone out of the ladder and inside a platform, go up since it cannot go left or right yet
        if (input[2] != ladderType && input[6] == ladderType ||
            input[6] == platformType && input[7] < 25) {
            return PlayerAction.UP;
        }

        // If there is a ladder on top of it, it is inside the ladder, so it should center itself so it can go up
        if (input[2] == ladderType) {
            return indexOfMinimum == 0 ? PlayerAction.LEFT : PlayerAction.RIGHT;
        }

        // Otherwise go to the closest ladder
        if (indexOfMinimum == 0) {
            return PlayerAction.RIGHT;
        }

        if (indexOfMinimum == 2) {
            return PlayerAction.LEFT;
        }

        // No more ladders
        return PlayerAction.JUMP;
    }

}