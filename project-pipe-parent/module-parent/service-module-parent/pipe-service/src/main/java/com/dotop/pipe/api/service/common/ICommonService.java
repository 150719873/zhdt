package com.dotop.pipe.api.service.common;

import java.util.Date;
import java.util.List;

import com.dotop.pipe.core.vo.alarm.AlarmNoticeRuleVo;
import com.dotop.pipe.core.vo.common.DictionaryVo;
import com.dotop.pipe.core.vo.common.NumRuleVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface ICommonService {

	DictionaryVo getByType(String type) throws FrameworkRuntimeException;

	DictionaryVo get(String id) throws FrameworkRuntimeException;

	/**
	 * 获取字典分页
	 *
	 * @param type
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Pagination<DictionaryVo> page(String type, Integer page, Integer pageSize) throws FrameworkRuntimeException;

	/**
	 * 删除字典
	 *
	 * @param id
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	String del(String id) throws FrameworkRuntimeException;

	/**
	 * 根据类型和名称查找字典
	 *
	 * @param type
	 * @param name
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	DictionaryVo getByTypeAndName(String type, String name) throws FrameworkRuntimeException;

	/**
	 * 新增字典
	 *
	 * @param name
	 * @param val
	 * @param type
	 * @param des
	 * @param unit
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	DictionaryVo add(String name, String val, String type, String des, String unit) throws FrameworkRuntimeException;

	/**
	 * 更新字典
	 *
	 * @param name
	 * @param val
	 * @param type
	 * @param des
	 * @param unit
	 * @param id
	 * @throws FrameworkRuntimeException
	 */
	void edit(String name, String val, String type, String des, String unit, String id)
			throws FrameworkRuntimeException;

	/**
	 * 查找编码规则的最大值
	 * 
	 * @param type
	 * @param operEid
	 * @return
	 */
	NumRuleVo getMaxCode(String type, String operEid);

	/**
	 * 编号规则更新
	 * 
	 * @param id
	 * @param maxValue
	 */
	void editNumRule(String id, Integer maxValue);

	/**
	 * 添加编号规则
	 * @param category
	 * @param title
	 * @param maxValue
	 * @param userby
	 * @param date
	 * @param operEid
	 */
	void addNumRule(String category, String title, Integer maxValue, String userby, Date date, String operEid);
	/**
	 * 查询预警通知配置的信息
	 * 
	 * @param operEid
	 * @return
	 */
	AlarmNoticeRuleVo getALarmNoticeRule(String operEid);

	List<String> getEnterpriseIdList();

}
