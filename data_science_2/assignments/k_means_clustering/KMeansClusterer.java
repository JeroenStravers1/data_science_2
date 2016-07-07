package k_means_clustering;

import java.util.*;

/**
 * Created by Jeroen Stravers on 05-Jul-16.
 */
public class KMeansClusterer {

    private static final String CLUSTERS_EQUAL_OR_GREATER_THAN_DATAPOINTS = "-++- The specified number of clusters " +
            "exceeds the number of available datapoints -++-";
    private static final int INITIAL_ITERATION = 0;
    private static final int EMPTY = 0;
    private static final int MAX_ITERATIONS = 10;
    private static final float ACCEPTABLE_ERROR =  0.000000001f;
    private int k;
    private int iterations;
    private DataExtractor dataExtractor;
    private ArrayList<Centroid> centroids;
    private int numberOfDatapoints;
    private int numberOfDimensions;

    public KMeansClusterer(DataExtractor dataExtractor, int k, int iterations) {
        this.k = k;
        this.iterations = iterations;
        this.dataExtractor = dataExtractor;
        this.numberOfDatapoints = dataExtractor.processedData.size();
        this.numberOfDimensions = dataExtractor.numberOfDimensions;
    }

    /**performs k means clustering, returns the clustered result with the lowest SSE and number of dimensions*/
    protected Vector performKMeansClustering() {
        Vector bestFoundCentroidsAndSSE = new Vector();
        if (k < numberOfDatapoints) {
            float lowestSSE = 0f;
            ArrayList<Centroid> lowestSSECentroidPositions = new ArrayList<Centroid>();
            for (int i = 0; i < iterations; i++) {
                boolean centroidPositionsNoLongerShifting = false;
                initializeClusterer();
                while (!centroidPositionsNoLongerShifting) {
                    assignDatapointsToClosestCentroids();
                    centroidPositionsNoLongerShifting = updateCentroidPositions();
                }
                float currentSSE = calculateSumOfSquaredErrors();
                if (currentSSE < lowestSSE || i == INITIAL_ITERATION) {
                    lowestSSE = currentSSE;
                    lowestSSECentroidPositions = centroids;

                }
            }
            bestFoundCentroidsAndSSE.add(lowestSSECentroidPositions);
            bestFoundCentroidsAndSSE.add(lowestSSE);
            bestFoundCentroidsAndSSE.add(numberOfDimensions);
        } else {
            System.out.println(CLUSTERS_EQUAL_OR_GREATER_THAN_DATAPOINTS);
        }
        return bestFoundCentroidsAndSSE;
    }

    /**reset the list of centroids, determine their starting positions*/
    private void initializeClusterer() {
        centroids = new ArrayList<Centroid>();
        initializeCentroids();
    }

    /**I used the Forgy method to initialize centroid positions; I randomly select k datapoints as the initial
     * centroid positions.
     * */
    private void initializeCentroids() { //TODO +++
        HashMap<Integer, Integer> usedIndices = new HashMap<Integer, Integer>();
        for (int i = 0; i < k; i++) {
            Centroid centroid = new Centroid();
            int centroidPosition = generateCentroidStartPosition(usedIndices, INITIAL_ITERATION);
            ArrayList<Float> selectedDatapointPosition = dataExtractor.processedData.get(centroidPosition).getPosition();
            centroid.setCurrentPosition(selectedDatapointPosition);
            centroids.add(centroid);
        }
    }

    /**Generates the index of the datapoint to be used as the current centroid's starting position. //TODO +++
     * I used recursion to ensure a randomly generated index
     * */
    private int generateCentroidStartPosition(HashMap<Integer, Integer> usedIndices, int positionGenerationIteration) {
        positionGenerationIteration++;
        if (positionGenerationIteration < MAX_ITERATIONS){
            int randomIndex = (int) (Math.random() * numberOfDatapoints);
            if (usedIndices.containsKey(randomIndex) == false) {
                return randomIndex;
            }
            else {
                return generateCentroidStartPosition(usedIndices, positionGenerationIteration);
            }
        }
        else {
            return generateFirstViableIndex(usedIndices);
        }
    }

    /**included solely to break a theoretically possible infinite loop*/ //TODO +++
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
        for (Centroid centroid: centroids) {
            centroid.clearDatapoints();
        }
        for (Datapoint datapoint: dataExtractor.processedData) {
            Centroid closestCentroid = findClosestCentroid(datapoint);
            closestCentroid.addDatapoint(datapoint);
        }
    }

    /** compare euclidian distances to find the centroid closest to the current datapoint
     * */
    private Centroid findClosestCentroid(Datapoint datapoint) {
        Centroid closestCentroid = null;
        float closestCentroidDistance = 0f;
        for (int centroidIndex = 0; centroidIndex < centroids.size(); centroidIndex++) {
            ArrayList<Float> datapointPosition = datapoint.getPosition();
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
    private boolean updateCentroidPositions() {
        boolean centroidPositionsUnchanged = false;
        for (Centroid centroid: centroids) {
            ArrayList<Float> updatedCentroidPosition = calculateAveragePositionOfCentroidDatapoints(centroid);
            if (centroidPositionUnchanged(centroid, updatedCentroidPosition)) {
                centroidPositionsUnchanged = true;
            }
            else {
                centroidPositionsUnchanged = false;
            }
            centroid.setCurrentPosition(updatedCentroidPosition);
        }
        return centroidPositionsUnchanged;
    }

    /**calculate the average position of the centroid, don't change it's position if it no longer has any datapoints
     * */
    private ArrayList<Float> calculateAveragePositionOfCentroidDatapoints(Centroid centroid) {
        int centroidDatapointsAmount = centroid.getCentroidDatapoints().size();
        ArrayList<Float> averageDimensionScores = new ArrayList<Float>();
        if (centroidDatapointsAmount > EMPTY) {
            ArrayList<Float> summedDimensionScores = sumCentroidDatapointDimensionScores(centroid);     //

            for (int i = 0; i < summedDimensionScores.size(); i++) {
                float averageDimensionScore = summedDimensionScores.get(i) / centroidDatapointsAmount;
                averageDimensionScores.add(averageDimensionScore);
            }
        }
        else {
            averageDimensionScores = centroid.getCurrentPosition();
        }
        return averageDimensionScores;

    }

    /**extract the summed dimension scores of a centroid's datapoints
     * */
    private ArrayList<Float> sumCentroidDatapointDimensionScores(Centroid centroid) {
        ArrayList<Datapoint> currentCentroidDatapoints = centroid.getCentroidDatapoints();
        ArrayList<Float> totalScore = new ArrayList<Float>();
        int numberOfDimensions = currentCentroidDatapoints.get(INITIAL_ITERATION).getPosition().size();
        for (int i = 0; i < numberOfDimensions; i++) {
            float totalScoreForDimension = 0f;
            for (Datapoint datapoint: currentCentroidDatapoints) {
                totalScoreForDimension += datapoint.getPosition().get(i);
            }
            totalScore.add(totalScoreForDimension);

        }
        return totalScore;
    }

    /**checks if the updated position of the centroid on all dimensions is different from it's current position
     * */
    private boolean centroidPositionUnchanged(Centroid centroid, ArrayList<Float> updatedCentroidPosition) {
        ArrayList<Float> currentCentroidPosition = centroid.getCurrentPosition();
        boolean updatedPositionIdenticalToCurrentPosition = true;
        for (int i = 0; i < currentCentroidPosition.size(); i++) {
            float differenceBetweenPositionsForDimension = Math.abs(currentCentroidPosition.get(i) -
                    updatedCentroidPosition.get(i));
            if(!(differenceBetweenPositionsForDimension < ACCEPTABLE_ERROR)) {
                updatedPositionIdenticalToCurrentPosition = false;
            }
        }
        return updatedPositionIdenticalToCurrentPosition;
    }

    /**calculate sse*/
    private float calculateSumOfSquaredErrors() {
        float sumOfSquaredErrors = 0f;
        for(Centroid centroid: centroids) {
            ArrayList<Float> centroidPosition = centroid.getCurrentPosition();
            ArrayList<Datapoint> datapoints = centroid.getCentroidDatapoints();
            for (Datapoint datapoint: datapoints) {
                ArrayList datapointPosition = datapoint.getPosition();
                float euclidianDistance = computeEuclidianDistanceBetweenDatapointAndCentroidPosition(
                        datapointPosition, centroidPosition);
                float squaredError = euclidianDistance * euclidianDistance;
                sumOfSquaredErrors += squaredError;
            }
        }
        return sumOfSquaredErrors;
    }
}
