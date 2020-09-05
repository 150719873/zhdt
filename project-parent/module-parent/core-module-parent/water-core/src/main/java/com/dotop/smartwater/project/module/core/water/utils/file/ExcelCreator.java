package com.dotop.smartwater.project.module.core.water.utils.file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;



import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class ExcelCreator {

	private OutputStream out;
	private WritableWorkbook workBook;
	private WritableSheet sheet;
	private int sheetCount;

	public ExcelCreator(String filepath) throws IOException {
		OutputStream os = new FileOutputStream(filepath);
		workBook = Workbook.createWorkbook(os);

		sheetCount = 0;
	}

	public void close() throws IOException {

		if (workBook != null) {
			try {
				workBook.write();
				workBook.close();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (out != null) {
			out.close();
		}
	}

	private WritableSheet getSheet(int i) throws ExcellException {
		if ((sheetCount - 1) == i && this.sheet != null) {
			return this.sheet;
		}

		WritableSheet sheet;
		if (workBook != null) {
			try {
				sheet = workBook.getSheet(i);
			} catch (IndexOutOfBoundsException e) {
				throw new ExcellException(e);
			}
		} else {
			throw new ExcellException("Workbook Not Create");
		}

		return sheet;
	}
	public WritableSheet getSheet(){
		return this.sheet;
	}

	public WritableSheet CreateSheet(String name) {
		WritableSheet sheet = workBook.createSheet(name, sheetCount++);
		this.sheet = sheet;
		return sheet;
	}

	public void writeExcel(String string, int sheetIndex, int i, int j)
			throws ExcellException {
		try {
			WritableSheet sheet = getSheet(sheetIndex);
			Label ClsinsCell = new Label(i, j, string);
			sheet.addCell(ClsinsCell);
		} catch (WriteException e) {
			throw new ExcellException(e);
		}
	}
	public void writeExcel(String string, int sheetIndex, int i, int j,CellFormat format)
			throws ExcellException {
		try {
			WritableSheet sheet = getSheet(sheetIndex);
			Label ClsinsCell = new Label(i, j, string,format);
			sheet.addCell(ClsinsCell);
		} catch (WriteException e) {
			throw new ExcellException(e);
		}
	}

	/**
	 * write into last sheet
	 *
	 * @param i
	 * @param j
	 * @throws ExcellException
	 */
	public void writeExcel(String string, int i, int j) throws ExcellException {
		writeExcel(string, sheetCount - 1, i, j);
	}
	public void writeExcel(String string, int i, int j,CellFormat format) throws ExcellException {
		writeExcel(string, sheetCount - 1, i, j,format);
	}

	public void writeExcel(Object obj, int i, int j,CellFormat format) throws ExcellException {
		if(obj == null) {
			writeExcel("", sheetCount - 1, i, j,format);
		}else {
			writeExcel(obj.toString(), sheetCount - 1, i, j,format);
		}

	}

	public WritableWorkbook getWorkBook() {
		return workBook;
	}

	public void setWorkBook(WritableWorkbook workBook) {
		this.workBook = workBook;
	}

	public WritableSheet findSheetByName(String headTestSheme) {
		WritableSheet[]  sheets = workBook.getSheets();
		if(sheets!=null ){
			for (WritableSheet ws : sheets) {
				if(ws.getName().equals(headTestSheme)){
					return ws;
				}
			}
		}
		return null;
	}


	/**
	 * @param column 开始列
	 * @param line 开始行
	 * @param column2 结束列
	 * @param line2 结束行
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void mergeCells(int column, int line, int column2, int line2) throws WriteException{
		sheet.mergeCells(column, line, column2, line2);
	}

	public void setColumnView(int i, int j) {
		sheet.setColumnView(i,j);
	}
}
