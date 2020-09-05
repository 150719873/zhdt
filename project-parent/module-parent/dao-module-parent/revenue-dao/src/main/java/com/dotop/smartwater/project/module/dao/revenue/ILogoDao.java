package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.auth.dto.LogoDto;
import com.dotop.smartwater.project.module.core.auth.vo.LogoVo;

/**

 * @date 2019年2月25日
 */
public interface ILogoDao extends BaseDao<LogoDto, LogoVo> {

	/**
	 * 添加图片
	 */
	@Override
	void add(LogoDto logoDto);

	/**
	 * 企业查询图片
	 */
	@Override
	LogoVo get(LogoDto logoDto);

	/**
	 * 删除图片
	 */
	@Override
	Integer del(LogoDto logoDto);

}
