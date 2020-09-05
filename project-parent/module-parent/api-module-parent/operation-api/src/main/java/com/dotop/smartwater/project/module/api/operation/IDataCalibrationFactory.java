package com.dotop.smartwater.project.module.api.operation;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.DataCalibrationForm;
import com.dotop.smartwater.project.module.core.water.vo.DataCalibrationVo;

/**
 * 数据校准
 *

 * @date 2019/3/5.
 */
public interface IDataCalibrationFactory extends BaseFactory<DataCalibrationForm, DataCalibrationVo> {

	/**
	 * 获取数据校准分页
	 *
	 * @param dataCalibrationForm 查询参数
	 * @return 分页
	 */
	@Override
	Pagination<DataCalibrationVo> page(DataCalibrationForm dataCalibrationForm);

	/**
	 * 查找
	 *
	 * @param dataCalibrationForm 参数
	 * @return 数据校准对象
	 */
	@Override
	DataCalibrationVo get(DataCalibrationForm dataCalibrationForm);

	/**
	 * 保存数据校准
	 *
	 * @param dataCalibrationForm 参数
	 */
	@Override
	DataCalibrationVo edit(DataCalibrationForm dataCalibrationForm);
}
