package com.dotop.smartwater.project.third.module.api.dao;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.third.module.core.water.dto.DockingDto;
import com.dotop.smartwater.project.third.module.core.water.vo.DockingVo;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 *
 */
public interface IMeterDockingDao extends BaseDao<DockingDto, DockingVo> {

    List<DockingVo> list(DockingDto dockingDto) throws DataAccessException;

    @Override
    DockingVo get(DockingDto dockingDto) throws DataAccessException;
}
