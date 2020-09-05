package com.dotop.pipe.api.service.historylog;

import java.util.List;

import com.dotop.pipe.core.bo.historylog.HistoryLogBo;
import com.dotop.pipe.core.dto.historylog.ChangeDto;
import com.dotop.pipe.core.vo.historylog.HistoryLogVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 * 
 * 
 *
 *
 */
public interface IHistoryLogService extends BaseService<HistoryLogBo, HistoryLogVo> {

	public Pagination<HistoryLogVo> page(HistoryLogBo historyLogBo) throws FrameworkRuntimeException;

	public List<HistoryLogVo> list(HistoryLogBo historyLogBo) throws FrameworkRuntimeException;

	public HistoryLogVo get(HistoryLogBo historyLogBo) throws FrameworkRuntimeException;

	public HistoryLogVo add(HistoryLogBo historyLogBo) throws FrameworkRuntimeException;

	public HistoryLogVo add(List<ChangeDto> list, String operEid, String userBy, String deviceId)
			throws FrameworkRuntimeException;

}
