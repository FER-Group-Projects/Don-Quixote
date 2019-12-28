package hr.fer.zemris.projekt.algorithm.fitness;

import hr.fer.zemris.projekt.algorithm.player.ArtificialPlayer;
import hr.fer.zemris.projekt.algorithm.solution.Solution;
import hr.fer.zemris.projekt.model.controller.GameController;
import hr.fer.zemris.projekt.model.controller.GameControllerListener;
import hr.fer.zemris.projekt.model.controller.PlayerAction;
import hr.fer.zemris.projekt.model.input.GameInputExtractor;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.impl.Player;
import hr.fer.zemris.projekt.model.scenes.SceneGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public abstract class GameFitnessFunction<G extends Solution<?>> implements FitnessFunction<G> {

    private final ExecutorService executorService;

    private final List<SceneGenerator> sceneGenerators;
    private final GameInputExtractor gameInputExtractor;

    private final int numberOfTicksToEvaluate = 400;
    private final int numberOfTicksBetweenMoves = 10;

    private final double playerDestroyedPunishment = -1E5;

    public GameFitnessFunction(List<SceneGenerator> sceneGenerators, GameInputExtractor gameInputExtractor) {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.sceneGenerators = sceneGenerators;
        this.gameInputExtractor = gameInputExtractor;
    }

    @Override
    public double calculateFitness(G solution) {
        List<Callable<Double>> scenes = new ArrayList<>();

        for (SceneGenerator sceneGenerator : sceneGenerators) {
            scenes.add(() -> calculateFitnessForSingleScene(solution, sceneGenerator));
        }

        List<Future<Double>> results = null;

        try {
            results = executorService.invokeAll(scenes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return results
                .stream()
                .mapToDouble(r -> {
                    try {
                        return r.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }).summaryStatistics().getAverage();
    }

    public abstract ArtificialPlayer initializeArtificialPlayer(G solution);

    private double calculateFitnessForSingleScene(G solution, SceneGenerator sceneGenerator) {
        double[] input = new double[gameInputExtractor.getNumberOfInputs()];
        double fitness = 0;

        GameController gameController = sceneGenerator.generateScene();
        ArtificialPlayer artificialPlayer = initializeArtificialPlayer(solution);
        PlayerAction previousAction = null;

        Player player = (Player) gameController
                .getGameObjects()
                .stream()
                .filter(go -> go instanceof Player)
                .findFirst()
                .get();
        double yBefore = player.getBoundingBox().getY();

        boolean[] playerDestroyed = new boolean[1];

        gameController.addListener(new GameControllerListener() {
            @Override
            public void gameObjectAdded(Game2DObject object) {}

            @Override
            public void gameObjectRemoved(Game2DObject object) {}

            @Override
            public void gameObjectChanged(Game2DObject object) {}

            @Override
            public void gameObjectDestroyed(Game2DObject object) {
                if (object instanceof Player) {
                    playerDestroyed[0] = true;
                }
            }

            @Override
            public void tickPerformed() {}

            @Override
            public void playerActionStateChanged(PlayerAction action, boolean isSet) {}
        });

        for (int tick = 0; tick < numberOfTicksToEvaluate && !playerDestroyed[0]; tick++) {
            gameController.tick();

            if ((tick % numberOfTicksBetweenMoves) != 0) continue;

            gameInputExtractor.extractInputs(gameController, input);

            PlayerAction newAction = null;
            try {
                newAction = artificialPlayer.calculateAction(input);
            } catch (Exception ex) {
                fitness += playerDestroyedPunishment;
                break;
            }

            if (previousAction != newAction && previousAction != null) {
                gameController.unsetPlayerAction(previousAction);
            }

            if (newAction != null) {
                gameController.setPlayerAction(newAction);
            }

            previousAction = newAction;
        }

        if (playerDestroyed[0]) {
            fitness += playerDestroyedPunishment;
        }

        double yAfter = player.getBoundingBox().getY();

        return 1000 * (yAfter - yBefore) + fitness;
    }

}
