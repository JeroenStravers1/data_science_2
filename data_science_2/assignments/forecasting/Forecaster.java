package forecasting;

/**
 * Created by Jeroen Stravers on 10-Jul-16.
 */
public class Forecaster {

    private static final String PATH_TO_RAW_DATA = "D:/Program Files (x86)/Schooldata/Data science (jaar 3)/" +
            "data_science_2/SwordForecasting.xlsm";
    //assumes 2 columns, x as the first and y as the second
    private static final int X = 0;
    private static final int Y = 1;
    private static final int ALPHA = 0;
    private static final int BETA = 1;
    private static final int N_MINUS_TWO = -2;
    private static final int N_MINUS_ONE = -1;

    private DataExtractor dataExtractor;
    private SimpleExponentialSmoother simpleExponentialSmoother;
    private DoubleExponentialSmoother doubleExponentialSmoother;

    /**read the dataset
      find the best values of the smoothing factors (only ALPHA for SES, ALPHA and BETA for DES) by minimizing the error measure
     seen in the slides
      using the best values found at the previous point, compute the forecast for the following year (time steps from
     37 to 48)
      visualize the original data series and the forecasted values for the next year. */

    public Forecaster() {
        dataExtractor = new DataExtractor(PATH_TO_RAW_DATA);
        dataExtractor.extractDataFromXlsmFile();
        simpleExponentialSmoother = new SimpleExponentialSmoother();
        doubleExponentialSmoother = new DoubleExponentialSmoother();
    }


}
