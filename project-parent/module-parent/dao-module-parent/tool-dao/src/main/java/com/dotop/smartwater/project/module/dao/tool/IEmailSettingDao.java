package com.dotop.smartwater.project.module.dao.tool;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dotop.smartwater.project.module.core.water.dto.EmailSettingDto;
import com.dotop.smartwater.project.module.core.water.vo.EmailSettingVo;


public interface IEmailSettingDao {
	/**
	 * 获取分页数据
	 *
	 * @param vo
	 * @return
	 */
	List<EmailSettingVo> getPage(EmailSettingDto vo);

	/**
	 * 根据企业ID查找邮箱配置
	 *
	 * @param enterpriseid
	 * @return
	 */
	EmailSettingVo findByEnterpriseId(@Param("enterpriseid") String enterpriseid);

	int edit(EmailSettingDto emailSettingDto);

	int add(EmailSettingDto emailSettingDto);
}
