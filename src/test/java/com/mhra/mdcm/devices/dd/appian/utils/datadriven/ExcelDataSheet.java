package com.mhra.mdcm.devices.dd.appian.utils.datadriven;

import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
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


    public List<User> getListOfUsers(String fileName, String sheet){

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
                        if(ignoreValue!=null && ignoreValue.toLowerCase().equals("yes"))
                        ignore = true;
                    }catch (Exception e){}

                    if(!ignore)
                        listOfTestUsers.add(new User(userName, password));
                }catch (Exception e){}
            }
            lineCount++;
        }

        return listOfTestUsers;
    }


    public Object[][] getListOf2DObjects(String fileName, String sheet){

        //Point to the resource file
        String dataFile = getDataFileFullPath(fileName);

        //Get all the data as string separated by \n
        String linesOfData = getDataFromFile(dataFile, sheet);

        //Create arraylist
        List<User> listOfCountries = new ArrayList<User>();
        String[] linesOfCSVData = linesOfData.split("\n");
        int lineCount = 0;
        for(String line: linesOfCSVData){

            if(lineCount > 0) {
                try {
                    String[] excelData = line.split(",");
                    String userName = excelData[0];
                    String password = excelData[1];
                    //String age = excelData[2];
                    //String job = excelData[3];
                    listOfCountries.add(new User(userName, password));
                }catch (Exception e){}
            }
            lineCount++;
        }

        //Convert to 2D Array Object
        Object[][] o = convertListTo2DArray(listOfCountries);

        return o;
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

}
