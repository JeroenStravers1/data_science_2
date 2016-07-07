package genetic_algorithm;

import java.util.Random;

/**
 * Created by Jeroen Stravers on 07-Jul-16.
 */
public class GeneticAlgorithm {

    private static final int POPULATION_SIZE = 32;
    private static final int GENE_SIZE = 5;
    private static final int GENE_OPTIONS = 2;
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

    /**generate a list of genesequences for the initial population*/ //TODO +
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

    /**create population*/
    private Individual[] createPopulation(String[] geneSequences) {
        Individual[] population = new Individual[POPULATION_SIZE];
        for(int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = IndividualFactory.spawnIndividualWithGenes(geneSequences[i]);
        }
        return population;
    }

    /*
    private Individual createIndividual() {

    }*/

    /**calculate fitness of an individual*/
    private float computeFitness(Individual individual) {
        int interpretedGeneSequence = Integer.parseInt(individual.getGeneSequence(), 2);
        float fitness = (-1 * (interpretedGeneSequence * interpretedGeneSequence)) + (7 * interpretedGeneSequence);
        return fitness;
    }

    private void selectTwoParents(Individual[] population, float[] populationFitness) {
        /**roulette selection? */
    }

    //private Individual[] crossover(Individual[] parents) {
        /**return 2 offspring*/

    //}

    private Individual mutateIndividual(Individual individual) {
        return new Individual();
    }


}
