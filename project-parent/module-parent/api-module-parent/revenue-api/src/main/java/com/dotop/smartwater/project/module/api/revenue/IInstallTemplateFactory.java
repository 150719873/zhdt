package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.InstallTemplateForm;
import com.dotop.smartwater.project.module.core.water.vo.InstallFunctionVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallTemplateRelationVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallTemplateVo;

import java.util.List;

/**
 * 报装模板

 */
public interface IInstallTemplateFactory extends BaseFactory<InstallTemplateForm, InstallTemplateVo> {

	/**
	 * 报装模板分页查询
	 */
	@Override
	Pagination<InstallTemplateVo> page(InstallTemplateForm form) ;

	/**
	 * 新增报装模板信息
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	boolean saveTemp(InstallTemplateForm form) ;

	/**
	 * 获取功能列表
	 * 
	 * @return
	 * @
	 */
	List<InstallFunctionVo> getFuncs() ;

	/**
	 * 获取模板信息
	 *
	 * @param form 模板对象
	 * @return 模板对象
	 */
	InstallTemplateVo getTemp(InstallTemplateForm form) ;

	/**
	 * 修改报装模板信息
	 * 
	 * @param form 模板对象
	 * @return 模板对象
	 * @
	 */
	boolean editTemp(InstallTemplateForm form) ;

	/**
	 * 删除报装模板信息
	 * 
	 * @param form 模板对象
	 * @return 模板对象
	 * @
	 */
	boolean deleteTemp(InstallTemplateForm form) ;

	/**
	 * 获取报装流程节点信息
	 * 
	 * @param form 模板对象
	 * @return
	 * @
	 */
	List<InstallTemplateRelationVo> getTempNodes(InstallTemplateForm form) ;

}
