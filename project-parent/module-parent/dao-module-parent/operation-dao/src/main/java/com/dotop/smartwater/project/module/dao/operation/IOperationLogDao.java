package com.dotop.smartwater.project.module.dao.operation;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.OperationLogDto;
import com.dotop.smartwater.project.module.core.water.vo.OperationLogVo;

import java.util.List;

/**

 * @date 2019/3/4.
 * 运维日志
 */
public interface IOperationLogDao extends BaseDao<OperationLogDto, OperationLogVo> {

	@Override
	OperationLogVo get(OperationLogDto operationLogDto);

	@Override
	List<OperationLogVo> list(OperationLogDto operationLogDto);

	@Override
	Integer edit(OperationLogDto operationLogDto);
}
