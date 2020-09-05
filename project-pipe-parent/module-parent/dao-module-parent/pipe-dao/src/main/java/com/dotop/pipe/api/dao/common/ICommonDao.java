package com.dotop.pipe.api.dao.common;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.dotop.pipe.core.dto.common.DictionaryDto;
import com.dotop.pipe.core.vo.alarm.AlarmNoticeRuleVo;
import com.dotop.pipe.core.vo.common.DictionaryVo;
import com.dotop.pipe.core.vo.common.NumRuleVo;

public interface ICommonDao {

	public List<DictionaryVo> getVo(@Param("type") String type, @Param("isDel") Integer isDel)
			throws DataAccessException;

	public DictionaryVo get(@Param("id") String id, @Param("isDel") Integer isDel) throws DataAccessException;

	List<DictionaryVo> list(DictionaryDto dictionaryDto) throws DataAccessException;

	Integer del(DictionaryDto dictionaryDto) throws DataAccessException;

	void add(DictionaryDto dictionaryDto) throws DataAccessException;

	DictionaryVo getMaxByType(@Param("type") String type) throws DataAccessException;

	List<DictionaryVo> getByTypeAndName(@Param("type") String type, @Param("name") String name)
			throws DataAccessException;

	Integer edit(DictionaryDto dictionaryDto) throws DataAccessException;

	public NumRuleVo getMaxCode(@Param("type") String type, @Param("operEid") String operEid);

	public AlarmNoticeRuleVo getALarmNoticeRule(@Param("operEid") String operEid,@Param("deviceCode") String deviceCode);

	/**
	 * 添加编号规则功能
	 * 
	 * @param category
	 * @param title
	 * @param maxValue
	 * @param isDel
	 * @param userBy
	 * @param cuur
	 * @param operEid
	 */
	public void addNumRule(@Param("category") String category, @Param("title") String title,
			@Param("maxValue") Integer maxValue, @Param("isDel") Integer isDel, @Param("userBy") String userBy,
			@Param("cuur") Date cuur, @Param("operEid") String operEid);

	/**
	 * 修改编号规则
	 * 
	 * @param id
	 * @param maxValue
	 */
	public void editNumRule(@Param("id") String id, @Param("maxValue") Integer maxValue);


	public List<String> getEnterpriseIdList(@Param("isDel") Integer isDel);

}
