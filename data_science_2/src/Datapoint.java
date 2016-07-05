import java.util.ArrayList;

/**
 * Created by Jeroen Stravers on 05-Jul-16.
 */
public class Datapoint {

    private ArrayList<Integer> values;


    public Datapoint(ArrayList<Integer> values) {
        this.values = values;
    }

    public ArrayList<Integer> getValues() {
        return values;
    }

    public void setValues(ArrayList<Integer> values) {
        this.values = values;
    }
}
