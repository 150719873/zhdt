package com.dotop.smartwater.project.module.dao.wechat;

import com.dotop.smartwater.project.module.core.third.vo.wechat.WechatOwnerVo;
import com.dotop.smartwater.project.module.core.water.dto.OwnerDto;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IWechatUserDao {

	List<WechatOwnerVo> getAllOpenidByOwnerid(String ownerid);

	OwnerVo getOwner(OwnerDto ownerDto);

	List<OwnerVo> getOwnerList(@Param("openid") String openid, @Param("enterpriseid") String enterpriseid);

	List<OwnerVo> getOwnerUserByMsg(@Param("usermsg") String usermsg, @Param("username") String username,
	                                @Param("enterpriseid") String enterpriseid);

	void updateWechatIsdefault(String openid);

	void blindOwner(@Param("openid") String openid, @Param("ownerid") String ownerid,
	                @Param("isdefault") Integer isdefault);

	void deleteOwnerBlind(@Param("openid") String openid, @Param("ownerid") String ownerid);

	void updateDefaultBlindStatus(@Param("openid") String openid, @Param("ownerid") String ownerid,
	                              @Param("isDefaultStatus") int isdefault);

	int setIschargebacks(@Param("ownerid") String ownerid, @Param("ischargebacks") Integer ischargebacks);
}
