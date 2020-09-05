package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.ReviewDetailForm;
import com.dotop.smartwater.project.module.core.water.form.ReviewDeviceForm;
import com.dotop.smartwater.project.module.core.water.vo.ReviewDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.ReviewDeviceVo;

public interface IReviewDeviceFactory extends BaseFactory<ReviewDeviceForm, ReviewDeviceVo> {

	/**
	 * 分页查询
	 */
	Pagination<ReviewDeviceVo> page(ReviewDeviceForm form);

	/**
	 * 设备复核详情
	 * 
	 * @param form
	 * @return @
	 */
	Pagination<ReviewDetailVo> detailPage(ReviewDetailForm form);

	/**
	 * 根据批次号、复核子任务ID获取设备信息
	 * 
	 * @param form
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	ReviewDetailVo getDevice(ReviewDetailForm form);

	/**
	 * 提交设备复核
	 * 
	 * @param form
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	boolean submitReviewDevice(ReviewDetailForm form);

	/**
	 * 生成设备复核数据
	 * 
	 * @param form
	 * @return @
	 */
	boolean generate(ReviewDeviceForm form);

	/**
	 * 获取设备详情
	 * 
	 * @param form
	 * @return
	 */
	@Override
	ReviewDeviceVo get(ReviewDeviceForm form);

	void addDeviceReview(ReviewDeviceForm form);
}
