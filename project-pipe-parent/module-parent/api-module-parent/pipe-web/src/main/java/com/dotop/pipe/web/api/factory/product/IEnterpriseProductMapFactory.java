package com.dotop.pipe.web.api.factory.product;

import com.dotop.pipe.core.form.EnterpriseProductMapForm;
import com.dotop.pipe.core.vo.product.EnterpriseProductMapVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 *
 * @date 2018/10/30.
 */
public interface IEnterpriseProductMapFactory extends BaseFactory<EnterpriseProductMapForm, EnterpriseProductMapVo> {
	/**
	 * 新增产品上线
	 *
	 * @param enterpriseProductMapForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	EnterpriseProductMapVo add(EnterpriseProductMapForm enterpriseProductMapForm) throws FrameworkRuntimeException;

	/**
	 * 产品上线分页
	 *
	 * @param enterpriseProductMapForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Pagination<EnterpriseProductMapVo> page(EnterpriseProductMapForm enterpriseProductMapForm)
			throws FrameworkRuntimeException;

	/**
	 * 删除上线产品
	 *
	 * @param enterpriseProductMapForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	String del(EnterpriseProductMapForm enterpriseProductMapForm) throws FrameworkRuntimeException;

	/**
	 * 新增批量产品上线
	 *
	 * @param enterpriseProductMapForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	void addMultiple(EnterpriseProductMapForm enterpriseProductMapForm) throws FrameworkRuntimeException;
}
