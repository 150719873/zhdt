package com.dotop.pipe.core.vo.historylog;

import java.util.Date;

import com.dotop.pipe.core.vo.common.DictionaryVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HistoryLogVo extends BasePipeVo {
	private String id;
	private String deviceId;
	private String fieldName;
	private String fieldNewVal;
	private String fieldOldVal;
	private Date createDate;
	private String createBy;
	private DeviceVo device;
	private String categoryName;
	private String categoryVal;
	private String deviceCode;
	// 敷设类型
	private DictionaryVo laying;
}
