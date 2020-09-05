package com.dotop.smartwater.project.module.service.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.ModeBindBo;
import com.dotop.smartwater.project.module.core.water.vo.ModeBindVo;

/**
 * 

 * @description 通讯方式配置
 * @date 2019-10-17 15:30
 *
 */
public interface IModeConfigureService extends BaseService<ModeBindBo, ModeBindVo> {

	/**
	 * 配置通讯方式
	 * @param modeBindBos
	 * @return
	 */
	String configureMode(List<ModeBindBo> modeBindBos);
	/**
	 * 获取通讯方式配置
	 * @param modeBindBo
	 * @return
	 */
	List<ModeBindVo> listModeConfigure(ModeBindBo modeBindBo);
}
