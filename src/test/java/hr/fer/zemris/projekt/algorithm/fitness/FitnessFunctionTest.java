package hr.fer.zemris.projekt.algorithm.fitness;

import hr.fer.zemris.projekt.algorithm.solution.DoubleArraySolution;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FitnessFunctionTest {

    @Test
    void testMaximumForSimplePolynomial() {
        assertEquals(0.0,
                new SimplePolynomialFitnessFunction()
                        .calculateFitness(new DoubleArraySolution(new double[]{ 10, 30 })), 1E-6);
    }

    @Test
    void testMaximumForAbsolutePlusCosine() {
        assertEquals(0.0,
                new AbsolutePlusCosineFitnessFunction()
                        .calculateFitness(new DoubleArraySolution(new double[1])), 1E-6);
    }

    @Test
    void testMaximumForRastriginFunction() {
        assertEquals(0.0,
                new RastriginFitnessFunction(100)
                        .calculateFitness(new DoubleArraySolution(new double[100])), 1E-6);
    }
    
}
