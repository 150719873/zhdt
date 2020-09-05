package com.dotop.smartwater.project.auth.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.dotop.smartwater.project.module.core.auth.vo.LogoVo;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
public interface ILogoDao {

	/**
	 * 
	 */
	@Insert("REPLACE into `logo` (enterpriseid, ossurl, name, stat) "
			+ "values (#{enterpriseid}, #{ossurl}, #{name}, #{stat})")
	int addLogo(LogoVo logo);

	/**
	 * 
	 */
	@Select("select * from `logo` where enterpriseid=#{enterpriseid}")
	LogoVo getLogo(@Param("enterpriseid") String enterpriseid);

	/**
	 * 
	 */
	@Delete("delete from logo where enterpriseid=#{enterpriseid}")
	int delLogo(@Param("enterpriseid") String enterpriseid);
}
