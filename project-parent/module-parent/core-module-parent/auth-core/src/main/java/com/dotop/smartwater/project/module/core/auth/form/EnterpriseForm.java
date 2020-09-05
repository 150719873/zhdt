package com.dotop.smartwater.project.module.core.auth.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class EnterpriseForm extends BaseForm{
	private String enterpriseid;

	private String name;

	private String website;

	private String description;

	private Date createtime;

	private String createuser;

	private String enprno;

	/**
	 * 地图中心经纬度
	 */
	private String center;

	/**
	 * 地图边界
	 */
	private String extent;

	/**
	 * 地图执照
	 */
	private String license;

	private String proleid;

	private Boolean calibration;

	/**
	 * 失效状态，0有效，1失效
	 */
	private Integer failureState;

	/**
	 * 失效时间
	 */
	private Date failureTime;
	/**显示用户操作手册*/
	private String userManual;
	/**显示工作中心App*/
	private String workApp;
	/**显示绑定App*/
	private String bindApp;
	
	/**域名前缀*/
	private String websitePrefix;
	/**域名后缀*/
	private String websiteSuffix;
}
