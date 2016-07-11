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

    private DataExtractor dataExtractor;

    public Forecaster() {
        dataExtractor = new DataExtractor(PATH_TO_RAW_DATA);
        dataExtractor.extractDataFromXlsmFile();
    }

    


}
