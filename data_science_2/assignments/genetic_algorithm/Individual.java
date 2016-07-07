package genetic_algorithm;

/**
 * Created by Jeroen Stravers on 07-Jul-16.
 */
public class Individual {

    private String geneSequence;
    private float fitness;

    public Individual() {
    }

    public float getFitness() {
        return fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }

    public String getGeneSequence() {
        return geneSequence;
    }

    public void setGeneSequence(String geneSequence) {
        this.geneSequence = geneSequence;
    }
}
