package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.OwnerRecordDto;
import com.dotop.smartwater.project.module.core.water.vo.OwnerRecordVo;

import java.util.List;

/**
 * @program: project-parent
 * @description: 账单相关

 * @create: 2019-02-28 18:32
 **/
public interface IOwnerRecordDao extends BaseDao<OwnerRecordDto, OwnerRecordVo> {
	void addOwnerRecord(OwnerRecordDto ownerRecordDto);

	List<OwnerRecordVo> getRecordList(OwnerRecordDto ownerRecordDto);
}
