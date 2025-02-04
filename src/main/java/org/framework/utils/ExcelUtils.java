package org.framework.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashMap;

public class ExcelUtils {
    XSSFWorkbook workbook;
    XSSFSheet globalSheet;
    public int sheetRowCount;
    String excelPath;
    String sheetName;
    HashMap<String, Integer> map = new HashMap<>();
    public ExcelUtils(String excelPath, String sheetName)
    {
        try {
            workbook = new XSSFWorkbook(excelPath);
            globalSheet = workbook.getSheet(sheetName);
            sheetRowCount = globalSheet.getLastRowNum();
            this.excelPath=excelPath;
            this.sheetName=sheetName;
            this.map=readHeaders();
        } catch (Exception e) {
            System.out.println("Exception message:" + e.getMessage());
            System.out.println("Exception cause:" + e.getCause());
        }
    }

    /** get value of specific cell
     */
    public String getCellData(int rowNum, int colNum)
    {
        return new DataFormatter().formatCellValue(globalSheet.getRow(rowNum).getCell(colNum));
    }

    /** Set value for specific cell
     */
    public void setCellData(int rowNum, int colNum, String value)
    {
        try (InputStream inp = new FileInputStream(excelPath)) {
            Workbook wb = WorkbookFactory.create(inp);
            Sheet tempSheet = wb.getSheet(sheetName);
            //get cell
            Cell cell = tempSheet.getRow(rowNum).getCell(colNum);
            //update cell value
            cell.setCellValue(value);
            // Write the output to the file
            try (OutputStream fileOut = new FileOutputStream(excelPath)) {
                wb.write(fileOut);
                globalSheet = (XSSFSheet) wb.getSheet(sheetName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Retrieve unused row number
     */
    public int getValidRowNumber(int usedFlagColumn)
    {
        try (InputStream inp = new FileInputStream(excelPath))
        {
            Workbook wb = WorkbookFactory.create(inp);
            this.globalSheet = (XSSFSheet) wb.getSheet(sheetName);
            String usedFlag;
            int rowNumber;
            for ( rowNumber = 1; rowNumber<= sheetRowCount; rowNumber++)
            {
                usedFlag = getCellData(rowNumber,usedFlagColumn);
                //check for usedFlag value
                if(usedFlag.toLowerCase().equals("false"))
                {
                    return rowNumber;
                }
                else if(usedFlag.equals(""))
                {
                    break;
                }
            }
        } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        return 0;
    }

    public HashMap <String, Integer> readHeaders () {
        XSSFSheet sheet = this.globalSheet;
        //HashMap<String, Integer> headers_positions = new HashMap<>();
        for (Cell cell :sheet.getRow(0)) {
            this.map.put(cell.getStringCellValue().toLowerCase(), cell.getColumnIndex());
        }
        return this.map;
    }

    public int getRowNum (String key) {
        try {
            String header = "testcases";
            //HashMap<String, Integer> headers = readHeaders();
            if (!map.containsKey(header.toLowerCase())) {
                System.out.println("Could not find header "+ header);
                return -1;
            }
            int headerPos = map.get(header.toLowerCase());
            for (int i = 1; i <= sheetRowCount; i++) {
                if (globalSheet.getRow(i).getCell(headerPos).getStringCellValue().toLowerCase()
                        .equals(key.toLowerCase()))
                    return i;
            }
            System.out.println("Could not find specified key : "+key);
            return -1;
        } catch (Exception e) {
            System.out.println( e.getMessage() + e.getCause());
            return -1;
        }
    }

    public int getColNumber (String key) {
        //HashMap<String, Integer> map = readHeaders();
        if (!this.map.containsKey(key.toLowerCase())) {
            System.out.println("Could not find header with key : " + key);
            return -1;
        }
        return this.map.get(key.toLowerCase());
    }
}
