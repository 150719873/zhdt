package com.dotop.smartwater.project.module.api.device;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.water.form.MeterReadingDetailForm;
import com.dotop.smartwater.project.module.core.water.form.MeterReadingTaskForm;
import com.dotop.smartwater.project.module.core.water.vo.MeterReadingDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.MeterReadingTaskVo;

/**
 * 抄表任务接口
 *

 * @date 2019/2/22.
 */
public interface IMeterReadingFactory extends BaseFactory<MeterReadingTaskForm, MeterReadingTaskVo> {

	/**
	 * 获取抄表任务分页
	 *
	 * @param meterReadingTaskForm 查询参数
	 * @return 分页
	 */
	@Override
	Pagination<MeterReadingTaskVo> page(MeterReadingTaskForm meterReadingTaskForm);

	/**
	 * 获取抄表任务分页
	 * 
	 * @param meterReadingTaskForm
	 * @return
	 */
	Pagination<MeterReadingTaskVo> pageApp(MeterReadingTaskForm meterReadingTaskForm);

	/**
	 * 查找
	 *
	 * @param meterReadingTaskForm 参数
	 * @return 抄表任务
	 */
	@Override
	MeterReadingTaskVo get(MeterReadingTaskForm meterReadingTaskForm);

	/**
	 * 保存抄表任务
	 *
	 * @param meterReadingTaskForm 参数
	 */
	@Override
	MeterReadingTaskVo edit(MeterReadingTaskForm meterReadingTaskForm);

	/**
	 * 获取抄表详情分页
	 * 
	 * @param meterReadingDetailForm 参数对象
	 * @return 抄表详情分页
	 */
	Pagination<MeterReadingDetailVo> detailPage(MeterReadingDetailForm meterReadingDetailForm);

	/**
	 * 设备详情
	 * 
	 * @param meterReadingDetailForm
	 * @return
	 */
	MeterReadingDetailVo deviceDetail(MeterReadingDetailForm meterReadingDetailForm);

	/**
	 * 提交抄表信息
	 * 
	 * @param meterReadingDetailForm
	 * @return
	 */
	boolean submitMeter(MeterReadingDetailForm meterReadingDetailForm);

	/**
	 * 获取抄表任务的区域列表
	 *
	 * @param meterReadingTaskForm 参数对象
	 * @return 抄表任务的区域列表
	 */
	List<AreaNodeVo> getTaskArea(MeterReadingTaskForm meterReadingTaskForm);

	/**
	 * 获取抄表员列表
	 * 
	 * @param meterReadingTaskForm 参数对象
	 * @return 抄表员列表
	 */
	List<String> getMeterReaders(MeterReadingTaskForm meterReadingTaskForm);

	/**
	 * 批量写入抄表信息
	 * 
	 * @param details 详情
	 * @return 结果
	 * @throws FrameworkRuntimeException
	 */
	boolean batchAdd(List<MeterReadingDetailForm> details);
}
