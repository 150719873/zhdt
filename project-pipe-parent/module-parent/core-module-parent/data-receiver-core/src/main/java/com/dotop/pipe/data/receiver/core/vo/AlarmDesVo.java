package com.dotop.pipe.data.receiver.core.vo;

import lombok.Data;

@Data
public class AlarmDesVo {

	private boolean status = false;
	private String alarmName;
	private String alarmDes;

}
