package com.dotop.pipe.core.utils;

public class AreaUtils {

	/**
	 * 由根节点的情况下 区域新增新增主键
	 * 
	 * @return
	 */
	public final static String createAreaModelKey(String maxAreaCode) {
		// 1 查看父节点的area_code areaModel.getParentCode();
		// 2 父节点下的第一层子节点的area_code
		// 3 转换成int 加一

		// 编码去掉后三位
		String areaCodeRemoveThree = maxAreaCode.substring(0, maxAreaCode.length() - 3);
		// 编码后三位
		String areaCodeLastThree = maxAreaCode.substring(maxAreaCode.length() - 3, maxAreaCode.length());
		// 编码的后三位+1操作
		int areaCodeAddOne = Integer.parseInt(areaCodeLastThree) + 1;
		String newAreaCode;
		/*
		 * if (areaCodeAddOne < 10) { newAreaCode = areaCodeRemoveThree +
		 * PipeConstants.AREA_CODE_ADD_DZERO + areaCodeAddOne; } else if (areaCodeAddOne
		 * < 100) { newAreaCode = areaCodeRemoveThree +
		 * PipeConstants.AREA_CODE_ADD_SZERO + areaCodeAddOne; } else { newAreaCode =
		 * areaCodeRemoveThree + areaCodeAddOne; }
		 */

		newAreaCode = areaCodeRemoveThree + String.format("%3d", areaCodeAddOne).replace(" ", "0");

		return newAreaCode;
	}
}
