package com.dotop.smartwater.project.module.api.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterFormForm;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterFormVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 工作中心表单管理
 * 

 * @date 2019年4月17日
 * @description
 */
public interface IFormFactory extends BaseFactory<WorkCenterFormForm, WorkCenterFormVo> {

	/**
	 * 新增
	 */
	@Override
	WorkCenterFormVo add(WorkCenterFormForm formForm) ;

	/**
	 * 查询详情
	 */
	@Override
	WorkCenterFormVo get(WorkCenterFormForm formForm) ;

	/**
	 * 查询分页
	 */
	@Override
	Pagination<WorkCenterFormVo> page(WorkCenterFormForm formForm) ;

	/**
	 * 修改
	 */
	@Override
	WorkCenterFormVo edit(WorkCenterFormForm formForm) ;

	/**
	 * 删除
	 */
	@Override
	String del(WorkCenterFormForm formForm) ;

	/**
	 * 查询表单选择下拉
	 */
	Pagination<WorkCenterFormVo> select(WorkCenterFormForm formForm) ;

	/**
	 * 数据源复制
	 */
	WorkCenterFormVo copy(WorkCenterFormForm formForm) ;

	/**
	 * 表单导出
	 */
	String export(WorkCenterFormForm formForm) ;

	/**
	 * 表单导入
	 */
	void imports(MultipartFile file) ;

	/**
	 * 查询运维表单下载模板分页
	 */
	Pagination<WorkCenterFormVo> pageByAdmin(WorkCenterFormForm formForm) ;

}
