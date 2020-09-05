package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.AppVersionDto;
import com.dotop.smartwater.project.module.core.water.vo.AppVersionVo;

import java.util.List;

/**
 * App版本控制
 *

 * @date 2019年3月5日 15:45
 */
public interface IAppVersionDao extends BaseDao<AppVersionDto, AppVersionVo> {

	@Override
	List<AppVersionVo> list(AppVersionDto appVersionDto);

	List<AppVersionVo> historyList(AppVersionDto appVersionDto);

	List<AppVersionVo> getAppName();

	@Override
	void add(AppVersionDto appVersionDto);

	Integer update(AppVersionDto appVersionDto);

	@Override
	Integer del(AppVersionDto appVersionDto);
	
	Integer judgeCode(AppVersionDto appVersionDto);
	
	Integer getMaxCode(AppVersionDto appVersionDto);
	
	@Override
	AppVersionVo get(AppVersionDto appVersionDto);
}
