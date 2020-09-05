package com.dotop.pipe.core.dto.historylog;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class ChangeDto {

	public ChangeDto() {

	}

	public ChangeDto(String fieldName, Object fieldOldVal, Object fieldNewVal) {
		this.fieldName = fieldName;
		this.fieldOldVal = String.valueOf(fieldOldVal);
		this.fieldNewVal = String.valueOf(fieldNewVal);

		if (StringUtils.isBlank(this.fieldOldVal) || "null".equals(this.fieldOldVal)) {
			this.fieldOldVal = "";
		}
		if (StringUtils.isBlank(this.fieldNewVal) || "null".equals(this.fieldNewVal)) {
			this.fieldNewVal = "";
		}
	}

	private String id = UUID.randomUUID().toString();
	private String fieldName;
	private String fieldOldVal;
	private String fieldNewVal;

}
