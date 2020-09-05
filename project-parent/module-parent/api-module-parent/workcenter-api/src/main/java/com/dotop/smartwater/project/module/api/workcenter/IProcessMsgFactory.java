package com.dotop.smartwater.project.module.api.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterProcessMsgForm;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessMsgVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 工作中心流程消息管理
 * 

 * @date 2019年4月17日
 * @description
 */
public interface IProcessMsgFactory extends BaseFactory<WorkCenterProcessMsgForm, WorkCenterProcessMsgVo> {
	/**
	 * 新增
	 */
	@Override
	WorkCenterProcessMsgVo add(WorkCenterProcessMsgForm processMsgForm) ;

	/**
	 * 查询分页
	 */
	@Override
	Pagination<WorkCenterProcessMsgVo> page(WorkCenterProcessMsgForm processMsgForm) ;

	/**
	 * 文件上传
	 */
	String upload(MultipartFile file, String processId, String type) ;

	/**
	 * 文件上传删除
	 */
	String uploadDel(String url, String processId) ;
}
