package k_means_clustering;

import java.util.ArrayList;

/**
 * Created by Jeroen Stravers on 05-Jul-16.
 */
public class Datapoint {

    private ArrayList<Float> position;
    private Centroid centroid;

    public ArrayList<Float> getPosition() {
        return position;
    }

    public void setPosition(ArrayList<Float> position) {
        this.position = position;
    }

    public Centroid getCentroid() {
        return centroid;
    }

    public void setCentroid(Centroid centroid) {
        this.centroid = centroid;
    }

}
