package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 返回文件存储路径和md5秘钥

 * @date 2019-08-27 14:02
 *
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class AppVersionMd5Vo extends BaseVo {
	//文件路径
	private String url;
	//MD5秘钥
	private String md5Key;
}
