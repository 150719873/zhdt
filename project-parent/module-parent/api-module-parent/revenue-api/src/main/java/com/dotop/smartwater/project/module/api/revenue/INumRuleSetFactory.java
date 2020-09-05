package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.water.form.NumRuleSetForm;
import com.dotop.smartwater.project.module.core.water.form.customize.MakeNumRequest;
import com.dotop.smartwater.project.module.core.water.vo.NumRuleSetVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.MakeNumVo;

import java.util.List;

/**
 * @program: project-parent
 * @description: 流水号设计

 * @create: 2019-02-26 09:23
 **/

public interface INumRuleSetFactory extends BaseFactory<NumRuleSetForm, NumRuleSetVo> {

	/**
	 * 获取基础业务Code
	 * @return 基础业务Code
	 */
	List<NumRuleSetVo> base() ;

	/**
	 * 改变状态
	 * @param numRuleSetForm 对象
	 */
	void changeStatus(NumRuleSetForm numRuleSetForm) ;

	/**
	 * 获取自定义业务流水号
	 * @param makeNumRequest 对象
	 * @return MakeNumVo
	 */
	MakeNumVo makeNo(MakeNumRequest makeNumRequest) ;

	/**
	 * 微信获取自定义业务流水号
	 * @param makeNumRequest 对象
	 * @return MakeNumVo
	 */
	MakeNumVo wechatMakeNo(MakeNumRequest makeNumRequest) ;

}
