package com.dotop.smartwater.project.auth.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
@Component
public interface IBaseDao {

	/**
	 * 
	 */
	Long getOwnerCountByCommunityid(@Param("communityid") String communityid);

	/**
	 * 
	 */
	Long getUserCountByEnterpriseid(@Param("enterpriseid") String enterpriseid);
}
