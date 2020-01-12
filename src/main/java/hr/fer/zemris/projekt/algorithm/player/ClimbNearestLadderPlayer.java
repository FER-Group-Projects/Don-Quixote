package hr.fer.zemris.projekt.algorithm.player;

import hr.fer.zemris.projekt.model.controller.PlayerAction;
import hr.fer.zemris.projekt.model.input.impl.RayColliderInputExtractor;
import hr.fer.zemris.projekt.model.objects.impl.Ladder;
import hr.fer.zemris.projekt.model.objects.impl.Platform;

import java.util.Arrays;

public class ClimbNearestLadderPlayer implements ArtificialPlayer {

    @Override
    public PlayerAction calculateAction(double[] input) {
        if (input.length != 8) {
            throw new IllegalArgumentException("Artificial player expects input to contain exactly 8 numbers.");
        }

        double ladderType = RayColliderInputExtractor.getOrdinalNumberOfGameObject(Ladder.class);
        double platformType = RayColliderInputExtractor.getOrdinalNumberOfGameObject(Platform.class);

        if (input[6] == platformType && input[7] > 8) return PlayerAction.UP;

        if (input[0] == ladderType && input[4] == ladderType) {
            if (input[1] < 30.5 && input[5] < 30.5 && input[1] > 24.5 && input[5] > 24.5) {
                return PlayerAction.UP;
            }
            else if (input[1] > 30.5 && input[5] < 24.5) return PlayerAction.RIGHT;
            else if (input[5] > 30.5 && input[1] < 24.5) return PlayerAction.LEFT;
            else if (input[1] < input[5]) return PlayerAction.RIGHT;
            else return PlayerAction.LEFT;
        }
        else if (input[0] == ladderType) {
            return PlayerAction.RIGHT;
        }
        else if (input[4] == ladderType) {
            return  PlayerAction.LEFT;
        }
        
        // No more ladders
        return PlayerAction.JUMP;

    }

}