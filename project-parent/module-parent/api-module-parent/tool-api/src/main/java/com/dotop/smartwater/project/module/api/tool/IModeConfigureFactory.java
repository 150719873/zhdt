package com.dotop.smartwater.project.module.api.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.water.form.ModeBindForm;
import com.dotop.smartwater.project.module.core.water.vo.ModeBindVo;

/**
 * 

 * @description 通讯方式配置
 * @date 2019-10-17 15:08
 *
 */
public interface IModeConfigureFactory extends BaseFactory<ModeBindForm, ModeBindVo> {
	/**
	 * 通讯方式配置
	 * @param modeBindForm
	 * @return
	 */
	String configureMode(List<ModeBindForm> modeBindForms);
	/**
	 * 获取通讯方式配置
	 * @param modeBindForm
	 * @return
	 */
	List<ModeBindVo> listModeConfigure(ModeBindForm modeBindForm);
}
