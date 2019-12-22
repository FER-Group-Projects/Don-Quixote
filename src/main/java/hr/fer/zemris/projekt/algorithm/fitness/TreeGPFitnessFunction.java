package hr.fer.zemris.projekt.algorithm.fitness;

import java.util.List;

import hr.fer.zemris.projekt.algorithm.player.ArtificialPlayer;
import hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation.Tree;
import hr.fer.zemris.projekt.algorithm.tree_based_GP_implementation.TreeEngine;
import hr.fer.zemris.projekt.model.controller.PlayerAction;
import hr.fer.zemris.projekt.model.input.GameInputExtractor;
import hr.fer.zemris.projekt.model.scenes.SceneGenerator;

public class TreeGPFitnessFunction extends GameFitnessFunction<Tree>{

	public TreeGPFitnessFunction(List<SceneGenerator> sceneGenerators, GameInputExtractor gameInputExtractor) {
		super(sceneGenerators, gameInputExtractor);	
	}

	@Override
	public ArtificialPlayer initializeArtificialPlayer(Tree solution) {
		
		return new ArtificialPlayer() {

			@Override
			public PlayerAction calculateAction(double[] input) {
				
				Double numSolution = TreeEngine.compute(solution, input);
				return TreeEngine.calculatePlayerAction(numSolution);
			}		
		};
	}
}
