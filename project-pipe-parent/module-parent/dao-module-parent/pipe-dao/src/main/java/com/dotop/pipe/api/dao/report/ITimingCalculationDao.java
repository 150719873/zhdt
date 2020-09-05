package com.dotop.pipe.api.dao.report;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.dotop.pipe.core.vo.report.TimingCalculationVo;
import com.dotop.pipe.data.report.core.dto.report.TimingCalculationDto;
import com.dotop.smartwater.dependence.core.common.BaseDao;

public interface ITimingCalculationDao extends BaseDao<TimingCalculationDto, TimingCalculationVo> {

	public List<TimingCalculationVo> list(TimingCalculationDto timingCalculationDto) throws DataAccessException;

	public TimingCalculationVo get(TimingCalculationDto timingCalculationDto) throws DataAccessException;

	public void add(TimingCalculationDto timingCalculationDto) throws DataAccessException;

	public Integer edit(TimingCalculationDto timingCalculationDto) throws DataAccessException;

	public Integer del(TimingCalculationDto timingCalculationDto) throws DataAccessException;

	public Boolean isExist(TimingCalculationDto timingCalculationDto) throws DataAccessException;
}
