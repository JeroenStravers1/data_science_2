package genetic_algorithm;

import java.util.Scanner;

public class GeneticAlgorithmRunner {

    private static final String START_UP_GREET = "-++- Greetings, human. I am Genetic Algorithmator 5000 -++-";
    private static final String START_UP_STATE_PURPOSE = "-++- I can perform a Genetic Algorithm. -++-";
    private static final String INPUT_CROSSOVERRATE = "-++- Please enter the crossover rate as decimals (double) and press enter -++-";
    private static final String INPUT_MUTATIONRATE = "-++- Enter the mutation rate as decimals (double) and press enter -++-";
    private static final String INPUT_ELITISM = "-++- Please specify if elitism should be applied (1 or 0, int) and press enter -++-";
    private static final String INPUT_POPULATION_SIZE = "-++- Please enter the desired population size (int) and press enter -++-";
    private static final String INPUT_ITERATIONS = "-++- Enter the desired number of iterations (int) and press enter -++-";
    private static final String INPUT_FAIL = "-++- All you had to do was enter some values. Pffft, humans... -++-";
    private static final String SHUT_DOWN = "-++- Shutting down -++-";

    private double crossoverRate;
    private double mutationRate;
    private boolean elitism;
    private int populationSize;
    private int iterations;
    private GeneticAlgorithm geneticAlgorithm;

    public static void main(String[] args) {
        GeneticAlgorithmRunner gar = new GeneticAlgorithmRunner();
        gar.runGeneticAlgorithm();
    }

    public GeneticAlgorithmRunner() {
    }


    private void runGeneticAlgorithm() {
        System.out.println(START_UP_GREET);
        System.out.println(START_UP_STATE_PURPOSE);
        boolean parametersSuccesfullyRetrieved = getParameters();
        if (parametersSuccesfullyRetrieved == true) {
            geneticAlgorithm = new GeneticAlgorithm(crossoverRate, mutationRate, elitism, populationSize, iterations);
            geneticAlgorithm.runForNumIterations();
        }
        System.out.println(SHUT_DOWN);
    }

    /**prompt user to input required parameters*/
    private boolean getParameters() {
        Scanner userInput = new Scanner(System.in);
        try {
            crossoverRate = Double.valueOf(getInputAsString(userInput, INPUT_CROSSOVERRATE));
            mutationRate = Double.valueOf(getInputAsString(userInput, INPUT_MUTATIONRATE));
            elitism = (Integer.valueOf(getInputAsString(userInput, INPUT_ELITISM)) != 0);
            populationSize = Integer.valueOf(getInputAsString(userInput, INPUT_POPULATION_SIZE));
            iterations = Integer.valueOf(getInputAsString(userInput, INPUT_ITERATIONS));
            return true;
        } catch(Exception e) {
            System.out.println(e + INPUT_FAIL);
            return false;
        }
    }

    private String getInputAsString(Scanner user_input, String message) {
        String input;
        System.out.println(message);
        input = user_input.next();
        return input;
    }
}
