package com.dotop.smartwater.project.module.dao.device;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.MeterReadingTaskDto;
import com.dotop.smartwater.project.module.core.water.vo.MeterReadingTaskVo;

/**

 * @date 2019/2/22.
 */
public interface IMeterReadingTaskDao extends BaseDao<MeterReadingTaskDto, MeterReadingTaskVo> {

	@Override
	MeterReadingTaskVo get(MeterReadingTaskDto meterReadingTaskDto);

	List<MeterReadingTaskVo> getTaskCountInfo(@Param("ids") List<String> ids);

	@Override
	List<MeterReadingTaskVo> list(MeterReadingTaskDto meterReadingTaskDto);

	List<MeterReadingTaskVo> listApp(MeterReadingTaskDto meterReadingTaskDto);

	@Override
	Integer edit(MeterReadingTaskDto meterReadingTaskDto);

	int updateStatus(MeterReadingTaskDto meterReadingTaskDto);
}
