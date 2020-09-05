package com.dotop.smartwater.project.auth.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.auth.api.IMenuFactory;
import com.dotop.smartwater.project.auth.service.IMenuService;
import com.dotop.smartwater.project.module.core.auth.bo.MenuBo;
import com.dotop.smartwater.project.module.core.auth.form.MenuForm;
import com.dotop.smartwater.project.module.core.auth.vo.MenuVo;

/**
 * 菜单管理
 * 

 * @date 2019年3月5日 10:52
 *
 */
@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
public class MenuFactoryImpl implements IMenuFactory {

	@Autowired
	private IMenuService iMenuService;

	@Override
	public MenuVo add(MenuForm menuForm) throws FrameworkRuntimeException {
		// 复制属性
		MenuBo menuBo = new MenuBo();
		BeanUtils.copyProperties(menuForm, menuBo);
		return iMenuService.add(menuBo);
	}

	@Override
	public Pagination<MenuVo> page(MenuForm menuForm) throws FrameworkRuntimeException {
		// 复制属性
		MenuBo menuBo = new MenuBo();
		BeanUtils.copyProperties(menuForm, menuBo);

		return iMenuService.page(menuBo);
	}

	@Override
	public MenuVo get(MenuForm menuForm) throws FrameworkRuntimeException {
		// 复制属性
		MenuBo menuBo = new MenuBo();
		BeanUtils.copyProperties(menuForm, menuBo);

		return iMenuService.get(menuBo);
	}

	@Override
	public MenuVo revise(MenuForm menuForm) throws FrameworkRuntimeException {

		// 复制属性
		MenuBo menuBo = new MenuBo();
		BeanUtils.copyProperties(menuForm, menuBo);

		return iMenuService.revise(menuBo);
	}

	@Override
	public Integer del(List<MenuForm> list) throws FrameworkRuntimeException {
		List<MenuBo> l = new ArrayList<>(list.size());
		// 复制属性
		for (MenuForm menuForm : list) {
			MenuBo menuBo = new MenuBo();
			BeanUtils.copyProperties(menuForm, menuBo);
			l.add(menuBo);
		}
		return iMenuService.del(l);
	}

}
