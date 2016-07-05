import java.util.ArrayList;

/**
 * Created by Jeroen Stravers on 05-Jul-16.
 */
public class DatapointFactory {

    public static Datapoint createDatapoint(ArrayList<Integer> values) {
        Datapoint datapoint = new Datapoint(values);
        return datapoint;
    }
}
