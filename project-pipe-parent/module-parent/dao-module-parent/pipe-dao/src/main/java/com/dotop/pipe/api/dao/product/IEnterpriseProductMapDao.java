package com.dotop.pipe.api.dao.product;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.dotop.pipe.core.dto.product.EnterpriseProductMapDto;
import com.dotop.pipe.core.vo.product.EnterpriseProductMapVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;

/**
 *
 * @date 2018/10/30.
 */
public interface IEnterpriseProductMapDao extends BaseDao<EnterpriseProductMapDto, EnterpriseProductMapVo> {

	/**
	 * 添加产品上线
	 *
	 * @param enterpriseProductMapDto
	 * @throws DataAccessException
	 */
	void add(EnterpriseProductMapDto enterpriseProductMapDto) throws DataAccessException;

	/**
	 * 获取产品上线
	 *
	 * @param enterpriseProductMapDto
	 * @return
	 * @throws DataAccessException
	 */
	EnterpriseProductMapVo get(EnterpriseProductMapDto enterpriseProductMapDto) throws DataAccessException;

	/**
	 * 获取产品上线列表
	 *
	 * @param enterpriseProductMapDto
	 * @return
	 * @throws DataAccessException
	 */
	List<EnterpriseProductMapVo> list(EnterpriseProductMapDto enterpriseProductMapDto) throws DataAccessException;

	/**
	 * 删除产品上线
	 *
	 * @param enterpriseProductMapDto
	 * @return
	 * @throws DataAccessException
	 */
	Integer del(EnterpriseProductMapDto enterpriseProductMapDto) throws DataAccessException;

}
