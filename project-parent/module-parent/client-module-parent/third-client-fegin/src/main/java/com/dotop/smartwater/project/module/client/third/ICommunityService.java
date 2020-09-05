package com.dotop.smartwater.project.module.client.third;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.CommunityBo;
import com.dotop.smartwater.project.module.core.water.vo.CommunityVo;

/**

 */
public interface ICommunityService extends BaseService<CommunityBo, CommunityVo> {

	/**
	 * 根据id查找区域
	 *
	 * @param communityid 区域id
	 * @return
	 */
	CommunityVo findById(String communityid);

	/**
	 * 查询区域
	 * @param communityBo 区域对象
	 * @return
	 */
	@Override
	CommunityVo get(CommunityBo communityBo);

}
