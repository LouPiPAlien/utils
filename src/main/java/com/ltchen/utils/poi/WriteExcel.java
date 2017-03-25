package com.ltchen.utils.poi;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class WriteExcel {

	public static void main(String [] args){
        try {
            HSSFWorkbook workbook= new HSSFWorkbook();
            HSSFSheet sheet= workbook.createSheet("haha");
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell= row.createCell(0);
            cell.setCellValue("test");
            FileOutputStream os = new FileOutputStream("fisrtExcel.xls");
            workbook.write(os);
            workbook.close();
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ok");
    }
}
