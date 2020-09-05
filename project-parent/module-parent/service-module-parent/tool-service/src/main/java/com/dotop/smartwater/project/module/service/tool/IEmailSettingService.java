package com.dotop.smartwater.project.module.service.tool;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.EmailSettingBo;
import com.dotop.smartwater.project.module.core.water.vo.EmailSettingVo;

public interface IEmailSettingService extends BaseService<EmailSettingBo, EmailSettingVo> {

	/**
	 * 分页查询邮件模板
	 * 
	 * @param vo
	 * @return
	 */
	@Override
	Pagination<EmailSettingVo> page(EmailSettingBo emailSettingBo);

	/**
	 * 插入或更新数据
	 * 
	 * @param vo
	 * @return
	 */
	int insertOrUpdate(EmailSettingBo emailSettingBo);
}
