import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Jeroen Stravers on 06-Jul-16.
 */
public class PostProcessor {

    private static final String PRINT_SSE = "SSE: ";
    private static final String PRINT_CLUSTER_SIZES = "Sizes of clusters: \n";
    private static final int CLUSTER = 0;
    private static final int SSE = 1;
    private int resultSSE;
    private ArrayList<Centroid> centroids;

    public PostProcessor() {

    }

    public void processClusteredResult(Vector result) {
        resultSSE = 0;
        // TODO fix getting of values
        printSSE();
        printClusterSizes();
        printClusterContent();

    }

    private void printSSE() {
        System.out.println(PRINT_SSE + resultSSE);
    }

    private void printClusterSizes() {
        System.out.println(PRINT_CLUSTER_SIZES);
        for (Centroid centroid: centroids) {
            int centroidSize = centroid.getCentroidDatapoints().size();
            System.out.println(centroidSize);
        }
    }

    private void printClusterContent() {
        for (Centroid centroid: centroids) {
            ArrayList<Datapoint> centroidDatapoints = centroid.getCentroidDatapoints();
            convertClusterContentToReadableFormat(centroidDatapoints);
        }
    }

    private void convertClusterContentToReadableFormat(ArrayList<Datapoint> centroidDatapoints) {
        // TODO sort descending
        // print "CLUSTER " + clustersize, dan de categorieen.
        //TODO fix comments op je functies.
    }
}
