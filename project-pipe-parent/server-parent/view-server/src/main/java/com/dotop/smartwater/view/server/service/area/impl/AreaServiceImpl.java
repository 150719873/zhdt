package com.dotop.smartwater.view.server.service.area.impl;

import com.dotop.smartwater.view.server.core.area.form.AreaForm;
import com.dotop.smartwater.view.server.core.area.vo.AreaVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.view.server.dao.pipe.area.IViewAreaDao;
import com.dotop.smartwater.view.server.service.area.IAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service("AreaServiceImpl2")
public class AreaServiceImpl implements IAreaService {

    @Autowired
    IViewAreaDao iAreaDao;

    @Override
    public List<AreaVo> listDma(AreaForm areaForm) throws FrameworkRuntimeException {
        return iAreaDao.listDma(areaForm);
    }
}
