package com.dotop.smartwater.project.module.dao.wechat;

import com.dotop.smartwater.project.module.core.third.dto.wechat.WechatOrderDto;
import com.dotop.smartwater.project.module.core.third.vo.wechat.WechatOrderVo;
import com.dotop.smartwater.project.module.core.water.dto.customize.WechatParamDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IWechatPayDao {

	WechatOrderVo orderQuery(WechatParamDto wechatParamDto);

	int updateOrderStatus(WechatOrderDto wechatOrderDto);

	List<WechatOrderVo> findPayingByOrderid(@Param("ownerid") String ownerid, @Param("orderid") String orderid);

	int updateOrderRecord(WechatOrderDto wechatOrderDto);

	void saveTradePostAndRecord(WechatOrderDto wechatOrderDto);

}
