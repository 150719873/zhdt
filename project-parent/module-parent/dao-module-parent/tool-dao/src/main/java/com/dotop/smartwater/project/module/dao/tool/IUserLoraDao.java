package com.dotop.smartwater.project.module.dao.tool;

import org.apache.ibatis.annotations.Param;

import com.dotop.smartwater.project.module.core.auth.dto.UserLoraDto;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoraVo;

public interface IUserLoraDao {
	UserLoraVo findByEnterpriseId(@Param("enterpriseid") String enterpriseid);

	void add(UserLoraDto userlora);

	Integer edit(UserLoraDto userlora);
}
