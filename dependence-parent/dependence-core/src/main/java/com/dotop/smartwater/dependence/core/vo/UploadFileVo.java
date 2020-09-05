package com.dotop.smartwater.dependence.core.vo;

import java.io.File;

import lombok.Data;

/**
 * 

 * @date 2019年5月8日
 * @description 文件上传抽象类
 */
@Data
public class UploadFileVo {

	private String fileName;

	private String filePath;

	private Long length;

	private File file;

}
