package com.dotop.smartwater.project.third.module.service;

import com.dotop.smartwater.project.third.module.api.dao.IWaterOwnerDao;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.third.module.api.service.IWaterOwnerService;
import com.dotop.smartwater.project.third.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.third.module.core.water.dto.OwnerDto;
import com.dotop.smartwater.project.third.module.core.water.vo.OwnerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@Component
public class WaterOwnerServiceImpl implements IWaterOwnerService {

    @Autowired
    private IWaterOwnerDao iWaterOwnerDao;

    @Override
    public List<OwnerVo> list(OwnerBo ownerBo) {
        OwnerDto ownerDto = BeanUtils.copy(ownerBo, OwnerDto.class);
        ownerBo.setIsDel(RootModel.NOT_DEL);
        return iWaterOwnerDao.list(ownerDto);
    }

    @Override
    public List<OwnerVo> findWaterOwnerByNoList(List<String> ccidList, String enterpriseid) {
        return iWaterOwnerDao.findWaterOwnerByNoList(ccidList, enterpriseid);
    }

    @Override
    public OwnerVo get(OwnerBo ownerBo) throws FrameworkRuntimeException {
        OwnerDto ownerDto = BeanUtils.copy(ownerBo, OwnerDto.class);
        ownerBo.setIsDel(RootModel.NOT_DEL);
        return iWaterOwnerDao.get(ownerDto);
    }
}
