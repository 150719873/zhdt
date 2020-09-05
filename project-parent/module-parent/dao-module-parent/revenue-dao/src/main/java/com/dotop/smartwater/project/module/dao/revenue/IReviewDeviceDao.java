package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.ReviewDeviceDto;
import com.dotop.smartwater.project.module.core.water.form.ReviewDetailForm;
import com.dotop.smartwater.project.module.core.water.vo.ReviewDeviceVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IReviewDeviceDao extends BaseDao<ReviewDeviceDto, ReviewDeviceVo> {

	List<ReviewDeviceVo> getList(ReviewDeviceDto dto);

	int generate(ReviewDeviceDto dto);

	@Override
	ReviewDeviceVo get(ReviewDeviceDto dto);

	List<ReviewDetailForm> getRandomDevice(@Param("communityIds") String communityIds,
	                                       @Param("devNumber") String devNumber);

	@Override
	Integer edit(ReviewDeviceDto dto);

	void delByBatchNo(@Param("batchNo") String batchNo);
}
