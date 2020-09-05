package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.project.module.core.water.dto.SmsTemplateDto;
import com.dotop.smartwater.project.module.core.water.vo.SmsTemplateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ISmsTemplateDao {
	List<SmsTemplateVo> getSmsTemplateVoList(SmsTemplateDto smsTemplate);

	Integer add(SmsTemplateDto smsTemplate);

	int update(SmsTemplateDto smsTemplate);

	SmsTemplateVo getSmsTemplate(@Param("enterpriseid") String enterpriseid,
	                             @Param("smstype") Integer smstype, @Param("status") Integer status);

	SmsTemplateVo getSmsTemplateVo(SmsTemplateDto smsTemplateDto);

	int delete(String id);

	int updateStatus(@Param("id") String id, @Param("status") int status);

	SmsTemplateVo getByEnterpriseidAndType(@Param("enterpriseid") String enterpriseid, @Param("smstype") Integer smstype);
}
