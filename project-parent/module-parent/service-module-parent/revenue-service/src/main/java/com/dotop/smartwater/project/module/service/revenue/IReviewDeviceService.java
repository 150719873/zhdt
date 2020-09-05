package com.dotop.smartwater.project.module.service.revenue;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.ReviewDetailBo;
import com.dotop.smartwater.project.module.core.water.bo.ReviewDeviceBo;
import com.dotop.smartwater.project.module.core.water.form.ReviewDetailForm;
import com.dotop.smartwater.project.module.core.water.vo.ReviewDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.ReviewDeviceVo;

public interface IReviewDeviceService extends BaseService<ReviewDeviceBo, ReviewDeviceVo> {

	@Override
	Pagination<ReviewDeviceVo> page(ReviewDeviceBo bo);

	Pagination<ReviewDetailVo> detailPage(ReviewDetailBo bo);

	ReviewDetailVo getDevice(ReviewDetailBo bo);

	boolean submitReviewDevice(ReviewDetailBo bo);

	boolean generate(ReviewDeviceBo bo);

	@Override
	ReviewDeviceVo get(ReviewDeviceBo bo);

	List<ReviewDetailForm> getRandomDevice(String communityIds, String devNumber);
}
