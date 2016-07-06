import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jeroen Stravers on 05-Jul-16.
 */
public class Centroid {

    private ArrayList<Float> currentPosition;
    private HashMap<Integer, Datapoint> pointsInCluster;

    public Centroid() {
        currentPosition = new ArrayList<Float>();
        pointsInCluster = new HashMap<Integer, Datapoint>();
    }

    public ArrayList<Float> getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(ArrayList<Float> currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void addDatapointToCentroid(int datapointId, Datapoint datapoint) {
        pointsInCluster.put(datapointId, datapoint);
    }

    public void removeDatapointFromCentroid(int datapointId) {

    }
}
