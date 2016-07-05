import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Jeroen Stravers on 05-Jul-16.
 */
public class DataExtractor {

    private static final String CSV_PATH = "D:/Program Files (x86)/Schooldata/Data science (jaar 3)/data_science_2/WineData.csv";
    private static final String CSV_DELIMITER = ",";
    private static final int FIRST_ROW = 0;
    private static final int EMPTY = 0;
    private static final int VALUE_CONSUMED = 1;

    private ArrayList<LinkedList<Integer>> unprocessedData;
    private ArrayList<Datapoint> processedData;


    public DataExtractor() {
        unprocessedData = new ArrayList<LinkedList<Integer>>(); // arraylists are faster for traversing, linkedlists are faster when adding/removing from start/end
        processedData = new ArrayList<Datapoint>();
    }


    public void extractDataFromCSV() {
        getDataAsIntegersFromCSV();
        convertUnprocessedDataToDatapoints();
        // TODO for assessment purposes:
        // show linkedlists created and emptied
        //System.out.println(unprocessedData.size());
        //for (LinkedList<Integer> row: unprocessedData){System.out.println(row);}
        // show datapoints created and content
        //System.out.println(processedData.size());
        //for (Datapoint point: processedData){System.out.println(point.getValues());}
    }


    /**
     * reads a csv file as linkedlist(list(int). LinkedLists have O(1) time for adding to the end of the list.
     */
    private void getDataAsIntegersFromCSV() {
        BufferedReader br = null;
        String line;
        LinkedList<Integer> currentRow;
        try {
            br = new BufferedReader(new FileReader(CSV_PATH));
            while ((line = br.readLine()) != null) {
                currentRow = new LinkedList<Integer>();
                String[] splitValues = line.split(CSV_DELIMITER);
                for (String currentValue : splitValues) {
                    currentRow.add(Integer.parseInt(currentValue));
                }
                unprocessedData.add(currentRow);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * assumes all row lengths are identical.
     */
    private void convertUnprocessedDataToDatapoints() {
        int datapointsRemaining = unprocessedData.get(FIRST_ROW).size();
        while (datapointsRemaining != EMPTY) {
            ArrayList<Integer> currentDatapointValues = new ArrayList<Integer>();
            for (LinkedList<Integer> currentRow : unprocessedData) {
                int currentColumnRowValue = currentRow.getFirst();
                currentDatapointValues.add(currentColumnRowValue);
                currentRow.removeFirst();
            }
            Datapoint currentDatapoint = DatapointFactory.createDatapoint(currentDatapointValues);
            processedData.add(currentDatapoint);
            datapointsRemaining -= VALUE_CONSUMED;
        }
    }
}
