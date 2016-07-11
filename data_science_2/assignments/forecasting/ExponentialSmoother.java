package forecasting;

import java.util.ArrayList;

/**
 * Created by Jeroen Stravers on 11-Jul-16.
 */
public abstract class ExponentialSmoother {

    protected static final int ALPHA_N_MODIFYER = -1;
    protected static final int BETA_N_MODIFIER = -2;
    protected static final int FIRST_TWELVE_DATAPOINTS = 12;
    protected static final float INITIAL_ALPHA = 0.001f;
    protected static final float INCREMENT_ALPHA = 0.001f;

    protected abstract float initialize(ArrayList<Integer> actualValues);

    /**calculate the sum of squared errors*/
    protected float calculateSSE(int nModifyer, ArrayList<Float> projectedValues, ArrayList<Integer> actualValues) {
        float sumOfSquaredErrors = 0;
        float sumSquaredDifference = 0;
        for(int i = 0; i < actualValues.size(); i++) {
            float currentDifference = projectedValues.get(i) - actualValues.get(i);
            float squaredDifference = currentDifference * currentDifference;
            sumSquaredDifference += squaredDifference;
        }
        int n = actualValues.size() + nModifyer;
        float currentSquaredError = sumSquaredDifference / n;
        sumOfSquaredErrors += currentSquaredError;
        return sumOfSquaredErrors;
    }
}
