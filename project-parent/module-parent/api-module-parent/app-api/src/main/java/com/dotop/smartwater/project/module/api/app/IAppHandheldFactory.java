package com.dotop.smartwater.project.module.api.app;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.app.vo.AppHandheldVo;
import com.dotop.smartwater.project.module.core.third.form.iot.DeviceReplaceForm;
import com.dotop.smartwater.project.module.core.third.form.iot.MeterDataForm;
import com.dotop.smartwater.project.module.core.third.form.iot.UserEntryForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceChangeForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceChangeVo;

/**
 * App接口

 */
public interface IAppHandheldFactory extends BaseFactory<BaseForm, AppHandheldVo> {

	/**
	 * 获取水司
	 * @return 结果对象
	 */
	AppHandheldVo getEnterprise() ;

	/**
	 登录
	 * @param userEntryForm 参数对象
	 * @return 结果对象
	 */
	AppHandheldVo login(UserEntryForm userEntryForm) ;

	/**
	 * 注册
	 * @param meterDataForm 参数对象
	 * @return 结果对象
	 */
	AppHandheldVo register(MeterDataForm meterDataForm) ;

	
	/**
	 * 更换水表
	 * @param meterDataForm
	 * @return
	 */
	AppHandheldVo replace(DeviceReplaceForm form);
	
	
	/**
	 * 获取换表记录（分页）
	 * @param form
	 * @return
	 */
	Pagination<DeviceChangeVo> replacePage(DeviceChangeForm form);
	
	
	/**
	 * 是否绑定
	 * @param meterDataForm 参数对象
	 * @return 结果对象
	 */
	AppHandheldVo isBind(MeterDataForm meterDataForm) ;

	/**
	 * 抄表
	 * @param meterDataForm 参数对象
	 * @return 结果对象
	 */
	AppHandheldVo manualMeterRead(MeterDataForm meterDataForm) ;

	/**
	 * 解绑
	 * @param meterDataForm 参数对象
	 * @return 结果对象
	 */
	AppHandheldVo unBind(MeterDataForm meterDataForm) ;

}
