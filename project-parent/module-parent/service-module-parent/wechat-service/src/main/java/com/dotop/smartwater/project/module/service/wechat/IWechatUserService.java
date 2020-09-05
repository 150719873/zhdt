package com.dotop.smartwater.project.module.service.wechat;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;

public interface IWechatUserService extends BaseService<BaseBo, BaseVo> {

	OwnerVo getOwner(OwnerBo ownerBo);

	/**
	 * 查询 绑定的业主列表
	 * 
	 * @param openid
	 * @param enterpriseid
	 * @return @
	 */
	List<OwnerVo> getOwnerList(String openid, String enterpriseid);

	/**
	 * 根据用户填入的信息 模糊匹配业主列表
	 * 
	 * @param usermsg
	 * @param username
	 * @param enterpriseid
	 * @return @
	 */
	List<OwnerVo> getOwnerUserByMsg(String usermsg, String username, String enterpriseid);

	/**
	 * 更新绑定的账号为非默认账号
	 * 
	 * @param openid
	 * @
	 */
	void updateWechatIsdefault(String openid);

	/**
	 * 绑定账号
	 * 
	 * @param openid
	 * @param ownerid
	 * @param isdefault
	 * @
	 */
	void blindOwner(String openid, String ownerid, Integer isdefault);

	/**
	 * 删除绑定关系
	 * 
	 * @param openid
	 * @param owneridPar
	 * @
	 */
	void deleteOwnerBlind(String openid, String owneridPar);

	/**
	 * 修改账号绑定状态
	 * 
	 * @param ownerid
	 * @param openid
	 * @param isdefault
	 * @
	 */
	void updateDefaultBlindStatus(String ownerid, String openid, int isdefault);

	/**
	 * 设置自动扣费
	 * 
	 * @param owner
	 * @return @
	 */
	int setIschargebacks(String ownerid, Integer ischargebacks);

}
