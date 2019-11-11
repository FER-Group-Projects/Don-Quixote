package hr.fer.zemris.projekt.algorithm.selection;

import java.util.Collection;

public interface Selection<T> {

    T selectFromPopulation(Collection<? extends T> population);

}
