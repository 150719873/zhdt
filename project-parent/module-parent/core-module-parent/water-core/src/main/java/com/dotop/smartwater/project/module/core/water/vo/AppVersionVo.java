package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;
/**
 * App版本控制
 *

 * @date 2019年3月5日 15:45
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AppVersionVo extends BaseVo {

	public final static String APPVERSION_PUBLISH = "publish"; // 已发布状态
	public final static String APPVERSION_WAITPUBLISH = "wait";// 待发布状态

	public final static String APPVERSION_AUTOUPDATE = "autoUpdate";// 强制升级
	public final static String APPVERSION_UPDATE = "update";// 非强制升级

	// 主键
	private String id;
	// 唯一编码
	private String code;
	// 名称
	private String name;
	// 版本号
	private String versionNo;
	//初始版本
	private String initialVersion;
	//版本序号(用来判断是否为最新版本)
	private Integer versionCode;
	//文件秘钥(MD5加密文件内容)
	private String md5Key;
	//附件
	private String access;
	// 附件Map
	private Map<String, String> accessMap;
	// 强制升级
	private String upgrade;
	// 二维码
	private String qrCode;
	// 创建人
	private String createUserId;
	// 创建人
	private String createUserName;
	// 创建时间
	private Date createTime;
	// 状态
	private String status;
	// 发布人
	private String publishUserId;
	// 发布人
	private String publishUserName;
	// 发布时间
	private Date publishTime;
	//预览图
	private String imgUrl;
	//版本简介
	private String introduction;
	//新增版本标识
	private Boolean sign;
}
