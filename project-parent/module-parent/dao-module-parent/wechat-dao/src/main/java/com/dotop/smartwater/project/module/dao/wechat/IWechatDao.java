package com.dotop.smartwater.project.module.dao.wechat;

import com.dotop.smartwater.project.module.core.third.dto.wechat.WechatNotifyMsgDto;
import com.dotop.smartwater.project.module.core.third.vo.wechat.WechatOwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.QueryBillVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IWechatDao {

	List<WechatOwnerVo> getAllOpenidByOwnerid(String ownerid);

	void updateDefaultBlindStatus(@Param("openid") String openid, @Param("ownerid") String ownerid,
	                              @Param("isdefault") int isdefault);

	List<QueryBillVo> getOrderTrend(@Param("ownerid") String ownerid);

	List<OrderVo> getOrderList(@Param("ownerid") String ownerId, @Param("page") Integer page,
	                           @Param("pageCount") Integer pageCount);

	int insertRecord(WechatNotifyMsgDto wechatNotifyMsgDto);

}
