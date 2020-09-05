package com.dotop.pipe.api.service.factory;

import java.util.Date;
import java.util.List;

import com.dotop.pipe.core.vo.factory.FactoryVo;
import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 *
 * @date 2018/10/25.
 */
public interface IFactoryService extends BaseService<BaseBo, FactoryVo> {

	/**
	 * 添加厂商
	 * 
	 * @param code
	 * @param name
	 * @param des
	 * @param curr
	 * @param userBy
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	FactoryVo add(String code, String name, String des, Date curr, String userBy) throws FrameworkRuntimeException;

	/**
	 * 获取厂商
	 * 
	 * @param factoryId
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	FactoryVo get(String factoryId) throws FrameworkRuntimeException;

	/**
	 * 通过编码获取厂商
	 * 
	 * @param code
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	FactoryVo getByCode(String code) throws FrameworkRuntimeException;

	/**
	 * 厂商分页
	 * 
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Pagination<FactoryVo> page(Integer page, Integer pageSize) throws FrameworkRuntimeException;

	/**
	 * 列出厂商
	 * 
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	List<FactoryVo> list() throws FrameworkRuntimeException;

	/**
	 * 编辑厂商
	 * 
	 * @param factoryId
	 * @param code
	 * @param name
	 * @param des
	 * @param curr
	 * @param userBy
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	FactoryVo edit(String factoryId, String code, String name, String des, Date curr, String userBy)
			throws FrameworkRuntimeException;

	/**
	 * 删除厂商
	 * 
	 * @param factoryId
	 * @param curr
	 * @param userBy
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	String del(String factoryId, Date curr, String userBy) throws FrameworkRuntimeException;
}
