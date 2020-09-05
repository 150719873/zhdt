package com.dotop.pipe.core.form;

import java.util.UUID;

import lombok.Data;

@Data
public class PointMapForm {
	// 主键
	private String mapId = UUID.randomUUID().toString();
	private String pointId;
	private String deviceId;
}
