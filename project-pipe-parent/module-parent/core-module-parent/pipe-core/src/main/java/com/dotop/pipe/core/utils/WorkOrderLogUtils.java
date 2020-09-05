package com.dotop.pipe.core.utils;

import java.util.Map;

import org.joda.time.DateTime;

import com.dotop.smartwater.dependence.core.vo.CompareVo;

public class WorkOrderLogUtils {

	public final static String toString(Map<String, CompareVo> map) {
		StringBuffer sb = new StringBuffer();
		CompareVo compareVo = null;
		for (String key : map.keySet()) {
			compareVo = map.get(key);
			switch (key) {
			case "name":
				sb.append("工单名称--");
				sb.append("修改前:" + compareVo.getBeforeValue() + ";");
				sb.append("修改后:" + compareVo.getAfterValue() + ".");
				break;
			case "des":
				sb.append("工单描述--");
				sb.append("修改前:" + compareVo.getBeforeValue() + ";");
				sb.append("修改后:" + compareVo.getAfterValue() + ".");
				break;
			case "startDate":
				sb.append("施工开始时间--");
				if (compareVo.getBeforeValue() != null) {
					sb.append("修改前:" + new DateTime(compareVo.getBeforeValue()).toString("yyyy-MM-dd HH:mm:ss") + ";");
				}
				if (compareVo.getAfterValue() != null) {
					sb.append("修改后:" + new DateTime(compareVo.getAfterValue()).toString("yyyy-MM-dd HH:mm:ss") + ".");
				}
				break;
			case "endDate":
				sb.append("施工结束时间--");
				if (compareVo.getBeforeValue() != null) {
					sb.append("修改前:" + new DateTime(compareVo.getBeforeValue()).toString("yyyy-MM-dd HH:mm:ss") + ";");
				}
				if (compareVo.getAfterValue() != null) {
					sb.append("修改后:" + new DateTime(compareVo.getAfterValue()).toString("yyyy-MM-dd HH:mm:ss") + ".");
				}
				break;
			case "intro":
				sb.append("工单介绍--");
				sb.append("修改前:" + compareVo.getBeforeValue() + ";");
				sb.append("修改后:" + compareVo.getAfterValue() + ".");
				break;
			case "team":
				sb.append("施工团队--");
				sb.append("修改前:" + compareVo.getBeforeValue() + ";");
				sb.append("修改后:" + compareVo.getAfterValue() + ".");
				break;
			case "teamLeader":
				sb.append("施工负责人--");
				sb.append("修改前:" + compareVo.getBeforeValue() + ";");
				sb.append("修改后:" + compareVo.getAfterValue() + ".");
				break;
			case "eval":
				sb.append("施工评估--");
				sb.append("修改前:" + compareVo.getBeforeValue() + ";");
				sb.append("修改后:" + compareVo.getAfterValue() + ".");
				break;
			case "survey":
				sb.append("实地勘测--");
				sb.append("修改前:" + compareVo.getBeforeValue() + ";");
				sb.append("修改后:" + compareVo.getAfterValue() + ".");
				break;
			case "prepareFeed":
				sb.append("备料信息--");
				sb.append("修改前:" + compareVo.getBeforeValue() + ";");
				sb.append("修改后:" + compareVo.getAfterValue() + ".");
				break;
			case "areaId":
				sb.append("所属区域--");
				sb.append("修改前:" + compareVo.getBeforeValue() + ";");
				sb.append("修改后:" + compareVo.getAfterValue() + ".");
				break;
			default:
				break;
			}
		}

		return sb.toString();
	}
}
