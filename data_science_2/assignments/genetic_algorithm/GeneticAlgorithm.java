package genetic_algorithm;

import java.util.Random;

/**
 * Created by Jeroen Stravers on 07-Jul-16.
 */
public class GeneticAlgorithm {

    private static final int GENE_SIZE = 5;
    private static final int GENE_OPTIONS = 2;
    private static final int PARENTS = 2;
    private static final int FIRST = 0;
    private static final int SECOND = 1;
    private static final int NEXT = 1;
    private static final int LOWER_BOUND = 1;
    private static final int UPPER_BOUND = 2;
    private static final int SEQUENCE_START = 0;
    private static final double HALF = 0.5;
    private static final int DOUBLE = 2;
    private static final String ACTIVE = "1";
    private static final String INACTIVE = "0";
    private static final String RESULT_AVERAGE_FITNESS = "The average fitness for the last population is ";
    private static final String RESULT_BEST_INDIVIDUAL_SEQUENCE = "The best Individual's genesequence is ";
    private static final String RESULT_BEST_INDIVIDUAL_FITNESS = "The best Individual's fitness is ";

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

    public void runForNumIterations() {
        String[] initialGenePool = generateInitialGenes();
        Individual[] population = createPopulation(initialGenePool);
        for(Individual indy:population){print("INITPOP " + indy.getGeneSequence());}
        float[] populationFitness = calculateFitnessForEachIndividual(population);
        for (int i = 0; i < numIterations; i++){
            Individual[] newPopulation = new Individual[populationSize];
            int startPopulationIndex = 0;
            if (elitism) {
                int bestIndividualIndex = getBestIndividualIndex(populationFitness);
                newPopulation[FIRST] = population[bestIndividualIndex];
                startPopulationIndex = 1;
            }
            for(int k = startPopulationIndex; k < populationSize; k+=2) {
                // get parents
                Individual[] selectedParents = selectTwoParents(population, populationFitness);
                System.out.println("selpar" +selectedParents[0] + selectedParents[1]);
                // crossover
                Individual[] offspring = crossover(selectedParents);
                System.out.println("seloff" +offspring[0] + offspring[1]);
                // mutate
                for (Individual individual: offspring) {
                    String currentGeneSequence = individual.getGeneSequence();
                    String mutatedGeneSequence = mutateGeneSequence(currentGeneSequence);
                    individual.setGeneSequence(mutatedGeneSequence);
                }
                // store in population
                newPopulation[k] = offspring[FIRST];
                try{
                    newPopulation[k + NEXT] = offspring[SECOND];
                }
                catch(Exception e) {} // only there for uneven population sizes

            }
            population = newPopulation;
            // calculate fitness
            print("\n-----");
            for(Individual in: population) System.out.println("population"+in);

            populationFitness = calculateFitnessForEachIndividual(population);
        }
        float totalPopulationFitness = calculateSumOfPopulationFitness(populationFitness);
        float averagePopulationFitness = totalPopulationFitness / populationSize;
        System.out.println(RESULT_AVERAGE_FITNESS + averagePopulationFitness);
        int bestIndividualIndex = 0;
        for (int i = 0; i < populationSize; i++) {
            if (populationFitness[i] > populationFitness[bestIndividualIndex]) {
                bestIndividualIndex = i;
            }
        }
        System.out.println(RESULT_BEST_INDIVIDUAL_SEQUENCE + population[bestIndividualIndex].getGeneSequence());
        System.out.println(RESULT_BEST_INDIVIDUAL_FITNESS + populationFitness[bestIndividualIndex]);
    }

    /**generate a list of random genesequences for the initial population*/ //TODO +
    private String[] generateInitialGenes() {
        Random rand = new Random();
        String[] generatedGenes = new String[populationSize];
        for (int i = 0; i < populationSize; i++) {
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
        Individual[] population = new Individual[populationSize];
        for(int i = 0; i < populationSize; i++) {
            population[i] = IndividualFactory.spawnIndividualWithGenes(geneSequences[i]);
        }
        return population;
    }

    /**calculate fitness for each member of the population*/ // TODO +
    private float[] calculateFitnessForEachIndividual(Individual[] population) {
        float[] populationFitness = new float[populationSize];
        for(int j = 0; j < populationSize; j++) {
            populationFitness[j] = computeFitness(population[j]);
        }
        return populationFitness;
    }

    /**calculate fitness of an individual*/ // TODO +
    private float computeFitness(Individual individual) {
        int interpretedGeneSequence = Integer.parseInt(individual.getGeneSequence(), 2);
        float fitness = (-1 * (interpretedGeneSequence * interpretedGeneSequence)) + (7 * interpretedGeneSequence);
        return fitness;
    }

    /**get the index identifying the most fit individual*/
    private int getBestIndividualIndex(float[] populationFitness) {
        int bestIndividualIndex = 0;
        for (int i = 0; i < populationSize; i++) {
            if (populationFitness[i] > populationFitness[bestIndividualIndex]) {
                bestIndividualIndex = i;
            }
        }
        return bestIndividualIndex;
    }

    /**select two parents using roulette selection*/
    private Individual[] selectTwoParents(Individual[] population, float[] populationFitness) {
        Individual[] selection = new Individual[PARENTS];
        float[] cumulativeSelectionChance = generateRouletteWheel(population, populationFitness);
        float selectionOne = (float) Math.random();
        float selectionTwo = (float) Math.random();
        selection[FIRST] = getSelectedParent(selectionOne, cumulativeSelectionChance, population);
        selection[SECOND] = getSelectedParent(selectionTwo, cumulativeSelectionChance, population);
        return selection;
    }

    /**generate an array of cumulative selection chances with index order == population index order*/
    private float[] generateRouletteWheel(Individual[] population, float[] populationFitness) {
        float populationTotalFitness = calculateSumOfPopulationFitness(populationFitness);
        float[] individualRelativeFitnessPercentage = new float[populationSize];
        float cumulativeFitness = 0f;
        for (int j = 0; j < populationSize; j++) {
            float currentIndividualRelativeFitness = populationFitness[j] / populationTotalFitness;
            float cumulativeSelectionChance = currentIndividualRelativeFitness + cumulativeFitness;
            individualRelativeFitnessPercentage[j] = cumulativeSelectionChance;
            cumulativeFitness += currentIndividualRelativeFitness;
        }
        return individualRelativeFitnessPercentage;
    }

    /**gets the summed population fitness*/
    private float calculateSumOfPopulationFitness(float[] populationFitness) {
        float populationTotalFitness = 0f;
        for (int i = 0; i < populationSize; i++) {
            populationTotalFitness += populationFitness[i];
        }
        return populationTotalFitness;
    }

    /**Linear search, it's just 32 individuals*/
    private Individual getSelectedParent(float selectedParent, float[] cumulativeSelectionChances, Individual[] population) {
        Individual parent = null;
        for (int i = 0; i < populationSize; i++) {
            if(parentFound(selectedParent, cumulativeSelectionChances, i)){
                parent =  population[i];
            }
        }
        return parent;
    }

    /**determines if the current "roulette wheel slot" is the winner */
    private boolean parentFound(float selection, float[] cumulativeSelectionChances, int index) {
        float currentRouletteWheelSlot = cumulativeSelectionChances[index];
        if(selection > currentRouletteWheelSlot) {
            float nextContender = cumulativeSelectionChances[index + NEXT];
            if ((selection < nextContender) || index + NEXT == cumulativeSelectionChances.length) {
                return true;
            }
        }
        return false;
    }

    /**Return 2 offspring using single point crossover*/
    private Individual[] crossover(Individual[] selectedParents) {
        Individual[] offspring = selectedParents;
        if (Math.random() <= crossoverRate) {
            offspring = performCrossoverGenesplicing(selectedParents[FIRST], selectedParents[SECOND]);
        }
        return offspring;
    }

    /**create a child with a geneSequence that's a combination of parent 1 and 2. Forces crossover point to be between
     * 1 and geneSequence.length() -1 to ensure crossover.*/
    private Individual[] performCrossoverGenesplicing(Individual currentParent, Individual otherParent) {
        Individual[] offspring = new Individual[PARENTS];
        print("1- " + currentParent.getGeneSequence() + " -2- " + otherParent.getGeneSequence());
        int crossoverPoint = LOWER_BOUND + rand.nextInt((GENE_SIZE - UPPER_BOUND));
        String newGeneSequenceFirstSlice = currentParent.getGeneSequence().substring(SEQUENCE_START, crossoverPoint);
        String newGeneSequenceSecondSlice = otherParent.getGeneSequence().substring(crossoverPoint, GENE_SIZE);
        String secondNewGeneSequenceFirstSlice = otherParent.getGeneSequence().substring(SEQUENCE_START, crossoverPoint);
        String secondNewGeneSequenceSecondSlice = currentParent.getGeneSequence().substring(crossoverPoint, GENE_SIZE);
        String childGeneSequence = newGeneSequenceFirstSlice + newGeneSequenceSecondSlice;
        String secondChildGeneSequence = secondNewGeneSequenceFirstSlice + secondNewGeneSequenceSecondSlice;
        Individual childOne = IndividualFactory.spawnIndividualWithGenes(childGeneSequence);
        Individual childTwo = IndividualFactory.spawnIndividualWithGenes(secondChildGeneSequence);
        offspring[FIRST] = childOne;
        offspring[SECOND] = childTwo;
        return offspring;
    }

    /**mutate an individual's genesequence, return the mutated sequence*/
    private String mutateGeneSequence(String geneSequence) {
        String geneSequenceAfterMutation = "";
        for(int i = 0; i < geneSequence.length(); i++) {
            String currentGene = Character.toString(geneSequence.charAt(i));
            if(Math.random() <= mutationRate) {
                currentGene = mutateGene(currentGene);
            }
            geneSequenceAfterMutation += currentGene;
        }
        return geneSequenceAfterMutation;
    }

    /**turn 0s to 1s and vice versa*/
    private String mutateGene(String gene) {
        if(gene == ACTIVE) {
            gene = INACTIVE;
        }
        else if (gene == INACTIVE) {
            gene = ACTIVE;
        }
        return gene;
    }

    private void print(String message){
        System.out.println(message);
    }


}
