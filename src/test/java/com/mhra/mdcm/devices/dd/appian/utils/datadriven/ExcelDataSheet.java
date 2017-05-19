package com.mhra.mdcm.devices.dd.appian.utils.datadriven;

import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.DeviceData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by TPD_Auto on 01/11/2016.
 */
public class ExcelDataSheet {

    private final String resourceFolder = "src" + File.separator + "test" + File.separator + "resources" + File.separator;

    private transient Collection data = null;

//    public ExcelDataSheet(final InputStream excelInputStream) throws IOException {
//        this.data = loadFromSpreadsheet(excelInputStream);
//    }

    public ExcelDataSheet(){

    }

    public Collection getData() {
        return data;
    }

    /**
     * Read any file and return each line separated by \n
     * @param dataFile
     * @return
     */
    private String getDataFromFile(String dataFile, String sheetName) {
        StringBuilder sb = new StringBuilder();

        try {
            File myFile = new File(dataFile);
            FileInputStream fis = new FileInputStream(myFile);

            // Finds the workbook instance for XLSX file
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);

            // Return first sheet from the XLSX workbook
            XSSFSheet mySheet = myWorkBook.getSheet(sheetName);

            // Get iterator to all the rows in current sheet
            Iterator<Row> rowIterator = mySheet.iterator();

            // Traversing over each row of XLSX file
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // For each row, iterate through each columns
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();
                    String v = cell.toString();
                    sb.append(v + ",");
                }
                sb.append("\n");

            }

            //System.out.println(sb.toString().replaceAll(",", "\t"));


        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


    public List<User> getListOfUsers(String fileName, String sheet, boolean applyIgnoreFilter){

        //Point to the resource file
        String dataFile = getDataFileFullPath(fileName);

        //Get all the data as string separated by \n
        String linesOfData = getDataFromFile(dataFile, sheet);

        //Create arraylist
        List<User> listOfTestUsers = new ArrayList<User>();
        String[] linesOfCSVData = linesOfData.split("\n");
        int lineCount = 0;
        for(String line: linesOfCSVData){

            if(lineCount > 0) {
                try {
                    String[] excelData = line.split(",");
                    String userName = excelData[0];
                    String password = excelData[1];
                    boolean ignore = false;
                    try {
                        String ignoreValue = excelData[2];
                        if(ignoreValue!=null && ignoreValue.toLowerCase().equals("yes") && applyIgnoreFilter)
                        ignore = true;
                    }catch (Exception e){}
                    String initials = excelData[3];

                    if(!ignore)
                        listOfTestUsers.add(new User(userName, password, initials));
                }catch (Exception e){}
            }
            lineCount++;
        }

        return listOfTestUsers;
    }


    public List<DeviceData> getListOfDeviceData(String fileName, String sheet) {

        //Point to the resource file
        String dataFile = getDataFileFullPath(fileName);

        //Get all the data as string separated by \n
        String linesOfData = getDataFromFile(dataFile, sheet);

        //Create arraylist
        List<DeviceData> listOfDeviceTestData = new ArrayList<>();
        List<String> headers = new ArrayList<>();

        String[] linesOfCSVData = linesOfData.split("\n");
        int lineCount = 0;
        boolean errors = false;
        String message = null;
        for(String line: linesOfCSVData){
            try {
                //First line is heading
                if (lineCount > 0 && !isEmptyLine(line)) {
                    message = areAllTheLinesValidInExcelDataSheet(dataFile, sheet, headers, line, lineCount);
                    if(message!=null){
                        errors = true;
                        //System.out.println("Error Line : " + (lineCount+1) + ": " + line);
                    }

                    int columnCount = 0;
                    String[] data = line.split(",");

                    if(data.length > 5) {
                        String key = data[0];
                        if(key!=null && !key.toLowerCase().equals("validateddata")) {
                            //System.out.println("Line : " + (lineCount+1) + ": " + line);
                            //System.out.println(line);
                            String[] dataUpdated = createUpdatedData(data, headers, columnCount);
                            listOfDeviceTestData.add(new DeviceData((lineCount+1), dataUpdated));
                        }
                    }

                }else{
                    String [] headings = line.split(",");
                    for(String hd: headings){
                        //else if(field.equals("toxicologicaldataavailable")){
                        //System.out.println("else if(field.equals(\""+ hd.toLowerCase() + "\")){\n}");
                        if(!hd.trim().equals(""))
                            headers.add(hd);
                    }
                }
            }catch (Exception e){
                //break;
            }
            lineCount++;
        }

        return listOfDeviceTestData;
    }


    private boolean isEmptyLine(String line) {
        boolean empty = false;
        if(line == null || line.trim().equals(""))
            empty = true;

        return empty;
    }

    private String areAllTheLinesValidInExcelDataSheet(String dataFile, String sheet, List<String> headers, String line, int count) {
        String message = null;
        String[] data = line.split(",");
        if(headers.size() != data.length){
            message = ("\nExcel Data File : " + dataFile + "\nSheet : " + sheet + "\nLine number : " + count + "\nInvalid line : " + line + "\nEmpty cells are not allowed, replace with : 'none'" + "\nRead : HowToUse section of excel data file" + "\n");
        }
        return message;
    }

    private String[] createUpdatedData(String[] data, List<String> headers, int columnCount) {
        String[] dataUpdated = new String[data.length];
        for(String dt: data){
            if(dt.equals("TRUE") || dt.equals("FALSE")){
                dt = dt.toLowerCase();
            }
            if(dt.contains("_XXX_"))
            dt = dt.replace("_XXX_", ",");

            dt = headers.get(columnCount)+"="+dt;
            dataUpdated[columnCount] = dt;
            columnCount++;
        }
        return dataUpdated;
    }

    private Object[][] convertListTo2DArray(List<?> listOfCountries) {
        Object[][] o = new Object[listOfCountries.size()][1];
        int pos = 0;
        for(Object c: listOfCountries){
            o[pos][0] = c;
            pos++;
        }

        return o;
    }


    private String getDataFileFullPath(String fileName) {
        File file = new File("");
        String rootFolder = file.getAbsolutePath();
        String data = (rootFolder + File.separator + resourceFolder + File.separator + fileName);
        return data;
    }

    public List<User> filterUsersBy(List<User> listOfUsers, String filterText) {
        List<User> filteredUser = new ArrayList<>();
        for(User u: listOfUsers){
            String userName = u.getUserName();
            if(userName.toLowerCase().contains(filterText)){
                filteredUser.add(u);
            }
        }

        return filteredUser;
    }


    public static String getFieldValue(String dt, int pos) {
        try{
            String[] split = dt.split("=");
            return split[pos];
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
