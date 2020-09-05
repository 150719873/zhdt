package com.dotop.smartwater.project.module.service.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.AppVersionBo;
import com.dotop.smartwater.project.module.core.water.vo.AppVersionVo;

/**
 * App版本控制
 * 

 * @date 2019年3月5日 15:45
 *
 */

public interface IAppVersionService extends BaseService<AppVersionBo, AppVersionVo> {

	@Override
	Pagination<AppVersionVo> page(AppVersionBo appVersionBo);

	List<AppVersionVo> getVersions(AppVersionBo appVersionBo);

	List<AppVersionVo> getAppName();
	
	AppVersionVo getApp(AppVersionBo appVersionBo);

	String addApp(AppVersionBo appVersionBo);

	Integer revise(AppVersionBo appVersionBo);

	@Override
	String del(AppVersionBo appVersionBo);
}
