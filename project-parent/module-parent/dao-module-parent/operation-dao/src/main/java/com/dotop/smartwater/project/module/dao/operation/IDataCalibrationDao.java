package com.dotop.smartwater.project.module.dao.operation;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DataCalibrationDto;
import com.dotop.smartwater.project.module.core.water.vo.DataCalibrationVo;

import java.util.List;

/**

 * @date 2019/3/5.
 */
public interface IDataCalibrationDao extends BaseDao<DataCalibrationDto, DataCalibrationVo> {

	@Override
	DataCalibrationVo get(DataCalibrationDto dataCalibrationDto);

	@Override
	List<DataCalibrationVo> list(DataCalibrationDto dataCalibrationDto);

	@Override
	Integer edit(DataCalibrationDto dataCalibrationDto);
}
