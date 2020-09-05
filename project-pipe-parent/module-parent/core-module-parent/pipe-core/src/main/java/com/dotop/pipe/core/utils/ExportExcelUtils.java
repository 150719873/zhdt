package com.dotop.pipe.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.dotop.pipe.core.vo.common.DictionaryVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.smartwater.dependence.core.log.LogMsg;

public class ExportExcelUtils {

	private final static Logger logger = LogManager.getLogger(ExportExcelUtils.class);

	// 显示的导出表的标题
	private String title;
	// 导出表的列名
	private String[] columnName;

	// 标题
	private Map<String, ExportExcelVo> eeMap;

	// 需要导出的数据集合
	private List<Object[]> dataList = new ArrayList<Object[]>();

	private List<DeviceVo> dataList07 = new ArrayList<DeviceVo>();

	private Map<String, Integer> rowNumMap = new HashMap<String, Integer>();

	/**
	 * 
	 * @param title
	 * @param columnName
	 * @param dataList
	 * @param request
	 * @param response
	 * @description 构造方法，传入要导出的数据
	 */
	// public ExportExcelUtils(String title, String[] columnName, List<Object[]>
	// dataList, List<DeviceVo> dataList07) {
	// this.dataList = dataList;
	// this.dataList07 = dataList07;
	// this.columnName = columnName;
	// this.title = title;
	// }

	public ExportExcelUtils(Map<String, ExportExcelVo> eeMap, List<DeviceVo> dataList07) {
		this.dataList07 = dataList07;
		this.eeMap = eeMap;
	}

	/**
	 * 导出execl 03 版
	 * 
	 * @return
	 * @throws Exception
	 */

	public HSSFWorkbook export() throws Exception {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
			HSSFSheet sheet = workbook.createSheet(title); // 创建工作表

			// 产生表格标题行
			HSSFRow rowm = sheet.createRow(0);
			HSSFCell cellTiltle = rowm.createCell(0);

			// 设置标题和单元格样式
			HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook); // 获取列头样式对象
			HSSFCellStyle style = this.getStyle(workbook); // 单元格样式对象

			// 合并单元格
			sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (columnName.length - 1)));
			cellTiltle.setCellStyle(columnTopStyle);
			cellTiltle.setCellValue(title);

			// 定义所需列数
			int columnNum = columnName.length;
			HSSFRow rowRowName = sheet.createRow(2); // 在索引2的位置创建行(最顶端的行开始的第二行)

			// 将列头设置到sheet的单元格中
			for (int n = 0; n < columnNum; n++) {
				HSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
				cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
				HSSFRichTextString text = new HSSFRichTextString(columnName[n]);
				cellRowName.setCellValue(text); // 设置列头单元格的值
				cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
			}

			// 将查询出的数据设置到sheet对应的单元格中
			for (int i = 0; i < dataList.size(); i++) {
				Object[] obj = dataList.get(i);// 遍历每个对象
				HSSFRow row = sheet.createRow(i + 3);// 创建所需的行数
				for (int j = 0; j < obj.length; j++) {
					HSSFCell cell = null; // 设置单元格的数据类型
					// 第一列为数字类型并设置单元格的值
					if (j == 0) {
						cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(i + 1);
					} else {
						// 其他列为字符串类型并设置单元格的值
						cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
						if (!"".equals(obj[j]) && obj[j] != null) {
							cell.setCellValue(obj[j].toString());
						}
					}
					cell.setCellStyle(style); // 设置单元格样式
				}
			}

			// 让列宽随着导出的列长自动适应
			for (int colNum = 0; colNum < columnNum; colNum++) {
				int columnWidth = sheet.getColumnWidth(colNum) / 256;
				for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
					HSSFRow currentRow;
					// 当前行未被使用过
					if (sheet.getRow(rowNum) == null) {
						currentRow = sheet.createRow(rowNum);
					} else {
						currentRow = sheet.getRow(rowNum);
					}
					if (currentRow.getCell(colNum) != null) {
						// 取得当前的单元格
						HSSFCell currentCell = currentRow.getCell(colNum);
						// 如果当前单元格类型为字符串
						if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							int length = currentCell.getStringCellValue().getBytes().length;
							if (columnWidth < length) {
								// 将单元格里面值大小作为列宽度
								columnWidth = length;
							}
						}
					}
				}
				// 再根据不同列单独做下处理
				if (colNum == 0) {
					sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
				} else {
					sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
				}
			}

			/*
			 * if (workbook != null) { try { String fileName = "Excel-" +
			 * String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls"; String
			 * headStr = "attachment; filename=\"" + fileName + "\"";
			 * //response.setContentType("APPLICATION/OCTET-STREAM");
			 * response.setHeader("Content-Disposition", headStr);
			 * response.setContentType("application/x-download"); OutputStream out1 =
			 * response.getOutputStream(); workbook.write(out1); out1.flush(); out1.close();
			 * File file = new File("E:/down/" + fileName); if
			 * (!file.getParentFile().exists()) { file.getParentFile().mkdirs(); } if
			 * (!file.exists()) { file.createNewFile(); } FileOutputStream out2 = new
			 * FileOutputStream(file); workbook.write(out2); out2.flush(); out2.close(); }
			 * catch (IOException e) { e.printStackTrace(); } }
			 */
			return workbook;

		} catch (Exception e) {
			logger.error(LogMsg.to(e));
		}
		return null;

	}

	/**
	 * 
	 * @date 2017/10/19 13:31
	 * @description 标题行的单元格样式
	 * @param workbook
	 * @return
	 */
	public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

		// 设置字体
		HSSFFont font = workbook.createFont();
		// 设置字体大小
		font.setFontHeightInPoints((short) 11);
		// 字体加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 设置字体名字
		font.setFontName("Courier New");
		// 设置样式;
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		// 设置底边框;
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		// 设置底边框颜色;
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		// 设置左边框;
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置左边框颜色;
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		// 设置右边框;
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		// 设置右边框颜色;
		style.setRightBorderColor(HSSFColor.BLACK.index);
		// 设置顶边框;
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// 设置顶边框颜色;
		style.setTopBorderColor(HSSFColor.BLACK.index);
		// 在样式用应用设置的字体;
		style.setFont(font);
		// 设置自动换行;
		style.setWrapText(false);
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return style;
	}

	public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
		// 设置字体
		HSSFFont font = workbook.createFont();
		// 设置字体大小
		// font.setFontHeightInPoints((short)10);
		// 字体加粗
		// font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 设置字体名字
		font.setFontName("Courier New");
		// 设置样式;
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置底边框;
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		// 设置底边框颜色;
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		// 设置左边框;
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置左边框颜色;
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		// 设置右边框;
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		// 设置右边框颜色;
		style.setRightBorderColor(HSSFColor.BLACK.index);
		// 设置顶边框;
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// 设置顶边框颜色;
		style.setTopBorderColor(HSSFColor.BLACK.index);
		// 在样式用应用设置的字体;
		style.setFont(font);
		// 设置自动换行;
		style.setWrapText(false);
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return style;
	}

	/**
	 * 导出execl 07 版
	 * 
	 * @return
	 * @throws Exception
	 */

	public XSSFWorkbook export07() throws Exception {
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(); // 创建工作簿对象

			// 设置标题和单元格样式
			XSSFCellStyle columnTopStyle = this.getColumnTopStyle07(workbook); // 获取列头样式对象
			XSSFCellStyle style = this.getStyle07(workbook); // 单元格样式对象

			// 创建工作表 根据titles 确定创建的工作表的页数及标题
			Map<String, XSSFSheet> sheetMap = this.createSheet(eeMap, workbook, columnTopStyle);

			// 遍历文件 将文件写入文件中
			// 将查询出的数据设置到sheet对应的单元格中
			for (int i = 0; i < dataList07.size(); i++) {
				DeviceVo deviceVo = dataList07.get(i);// 遍历每个对象
				ProductVo productVo = deviceVo.getProduct();
				if (productVo == null) {
					continue;
				}
				DictionaryVo dictionaryVo = productVo.getCategory();
				if (dictionaryVo == null) {
					continue;
				}
				String category = dictionaryVo.getVal();
				ExportExcelVo ee = eeMap.get(category);
				if (ee == null) {
					continue;
				}
				String categoryName = ee.getCategoryName();
				XSSFSheet sheet = sheetMap.get(categoryName);

				if (sheet != null) {
					int j = this.rowNumMap.get(categoryName);
					this.createCell(sheet, deviceVo, style, j, categoryName);
					this.rowNumMap.put(categoryName, j + 1);
				}
			}
			return workbook;
		} catch (Exception e) {
			logger.error(LogMsg.to(e));
		}
		return null;
	}

	/**
	 * 创建多个 sheet 页
	 * 
	 * @param titlessheet页的标题和字段
	 * @param workbook工作表
	 * @param columnTopStyle标题样式
	 * @return
	 */
	public Map<String, XSSFSheet> createSheet(Map<String, ExportExcelVo> eeMap, XSSFWorkbook workbook,
			XSSFCellStyle columnTopStyle) {
		Map<String, XSSFSheet> sheetMap = new HashMap<String, XSSFSheet>();
		for (String category : eeMap.keySet()) {
			ExportExcelVo ee = eeMap.get(category);
			XSSFSheet sheet = workbook.createSheet(ee.getCategoryName()); // 创建工作表并指定名称
			this.rowNumMap.put(ee.getCategoryName(), 1);
			String[] columnName = ee.getTitles();
			int columnNum = columnName.length;
			XSSFRow rowRowName = sheet.createRow(0); // 创建文件标题行
			// 将列头设置到sheet的单元格中
			for (int n = 0; n < columnNum; n++) {
				XSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
				cellRowName.setCellType(XSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
				XSSFRichTextString text = new XSSFRichTextString(columnName[n]);
				cellRowName.setCellValue(text); // 设置列头单元格的值
				cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
			}

			sheetMap.put(ee.getCategoryName(), sheet);
		}
		return sheetMap;
	}

	/**
	 * 创建单元格 并写入数据
	 * 
	 * @data 2018 12 06
	 * @param sheet单元页
	 * @param deviceVo实体对象
	 * @param style单元格样式
	 * @param i创建的单元格
	 * @param category设备类型
	 */
	public void createCell(XSSFSheet sheet, DeviceVo deviceVo, XSSFCellStyle style, int i, String category) {
		XSSFRow row = sheet.createRow(i);// 创建所需的行数
		XSSFCell cell = null; // 设置单元格的数据类型 这里可能根据要导出的数据列数 确认单元格
		cell = row.createCell(0, XSSFCell.CELL_TYPE_STRING); // 创建每行中的列
		cell.setCellValue(deviceVo.getCode()); // 设置列的值 1 设备编码
		cell.setCellStyle(style); // 设置单元格样式

		cell = row.createCell(1, XSSFCell.CELL_TYPE_STRING); // 创建每行中的列
		cell.setCellValue(deviceVo.getName()); // 设置列的值 2 设备名称
		cell.setCellStyle(style); // 设置单元格样式

		cell = row.createCell(2, XSSFCell.CELL_TYPE_STRING); // 创建每行中的列
		cell.setCellValue(deviceVo.getDes()); // 设置列的值 3 设备描述
		cell.setCellStyle(style); // 设置单元格样式

		cell = row.createCell(3, XSSFCell.CELL_TYPE_STRING); // 创建每行中的列
		cell.setCellValue(deviceVo.getLength()); // 设置列的值 3 长度
		cell.setCellStyle(style); // 设置单元格样式

		cell = row.createCell(4, XSSFCell.CELL_TYPE_STRING); // 创建每行中的列
		cell.setCellValue(deviceVo.getDepth()); // 设置列的值 4 深度
		cell.setCellStyle(style); // 设置单元格样式

		cell = row.createCell(5, XSSFCell.CELL_TYPE_STRING); // 创建每行中的列
		cell.setCellValue(deviceVo.getPipeElevation()); // 设置列的值 5管顶高程
		cell.setCellStyle(style); // 设置单元格样式

		cell = row.createCell(6, XSSFCell.CELL_TYPE_STRING); // 创建每行中的列
		cell.setCellValue(deviceVo.getGroundElevation()); // 设置列的值 6 地面高程
		cell.setCellStyle(style); // 设置单元格样式

		cell = row.createCell(7, XSSFCell.CELL_TYPE_STRING); // 创建每行中的列
		cell.setCellValue(deviceVo.getVersion()); // 设置列的值 7 版本
		cell.setCellStyle(style); // 设置单元格样式

		cell = row.createCell(8, XSSFCell.CELL_TYPE_STRING); // 创建每行中的列
		cell.setCellValue(deviceVo.getArea().getName()); // 设置列的值 8 区域名称
		cell.setCellStyle(style); // 设置单元格样式

		cell = row.createCell(9, XSSFCell.CELL_TYPE_STRING); // 创建每行中的列
		cell.setCellValue(deviceVo.getArea().getAreaCode()); // 设置列的值 9 区域编号
		cell.setCellStyle(style); // 设置单元格样式

		cell = row.createCell(10, XSSFCell.CELL_TYPE_STRING); // 创建每行中的列
		cell.setCellValue(deviceVo.getProduct().getCode()); // 设置列的值 产品编码
		cell.setCellStyle(style); // 设置单元格样式

		cell = row.createCell(11, XSSFCell.CELL_TYPE_STRING); // 创建每行中的列
		cell.setCellValue(deviceVo.getProduct().getCategory().getVal()); // 设置列的值 产品类型
		cell.setCellStyle(style); // 设置单元格样式

		cell = row.createCell(12, XSSFCell.CELL_TYPE_STRING); // 创建每行中的列
		cell.setCellValue(deviceVo.getPoints().get(0).getCode()); // 设置列的值 坐标编号
		cell.setCellStyle(style); // 设置单元格样式

		cell = row.createCell(13, XSSFCell.CELL_TYPE_STRING); // 创建每行中的列
		cell.setCellValue(deviceVo.getPoints().get(0).getLongitude().toString()); // 设置列的值 横坐标
		cell.setCellStyle(style); // 设置单元格样式

		cell = row.createCell(14, XSSFCell.CELL_TYPE_STRING); // 创建每行中的列
		cell.setCellValue(deviceVo.getPoints().get(0).getLatitude().toString()); // 设置列的值 纵坐标
		cell.setCellStyle(style); // 设置单元格样式

		if ("管道".equals(category)) {
			if (deviceVo.getPoints().get(1) != null) {
				cell = row.createCell(15, XSSFCell.CELL_TYPE_STRING); // 创建每行中的列
				cell.setCellValue(deviceVo.getPoints().get(1).getCode()); // 设置列的值 坐标编号
				cell.setCellStyle(style); // 设置单元格样式

				cell = row.createCell(16, XSSFCell.CELL_TYPE_STRING); // 创建每行中的列
				cell.setCellValue(deviceVo.getPoints().get(1).getLongitude().toString()); // 设置列的值 横坐标
				cell.setCellStyle(style); // 设置单元格样式

				cell = row.createCell(17, XSSFCell.CELL_TYPE_STRING); // 创建每行中的列
				cell.setCellValue(deviceVo.getPoints().get(1).getLatitude().toString()); // 设置列的值 纵坐标
				cell.setCellStyle(style); // 设置单元格样式
			}
		}

		// 设置单元格适应文件内容大小
		for (int colNum = 0; colNum < row.getLastCellNum(); colNum++) {
			int columnWidth = sheet.getColumnWidth(colNum) / 256;
			if (row.getCell(colNum) != null) {
				// 取得当前的单元格
				XSSFCell currentCell = row.getCell(colNum);
				// 如果当前单元格类型为字符串
				if (currentCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
					int length = currentCell.getStringCellValue().getBytes().length;
					if (columnWidth < length) {
						// 将单元格里面值大小作为列宽度
						columnWidth = length;
						sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
					}
				}
			}
		}

	}

	/**
	 * 
	 * @date 2017/10/19 13:31
	 * @description 标题行的单元格样式
	 * @param workbook
	 * @return
	 */
	public XSSFCellStyle getColumnTopStyle07(XSSFWorkbook workbook) {

		// 设置字体
		XSSFFont font = workbook.createFont();
		// 设置字体大小
		font.setFontHeightInPoints((short) 11);
		// 字体加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 设置字体名字
		font.setFontName("Courier New");
		// 设置样式;
		XSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		// 设置底边框;
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		// 设置底边框颜色;
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		// 设置左边框;
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置左边框颜色;
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		// 设置右边框;
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		// 设置右边框颜色;
		style.setRightBorderColor(HSSFColor.BLACK.index);
		// 设置顶边框;
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// 设置顶边框颜色;
		style.setTopBorderColor(HSSFColor.BLACK.index);
		// 在样式用应用设置的字体;
		style.setFont(font);
		// 设置自动换行;
		style.setWrapText(false);
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return style;
	}

	public XSSFCellStyle getStyle07(XSSFWorkbook workbook) {
		// 设置字体
		XSSFFont font = workbook.createFont();
		// 设置字体大小
		// font.setFontHeightInPoints((short)10);
		// 字体加粗
		// font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 设置字体名字
		font.setFontName("Courier New");
		// 设置样式;
		XSSFCellStyle style = workbook.createCellStyle();
		// 设置底边框;
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		// 设置底边框颜色;
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		// 设置左边框;
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置左边框颜色;
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		// 设置右边框;
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		// 设置右边框颜色;
		style.setRightBorderColor(HSSFColor.BLACK.index);
		// 设置顶边框;
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// 设置顶边框颜色;
		style.setTopBorderColor(HSSFColor.BLACK.index);
		// 在样式用应用设置的字体;
		style.setFont(font);
		// 设置自动换行;
		style.setWrapText(false);
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return style;
	}
}
