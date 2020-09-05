package com.dotop.pipe.api.dao.report;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.dotop.pipe.core.vo.report.TimingFormulaVo;
import com.dotop.pipe.data.report.core.dto.report.TimingFormulaDto;
import com.dotop.smartwater.dependence.core.common.BaseDao;

public interface ITimingFormulaDao extends BaseDao<TimingFormulaDto, TimingFormulaVo> {

	public void add(TimingFormulaDto timingFormulaDto) throws DataAccessException;

	public Integer edit(TimingFormulaDto timingFormulaDto) throws DataAccessException;

	public Integer del(TimingFormulaDto timingFormulaDto) throws DataAccessException;

	public Boolean isExist(TimingFormulaDto timingFormulaDto) throws DataAccessException;

	public List<TimingFormulaVo> list(TimingFormulaDto timingFormulaDto) throws DataAccessException;

}
