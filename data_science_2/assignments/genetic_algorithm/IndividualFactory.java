package genetic_algorithm;

/**
 * Created by Jeroen Stravers on 07-Jul-16.
 */
public class IndividualFactory {

    public static Individual spawnIndividualWithGenes(String geneSequence) { // genes?
        Individual individual = new Individual();
        individual.setGeneSequence(geneSequence);
        return individual;
    }
}
