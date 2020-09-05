package com.dotop.smartwater.project.module.service.operation;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DataCalibrationBo;
import com.dotop.smartwater.project.module.core.water.vo.DataCalibrationVo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据校准
 *

 * @date 2019/3/5.
 */
public interface IDataCalibrationService extends BaseService<DataCalibrationBo, DataCalibrationVo> {

	/**
	 * 获取数据校准分页
	 *
	 * @param dataCalibrationBo 查询参数
	 * @return 分页
	 */
	@Override
	Pagination<DataCalibrationVo> page(DataCalibrationBo dataCalibrationBo);

	/**
	 * 查找
	 *
	 * @param dataCalibrationBo 参数
	 * @return 数据校准对象
	 */
	@Override
	DataCalibrationVo get(DataCalibrationBo dataCalibrationBo);

	/**
	 * 保存数据校准
	 *
	 * @param dataCalibrationBo 参数
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	DataCalibrationVo edit(DataCalibrationBo dataCalibrationBo);
}
