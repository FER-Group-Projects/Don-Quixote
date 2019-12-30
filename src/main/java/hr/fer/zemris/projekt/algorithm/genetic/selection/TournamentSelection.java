package hr.fer.zemris.projekt.algorithm.genetic.selection;

import hr.fer.zemris.projekt.algorithm.solution.Solution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class TournamentSelection<S extends Solution<?>> implements GeneticSelection<S> {

    private Random random = new Random();

    private final int sizeOfTournament;

    public TournamentSelection(int sizeOfTournament) {
        this.sizeOfTournament = sizeOfTournament;
    }

    @Override
    public S selectFromPopulation(Collection<? extends S> population) {
        List<S> list = new ArrayList<>(population);

        S ind;
        S best = null;

        for (int i = 0; i < sizeOfTournament; i++) {
            ind = list.get(random.nextInt(population.size()));

            if (best == null) {
                best = list.get(random.nextInt(population.size()));
            }

            if (ind.getFitness() > best.getFitness()) {
                best = ind;
            }
        }

        return best;
    }

}
