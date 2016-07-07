package genetic_algorithm;

import com.sun.org.apache.bcel.internal.generic.POP;

import java.util.Random;

/**
 * Created by Jeroen Stravers on 07-Jul-16.
 */
public class GeneticAlgorithm {

    private static final int POPULATION_SIZE = 32;
    private static final int GENE_SIZE = 5;
    private static final int GENE_OPTIONS = 2;
    private static final int PARENTS = 2;
/*
    Func<Ind> createIndividual ==> input is nothing, output is a new individual;
     Func<Ind,double> computeFitness ==> input is one individual, output is its fitness;
     Func<Ind[],double[],Func<Tuple<Ind,Ind>>> selectTwoParents ==> input is an array of individuals (population)
    and an array of corresponding fitnesses, output is a function which (without any input) returns a tuple with two
    individuals (parents);
     Func<Tuple<Ind, Ind>, Tuple<Ind, Ind>> crossover ==> input is a tuple with two individuals (parents), output is a
    tuple with two individuals (offspring/children);
     Func<Ind, double, Ind> mutation ==> input is one individual and mutation rate, output is the mutated individual
*/

    double crossoverRate;
    double mutationRate;
    boolean elitism;
    int populationSize;
    int numIterations;
    Random rand;

    public GeneticAlgorithm(double crossoverRate, double mutationRate, boolean elitism, int populationSize, int numIterations)
    {
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.elitism = elitism;
        this.populationSize = populationSize;
        this.numIterations = numIterations;
        rand =  new Random();
    }

    public void run(int iterations) {
        String[] initialGenePool = generateInitialGenes();
        Individual[] population = createPopulation(initialGenePool);
        /**hier komt de for loop (iterations/convergence)*/
        // calc pop fitness
        float[] populationFitness = new float[POPULATION_SIZE];
        for(int i = 0; i < POPULATION_SIZE; i++) {
            populationFitness[i] = computeFitness(population[i]);
        }
        // parents
        selectTwoParents(population, populationFitness);





    }

    /**generate a list of random genesequences for the initial population*/ //TODO +
    private String[] generateInitialGenes() {
        Random rand = new Random();
        String[] generatedGenes = new String[POPULATION_SIZE];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            String geneSequence = "";
            for (int j = 0; j < GENE_SIZE; j++){
                int gene = rand.nextInt(GENE_OPTIONS);
                geneSequence += gene;
            }
            generatedGenes[i] = geneSequence;
        }
        return generatedGenes;
    }

    /**create population from geneSequences*/ //TODO+
    private Individual[] createPopulation(String[] geneSequences) {
        Individual[] population = new Individual[POPULATION_SIZE];
        for(int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = IndividualFactory.spawnIndividualWithGenes(geneSequences[i]);
        }
        return population;
    }

    /**calculate fitness of an individual*/
    private float computeFitness(Individual individual) {
        int interpretedGeneSequence = Integer.parseInt(individual.getGeneSequence(), 2);
        float fitness = (-1 * (interpretedGeneSequence * interpretedGeneSequence)) + (7 * interpretedGeneSequence);
        return fitness;
    }

    /**select two parents using roulette selection*/
    private void selectTwoParents(Individual[] population, float[] populationFitness) {
        Individual[] selection = new Individual[PARENTS];
        float[] cumulativeSelectionChance = generateRouletteWheel(population, populationFitness);
        float selectionOne = (float) Math.random();
        float selectionTwo = (float) Math.random();
        getSelectedParent();
    }

    private float[] generateRouletteWheel(Individual[] population, float[] populationFitness) {
        // calc total population fitness
        float populationTotalFitness = calculateTotalPopulationFitness();

        float[] individualRelativeFitnessPercentage = new float[POPULATION_SIZE];
        float cumulativeFitness = 0f;
        for (int j = 0; j < POPULATION_SIZE; j++) {
            float currentIndividualRelativeFitness = populationFitness[j] / populationTotalFitness;
            float cumulativeSelectionChance = currentIndividualRelativeFitness + cumulativeFitness;
            individualRelativeFitnessPercentage[j] = cumulativeSelectionChance;
        }
        return individualRelativeFitnessPercentage;
    }

    private float calculateTotalPopulationFitness(float[] populationFitness) {
        float populationTotalFitness = 0f;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            populationTotalFitness += populationFitness[i];
        }
        return populationTotalFitness;
    }

    /**Linear search, it's just 32 individuals*/
    private void getSelectedParent(float selectedParent, float[] cumulativeSelectionChances) {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            if(selectedParent > cumulativeSelectionChances[i]) {
                return i;
            }
        }
    }

    //private Individual[] crossover(Individual[] parents) {
        /**return 2 offspring*/

    //}

    private Individual mutateIndividual(Individual individual) {
        return new Individual();
    }


}
