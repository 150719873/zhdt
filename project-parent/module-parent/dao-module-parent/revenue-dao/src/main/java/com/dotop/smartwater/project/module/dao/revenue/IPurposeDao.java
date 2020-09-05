package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.PurposeDto;
import com.dotop.smartwater.project.module.core.water.vo.PurposeVo;
import org.apache.ibatis.annotations.MapKey;

import java.util.Map;

/**
 * @program: project-parent
 * @description: 水费用途

 * @create: 2019-02-28 16:29
 **/
public interface IPurposeDao extends BaseDao<PurposeDto, PurposeVo> {
	PurposeVo findById(String id);

	@MapKey("name")
	Map<String, PurposeVo> getPurposeMap(String enterpriseid);

}
