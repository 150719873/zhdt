package com.dotop.smartwater.project.auth.service;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.bo.MenuBo;
import com.dotop.smartwater.project.module.core.auth.vo.MenuVo;

/**
 * 菜单管理
 * 

 * @date 2019年3月5日 10:52
 *
 */
public interface IMenuService extends BaseService<MenuBo, MenuVo> {
	/**
	 * 新增
	 */
	@Override
	MenuVo add(MenuBo menuBo) throws FrameworkRuntimeException;

	/**
	 * 获取菜单并分页
	 */
	Pagination<MenuVo> page(MenuBo menuBo) throws FrameworkRuntimeException;

	/**
	 * 获取单条菜单信息
	 */
	MenuVo get(MenuBo menuBo) throws FrameworkRuntimeException;

	/**
	 * 修改菜单详情
	 * 
	 * @param menuBo
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	MenuVo revise(MenuBo menuBo) throws FrameworkRuntimeException;

	/**
	 * 删除
	 */
	Integer del(List<MenuBo> list) throws FrameworkRuntimeException;
}
