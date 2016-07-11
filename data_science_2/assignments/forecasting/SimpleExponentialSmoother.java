package forecasting;

import java.util.ArrayList;

/**
 * Created by Jeroen Stravers on 11-Jul-16.
 */
public class SimpleExponentialSmoother extends ExponentialSmoother {

    private static final int ALPHA_AND_SSE = 2;
    private static final int ALPHA = 0;
    private static final int SSE = 1;

    protected float initialize(ArrayList<Integer> actualValues) {
        float sumOfFirstTwelveValues = 0;
        for (int i = 0; i < FIRST_TWELVE_DATAPOINTS; i++) {
            sumOfFirstTwelveValues += actualValues.get(i);
        }
        float initializedValue = sumOfFirstTwelveValues / FIRST_TWELVE_DATAPOINTS;
        return initializedValue;
    }

    /**get the best alpha value*/
    public float[] calculateBestAlphaAndSSE(ArrayList<Integer> actualValues) {
        float smallestSse = Float.MAX_VALUE;
        float bestAlpha = 0;
        float[] AlphaAndSSE = new float[ALPHA_AND_SSE];
        for (float alpha = INITIAL_ALPHA; alpha <= 1; alpha += INCREMENT_ALPHA) {
            ArrayList<Float> projectedValues = computeSmoothing(actualValues, alpha);
            float sse = calculateSSE(ALPHA_N_MODIFYER, projectedValues, actualValues);
            if(sse < smallestSse){
                smallestSse = sse;
                bestAlpha = alpha;
            }
        }
        AlphaAndSSE[ALPHA] = bestAlpha;
        AlphaAndSSE[SSE] = smallestSse;
        return AlphaAndSSE;
    }

    public ArrayList<Float> computeSmoothing(ArrayList<Integer> actualValues, float alpha) {
        ArrayList<Float> projectedValues = new ArrayList<Float>();
        projectedValues.add(initialize(actualValues));
        for (int i = 1; i < actualValues.size(); i++) {
            float smoothenedValue = alpha * actualValues.get(i - 1) + (1 - alpha) * actualValues.get(i - 1);
            projectedValues.add(smoothenedValue);
        }
        return projectedValues;
    }


}
