import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Jeroen Stravers on 05-Jul-16.
 */
public class KMeansClusterer {

    private static final String CLUSTERS_EQUAL_OR_GREATER_THAN_DATAPOINTS = "-++- The specified number of clusters " +
            "exceeds the number of available datapoints -++-";
    private static final int INITIAL_ITERATION = 0;
    private static final int MAX_ITERATIONS = 10;
    private static final int CLOSEST_CENTROID_INDEX = 0;
    private static final int CLOSEST_CENTROID_DISTANCE = 1;
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

    /**assign each datapoint to it's closest centroid by calculating and comparing the euclidian distance to each
     * centroid for each datapoint.
     * */
    private void assignDatapointsToClosestCentroids() {
        for (Datapoint datapoint: dataExtractor.processedData) {
            Centroid closestCentroid = findClosestCentroid(datapoint);
            int datapointId = datapoint.getId();
            closestCentroid.addDatapointToCentroid(datapointId, datapoint);
        }
    }

    /** compare euclidian distances to find the centroid closest to the current datapoint
     * */
    private Centroid findClosestCentroid(Datapoint datapoint) {
        Centroid closestCentroid = null;
        float closestCentroidDistance = 0f;
        for (int centroidIndex = 0; centroidIndex < centroids.size(); centroidIndex++) {
            ArrayList<Float> datapointPosition = datapoint.getValues();
            ArrayList<Float> centroidPosition = centroids.get(centroidIndex).getCurrentPosition();
            float distanceToCentroid = computeEuclidianDistanceBetweenDatapointAndCentroidPosition(
                    datapointPosition, centroidPosition);
            if (distanceToCentroid < closestCentroidDistance || centroidIndex == INITIAL_ITERATION) {
                closestCentroid = centroids.get(centroidIndex);
                closestCentroidDistance = distanceToCentroid;
            }
        }
        return closestCentroid;
    }

    /**calculate euclidian distance between a datapoint and a centroid*/
    private float computeEuclidianDistanceBetweenDatapointAndCentroidPosition(
            ArrayList<Float> datapointPosition, ArrayList<Float> centroidPosition) {
        float totalSquaredDifference = 0f;
        for(int i = 0; i < datapointPosition.size(); i++) {
            float currentDifference = datapointPosition.get(i) - centroidPosition.get(i);
            float currentSquaredDifference = currentDifference * currentDifference;
            totalSquaredDifference += currentSquaredDifference;
        }
        float euclidianDistance = (float) Math.sqrt(totalSquaredDifference);
        return euclidianDistance;
    }

    /**calculate the centroid position based on the average position of its constituent datapoints*/
    private void computeCentroidPosition() {

    }



    /**flow:
     * initialize
     *
     * loop 50-100x
     *      loop for (iterations)
         *      assign datapoints to closest centroid
         *      recalculate centroid position
         *      break all centroid positions don't change OR iterations reached
     *      store results
     *  display best result, print output
     * */
 }
