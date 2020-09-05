package com.dotop.pipe.core.form;

import lombok.Data;

@Data
public class LeakageAnalysisItemForm {

	private String areaId;    // 区域id

	private String deviceId;  // 设备id

	private String direction; // 流向

	private String multiple;  // 倍数
}
