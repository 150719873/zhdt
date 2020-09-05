package com.dotop.smartwater.project.module.service.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DesignPrintBo;
import com.dotop.smartwater.project.module.core.water.bo.PrintBindBo;
import com.dotop.smartwater.project.module.core.water.vo.DesignPrintVo;
import com.dotop.smartwater.project.module.core.water.vo.PrintBindVo;

/**
 * 

 * @date 2019年4月2日
 */
public interface IPrintBindService extends BaseService<PrintBindBo, PrintBindVo> {

	/**
	 * 打印模板分页查询
	 * 
	 * @param wechatSettingParamBo
	 * @return
	 */
	@Override
	Pagination<PrintBindVo> page(PrintBindBo printBindBo);

	/**
	 * 模板查询
	 * 
	 * @param wechatSettingParamBo
	 * @return
	 */
	List<PrintBindVo> listAll();

	/**
	 * 
	 * 新增
	 */
	@Override
	PrintBindVo add(PrintBindBo printBindBo);

	/**
	 * 删除 绑定 del
	 */
	@Override
	String del(PrintBindBo printBindBo);

	/**
	 * 查询详情
	 */
	DesignPrintVo get(DesignPrintBo designPrintBo);

	/**
	 * getRelationDesign
	 */
	DesignPrintVo getRelationDesign(DesignPrintBo designPrintBo);

	/**
	 * 校验是否存在
	 * 
	 * @param wechatPublicSettingBo
	 * @return @
	 */
	@Override
	boolean isExist(PrintBindBo printBindBo);

	/**
	 * getPrintStatus 查询打印状态
	 */

	PrintBindVo getPrintStatus(String enterpriseid, Integer intValue);
}
