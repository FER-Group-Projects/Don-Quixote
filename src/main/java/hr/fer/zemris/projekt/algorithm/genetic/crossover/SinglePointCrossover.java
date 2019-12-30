package hr.fer.zemris.projekt.algorithm.genetic.crossover;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

import java.util.Random;


public class SinglePointCrossover<G, S extends Solution<G>> implements GeneticCrossover<S> {

    private Random random = new Random();

    @Override
    public S crossover(S firstParent, S secondParent) {
        S child = (S) firstParent.copy();

        int crossPoint = random.nextInt(firstParent.getNumberOfGenes());

        for (int i = 0; i < firstParent.getNumberOfGenes(); i++) {
            if (i < crossPoint) {
                child.setGeneAt(i, firstParent.getGeneAt(i));
            } else {
                child.setGeneAt(i, secondParent.getGeneAt(i));
            }
        }

        return child;
    }

}
