package hr.fer.zemris.projekt.algorithm.mutation;

public interface Mutation<T> {

    T mutate(T solutionToMutate);

}
