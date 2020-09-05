package com.dotop.smartwater.project.auth.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.auth.dto.EnterpriseDto;
import com.dotop.smartwater.project.module.core.auth.dto.SettlementDto;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.LogoVo;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.auth.vo.select.Obj;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
public interface IEnterpriseDao extends BaseDao<EnterpriseDto, EnterpriseVo> {

	/**
	 * 
	 */
	int deleteById(@Param("enterpriseid") String enterpriseid);

	/**
	 * 
	 */
	int insert(EnterpriseDto enterprise);

	/**
	 * 
	 */
	EnterpriseVo findById(@Param("enterpriseid") String enterpriseid);

	/**
	 * 
	 */
	EnterpriseVo findEnterpriseByWebsite(String website);

	/**
	 * 
	 */
	int update(EnterpriseDto enterprise);

	/**
	 * 
	 */
	EnterpriseVo findEnterpriseByName(EnterpriseDto enterprise);

	/**
	 * 
	 */
	EnterpriseVo findEnterpriseByNameAndId(EnterpriseDto enterprise);

	/**
	 * 
	 */
	List<EnterpriseVo> getEnterpriseList(EnterpriseDto enterprise);

	/**
	 * 
	 */
	List<EnterpriseVo> getErpList();

	/**
	 * 
	 */
	LogoVo getLogo(@Param("enterpriseid") String enterpriseid);

	/**
	 * 
	 */
	int updateEnprno(@Param("enterpriseid") String enterpriseid);

	/**
	 * 
	 */
	SettlementVo getSettlement(@Param("enterpriseid") String enterpriseid);

	/**
	 * 
	 */
	List<SettlementVo> getSettlements();

	/**
	 * 
	 */
	int addSettlement(SettlementDto settlement);

	/**
	 * 
	 */
	Map<String, EnterpriseVo> getEnterpriseMap();

	/**
	 * 
	 */
	@MapKey("id")
	Map<String, Obj> getOrganizationChartMap(@Param("enterpriseid") String enterpriseid);
}
