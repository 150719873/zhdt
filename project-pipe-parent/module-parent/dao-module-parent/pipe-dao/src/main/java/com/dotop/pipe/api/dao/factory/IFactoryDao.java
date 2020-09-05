package com.dotop.pipe.api.dao.factory;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.dotop.pipe.core.dto.factory.FactoryDto;
import com.dotop.pipe.core.vo.factory.FactoryVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;

/**
 *
 * @date 2018/10/25.
 */
public interface IFactoryDao extends BaseDao<FactoryDto, FactoryVo> {

	/**
	 * 添加厂商
	 * 
	 * @param factoryDto
	 * @throws DataAccessException
	 */
	void add(FactoryDto factoryDto) throws DataAccessException;

	/**
	 * 获取厂商
	 * 
	 * @param factoryDto
	 * @return
	 * @throws DataAccessException
	 */
	FactoryVo get(FactoryDto factoryDto) throws DataAccessException;

	/**
	 * 获取厂商列表
	 * 
	 * @param factoryDto
	 * @return
	 * @throws DataAccessException
	 */
	List<FactoryVo> list(FactoryDto factoryDto) throws DataAccessException;

	/**
	 * 编辑厂商
	 * 
	 * @param factoryDto
	 * @return
	 * @throws DataAccessException
	 */
	Integer edit(FactoryDto factoryDto) throws DataAccessException;

	/**
	 * 删除厂商
	 * 
	 * @param factoryDto
	 * @return
	 * @throws DataAccessException
	 */
	Integer del(FactoryDto factoryDto) throws DataAccessException;
}
