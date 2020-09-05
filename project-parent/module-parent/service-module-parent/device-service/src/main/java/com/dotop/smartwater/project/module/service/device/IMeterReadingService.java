package com.dotop.smartwater.project.module.service.device;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.MeterReadingDetailBo;
import com.dotop.smartwater.project.module.core.water.bo.MeterReadingTaskBo;
import com.dotop.smartwater.project.module.core.water.vo.MeterReadingDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.MeterReadingTaskVo;

/**
 * 抄表任务
 *

 * @date 2019/2/22.
 */
public interface IMeterReadingService extends BaseService<MeterReadingTaskBo, MeterReadingTaskVo> {

	/**
	 * 获取抄表任务分页
	 *
	 * @param meterReadingTaskBo 查询参数
	 * @return 分页
	 */
	@Override
	Pagination<MeterReadingTaskVo> page(MeterReadingTaskBo meterReadingTaskBo);

	/**
	 * 获取抄表分页
	 * 
	 * @param meterReadingTaskBo
	 * @return
	 */
	Pagination<MeterReadingTaskVo> pageApp(MeterReadingTaskBo meterReadingTaskBo, List<String> areaIds);

	/**
	 * 查找
	 *
	 * @param meterReadingTaskBo 参数
	 * @return 抄表任务对象
	 */
	@Override
	MeterReadingTaskVo get(MeterReadingTaskBo meterReadingTaskBo);

	/**
	 * 保存抄表任务
	 *
	 * @param meterReadingTaskBo 参数
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	MeterReadingTaskVo edit(MeterReadingTaskBo meterReadingTaskBo);

	/**
	 * 获取抄表详情分页
	 *
	 * @param meterReadingDetailBo
	 * @return
	 */
	Pagination<MeterReadingDetailVo> detailPage(MeterReadingDetailBo meterReadingDetailBo);

	/**
	 * 设备详情
	 *
	 * @param meterReadingDetailBo
	 * @return
	 */
	MeterReadingDetailVo deviceDetail(MeterReadingDetailBo meterReadingDetailBo);

	/**
	 * 提交抄表信息
	 * 
	 * @param meterReadingDetailBo
	 * @return
	 */
	int submitMeter(MeterReadingDetailBo meterReadingDetailBo);

	/**
	 * 编辑详情
	 * 
	 * @param meterReadingDetailBo
	 * @return
	 */
	int editDetails(MeterReadingDetailBo meterReadingDetailBo);

	/**
	 * 获取抄表任务区域列表
	 * 
	 * @param meterReadingTaskBo
	 * @return
	 */
	List<String> getTaskAreaIds(MeterReadingTaskBo meterReadingTaskBo);

	/**
	 * 获取抄表员列表
	 * 
	 * @param meterReadingTaskBo
	 * @return
	 */
	List<String> getMeterReaders(MeterReadingTaskBo meterReadingTaskBo);

	/**
	 * 批量写入抄表信息
	 * 
	 * @param list
	 * @return @
	 */
	boolean batchAdd(List<MeterReadingDetailBo> list);
}
