package hr.fer.zemris.projekt.LGP;

import hr.fer.zemris.projekt.LGP.LGP;
import hr.fer.zemris.projekt.LGP.LGPFitnessFunction;
import hr.fer.zemris.projekt.LGP.LGPPopulationInitializer;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.algorithm.OptimizationAlgorithm;
import hr.fer.zemris.projekt.algorithm.fitness.GameFitnessFunction;
import hr.fer.zemris.projekt.algorithm.solution.Solution;
import hr.fer.zemris.projekt.model.input.impl.RayColliderInputExtractor;
import hr.fer.zemris.projekt.model.scenes.ClimbingScene;

import java.util.List;

public class LGPGameTest {

    public static void main(String[] args) {
    	final int numOfRegisters = 8;
    	final long maxSteps = 100;
    	final int maxLength = 50;
    	final double maxAbsMovConstant = Double.MAX_VALUE;
    	
        GameFitnessFunction<Solution<EasyLGPInstruction>> fitnessFunction = new LGPFitnessFunction(
                List.of(
                        new ClimbingScene(60, 1000, 0.5, 100, 100, 300, 75, 75, 25, 50, 420, 20, 35)
                ),
                new RayColliderInputExtractor(4), numOfRegisters, maxSteps);

        OptimizationAlgorithm<Solution<EasyLGPInstruction>> algorithm =
                new LGP(10_000, 230_000 , Long.MAX_VALUE, 0.075, new LGPPopulationInitializer(maxLength, numOfRegisters, maxAbsMovConstant), 
                		new LGPBlockCrossover(), new LGPBlockMutation(numOfRegisters, maxAbsMovConstant), fitnessFunction);

        Solution<EasyLGPInstruction> solution = algorithm.run();

        System.out.println(solution.toString());
        System.out.println(fitnessFunction.calculateFitness(solution));
        try {
            Thread.sleep(3_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArtificialPlayerTest.show(fitnessFunction.initializeArtificialPlayer(solution));
//        ArtificialPlayerTest.show(null);
    }

}