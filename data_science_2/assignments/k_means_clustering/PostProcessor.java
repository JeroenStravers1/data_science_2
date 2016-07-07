package k_means_clustering;

import java.util.*;

/**
 * Created by Jeroen Stravers on 06-Jul-16.
 */
public class PostProcessor {

    private static final String PRINT_SSE = "\nSSE: ";
    private static final String PRINT_CLUSTER_SIZES = "\nSize of current cluster: ";
    private static final String PRINT_DIMENSION = "Offer ";
    private static final String PRINT_DIMENSION_SCORE = ", times taken: ";
    private static final int MIN_TIMES_TAKEN = 3;
    private static final int CLUSTER = 0;
    private static final int SSE = 1;
    private static final int DIMENSIONS = 2;

    private float resultSSE;
    private ArrayList<Centroid> centroids;
    private int numberOfDimensions;


    public void processClusteredResult(Vector result) {
        if (result.isEmpty() == false) {
            centroids = (ArrayList<Centroid>) result.get(CLUSTER);
            Object unidentifiedObject = result.get(SSE);
            resultSSE = (Float) unidentifiedObject;
            Object secondUnidentifiedObject = result.get(DIMENSIONS);
            numberOfDimensions = (Integer) secondUnidentifiedObject;
            printSSE();
            printClusterContent();
        }
    }

    private void printSSE() {
        System.out.println(PRINT_SSE + resultSSE);
    }

    private void printClusterContent() {
        for (Centroid centroid: centroids) {
            ArrayList<Datapoint> centroidDatapoints = centroid.getCentroidDatapoints();
            NavigableMap descendingResults = convertClusterContentToReadableFormat(centroidDatapoints);
            printClusterSize(centroid);
            printSortedClusterContent(descendingResults);
        }
    }

    private void printClusterSize(Centroid centroid) {
        int centroidSize = centroid.getCentroidDatapoints().size();
        System.out.println(PRINT_CLUSTER_SIZES + centroidSize);
    }

    private NavigableMap convertClusterContentToReadableFormat(ArrayList<Datapoint> centroidDatapoints) {
        TreeMap<Integer, Float> results = new TreeMap<Integer, Float>();
        for (Datapoint datapoint: centroidDatapoints) {
            ArrayList<Float> datapointPosition = datapoint.getPosition();
            for (int i = 0; i < numberOfDimensions; i++) {
                if(results.containsKey(i)) {
                    float updatedDimensionValue = results.get(i) + datapointPosition.get(i);
                    results.put(i, updatedDimensionValue);
                }
                else {
                    results.put(i, datapointPosition.get(i));
                }
            }
        }
        NavigableMap descendingResults = results.descendingMap();
        return  descendingResults;
    }

    private void printSortedClusterContent(NavigableMap<Integer, Float> descendingResults) {
        for(Map.Entry<Integer, Float> dimensionWithValue: descendingResults.entrySet()) {
            if(dimensionWithValue.getValue() > MIN_TIMES_TAKEN) {
                int dimensionId = dimensionWithValue.getKey();
                int timesTaken = dimensionWithValue.getValue().intValue();
                System.out.println(PRINT_DIMENSION + dimensionId + PRINT_DIMENSION_SCORE
                    + timesTaken);
            }
        }
    }
}