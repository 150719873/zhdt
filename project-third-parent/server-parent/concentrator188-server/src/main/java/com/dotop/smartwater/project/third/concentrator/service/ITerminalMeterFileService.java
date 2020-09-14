package com.dotop.smartwater.project.third.concentrator.service;

import com.dotop.smartwater.project.third.concentrator.core.bo.TerminalMeterFileBo;
import com.dotop.smartwater.project.third.concentrator.core.vo.TerminalMeterFileVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 * 终端读取档案
 *
 *
 */
public interface ITerminalMeterFileService extends BaseService<TerminalMeterFileBo, TerminalMeterFileVo> {

    @Override
    TerminalMeterFileVo add(TerminalMeterFileBo terminalMeterFileBo) throws FrameworkRuntimeException;

    @Override
    Pagination<TerminalMeterFileVo> page(TerminalMeterFileBo terminalMeterFileBo) throws FrameworkRuntimeException;
}
