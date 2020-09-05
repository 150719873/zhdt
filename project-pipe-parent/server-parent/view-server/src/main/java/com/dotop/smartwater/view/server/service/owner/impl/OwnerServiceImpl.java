package com.dotop.smartwater.view.server.service.owner.impl;

import com.dotop.smartwater.view.server.dao.water.owner.IOwnerDao;
import com.dotop.smartwater.view.server.service.owner.IOwnerService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.view.server.core.owner.form.OwnerForm;
import com.dotop.smartwater.view.server.core.owner.vo.OwnerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class OwnerServiceImpl implements IOwnerService {

    @Autowired
    IOwnerDao iOwnerDao;

    @Override
    public List<OwnerVo> list(OwnerForm ownerForm) throws FrameworkRuntimeException {
        return iOwnerDao.list(ownerForm);
    }

    @Override
    public Integer count(OwnerForm ownerForm) throws FrameworkRuntimeException {
        return iOwnerDao.count(ownerForm);
    }
}
