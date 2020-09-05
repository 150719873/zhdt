/** @author : KangJunRong
 *  @description : 读取Excel
 *  @date : 2017年12月21日 上午10:03:58
 */
package com.dotop.smartwater.project.module.core.water.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class ExcelUtil {
	private static POIFSFileSystem fs;
	private static HSSFWorkbook wb;
	private static HSSFSheet sheet;
	private static HSSFRow row;

	private static XSSFWorkbook wbX;
	private static XSSFSheet sheetX;
	private static XSSFRow rowX;

	public static String[][] readExcelContent(InputStream is) throws IOException {
		fs = new POIFSFileSystem(is);
		wb = new HSSFWorkbook(fs);
		sheet = wb.getSheetAt(0);
		// 获取总长度
		int rowNum = sheet.getPhysicalNumberOfRows();
		// System.out.println("Excel行数:"+rowNum);
		row = sheet.getRow(0);
		String[][] strArray = new String[rowNum][11];
		for (int i = 2; i < rowNum; i++) {
			String str = "";
			row = sheet.getRow(i);
			for (int j = 0; j < 11; j++) {
				str = "";
				str += getCellFormatValue(row.getCell((short) j)).trim();
				strArray[i][j] = str;
			}
		}
		is.close();
		is = null;
		return strArray;
	}

	@SuppressWarnings("deprecation")
	public static String getCellFormatValue(HSSFCell cell) {
		String cellValue = "";
		DecimalFormat df = new DecimalFormat("#.##");
		if (cell != null) {
			switch (cell.getCellType()) {
				case HSSFCell.CELL_TYPE_STRING:
					cellValue = cell.getRichStringCellValue().getString();
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					if (cell.toString().contains("-") && checkDate(cell.toString())) { // 判断是日期类型
						cellValue = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(cell.getDateCellValue());
					} else {
						cellValue = df.format(cell.getNumericCellValue());
					}
					break;
				default:
					cellValue = "";
					break;
			}
		}
		df = null;
		return cellValue;
	}

	public static String getCellFormatValue(XSSFCell cell) {
		String cellValue = "";
		DecimalFormat df = new DecimalFormat("#");
		if (cell != null) {
			switch (cell.getCellType()) {
				case HSSFCell.CELL_TYPE_STRING:
					cellValue = cell.getRichStringCellValue().getString();
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					if (cell.toString().contains("-") && checkDate(cell.toString())) { // 判断是日期类型
						cellValue = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(cell.getDateCellValue());
					} else {
						cellValue = df.format(cell.getNumericCellValue());
					}
					break;
				default:
					cellValue = "";
					break;
			}
		}
		df = null;
		return cellValue;
	}

	/**
	 * 判断是否是“02-十一月-2006”格式的日期类型
	 */
	private static boolean checkDate(String str) {
		String[] dataArr = str.split("-");
		try {
			if (dataArr.length == 3) {
				int x = Integer.parseInt(dataArr[0]);
				String y = dataArr[1];
				int z = Integer.parseInt(dataArr[2]);
				if (x > 0 && x < 32 && z > 0 && z < 10000 && y.endsWith("月")) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}


	public static String[][] readExcelContent(InputStream is, int beginRow, int endCol) throws IOException {
		fs = new POIFSFileSystem(is);
		wb = new HSSFWorkbook(fs);
		sheet = wb.getSheetAt(0);
		// 获取总长度
		int rowNum = sheet.getPhysicalNumberOfRows();
		// System.out.println("Excel行数:"+rowNum);
		row = sheet.getRow(0);
		String[][] strArray = new String[rowNum][endCol];
		for (int i = beginRow; i < rowNum; i++) {
			String str = "";
			row = sheet.getRow(i);
			for (int j = 0; j < endCol; j++) {
				str = "";
				str += getCellFormatValue(row.getCell((short) j)).trim();
				strArray[i][j] = str;
			}
		}
		is.close();
		is = null;
		return strArray;
	}

	public static String[][] read2007ExcelContent(InputStream is, int beginRow, int endCol) throws IOException {
		wbX = new XSSFWorkbook(is);
		sheetX = wbX.getSheetAt(0);
		// 获取总长度
		int rowNum = sheetX.getPhysicalNumberOfRows();
		// System.out.println("Excel行数:"+rowNum);
		rowX = sheetX.getRow(0);
		String[][] strArray = new String[rowNum][endCol];
		for (int i = beginRow; i < rowNum; i++) {
			String str = "";
			rowX = sheetX.getRow(i);
			for (int j = 0; j < endCol; j++) {
				str = "";
				str += getCellFormatValue(rowX.getCell((short) j)).trim();
				strArray[i][j] = str;
			}
		}
		is.close();
		is = null;
		return strArray;
	}
}
