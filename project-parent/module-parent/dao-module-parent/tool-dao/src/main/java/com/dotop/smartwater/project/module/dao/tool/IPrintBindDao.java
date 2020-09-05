package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.project.module.core.water.dto.DesignPrintDto;
import com.dotop.smartwater.project.module.core.water.dto.PrintBindDto;
import com.dotop.smartwater.project.module.core.water.vo.DesignPrintVo;
import com.dotop.smartwater.project.module.core.water.vo.PrintBindVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IPrintBindDao {

	/**
	 * 分页查询
	 *
	 * @param printBindDto
	 * @return
	 * @
	 */
	List<PrintBindVo> list(PrintBindDto printBindDto);

	/**
	 * 打印模板查询
	 *
	 * @return
	 * @
	 */
	List<PrintBindVo> listAll();

	/**
	 * 新增模板绑定
	 *
	 * @param printBindDto
	 * @return
	 */
	int add(PrintBindDto printBindDto);

	/**
	 * 删除模板绑定
	 *
	 * @param printBindDto
	 * @
	 */
	void del(PrintBindDto printBindDto);

	DesignPrintVo getRelationDesign(DesignPrintDto designPrintDto);

	/**
	 * 查询模板详情
	 *
	 * @param printBindDto
	 * @return
	 * @
	 */
	DesignPrintVo get(DesignPrintDto designPrintDto);

	/**
	 * 其他方法调用
	 *
	 * @param enterpriseid
	 * @param smsType
	 * @return
	 */
	PrintBindVo getByEnterpriseidAndsmsType(@Param("enterpriseid") String enterpriseid,
	                                        @Param("smsType") Integer smsType);

	/**
	 * 是否已存在
	 *
	 * @param printBindDto
	 * @return
	 * @
	 */
	boolean isExist(PrintBindDto printBindDto);

	/**
	 * 查询状态
	 *
	 * @param enterpriseid
	 * @param intValue
	 * @return
	 * @
	 */
	PrintBindVo getPrintStatus(@Param("enterpriseid") String enterpriseid, @Param("smstype") Integer smstype);
}
