package com.dotop.smartwater.project.module.api.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.AppVersionForm;
import com.dotop.smartwater.project.module.core.water.vo.AppVersionVo;

/**
 * App版本控制
 * 

 * @date 2019年3月5日 15:45
 *
 */
public interface IAppVersionFactory extends BaseFactory<AppVersionForm, AppVersionVo> {
	/**
	 * 分页查询
	 */
	@Override
	Pagination<AppVersionVo> page(AppVersionForm appVersionForm);
	/**
	 * 获取app历史版本
	 */
	List<AppVersionVo> getVersions(AppVersionForm appVersionForm) ;
	/**
	 * 获取App详情
	 * @param appVersionForm
	 * @return
	 */
	AppVersionVo getApp(AppVersionForm appVersionForm) ;
	/**
	 * 获取App名称及唯一编码
	 * @param 
	 * @return
	 * @
	 */
	List<AppVersionVo> getAppName() ;
	/**
	 * 新增版本
	 */
	String addApp(AppVersionForm appVersionForm) ;
	/**
	 * 获取app最新版本
	 * @param appVersionForm
	 * @return
	 * @
	 */
	AppVersionVo getLatest(AppVersionForm appVersionForm) ;
	
	/**
	 * 修改版本信息
	 * @param appVersionForm
	 * @return
	 * @
	 */
	Integer revise(AppVersionForm appVersionForm) ;
	
	@Override
	String del(AppVersionForm appVersionForm) ;
}
