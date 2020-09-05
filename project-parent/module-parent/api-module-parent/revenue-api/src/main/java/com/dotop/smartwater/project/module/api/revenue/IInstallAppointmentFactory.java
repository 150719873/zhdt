package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.*;
import com.dotop.smartwater.project.module.core.water.vo.*;

import java.util.List;

/**
 * 报装App接口

 */
public interface IInstallAppointmentFactory extends BaseFactory<InstallAppointmentForm, InstallAppointmentVo> {

	/**
	 * 验证是否有待办申请未处理
	 * 
	 * @param form 参数对象
	 * @return 结果
	 */
	int checkNohandles(InstallAppointmentForm form) ;

	/**
	 * 预约管理-获取预约次数详情
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	List<InstallAppointmentDetailVo> getAppointmentDetail(InstallAppointmentDetailForm form);

	/**
	 * 提交报装申请
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	boolean save(InstallAppointmentForm form) ;

	/**
	 * 预约管理-分页查询
	 */
	@Override
	Pagination<InstallAppointmentVo> page(InstallAppointmentForm form) ;

	/**
	 * 预约管理-分页查询-微信公众号
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	Pagination<InstallAppointmentVo> wechatPage(InstallAppointmentForm form) ;

	/**
	 * 预约管理-用户管理-分页查询
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	Pagination<InstallUserVo> pageUser(InstallUserForm form) ;

	/**
	 * 预约管理-新增用户信息
	 * 
	 * @param form 参数对象
	 * @return 结果
	 * @
	 */
	int addUser(InstallUserForm form) ;

	/**
	 * 预约管理-修改用户信息
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	int editUser(InstallUserForm form) ;

	/**
	 * 预约管理-删除用户信息
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	int delUser(InstallUserForm form) ;

	/**
	 * 导入到用户档案
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	int importUsers(InstallUserForm form) ;

	/**
	 * 指定预约模板
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	boolean setTemp(InstallAppointmentForm form) ;

	/**
	 * 删除预约信息
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	boolean delete(InstallAppointmentForm form) ;

	/**
	 * 办理
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	@Override
	InstallAppointmentVo get(InstallAppointmentForm form) ;

	/**
	 * 详情
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	InstallAppointmentVo detail(InstallAppointmentForm form) ;

	/**
	 * 详情-微信
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	InstallAppointmentVo wechatDetail(InstallAppointmentForm form) ;

	/**
	 * 根据number获取报装信息
	 * 
	 * @param form
	 * @return
	 * @
	 */
	InstallApplyVo getApply(InstallAppointmentForm form) ;

	/**
	 * 完善报装申请信息
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	int submitApply(InstallApplyForm form) ;

	/**
	 * 生成勘测任务
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	int submitSurvey(InstallSurveyForm form) ;

	/**
	 * 获取勘测任务
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	Pagination<InstallSurveyVo> surveyPage(InstallSurveyForm form) ;

	/**
	 * 提交合同信息
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	int submitContract(InstallContractForm form) ;

	/**
	 * 生成费用、支付信息
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	int submitAmount(InstallAmountForm form) ;

	/**
	 * 生成出货信息
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	int submitShip(InstallShipmentForm form) ;

	/**
	 * 指派验收人员
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	int submitAcceptance(InstallAcceptanceForm form) ;

	/**
	 * 获取验收任务
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	Pagination<InstallAcceptanceVo> acceptancePage(InstallAcceptanceForm form) ;

	/**
	 * 提交验收信息
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	int acceptance(InstallAcceptanceForm form) ;

	/**
	 * 同步用户信息
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	int syncUsers(InstallUserForm form) ;

	/**
	 * 提交勘测信息
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	int survey(InstallSurveyForm form) ;

	/**
	 * 当前环节进入到下一步
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	boolean next(InstallAppointmentForm form);
	
	/**
	 * 结束预约
	 * @return
	 * @
	 */
	boolean end(InstallAppointmentForm form);
	
	/**
	 * 撤回到上一步
	 * @return
	 * @
	 */
	boolean prev(InstallAppointmentForm form);

	/**
	 * 检查当前节点是否已提交
	 * 
	 * @param form 参数对象
	 * @return
	 * @
	 */
	boolean inspectNode(InstallAppointmentForm form) ;

}
