package com.dotop.smartwater.project.module.client.third.impl;

import com.dotop.smartwater.project.module.client.third.ICommunityService;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;
import com.dotop.smartwater.project.module.core.water.bo.CommunityBo;
import com.dotop.smartwater.project.module.core.water.vo.CommunityVo;
import com.dotop.water.tool.service.BaseInf;
import org.springframework.stereotype.Service;

/**

 */
@Service
public class CommunityServiceImpl implements ICommunityService {

	@Override
	public CommunityVo findById(String communityid) {
		CommunityBo communityBo = new CommunityBo();
		communityBo.setCommunityid(communityid);
		return get(communityBo);
	}

	@Override
	public CommunityVo get(CommunityBo communityBo) {
		AreaVo area = BaseInf.getAreaById(communityBo.getCommunityid());
		if (area == null) {
			return null;
		}
		CommunityVo community = new CommunityVo();
		community.setCommunityid(area.getId());
		community.setName(area.getName());
		return community;
	}

}
