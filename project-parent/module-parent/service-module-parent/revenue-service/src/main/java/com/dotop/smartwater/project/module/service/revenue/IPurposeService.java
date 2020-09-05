package com.dotop.smartwater.project.module.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.PurposeBo;
import com.dotop.smartwater.project.module.core.water.vo.PurposeVo;

/**
 * 水费用途
 *

 * @date 2019年2月25日
 */
public interface IPurposeService extends BaseService<PurposeBo, PurposeVo> {

	PurposeVo findById(String id);

}
