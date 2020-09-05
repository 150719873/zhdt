package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.project.module.core.water.dto.EmailTemplateDto;
import com.dotop.smartwater.project.module.core.water.vo.EmailTemplateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;



/**

 * @date 2018/11/29.
 */

public interface IEmailTemplateDao {
	/**
	 * 获取分页数据
	 *
	 * @param vo
	 * @return
	 */
	List<EmailTemplateVo> getPage(EmailTemplateDto vo);

	/**
	 * 获取记录总数
	 *
	 * @param vo
	 * @return
	 */
	Long getPageCount(EmailTemplateDto vo);

	int insertOrUpdate(EmailTemplateDto vo);

	EmailTemplateVo getByEnterpriseAndType(@Param("enterpriseid") String enterpriseid,
			@Param("modeltype") Integer modeltype);

	Integer edit(EmailTemplateDto emailTemplateDto);

	Integer add(EmailTemplateDto emailTemplateDto);
}
