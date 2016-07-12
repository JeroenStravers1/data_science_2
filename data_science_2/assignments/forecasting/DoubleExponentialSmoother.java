package forecasting;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Jeroen Stravers on 11-Jul-16.
 */
public class DoubleExponentialSmoother extends ExponentialSmoother {

    private static final int ALPHA_AND_SSE = 2;
    private static final int ALPHA = 1;
    private static final int SSE = 0;

    /**get the best alpha value (the alpha value causing the lowest SSE)*/
    public float[] calculateBestSSEAndAlpha(ArrayList<Integer> actualValues) {
        float smallestSse = Float.MAX_VALUE;
        float bestAlpha = 0;
        float[] AlphaAndSSE = new float[ALPHA_AND_SSE];
        for (float alpha = INITIAL_ALPHA; alpha <= 1; alpha += INCREMENT_ALPHA) {
            ArrayList<Float> smoothenedValues = applySmoothing(actualValues, alpha);
            float sse = calculateSSE(ALPHA_N_MODIFYER, smoothenedValues, actualValues);
            if(sse < smallestSse){
                smallestSse = sse;
                bestAlpha = alpha;
            }
        }
        AlphaAndSSE[ALPHA] = bestAlpha;
        AlphaAndSSE[SSE] = smallestSse;
        return AlphaAndSSE;
    }

    /**initialize the smoothed list by taking the average of the first 12 datapoints*/
    protected float initialize(ArrayList<Integer> actualValues) {
        float sumOfFirstTwelveValues = 0;
        for (int i = 0; i < FIRST_TWELVE_DATAPOINTS; i++) {
            sumOfFirstTwelveValues += actualValues.get(i);
        }
        float initializedValue = sumOfFirstTwelveValues / FIRST_TWELVE_DATAPOINTS;
        return initializedValue;
    }

    /**applies simple exponential smoothing to the dataset*/
    protected ArrayList<Float> applySmoothing(ArrayList<Integer> actualValues, float alpha) {
        ArrayList<Float> smoothenedValues = new ArrayList<Float>();
        smoothenedValues.add(initialize(actualValues));
        for (int i = 1; i < actualValues.size(); i++) {
            float smoothenedValue = (alpha * actualValues.get(i - 1)) + ((1 - alpha) * smoothenedValues.get(i - 1));
            smoothenedValues.add(smoothenedValue);
        }
        return smoothenedValues;
    }

    /**applies forecasting to the smoothened dataset*/
    protected void applyForecastingToSmoothenedDataset(float alpha, int desiredForecastLength,
                                                       ArrayList<Integer> actualValues, ArrayList<Float> smoothenedValues) {
        ArrayList<Float> forecastedValues = smoothenedValues;
        float finalSmoothenedValue = alpha * actualValues.get(actualValues.size() - 1) + (1 - alpha)
                * smoothenedValues.get(smoothenedValues.size() - 1);
        for (int i = 0; i < desiredForecastLength; i++) {
            forecastedValues.add(finalSmoothenedValue);
        }
    }

    /**plots a graph based on the outcomes*/
    protected JPanel plotGraph(ArrayList<Integer> actualValues, ArrayList<Float> smoothenedValues,
                               double bestAlpha, double smallestSSE) {
        String chartTitle = String.format("Forecast (SES): demand for Anduril\n" +
                "best Alpha %.2f  sse %.2f", bestAlpha, smallestSSE);
        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, "Months", "Demand",
                createDataset(actualValues, smoothenedValues),
                PlotOrientation.VERTICAL, true, false, false);
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesPaint(1, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        plot.setRenderer(renderer);
        return new ChartPanel(chart);
    }
}
