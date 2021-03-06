package forecasting;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Jeroen Stravers on 10-Jul-16.
 */
public class Forecaster {

    private static final String PATH_TO_RAW_DATA = "D:/Program Files (x86)/Schooldata/Data science (jaar 3)/" +
            "data_science_2/SwordForecasting.xlsm";
    //assumes 2 columns, x as the first and y as the second
    private static final int X = 0;
    private static final int Y = 1;
    private static final int SSE = 0;
    private static final int ALPHA = 1;
    private static final int BETA = 2;
    private static final int DESIRED_FORECAST_LENGTH = 12;
    private static final int X_AXIS_INTERVAL = 1;
    private static final int SMOOTHENED = 0;
    private static final int TREND = 1;

    private DataExtractor dataExtractor;
    private SimpleExponentialSmoother simpleExponentialSmoother;
    private DoubleExponentialSmoother doubleExponentialSmoother;

    private String extractedXName;
    private String extractedYName;
    private ArrayList<Integer> extractedXValues;
    private ArrayList<Integer> extractedYValues;


    /**read the dataset
      find the best values of the smoothing factors (only ALPHA for SES, ALPHA and BETA for DES) by minimizing the error measure
     seen in the slides
      using the best values found at the previous point, compute the forecast for the following year (time steps from
     37 to 48)
      visualize the original data series and the forecasted values for the next year. */

    public static void main(String[] args){
        Forecaster forecaster = new Forecaster();
        forecaster.performForecasting();
    }

    public Forecaster() {
        dataExtractor = new DataExtractor(PATH_TO_RAW_DATA);
        dataExtractor.extractDataFromXlsmFile();
        simpleExponentialSmoother = new SimpleExponentialSmoother();
        doubleExponentialSmoother = new DoubleExponentialSmoother();
        extractedXValues = new ArrayList<Integer>();
        extractedYValues = new ArrayList<Integer>();
    }

    public void performForecasting() {
        extractedXName = dataExtractor.columnNames.get(X);
        extractedYName = dataExtractor.columnNames.get(Y);
        extractedXValues = dataExtractor.columnsWithValues.get(extractedXName);
        extractedYValues = dataExtractor.columnsWithValues.get(extractedYName);

        extendXAxisWithDesiredForecastLength();
        performForecastingWithSES();
        performForecastingWithDES();

    }

    /**adds values to the x axis to accomodate the desired forecast length*/
    private void extendXAxisWithDesiredForecastLength() {
        for (int i = 0; i < DESIRED_FORECAST_LENGTH; i++) {
            int lastXValueIndex = extractedXValues.size();
            int lastXValue = extractedXValues.get(lastXValueIndex - 1);
            extractedXValues.add(lastXValue + X_AXIS_INTERVAL);
        }
    }

    private void performForecastingWithSES() {
        float[] bestAlphaAndSSE = simpleExponentialSmoother.calculateBestSSEAndAlpha(extractedYValues);
        ArrayList<Float> smoothenedValues = simpleExponentialSmoother.applySmoothing(extractedYValues, bestAlphaAndSSE[ALPHA]);
        simpleExponentialSmoother.applyForecastingToSmoothenedDataset(bestAlphaAndSSE[ALPHA], DESIRED_FORECAST_LENGTH,
                extractedYValues, smoothenedValues);
        JFrame jFrame = new JFrame();
        jFrame.setTitle("SES ASSIGNMENT");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setContentPane(simpleExponentialSmoother.plotGraph(extractedYValues, smoothenedValues,
                bestAlphaAndSSE[ALPHA], bestAlphaAndSSE[SSE]));
        jFrame.pack();
        jFrame.setVisible(true);
    }

    private void performForecastingWithDES() {
        float[] SSEAlphaBeta = doubleExponentialSmoother.getSSEAndAlphaAndBeta(extractedYValues);
        ArrayList<ArrayList<Float>> smoothenedAndTrendValues = doubleExponentialSmoother.calculateSmoothingAndTrend(
                extractedYValues, SSEAlphaBeta[ALPHA], SSEAlphaBeta[BETA]);
        ArrayList<Float> forecastValues = doubleExponentialSmoother.getForecastValues(
                smoothenedAndTrendValues.get(SMOOTHENED), smoothenedAndTrendValues.get(TREND), DESIRED_FORECAST_LENGTH);

//TODO alleen de plotting nog nu. Dan ben je hopelijk klaar.
        JFrame jFrame = new JFrame();
        jFrame.setTitle("DES ASSIGNMENT");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setContentPane(plotGraph(dataSequence,trendAndSmoothSeq.get(1),forecastValues,
                alphaBetaSSE[0],alphaBetaSSE[1],alphaBetaSSE[2]));
        jFrame.pack();
        jFrame.setVisible(true);
    }



}
