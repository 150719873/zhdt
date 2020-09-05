package com.dotop.smartwater.project.third.module.service;

import com.dotop.smartwater.project.third.module.api.dao.IMeterDockingDao;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.third.module.api.service.IMeterDockingService;
import com.dotop.smartwater.project.third.module.core.water.bo.DockingBo;
import com.dotop.smartwater.project.third.module.core.water.dto.DockingDto;
import com.dotop.smartwater.project.third.module.core.water.vo.DockingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@Component
public class MeterDockingServiceImpl implements IMeterDockingService {

    @Autowired
    private IMeterDockingDao iMeterDockingDao;

    @Override
    public List<DockingVo> list(DockingBo dockingBo) throws FrameworkRuntimeException {
        DockingDto dockingDto = BeanUtils.copy(dockingBo, DockingDto.class);
        dockingDto.setIsDel(RootModel.NOT_DEL);
        return iMeterDockingDao.list(dockingDto);
    }

    @Override
    public DockingVo get(DockingBo dockingBo) throws FrameworkRuntimeException {
        DockingDto dockingDto = BeanUtils.copy(dockingBo, DockingDto.class);
        dockingDto.setIsDel(RootModel.NOT_DEL);
        return iMeterDockingDao.get(dockingDto);
    }
}
