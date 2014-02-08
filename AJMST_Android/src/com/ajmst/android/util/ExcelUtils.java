package com.ajmst.android.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;

public class ExcelUtils {
	
	public static Workbook getWorkbook(String path) throws BiffException, IOException{
		File file = new File(path);
		Workbook wb = Workbook.getWorkbook(file);
		return wb;
	}
	
	/**
	 * @see #getData(Workbook, int)
	 * @param wb
	 * @return
	 */
	public static List<List<String>> getData(Workbook wb){
		return getData(wb,0);
	}
	
	/**
	 * @see #getData(String, int)
	 * @param wb
	 * @return
	 */
	public static List<List<String>> getData(String path){
		return getData(path,0);
	}
	
	/**
	 * @see #getData(Workbook, int)
	 * @param path
	 * @param sheetIdx
	 * @return
	 */
	public static List<List<String>> getData(String path,int sheetIdx){
		try {
			Workbook wb = ExcelUtils.getWorkbook(path);
			return getData( wb, sheetIdx);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param wb
	 * @param sheetIdx
	 * @return 二位数据列表
	 */
	public static List<List<String>> getData(Workbook wb,int sheetIdx){
		List<List<String>> data = new ArrayList<List<String>>();
		try {
			Sheet sheet = wb.getSheet(sheetIdx);

			for(int r = 0; r < sheet.getRows(); r++){
				Cell[] cells = sheet.getRow(r);
				List<String> rowData = new ArrayList<String>();
				for(int c = 0; c < cells.length; c++){
					rowData.add(cells[c].getContents());
				}
				data.add(rowData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	
	public static String getCellStr(Sheet ws,int row, int col){
		String value = null;
		Cell cell = ws.getCell(col, row);
		if(cell != null){
			value = cell.getContents();
		}
		return value;
	}
	
    public static void setCell(WritableSheet ws, final int row, final int col, Object value) throws Exception {
/*        // WritableCell cell =ws.getWritableCell(col, row);//获取单元格
        //jxl.format.CellFormat cf = cell.getCellFormat();//获取单元格的格式
    	//WritableCellFormat wcf = new WritableCellFormat(ws.getCell(col, row).getCellFormat());
        jxl.write.Label lbl = new jxl.write.Label(col, row, value);//修改单元格的值
        //lbl.setCellFormat(wcf);//将修改后的单元格的格式设定成跟原来一样
        ws.addCell(lbl);//将改过的单元格保存到sheet
*/    
    	WritableCellFormat wcFormat = new WritableCellFormat(ws.getCell(col, row).getCellFormat());
    	
    	//wcFormat.setBorder(Border.ALL, BorderLineStyle.NONE);  
    	WritableCell wc = null; 
	    // 判断数据是否为STRING类型，是用LABLE形式插入，否则用NUMBER形式插入  
	    if (value == null) {
	        wc = new jxl.write.Blank(col, row,wcFormat);
	    } else if (value instanceof String) {
	        jxl.write.Label label = new jxl.write.Label(col, row,  
	                value.toString(),wcFormat);  
	        wc = label;
	    } else {
	        wc = new jxl.write.Number(col, row,
	                Double.valueOf(value.toString()),wcFormat);
	    }
	    ws.addCell(wc);
   }
    
    
	
}
