import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jeroen Stravers on 05-Jul-16.
 */
public class Centroid {

    private ArrayList<Float> currentPosition;
    private ArrayList<Datapoint> pointsInCluster;

    public Centroid() {
        currentPosition = new ArrayList<Float>();
        pointsInCluster = new ArrayList<Datapoint>();
    }

    public ArrayList<Float> getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(ArrayList<Float> currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void addDatapoint(Datapoint datapoint) {
        pointsInCluster.add(datapoint);
    }

    public void removeDatapointFromCentroid(int datapointId) {

    }

    public ArrayList<Datapoint> getCentroidDatapoints() {
        return pointsInCluster;
    }

    public void clearDatapoints() {
        pointsInCluster.clear();
    }
}
