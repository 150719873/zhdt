package com.dotop.smartwater.project.module.service.tool;

import java.util.List;
import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DesignPrintBo;
import com.dotop.smartwater.project.module.core.water.bo.PrintBindBo;
import com.dotop.smartwater.project.module.core.water.vo.DesignPrintVo;
import com.dotop.smartwater.project.module.core.water.vo.PrintBindVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.DesignFieldVo;

/**
 * 字典配置
 *

 * @date 2019年2月23日
 */
public interface IDesignPrintService extends BaseService<PrintBindBo, PrintBindVo> {

	List<PrintBindVo> templateList(PrintBindBo printBindBo);

	/**
	 * 模板分页查询----对原来的方法进行修改
	 * 
	 * @param condition
	 * @return @
	 */
	Pagination<DesignPrintVo> getDesignPrintList(DesignPrintBo designPrintBo);

	/**
	 * 查询 详情
	 * 
	 * @param id
	 * @return @
	 */
	DesignPrintVo get(String id);

	int deleteDesignPrint(String id);

	List<DesignFieldVo> getFields(String id);

	int deleteDesignFields(String id);

	Boolean updateDesignPrint(DesignPrintBo designPrint);

	Boolean saveDesignPrint(DesignPrintBo designPrint);

	/**
	 * 打印预览
	 * 
	 * @param sql
	 * @return @
	 */
	Map<String, String> getView(String sql);

	List<Map<String, String>> getSqlView(String string);

	void updateTemplate(DesignPrintBo designPrintbo);

	void addTemplate(DesignPrintBo designPrintbo);

	DesignPrintVo getPrintStatus(String enterpriseid, Integer type);
}
