package com.dotop.smartwater.project.module.dao.device;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.MeterReadingDetailDto;
import com.dotop.smartwater.project.module.core.water.vo.MeterReadingDetailVo;

/**

 * @date 2019/3/21.
 */
public interface IMeterReadingDetailDao extends BaseDao<MeterReadingDetailDto, MeterReadingDetailVo> {
	@Override
	MeterReadingDetailVo get(MeterReadingDetailDto meterReadingDetailDto);

	@Override
	List<MeterReadingDetailVo> list(MeterReadingDetailDto meterReadingDetailDto);

	MeterReadingDetailVo deviceDetail(MeterReadingDetailDto meterReadingDetailDto);

	int editDetails(MeterReadingDetailDto meterReadingDetailDto);

	int submitMeter(MeterReadingDetailDto meterReadingDetailDto);

	@Override
	Integer edit(MeterReadingDetailDto meterReadingDetailDto);

	List<String> getTaskAreaIds(@Param("batchId") String batchId);

	List<String> getMeterReaders(@Param("batchId") String batchId);

	int batchAdd(@Param("list") List<MeterReadingDetailDto> list);
}
