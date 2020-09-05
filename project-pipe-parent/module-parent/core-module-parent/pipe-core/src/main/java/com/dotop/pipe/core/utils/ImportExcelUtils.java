package com.dotop.pipe.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 文件导入 07版 execl
 * 
 *
 *
 */
public class ImportExcelUtils {

	// private POIFSFileSystem fs;
	private XSSFWorkbook wb;
	private XSSFSheet sheet;
	private XSSFRow row;

	// 表明和sheet页面
	private Map<String, XSSFSheet> sheetMap = new HashMap<>();

	// 每个sheet页的标题
	private Map<String, String[]> titleMap = new HashMap<>();

	public Map<String, String[]> getTitleMap() {
		return this.titleMap;
	}

	public Map<String, XSSFSheet> getSheetMap() {
		return this.sheetMap;
	}

	/**
	 * 读取Excel表格表头的内容
	 * 
	 * @param is
	 * @return String 表头内容的数组
	 */
	public void readExcelTitle(InputStream is) {
		try {
			wb = new XSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 获取所有的sheet 页 并放入map中
		int sheets = wb.getNumberOfSheets();
		for (int i = 0; i < sheets; i++) {
			sheet = wb.getSheetAt(i);
			sheetMap.put(sheet.getSheetName(), sheet);
			row = sheet.getRow(0); // 获取标题行
			if(row == null) {
				break;
			}
			int colNum = row.getPhysicalNumberOfCells();
			String[] title = new String[colNum];
			for (int j = 0; j < colNum; j++) {
				title[j] = getCellFormatValue(row.getCell(j));
				System.out.print(title[j] + "==");
			}
			this.titleMap.put(sheet.getSheetName(), title);
		}
	}

	/**
	 * 读取Excel数据内容
	 * 
	 * @param is
	 * @return Map 包含单元格数据内容的Map对象
	 */
	// public Map<Integer, String> readExcelContent(InputStream is) {
	// Map<Integer, String> content = new HashMap<Integer, String>();
	// String str = "";
	// try {
	// //fs = new POIFSFileSystem(is);
	// wb = new XSSFWorkbook(is);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// sheet = wb.getSheetAt(0);
	// // 得到总行数
	// int rowNum = sheet.getLastRowNum();
	// // 第一行是标题 从第二行开始
	// row = sheet.getRow(1);
	//
	//
	// int colNum = row.getPhysicalNumberOfCells();
	// // 正文内容应该从第二行开始,第一行为表头的标题
	// for (int i = 2; i <= rowNum; i++) {
	// row = sheet.getRow(i);
	// int j = 0;
	// while (j < colNum) {
	// str += getCellFormatValue(row.getCell((short) j)).trim() + "-";
	// j++;
	// }
	// content.put(i, str);
	// str = "";
	// }
	// return content;
	// }

	/**
	 * 获取单元格数据内容为字符串类型的数据
	 *
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	private String getStringCellValue(HSSFCell cell) {
		if (cell == null) {
			return "";
		}
		String strCell = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			strCell = String.valueOf(cell.getNumericCellValue());
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		return strCell;
	}

	/**
	 * 获取单元格数据内容为日期类型的数据
	 *
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	private String getDateCellValue(HSSFCell cell) {
		String result = "";
		try {
			int cellType = cell.getCellType();
			if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
				Date date = cell.getDateCellValue();
				result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
			} else if (cellType == HSSFCell.CELL_TYPE_STRING) {
				String date = getStringCellValue(cell);
				result = date.replaceAll("[年月]", "-").replace("日", "").trim();
			} else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
				result = "";
			}
		} catch (Exception e) {
			System.out.println("日期格式不正确!");
			e.printStackTrace();
		}
		return result;
	}

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 获取文件的列数据
	 * 
	 * @param cell
	 * @return
	 */
	public String getCellFormatValue(XSSFCell cell) {
		String cellvalue = null, str;
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			case XSSFCell.CELL_TYPE_NUMERIC:
				// 如果当前Cell的Type为NUMERIC
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				str = cell.getStringCellValue();
				if (StringUtils.isNotEmpty(str)) {
					cellvalue = String.valueOf(str.trim());
				}
				break;
			case XSSFCell.CELL_TYPE_FORMULA: {
				// 判断当前的cell是否为Date
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					Date date = cell.getDateCellValue();
					cellvalue = sdf.format(date);
				}
				// 如果是纯数字
				else {
					// 取得当前Cell的数值
					// cellvalue = String.valueOf(cell.getStringCellValue());
					str = cell.getStringCellValue();
					if (StringUtils.isNotEmpty(str)) {
						cellvalue = String.valueOf(str.trim());
					}
				}
				break;
			}
			case XSSFCell.CELL_TYPE_STRING:
				// 如果当前Cell的Type为STRIN
				// 取得当前的Cell字符串
				// cellvalue = cell.getRichStringCellValue().getString().trim();
				str = cell.getStringCellValue();
				if (StringUtils.isNotEmpty(str)) {
					cellvalue = String.valueOf(str.trim());
				}
				break;
			default:
				// 默认的Cell值
				cellvalue = null;
			}
		}
		return cellvalue;

	}

	// public static void main(String[] args) {
	// try {
	// // 对读取Excel表格标题测试
	// InputStream is = new FileInputStream("d:\\test2.xls");
	// ImportExcelUtils excelReader = new ImportExcelUtils();
	// String[] title = excelReader.readExcelTitle(is);
	// System.out.println("获得Excel表格的标题:");
	// for (String s : title) {
	// System.out.print(s + " ");
	// }
	// System.out.println();
	//
	// // 对读取Excel表格内容测试
	// InputStream is2 = new FileInputStream("d:\\test2.xls");
	// Map<Integer, String> map = excelReader.readExcelContent(is2);
	// System.out.println("获得Excel表格的内容:");
	// // 这里由于xls合并了单元格需要对索引特殊处理
	// for (int i = 2; i <= map.size() + 1; i++) {
	// System.out.println(map.get(i));
	// }
	//
	// } catch (FileNotFoundException e) {
	// System.out.println("未找到指定路径的文件!");
	// e.printStackTrace();
	// }
	// }

}
