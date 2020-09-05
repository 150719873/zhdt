package com.dotop.smartwater.project.module.api.revenue;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;

public interface IDeleteOrderExtTaskFactory {

	void clearPreviewByCondition(Object obj) throws FrameworkRuntimeException;

	/** 生成账单、抄表并生成账单、定时任务“滞纳金”三个功能，
	 * 在生成账单或计算费用时需要验证是否满足欠费关阀设置，
	 * 如果满足设置的金额，则自动关阀 **/
	void sendArrearsCloseNotice(String ownerId, SettlementVo settlementVo) throws FrameworkRuntimeException;


	/** 检测用户水表是否关阀，如果关阀则打开 **/
	void checkDeviceOpen(String ownerId) throws FrameworkRuntimeException;
}
