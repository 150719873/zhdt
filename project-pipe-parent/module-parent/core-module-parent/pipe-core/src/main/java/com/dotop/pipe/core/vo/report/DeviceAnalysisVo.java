package com.dotop.pipe.core.vo.report;

import java.util.List;

import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceAnalysisVo extends BasePipeVo {

	// 设备总数
	private int totalNum;

	// 报警设备
	private int alarmNum;

	// 在线设备
	private int onlineNum;

	// 离线设备
	private int offlineNum;

	// 设备信息
	private List<DeviceVo> devices;

	public DeviceAnalysisVo() {
		super();
	}

	public DeviceAnalysisVo(int totalNum, int alarmNum, int onlineNum, int offlineNum) {
		super();
		this.totalNum = totalNum;
		this.alarmNum = alarmNum;
		this.onlineNum = onlineNum;
		this.offlineNum = offlineNum;
	}

	public DeviceAnalysisVo(List<DeviceVo> devices) {
		super();
		this.devices = devices;
	}

}
