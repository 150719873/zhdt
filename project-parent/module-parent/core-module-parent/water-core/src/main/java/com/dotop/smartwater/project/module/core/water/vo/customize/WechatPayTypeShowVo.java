package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.PayTypeVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.PayTypeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 微信公众号显示收费类型和近半年账单用水趋势
 * 
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatPayTypeShowVo extends BaseVo {
	private PayTypeVo payTypeVo;
	private List<OrderVo> orderVoList;
}
