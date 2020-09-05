package com.dotop.smartwater.project.module.core.water.utils.file;

import jxl.CellView;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

public class CellStyleFactory {
	
	//COLOR
	public static final int COLOR_NONE =0; //Default
	public static final int COLOR_GREEN = 1;
	public static final int COLOR_RED = 2;
	public static final int COLOR_BLUE = 3;
	
	//ALIGNMENT
	public static final int ALIGNMENT_NONE = 0; //Default
	public static final int ALIGNMENT_LEFT = 1; 
	public static final int ALIGNMENT_CENTER = 2;
	public static final int ALIGNMENT_RIGHT = 3; 
	//VerticalAlignment
	public static final int VERTICALALIGNMENT_NONE = 0; //Default
	public static final int VERTICALALIGNMENT_BOTTOM = 1; 
	public static final int VERTICALALIGNMENT_CENTER = 2;
	public static final int VERTICALALIGNMENT_TOP = 3; 
	
	//
//	private List<WritableCellFormat> list = new ArrayList<WritableCellFormat>();
	public static WritableCellFormat getCellStyle(int length,int height) {
		WritableCellFormat wcf = new WritableCellFormat();
		try {
			wcf.setBackground(Colour.GREEN);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return wcf;

	}

	public static WritableCellFormat getCellStyle(WritableFont writableFont) {
		WritableCellFormat wcf = new WritableCellFormat(writableFont);
		try {
			wcf.setIndentation(24);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return wcf;
	}
	public static CellView getCellStyle() {
		CellView cell = new CellView();
		cell.setAutosize(true);
		return cell;
	}
	
	
//	private static WritableCellFormat headTopStyle;
	public static WritableCellFormat getHeadTopStyle() {
//		if(headTopStyle==null){
			WritableCellFormat wcf = new WritableCellFormat();
			try {
//				wcf.setBackground(Colour.BROWN);
				wcf.setIndentation(0);
				wcf.setWrap(false);
				wcf.setAlignment(Alignment.LEFT);
//				wcf.setBackground(Colour.GREEN);
				
				WritableFont wf = new WritableFont(WritableFont.ARIAL, 24);
//				wf.setColour(Colour.RED);
				wf.setBoldStyle(WritableFont.BOLD);
				wcf.setFont(wf);
			} catch (WriteException e) {
				e.printStackTrace();
			}
			return wcf;
			
//			headTopStyle = wcf;
//		}
//		return headTopStyle;
	}
	
//	private static WritableCellFormat headStyle;
	public static WritableCellFormat getHeadStyle() {
//		if(headStyle==null){
			WritableCellFormat wcf = new WritableCellFormat();
			try {
				wcf.setIndentation(0);
				wcf.setWrap(false);
				wcf.setAlignment(Alignment.LEFT);
				
				WritableFont wf = new WritableFont(WritableFont.ARIAL, 12);
				wf.setBoldStyle(WritableFont.BOLD);
				wcf.setFont(wf);
			} catch (WriteException e) {
				e.printStackTrace();
			}
			return wcf;
			
//			headStyle = wcf;
//		}
//		return headStyle;
	}

	public static WritableCellFormat getMenuTopStyle() {
		WritableCellFormat wcf = new WritableCellFormat();
		try {
			wcf.setIndentation(0);
			wcf.setWrap(false);
			wcf.setAlignment(Alignment.LEFT);
			
			WritableFont wf = new WritableFont(WritableFont.ARIAL, 12);
			wf.setBoldStyle(WritableFont.BOLD);
			wcf.setFont(wf);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return wcf;
	}

	public static WritableCellFormat getMenupStyle() {
		WritableCellFormat wcf = new WritableCellFormat();
		try {
			wcf.setIndentation(0);
			wcf.setWrap(false);
			wcf.setAlignment(Alignment.LEFT);
			
			WritableFont wf = new WritableFont(WritableFont.ARIAL, 12);
			wcf.setFont(wf);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return wcf;
	}

	public static WritableCellFormat getSchemeTopStyle() {
		WritableCellFormat wcf = new WritableCellFormat();
		try {
			wcf.setIndentation(0);
			wcf.setWrap(false);
			wcf.setAlignment(Alignment.LEFT);
			
			WritableFont wf = new WritableFont(WritableFont.ARIAL, 12);
			wf.setBoldStyle(WritableFont.BOLD);
			wcf.setFont(wf);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return wcf;
	}

	public static WritableCellFormat getSchemeStyle() {
		WritableCellFormat wcf = new WritableCellFormat();
		try {
			wcf.setIndentation(0);
			wcf.setWrap(false);
			wcf.setAlignment(Alignment.LEFT);
			
			WritableFont wf = new WritableFont(WritableFont.ARIAL);
			wcf.setFont(wf);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return wcf;
	}
	public static WritableCellFormat getItemSchemeStyle() {
		WritableCellFormat wcf = new WritableCellFormat();
		try {
			wcf.setIndentation(0);
			wcf.setWrap(true);
			wcf.setAlignment(Alignment.CENTRE);
			wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
			
			WritableFont wf = new WritableFont(WritableFont.ARIAL);
			wcf.setFont(wf);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return wcf;
	}

	public static WritableCellFormat getTaskContentStyle(int clr,int alg,int valg) {
		WritableCellFormat wcf = new WritableCellFormat();
		try {
			wcf.setIndentation(0);
			wcf.setWrap(true);

			WritableFont wf = new WritableFont(WritableFont.ARIAL);
			wcf.setFont(wf);
			
			switch(clr){
				case COLOR_GREEN: wcf.setBackground(Colour.GREEN);;break;
				case COLOR_RED:	wcf.setBackground(Colour.RED);break;
				case COLOR_BLUE:	wcf.setBackground(Colour.BLUE);break;
				default:
			}
			switch(alg){
				case ALIGNMENT_CENTER: wcf.setAlignment(Alignment.CENTRE);break;
				case ALIGNMENT_LEFT:   wcf.setAlignment(Alignment.LEFT);break;
				case ALIGNMENT_RIGHT:  wcf.setAlignment(Alignment.RIGHT);break;
				default:
			}
			switch(valg){
				case VERTICALALIGNMENT_CENTER:	wcf.setVerticalAlignment(VerticalAlignment.CENTRE);break;
				case VERTICALALIGNMENT_TOP:		wcf.setVerticalAlignment(VerticalAlignment.TOP);break;
				case VERTICALALIGNMENT_BOTTOM:	wcf.setVerticalAlignment(VerticalAlignment.BOTTOM);break;
				default:
			}
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return wcf;
	}

	public static WritableCellFormat getTaskHeadStyle() {
		WritableCellFormat wcf = new WritableCellFormat();
		try {
			wcf.setIndentation(0);
			wcf.setWrap(false);
			wcf.setAlignment(Alignment.CENTRE);
			
			WritableFont wf = new WritableFont(WritableFont.ARIAL, 12);
			wf.setBoldStyle(WritableFont.BOLD);
			wcf.setFont(wf);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return wcf;
	}

}
