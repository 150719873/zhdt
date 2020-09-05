package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.ReviewDetailDto;
import com.dotop.smartwater.project.module.core.water.vo.ReviewDetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IReviewDetailDao extends BaseDao<ReviewDetailDto, ReviewDetailVo> {

	int batchAdd(@Param("list") List<ReviewDetailDto> list);

	List<ReviewDetailVo> getList(ReviewDetailDto dto);

	ReviewDetailVo getDevice(ReviewDetailDto dto);

	int submitReviewDevice(ReviewDetailDto dto);

	void delByBatchNo(@Param("batchNo") String batchNo);
}
