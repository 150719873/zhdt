package com.dotop.smartwater.project.module.api.workcenter;

import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.water.bo.customize.WorkCenterBuildBo;
import com.dotop.smartwater.project.module.core.water.constants.CacheKey;
import com.dotop.smartwater.project.module.core.water.form.customize.WorkCenterBuildForm;
import com.dotop.smartwater.project.module.core.water.vo.customize.WorkCenterBuildVo;

/**
 * 工作中心流程初始化缓存构建
 * 

 * @date 2019年4月17日
 * @description
 */
public interface IWorkCenterBuildFactory {

	/**
	 * 设置缓存
	 */
	AbstractValueCache<WorkCenterBuildBo> getCache() ;

	/**
	 * 设置构建缓存
	 */
	default void set(WorkCenterBuildBo buildBo)  {
		AbstractValueCache<WorkCenterBuildBo> avc = getCache();
		String businessId = buildBo.getBusinessId();
		avc.set(CacheKey.WORK_CENTER_BUILD + businessId, buildBo);
	}

	/**
	 * 获取构建缓存
	 */
	default WorkCenterBuildBo get(String businessId)  {
		AbstractValueCache<WorkCenterBuildBo> avc = getCache();
		return avc.get(CacheKey.WORK_CENTER_BUILD + businessId, WorkCenterBuildBo.class);
	}

	/**
	 * 构造器
	 */
	default WorkCenterBuildVo init(WorkCenterBuildForm buildForm)  {
		WorkCenterBuildBo buildBo = BeanUtils.copy(buildForm, WorkCenterBuildBo.class);
		build(buildBo);
		set(buildBo);
		WorkCenterBuildVo buildVo = new WorkCenterBuildVo();
		buildVo.setBusinessId(buildBo.getBusinessId());
		buildVo.setBusinessType(buildBo.getBusinessType());
		return buildVo;
	}

	/**
	 * 组装流程参数
	 */
	void build(WorkCenterBuildBo buildBo) ;
}
