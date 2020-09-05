package com.dotop.smartwater.project.module.api.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterTmplForm;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 工作中心模板管理
 * 

 * @date 2019年4月17日
 * @description
 */
public interface ITmplFactory extends BaseFactory<WorkCenterTmplForm, WorkCenterTmplVo> {
	/**
	 * 新增
	 */
	@Override
	WorkCenterTmplVo add(WorkCenterTmplForm tmplForm) ;

	/**
	 * 查询详情
	 */
	@Override
	WorkCenterTmplVo get(WorkCenterTmplForm tmplForm) ;

	/**
	 * 查询分页
	 */
	@Override
	Pagination<WorkCenterTmplVo> page(WorkCenterTmplForm tmplForm) ;

	/**
	 * 修改
	 */
	@Override
	WorkCenterTmplVo edit(WorkCenterTmplForm tmplForm) ;

	/**
	 * 删除
	 */
	@Override
	String del(WorkCenterTmplForm tmplForm) ;

	/**
	 * 查询模板选择下拉
	 */
	Pagination<WorkCenterTmplVo> select(WorkCenterTmplForm tmplForm) ;

	/**
	 * 修改模板的节点集合
	 */
	void editNodes(WorkCenterTmplForm tmplForm) ;

	/**
	 * 复制模板
	 */
	WorkCenterTmplVo copy(WorkCenterTmplForm tmplForm) ;

	/**
	 * 模板导出
	 */
	String export(WorkCenterTmplForm tmplForm) ;

	/**
	 * 模板导入
	 */
	void imports(MultipartFile file) ;

	/**
	 * 查询运维表单下载模板分页
	 */
	Pagination<WorkCenterTmplVo> pageByAdmin(WorkCenterTmplForm tmplForm) ;

}
