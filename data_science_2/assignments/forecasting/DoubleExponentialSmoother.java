package forecasting;

import java.util.ArrayList;

/**
 * Created by Jeroen Stravers on 11-Jul-16.
 */
public class DoubleExponentialSmoother extends ExponentialSmoother {

    private static final int SSE_ALPHA_BETA = 3;
    private static final int ALPHA = 1;
    private static final int SSE = 0;
    private static final int BETA = 2;
    private static final int SMOOTHENED = 0;
    private static final int TREND = 1;
    private static final int FIRST = 0;
    private static final int SECOND = 1;

    /**get the alpha and beta values that lead to the lowest SSE*/
    public float[] getSSEAndAlphaAndBeta(ArrayList<Integer> actualValues) { //TODO +
        float[] SSEAlphaBeta = new float[SSE_ALPHA_BETA];
        float smallestSSE = Float.MAX_VALUE;
        float bestAlpha = 0;
        float bestBeta  = 0;
        for (float alpha = INITIAL_ALPHA; alpha < 1; alpha += INCREMENT_ALPHA) {
            for (float beta = INITIAL_ALPHA; beta < 1 ; beta += INCREMENT_ALPHA) {
                ArrayList<ArrayList<Float>> smoothenedAndTrendValues = calculateSmoothingAndTrend(actualValues, alpha, beta);
                ArrayList<Float> initialForecastSeq = getInitialForecastValue(smoothenedAndTrendValues.get(SMOOTHENED),
                        smoothenedAndTrendValues.get(TREND));
                float SSE = calculateSSE(BETA_N_MODIFIER, initialForecastSeq, actualValues);
                if(SSE < smallestSSE){
                    smallestSSE = SSE;
                    bestAlpha = alpha;
                    bestBeta = beta;
                }
            }
        }
        SSEAlphaBeta[SSE] = smallestSSE;
        SSEAlphaBeta[ALPHA] = bestAlpha;
        SSEAlphaBeta[BETA] = bestBeta;
        return SSEAlphaBeta;
    }

    /**calculate smoothing and trend values*/
    public ArrayList<ArrayList<Float>> calculateSmoothingAndTrend(ArrayList<Integer> actualValues, float alpha,
                                                                  float beta) { //TODO +
        ArrayList<Float> smoothenedValues = new ArrayList<Float>();
        ArrayList<Float> trendValues = new ArrayList<Float>();
        ArrayList<ArrayList<Float>> trendAndSmoothingList = new ArrayList<ArrayList<Float>>();
        smoothenedValues.add((float)actualValues.get(FIRST));
        trendValues.add((float) actualValues.get(SECOND) - actualValues.get(FIRST));
        int currentIndex = 1;
        for (int i = 1; i < actualValues.size(); i++) {
            float smoothenedValue = alpha * actualValues.get(currentIndex) + (1 - alpha)
                    * (smoothenedValues.get(currentIndex - 1) + trendValues.get(currentIndex - 1));
            smoothenedValues.add(smoothenedValue);
            float trendValue = beta * (smoothenedValues.get(currentIndex) - smoothenedValues.get(currentIndex - 1))
                    + (1 - beta) * trendValues.get(currentIndex - 1);
            trendValues.add(trendValue);
            currentIndex++;
        }
        trendAndSmoothingList.add(smoothenedValues);
        trendAndSmoothingList.add(trendValues);
        return trendAndSmoothingList;
    }

    /**initialize the forecasting list*/ //TODO+
    private ArrayList<Float> getInitialForecastValue(ArrayList<Float> smoothenedValues, ArrayList<Float> trendValues) {
        ArrayList<Float> initialForecasting = new ArrayList<Float>();
        for (int i = 1; i < smoothenedValues.size(); i++) {
            initialForecasting.add(smoothenedValues.get(i) + trendValues.get(i));
        }
        return initialForecasting;
    }

    /**perform forecasting*/ //TODO+
    protected ArrayList<Float> getForecastValues(ArrayList<Float> smoothenedValues, ArrayList<Float> trendValues,
                                                    int desiredForecastLength) {
        ArrayList<Float> forecast = new ArrayList<Float>();
        float lastSmoothenedValue = smoothenedValues.get(smoothenedValues.size() - 1);
        float lastTrendValue = trendValues.get(trendValues.size() - 1);
        for (int i = 1; i <= desiredForecastLength; i++) {
            forecast.add(lastSmoothenedValue + (i * lastTrendValue));
        }
        return forecast;
    }

}
