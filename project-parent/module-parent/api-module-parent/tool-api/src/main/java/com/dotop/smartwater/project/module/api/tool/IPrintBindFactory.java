package com.dotop.smartwater.project.module.api.tool;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.DesignPrintForm;
import com.dotop.smartwater.project.module.core.water.form.PrintBindForm;
import com.dotop.smartwater.project.module.core.water.vo.DesignPrintVo;
import com.dotop.smartwater.project.module.core.water.vo.PrintBindVo;

import java.util.List;

/**
 * 微信接入公共配置
 * 

 * @date 2019年4月1日
 */
public interface IPrintBindFactory extends BaseFactory<PrintBindForm, PrintBindVo> {

	/**
	 * 分页查询
	 */
	@Override
	public Pagination<PrintBindVo> page(PrintBindForm printBindForm);

	/**
	 * 查询询所有模板
	 * 
	 * @param printBindForm
	 * @return @
	 */
	public List<PrintBindVo> listAll(PrintBindForm printBindForm);

	/**
	 * 查询详情
	 */
	public DesignPrintVo get(DesignPrintForm designPrintForm);

	/**
	 * 新增
	 */
	@Override
	public PrintBindVo add(PrintBindForm printBindForm);

	/**
	 * 查询详情
	 *
	 * @return @
	 */
	public DesignPrintVo getRelationDesign(DesignPrintForm designPrintForm);

	/**
	 * 删除
	 */
	@Override
	public String del(PrintBindForm printBindForm);
	
	PrintBindVo getPrintStatus(String enterpriseid, Integer intValue);
}
