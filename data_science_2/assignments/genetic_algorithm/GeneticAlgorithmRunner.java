package genetic_algorithm;

import java.util.Scanner;
import java.util.Vector;

public class GeneticAlgorithmRunner {

    private static final String START_UP_GREET = "-++- Greetings, human. I am K-Meansatronator 4000 -++-";
    private static final String START_UP_STATE_PURPOSE = "-++- I can perform K-Means clustering for you. -++-";
    private static final String INPUT_K = "-++- Please enter the desired number of clusters and press enter -++-";
    private static final String INPUT_ITERATIONS = "-++- Enter the desired number of iterations and press enter -++-";
    private static final String INPUT_FAIL = "-++- All you had to do was enter 2 digits. Pffft, humans... -++-";
    private static final String SHUT_DOWN = "-++- Shutting down -++-";
    private static final int CROSSOVER_RATE = 0;
    private static final int MUTATION_RATE = 1;
    private static final int ELITISM = 2;
    private static final int POPULATION_SIZE = 3;
    private static final int ITERATIONS = 4;

    private static final int REQUIRED_PARAMETERS = 5;

    private GeneticAlgorithm geneticAlgorithm;

    private PostProcessor postProcessor;


    public static void main(String[] args) {
        GeneticAlgorithmRunner gar = new GeneticAlgorithmRunner();
        gar.runGeneticAlgorithm();
    }

    public GeneticAlgorithmRunner() {
        postProcessor = new PostProcessor();
    }

    /**Crossover rate (value between 0 and 1 indicating the probability of actually carrying out the crossover between
     parents)
      Mutation rate (value between 0 and 1 indicating the probability of carrying out a mutation)
      Elitism (Boolean indicating if elitism is used or not in the algorithm)
      Population size (integer indicating the amount of individuals in the population)
      Number of iterations (integer indicating after how many iterations/generations the algorithm will stop)*/

    private void runGeneticAlgorithm() {
        System.out.println(START_UP_GREET);
        System.out.println(START_UP_STATE_PURPOSE);
        int[] kAndIterations = getParameters();
        if (kAndIterations != null) {
            /**perform actions*/
        }
        System.out.println(SHUT_DOWN);
    }

    /**prompt user to input K and amount of desired iterations*/
    private int[] getParameters() {
        Scanner userInput = new Scanner(System.in);
        int crossoverRate;
        int mutationRate;
        boolean elitism;
        int populationSize;
        int iterations;
        int[] storedInput = new int[REQUIRED_PARAMETERS];
        try {
            /*k = getInputAsString(userInput, INPUT_K);
            iterations = getInputAsString(userInput, INPUT_ITERATIONS);
            storedInput[K] = k;
            storedInput[ITERATIONS] = iterations;*/
        } catch(Exception e) {
            System.out.println(INPUT_FAIL);
            return null;
        }
        return storedInput;
    }

    private int getInputAsString(Scanner user_input, String message) {
        int input;
        System.out.println(message);
        input = Integer.parseInt(user_input.next());
        return input;
    }
}
