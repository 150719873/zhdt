package com.dotop.smartwater.project.auth.service;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.bo.MdDockingBo;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingVo;
import com.dotop.smartwater.project.module.core.auth.vo.md.ConfigListVo;

import java.util.List;

/**

 * @date 2019年5月9日
 * @description
 */
public interface IMdDockingService extends BaseService<MdDockingBo, MdDockingVo> {

	@Override
	Pagination<MdDockingVo> page(MdDockingBo bo);

	@Override
	MdDockingVo get(MdDockingBo bo);

	@Override
	MdDockingVo add(MdDockingBo bo);

	@Override
	MdDockingVo edit(MdDockingBo bo);

	@Override
	String del(MdDockingBo bo);

	@Override
	List<MdDockingVo> list(MdDockingBo bo);

	void delByMdeId(String mdeId);

	void delByMdeIdAndFactory(String mdeId,String factory);

	void updateByMdeId(String mdeId,Boolean isDel);

	void save(String mdeId, List<MdDockingVo> list);

	ConfigListVo loadConfigList(String enterpriseid,String systemCode, String modeCode);

	void delByFactoryAndType(MdDockingBo mdDockingBo);
}
