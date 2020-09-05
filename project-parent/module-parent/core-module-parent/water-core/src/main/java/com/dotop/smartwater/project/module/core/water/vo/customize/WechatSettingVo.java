package com.dotop.smartwater.project.module.core.water.vo.customize;

import lombok.Data;
import lombok.EqualsAndHashCode;

//表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatSettingVo extends WechatPublicSettingVo {

	private String enterprisename;

}
