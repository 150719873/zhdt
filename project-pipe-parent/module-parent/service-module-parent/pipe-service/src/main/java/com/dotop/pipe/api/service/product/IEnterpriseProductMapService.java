package com.dotop.pipe.api.service.product;

import java.util.Date;
import java.util.List;

import com.dotop.pipe.core.vo.product.EnterpriseProductMapVo;
import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 *
 * @date 2018/10/30.
 */
public interface IEnterpriseProductMapService extends BaseService<BaseBo, EnterpriseProductMapVo> {
	/**
	 * 添加产品上线
	 * 
	 * @param enterpriseId
	 * @param productId
	 * @param curr
	 * @param userBy
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	EnterpriseProductMapVo add(String enterpriseId, String productId, Date curr, String userBy)
			throws FrameworkRuntimeException;

	/**
	 * 获取唯一上线产品
	 * 
	 * @param enterpriseId
	 * @param productId
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	EnterpriseProductMapVo get(String enterpriseId, String productId) throws FrameworkRuntimeException;

	/**
	 * 产品上线分页
	 * 
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Pagination<EnterpriseProductMapVo> page(Integer page, Integer pageSize, String enterpriseId)
			throws FrameworkRuntimeException;

	/**
	 * 删除产品上线
	 * 
	 * @param mapId
	 * @param curr
	 * @param userBy
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	String del(String mapId, String enterpriseId, String productId, Date curr, String userBy)
			throws FrameworkRuntimeException;

	/**
	 * 获取上线企业产品列表
	 * 
	 * @param enterpriseId
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	List<EnterpriseProductMapVo> list(String enterpriseId) throws FrameworkRuntimeException;
}
