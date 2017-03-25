package com.ltchen.utils.poi;

import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ReadExcel {

	public static void main(String [] args){
		try {
			FileInputStream file= new FileInputStream("fisrtExcel.xls");
			POIFSFileSystem ts= new POIFSFileSystem(file);
			HSSFWorkbook workbook=new HSSFWorkbook(ts);
			HSSFSheet sheet= workbook.getSheetAt(0);

			int rowStart = Math.min(0, sheet.getFirstRowNum());
		    int rowEnd = Math.max(20, sheet.getLastRowNum());

		    for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
		       Row r = sheet.getRow(rowNum);
		       if (r == null) {
		          // This whole row is empty
		          // Handle it as needed
		          continue;
		       }

		       int lastColumn = Math.max(r.getLastCellNum(), 10);

		       for (int cn = 0; cn < lastColumn; cn++) {
		          Cell c = r.getCell(cn,Row.MissingCellPolicy.RETURN_NULL_AND_BLANK);
		          if (c == null) {
		             // The spreadsheet is empty in this cell
		        	 continue;
		          } else {
		        	  System.out.print("[("+rowNum+","+cn+"),"+r.getCell(cn)+"]");
		          }
		       }
		    }
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ok");
	}
}
