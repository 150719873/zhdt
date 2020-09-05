package com.dotop.smartwater.project.third.module.service;

import com.dotop.smartwater.project.third.module.api.dao.IMeterOwnerDao;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.third.module.api.service.IMeterOwnerService;
import com.dotop.smartwater.project.third.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.third.module.core.water.dto.OwnerDto;
import com.dotop.smartwater.project.third.module.core.water.vo.OwnerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
public class MeterOwnerServiceImpl implements IMeterOwnerService {

    @Autowired
    private IMeterOwnerDao iMeterOwnerDao;

    @Override
    public List<OwnerVo> list(OwnerBo ownerBo) throws FrameworkRuntimeException {
        OwnerDto ownerDto = BeanUtils.copy(ownerBo, OwnerDto.class);
        ownerDto.setIsDel(RootModel.NOT_DEL);
        return iMeterOwnerDao.list(ownerDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void adds(List<OwnerBo> ownerBos) throws FrameworkRuntimeException {
        Date curr = new Date();
        List<OwnerDto> ownerDtos = BeanUtils.copy(ownerBos, OwnerDto.class);
        ownerDtos.forEach(ownerDto -> {
            ownerDto.setCurr(curr);
            ownerDto.setIsDel(RootModel.NOT_DEL);
        });
        iMeterOwnerDao.adds(ownerDtos);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void edits(List<OwnerBo> ownerBos, String enterpriseid) throws FrameworkRuntimeException {
        Date curr = new Date();
        List<OwnerDto> ownerDtos = BeanUtils.copy(ownerBos, OwnerDto.class);
        ownerDtos.forEach(ownerDto -> {
            ownerDto.setCurr(curr);
        });
        iMeterOwnerDao.updateBatch(ownerDtos, enterpriseid);
    }

    @Override
    public OwnerVo get(OwnerBo ownerBo) throws FrameworkRuntimeException {
        OwnerDto ownerDto = BeanUtils.copy(ownerBo, OwnerDto.class);
        ownerDto.setIsDel(RootModel.NOT_DEL);
        return iMeterOwnerDao.get(ownerDto);
    }
}
