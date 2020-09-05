package com.dotop.smartwater.project.auth.api;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.form.MenuForm;
import com.dotop.smartwater.project.module.core.auth.vo.MenuVo;

/**
 * 菜单管理
 * 

 * @date 2019年3月5日 10:52
 *
 */
public interface IMenuFactory extends BaseFactory<MenuForm, MenuVo> {

	/**
	 * 新增
	 */
	MenuVo add(MenuForm menuForm) throws FrameworkRuntimeException;

	/**
	 * 获取菜单并分页
	 */
	Pagination<MenuVo> page(MenuForm menuForm) throws FrameworkRuntimeException;

	/**
	 * 获取单个菜单信息
	 */
	MenuVo get(MenuForm menuForm) throws FrameworkRuntimeException;

	/**
	 * 修改菜单详情
	 * 
	 * @param menuForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	MenuVo revise(MenuForm menuForm) throws FrameworkRuntimeException;

	/**
	 * 删除菜单项
	 */
	Integer del(List<MenuForm> list) throws FrameworkRuntimeException;
}
