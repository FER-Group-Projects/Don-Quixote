package hr.fer.zemris.projekt.algorithm.player;

import hr.fer.zemris.projekt.model.controller.PlayerAction;

import java.io.Serializable;

public interface ArtificialPlayer extends Serializable {

    PlayerAction calculateAction(double[] input);

}
