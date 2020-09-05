package com.dotop.smartwater.project.auth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.auth.dto.MenuDto;
import com.dotop.smartwater.project.module.core.auth.vo.MenuVo;

/**
 * 菜单管理
 * 

 * @date 2019年3月5日 10:52
 *
 */
public interface IMenuDao extends BaseDao<MenuDto, MenuVo> {

	/**
	 * 
	 */
	List<MenuVo> adminList(MenuDto menuDto) throws DataAccessException;

	/**
	 * 
	 */
	List<MenuVo> list(MenuDto menuDto) throws DataAccessException;

	/**
	 * 
	 */
	MenuVo adminGet(MenuDto menuDto) throws DataAccessException;

	/**
	 * 
	 */
	MenuVo get(MenuDto menuDto) throws DataAccessException;

	/**
	 * 
	 */
	void adminAdd(MenuDto menuDto) throws DataAccessException;

	/**
	 * 
	 */
	void add(MenuDto menuDto) throws DataAccessException;

	/**
	 * 
	 */
	Integer adminRevise(MenuDto menuDto) throws DataAccessException;

	/**
	 * 
	 */
	Integer revise(MenuDto menuDto) throws DataAccessException;

	/**
	 * 
	 */
	Integer adminDel(@Param("list") List<MenuDto> list) throws DataAccessException;

	/**
	 * 
	 */
	Integer del(@Param("list") List<MenuDto> list) throws DataAccessException;
}
