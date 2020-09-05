package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.BillForm;
import com.dotop.smartwater.project.module.core.water.form.DesignPrintForm;
import com.dotop.smartwater.project.module.core.water.form.PrintBindForm;
import com.dotop.smartwater.project.module.core.water.vo.DesignPrintVo;
import com.dotop.smartwater.project.module.core.water.vo.PrintBindVo;

import java.util.List;
import java.util.Map;

/**
 * 打印接口
 * @program: project-parent
 * @description: 打印设计

 * @create: 2019-02-26 09:23
 **/
public interface IDesignPrintFactory extends BaseFactory<PrintBindForm, PrintBindVo> {

	/**
	 * 打印绑定列表
	 * @param printBindForm 参数对象
	 * @return 打印绑定列表
	 */
	List<PrintBindVo> templateList(PrintBindForm printBindForm) ;

	/**
	 * 打印预览
	 * 
	 * @param designPrintForm 参数对象
	 * @return 结果
	 * @
	 */
	String view(DesignPrintForm designPrintForm) ;

	/**
	 * 预览sql
	 * 
	 * @param designPrintForm 参数对象
	 * @return sql
	 */
	Map<String, String> preview(DesignPrintForm designPrintForm) ;

	/**
	 * 打印列表分页查询
	 * @param designPrintForm 参数对象
	 * @return 打印分页
	 */
	Pagination<DesignPrintVo> getDesignPrintList(DesignPrintForm designPrintForm) ;

	/**
	 * 保存打印设计
	 * @param designPrintForm 参数对象
	 */
	void saveDesignPrint(DesignPrintForm designPrintForm) ;

	/**
	 * 账单信息打印
	 * @param bill 参数对象
	 * @return 结果
	 */
	String viewDetail(BillForm bill) ;

	/**
	 * 获取打印对象
	 * @param designPrintForm 参数对象
	 * @return 打印对象
	 */
	DesignPrintVo get(DesignPrintForm designPrintForm) ;

	/**
	 * 删除
	 * @param designPrintForm 参数对象
	 */
	void delete(DesignPrintForm designPrintForm) ;

	/**
	 * 增加模板
	 * @param designPrintForm 参数对象
	 */
	void addTemplate(DesignPrintForm designPrintForm) ;

	/**
	 * 更新模板
	 * @param designPrintForm 参数对象
	 */
	void updateTemplate(DesignPrintForm designPrintForm) ;

	/**
	 * 打印状态
	 * @param designPrintForm 参数对象
	 * @return 打印状态
	 */
	DesignPrintVo getPrintStatus(DesignPrintForm designPrintForm) ;

	/**
	 * 打印预览
	 * @param designPrintForm 参数对象
	 * @return 打印预览
	 */
	String getPrintView(DesignPrintForm designPrintForm) ;

	/**
	 * 预览
	 * @param designPrintForm 参数对象
	 * @return 预览
	 */
	String getPreview(DesignPrintForm designPrintForm) ;

}
