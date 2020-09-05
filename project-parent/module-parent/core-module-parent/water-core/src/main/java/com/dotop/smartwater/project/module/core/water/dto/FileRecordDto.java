package com.dotop.smartwater.project.module.core.water.dto;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件上传记录Dto

 * @date 2019-08-21 15:30
 *
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class FileRecordDto extends BaseDto{
	//记录ID
	private String fileRecordId;
	//文件名称
	private String fileName;
	//文件大小
	private Long fileSize;
	//文件类型
	private String fileType;
	//文件来源
	private String fileSource;
	//文件地址
	private String fileAddress;
	//上传时间
	private Date uploadTime;
	//上传人ID
	private String uploadUserId;
}
