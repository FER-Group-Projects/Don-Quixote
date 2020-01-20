package hr.fer.zemris.projekt.algorithm;

import hr.fer.zemris.projekt.LGP.LGP;
import hr.fer.zemris.projekt.LGP.LGPBlockCrossover;
import hr.fer.zemris.projekt.LGP.LGPBlockMutation;
import hr.fer.zemris.projekt.LGP.LGPFitnessFunction;
import hr.fer.zemris.projekt.LGP.LGPPopulationInitializer;
import hr.fer.zemris.projekt.LGP.lang.EasyLGPInstruction;
import hr.fer.zemris.projekt.algorithm.OptimizationAlgorithm;
import hr.fer.zemris.projekt.algorithm.fitness.GameFitnessFunction;
import hr.fer.zemris.projekt.algorithm.solution.Solution;
import hr.fer.zemris.projekt.model.input.impl.RayColliderInputExtractor;
import hr.fer.zemris.projekt.model.scenes.ClimbingBarrelScene;
import hr.fer.zemris.projekt.model.scenes.ClimbingScene;
import hr.fer.zemris.projekt.model.serialization.JavaArtificialPlayerSerializer;
import hr.fer.zemris.projekt.model.serialization.SerializationException;

import java.nio.file.Paths;
import java.util.List;

public class LGPGameTest {

    public static void main(String[] args) {
    	final int numOfRegisters = 8;
    	final long maxSteps = 5000;
    	final int maxLength = 100;
    	final double maxAbsMovConstant = 0;
    	
        GameFitnessFunction<Solution<EasyLGPInstruction>> fitnessFunction = new LGPFitnessFunction(
                List.of(
                		new ClimbingScene(60, 1000, 1, 100, 100, 300, 75, 75, 25, 50, 420, 20, 35),
                		new ClimbingBarrelScene(60, 1000, 1, 100, 100, 300, 75, 75, 25, 50, 420, 20, 35, 20, 20)
                ),
                new RayColliderInputExtractor(4), numOfRegisters, maxSteps);

        OptimizationAlgorithm<Solution<EasyLGPInstruction>> algorithm =
                new LGP(10_000, 215_000, Long.MAX_VALUE, 0.05, new LGPPopulationInitializer(maxLength, numOfRegisters, maxAbsMovConstant), 
                		new LGPBlockCrossover(), new LGPBlockMutation(numOfRegisters, maxAbsMovConstant), fitnessFunction);

        Solution<EasyLGPInstruction> solution = algorithm.run();

        try {
            new JavaArtificialPlayerSerializer().serialize(Paths.get("player.lgp"), fitnessFunction.initializeArtificialPlayer(solution));
        } catch (SerializationException e) {
            e.printStackTrace();
        }

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