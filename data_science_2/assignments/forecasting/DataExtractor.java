package forecasting;

/**
 * Created by Jeroen Stravers on 11-Jul-16.
 */

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataExtractor {

    private static final int FIRST = 0;
    private String pathToData;

    protected LinkedHashMap<String, ArrayList<Integer>> columnsWithValues;
    protected ArrayList<String> columnNames;

    public DataExtractor(String pathToData) {
        columnsWithValues = new LinkedHashMap<String, ArrayList<Integer>>();
        columnNames = new ArrayList<String>();
        this.pathToData = pathToData;
    }

    public void extractDataFromXlsmFile(){
        Row row;
        Cell cell;
        int rowIterations = FIRST;
        Iterator<Row> rowIterator = getSheetIterator();
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            int currentColumn = FIRST;
            while (cellIterator.hasNext()) {
                cell = cellIterator.next();
                if (rowIterations == FIRST) { // get column names, make arraylist per column
                    makeListPerColumn(cell);
                }
                else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {// store numerical data
                    storeValueInColumn(cell, currentColumn);
                    currentColumn++;
                }
                else {// stop when relevant data has been read
                    break;
                }
            }
            rowIterations++;
        }
    }

    private Iterator getSheetIterator() {
        try{
            FileInputStream file = new FileInputStream(new File(pathToData));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(FIRST);
            return sheet.iterator();
        }
        catch (Exception e) {
            return null;
        }
    }

    private void makeListPerColumn(Cell cell) {
        String columnName = cell.getStringCellValue();
        ArrayList<Integer> values = new ArrayList<Integer>();
        columnNames.add(columnName);
        columnsWithValues.put(columnName, values);
    }

    private void storeValueInColumn(Cell cell, int currentColumn) {
        String currentColumnName = columnNames.get(currentColumn);
        ArrayList<Integer> currentColumnValues = columnsWithValues.get(currentColumnName);
        int currentCellValue = (int) cell.getNumericCellValue();
        currentColumnValues.add(currentCellValue);
        columnsWithValues.put(currentColumnName, currentColumnValues);
    }
}
