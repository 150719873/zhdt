package com.dotop.pipe.web.api.factory.historylog;

import com.dotop.pipe.core.form.HistoryLogForm;
import com.dotop.pipe.core.vo.historylog.HistoryLogVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface IHistoryLogFactory extends BaseFactory<HistoryLogForm, HistoryLogVo> {

	public Pagination<HistoryLogVo> page(HistoryLogForm nodeForm) throws FrameworkRuntimeException;

	public HistoryLogVo get(HistoryLogForm nodeForm) throws FrameworkRuntimeException;

	public HistoryLogVo add(HistoryLogForm nodeForm) throws FrameworkRuntimeException;

}
