package com.dotop.pipe.data.report.core.vo;

import java.util.List;

import lombok.Data;

/**
 *
 * @date 2018/11/2.
 */
@Data
public class RegionReportVo {
	public String regionId;
	public String regionName;
	public String regionCode;
	public String regionDes;
	public Double flwTotalValue;
	public Integer deviceCount;
	public String deviceId;
	public List<ReportVo> deviceList;
}
