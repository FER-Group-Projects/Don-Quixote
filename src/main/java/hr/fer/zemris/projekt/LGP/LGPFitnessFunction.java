package hr.fer.zemris.projekt.LGP;

import java.util.List;

import hr.fer.zemris.projekt.LGP.lang.EasyLGPContext;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPEngine;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.algorithm.fitness.GameFitnessFunction;
import hr.fer.zemris.projekt.algorithm.player.ArtificialPlayer;
import hr.fer.zemris.projekt.algorithm.solution.Solution;
import hr.fer.zemris.projekt.model.controller.PlayerAction;
import hr.fer.zemris.projekt.model.input.GameInputExtractor;
import hr.fer.zemris.projekt.model.scenes.SceneGenerator;

public class LGPFitnessFunction extends GameFitnessFunction<Solution<EasyLGPInstruction>> {
	
	private final PlayerAction actions[] = PlayerAction.values();
	
	private final int numberOfRegisters;
	private final long maxSteps;

	public LGPFitnessFunction(List<SceneGenerator> sceneGenerators, GameInputExtractor gameInputExtractor, int numberOfRegisters, long maxSteps) {
		super(sceneGenerators, gameInputExtractor);
		if(numberOfRegisters < actions.length) throw new IllegalArgumentException();
		this.numberOfRegisters = numberOfRegisters;
		this.maxSteps = maxSteps;
	}

	@Override
	public ArtificialPlayer initializeArtificialPlayer(Solution<EasyLGPInstruction> solution) {
		if(!(solution instanceof LGPSolution)) throw new IllegalArgumentException();
		
		return new ArtificialPlayer() {
			
			private EasyLGPContext context = new EasyLGPContext(numberOfRegisters);
			private LGPSolution lgpSolution = (LGPSolution) solution;
			
			@Override
			public PlayerAction calculateAction(double[] input) {
				context.clear();
				for(int i=0; i<input.length; i++) {
					context.setRegister(i, input[i]);
				}
				
				EasyLGPEngine.execute(lgpSolution.getInstructions(), context, maxSteps);
				
				double maxValue = context.getRegister(0);
				int maxIndex = 0;
				for(int i=1; i<actions.length; i++) {
					if(context.getRegister(i) > maxValue) {
						maxValue = context.getRegister(i);
						maxIndex = i;
					}
				}
				
				return actions[maxIndex];
			}
			
		};
	}

}
