package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.water.form.PayTypeForm;
import com.dotop.smartwater.project.module.core.water.vo.MenuVo;
import com.dotop.smartwater.project.module.core.water.vo.PayTypeVo;

import java.util.List;

/**
 * 菜单列表
 * @program: project-parent
 * @description: 菜单

 * @create: 2019-02-26 09:23
 **/
public interface IRoleFactory extends BaseFactory<PayTypeForm, PayTypeVo> {

	/**
	 * 菜单列表
	 * @param parentid 父节点ID
	 * @return 菜单列表
	 */
	List<MenuVo> getMenuChild(String parentid) ;

	List<MenuVo> getMenu() ;
}
