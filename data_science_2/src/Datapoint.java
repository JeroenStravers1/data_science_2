import java.util.ArrayList;

/**
 * Created by Jeroen Stravers on 05-Jul-16.
 */
public class Datapoint {

    private ArrayList<Float> values;
    private int id;

    public ArrayList<Float> getValues() {
        return values;
    }

    public void setValues(ArrayList<Float> values) {
        this.values = values;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
