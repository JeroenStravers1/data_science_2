import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jeroen Stravers on 05-Jul-16.
 */
public class KMeansClusterer {

    private static final String CLUSTERS_EQUAL_OR_GREATER_THAN_DATAPOINTS = "-++- The specified number of clusters " +
            "exceeds the number of available datapoints -++-";
    private static final int INITIAL_ITERATION = 0;
    private static final int MAX_ITERATIONS = 10;
    private int k;
    private int iterations;
    private DataExtractor dataExtractor;
    private ArrayList<Centroid> centroids;
    private int numberOfDatapoints;

    public KMeansClusterer(DataExtractor dataExtractor, int k, int iterations) {
        this.k = k;
        this.iterations = iterations;
        this.dataExtractor = dataExtractor;
        this.numberOfDatapoints = dataExtractor.processedData.size();
    }

    protected void performKMeansClustering() {
        if (k < numberOfDatapoints){
            initializeClusterer();
        }
        else {
            System.out.println(CLUSTERS_EQUAL_OR_GREATER_THAN_DATAPOINTS);
        }
    }

    private void initializeClusterer() {
        centroids = new ArrayList<Centroid>();
        initializeCentroids();
    }

    /**I used the Forgy method to initialize centroid positions; I randomly select k datapoints as the initial
     * centroid positions.
     * */
    private void initializeCentroids() {
        HashMap<Integer, Integer> usedIndices = new HashMap<Integer, Integer>();
        for (int i = 0; i < k; i++) {
            Centroid centroid = new Centroid();
            int centroidPosition = generateCentroidStartPosition(usedIndices, INITIAL_ITERATION);
            ArrayList<Float> selectedDatapointPosition = dataExtractor.processedData.get(centroidPosition).getValues();
            centroid.setCurrentPosition(selectedDatapointPosition);
            centroids.add(centroid);
        }
    }

    /**Generates the index of the datapoint to be used as the current centroid's starting position.
     * I used recursion to ensure a randomly generated index
     * */
    private int generateCentroidStartPosition(HashMap<Integer, Integer> usedIndices, int iterations) {
        iterations++;
        if (iterations < MAX_ITERATIONS){
            int randomIndex = (int) Math.random() * numberOfDatapoints;
            if (usedIndices.containsKey(randomIndex) == false) {
                return randomIndex;
            }
            else {
                return generateCentroidStartPosition(usedIndices, iterations);
            }
        }
        else {
            return generateFirstViableIndex(usedIndices);
        }
    }

    /**included solely to break a theoretically possible infinite loop*/
    private int generateFirstViableIndex(HashMap<Integer, Integer> usedIndices) {
        int uniqueIndex = 0;
        for (int i = 0; i < numberOfDatapoints; i++) {
            if (!usedIndices.containsKey(i)) {
                uniqueIndex = i;
                break;
            }
        }
        return uniqueIndex;
    }

    /**TODO
     * hebt nu data extraction en centroid initialization
     * moet euclidist, sse, centroid startpunt storage, output printing
     * */
}
